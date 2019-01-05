package visitante;

import condition.Condition;
import condition.TrueCondition;
import condition.Expression;
import condition.ConditionalNode;
import condition.ConditionalNode.ConditionalOperationType;
import condition.DataNode;
import condition.LiteralNode;
import condition.LiteralNode.LiteralType;
import condition.OperationNode;
import condition.OperationNode.OperationType;
import condition.RelationNode;
import condition.RelationNode.RelationType;
import exceptions.DatabaseException;
import exceptions.DBMSException;
import exceptions.TableException;
import grammar.SQLGrammarParser;
import grammar.SQLGrammarParser.ExpressionContext;
import grammar.SQLGrammarParser.IdValueContext;
import grammar.SQLGrammarBaseVisitor;
import grammar.SQLGrammarParser.ActionContext;
import grammar.SQLGrammarParser.ConstraintListContext;
import grammar.SQLGrammarParser.ConstraintTypeContext;
import grammar.SQLGrammarParser.OrderExpContext;
import userInterface.MessagePrinter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import motor.Database;
import motor.Data;
import motor.restriction.Restriction;
import motor.Table;
import motor.DataType;
import motor.relation.Row;
import motor.relation.Relation;
import motor.relation.FilteredRelation;
import motor.relation.OrderedRelation;
import motor.relation.OrderedRelation.OrderType;
import motor.relation.CrossProductRelation;
import motor.relation.ProjectedRelation;
import motor.restriction.CharRestriction;
import motor.restriction.CheckRestriction;
import motor.restriction.PrimaryKeyRestriction;
import org.antlr.v4.runtime.tree.TerminalNode;
import condition.Node;

/**
 * Visitor for the SQL language
 */
public class SQLVisitor extends SQLGrammarBaseVisitor<Object> {

	private Database currentDatabase = null;
	private Table currentTable;

	private boolean echo = true;
	private boolean printInsert = false;
	private int insertCounter = 0;

	/**
	 * Constructor
	 */
	public SQLVisitor() {
	}

	@Override
	public Object visitProgram(SQLGrammarParser.ProgramContext ctx) {
		visitChildren(ctx);

		// If echo disabled, print summary
		if (printInsert) {
			MessagePrinter.printUserMessage(String.format("%s values were inserted successfuly.", insertCounter));
			insertCounter = 0;
			printInsert = false;
		}

		// Commit
		if (currentDatabase != null) {
			currentDatabase.saveChanges();
		}
		return true;

	}

	@Override
	public Object visitCreateDatabase(SQLGrammarParser.CreateDatabaseContext ctx) {

		Database.create(ctx.ID().getText());
		MessagePrinter.printUserMessage(String.format("Database '%s' created successfuly.", ctx.ID().getText()));

		return true;

	}

	@Override
	public Object visitAlterDatabase(SQLGrammarParser.AlterDatabaseContext ctx) {
		// Changes the database name
		Database.renameDatabase(ctx.ID(0).getText(), ctx.ID(1).getText());
		MessagePrinter.printUserMessage(String.format("Database name changed from %s to %s.", ctx.ID(0).getText(), ctx.ID(1).getText()));

		return true;
	}

	@Override
	public Object visitDropDatabase(SQLGrammarParser.DropDatabaseContext ctx) {
		Table[] totalTables = Database.findDatabase(ctx.ID().getText()).getTables();
		int rowAmount = 0;

		for (int i = 0; i < totalTables.length; i++) {
			rowAmount += totalTables[i].getRelation().getRowNumber();
		}

		// Ask for user confirmation
		boolean delete = MessagePrinter.getConfirmation(String.format("Are you sure you want to delete database %s with %s rows?", ctx.ID().getText(), rowAmount));

		if (delete) {
			Database.deleteDatabase(ctx.ID().getText());
			MessagePrinter.printUserMessage(String.format("Database %s eliminated succesfuly", ctx.ID().getText()));

			return true;
		} else {

			return false;

		}
	}

	@Override
	public Object visitShowDatabases(SQLGrammarParser.ShowDatabasesContext ctx) {
		MessagePrinter.printUserMessage("Databases: ");
		ArrayList<String> basesDatos = Database.getDatabaseNames();
		for (String baseDatos : basesDatos) {
			MessagePrinter.printUserMessage("\t" + baseDatos);
		}

		return true;
	}

	@Override
	public Object visitUseDatabase(SQLGrammarParser.UseDatabaseContext ctx) {
		currentDatabase = Database.findDatabase(ctx.ID().getText());
		MessagePrinter.printUserMessage(String.format("Current selected database is %s.", ctx.ID().getText()));

		return true;

	}

	@Override
	public Object visitCreateTable(SQLGrammarParser.CreateTableContext ctx) {
		// Verifies if there is a selected database
		if (currentDatabase != null) {

			// Crear la tabla en la base de datos
			currentTable = currentDatabase.addNewTable(ctx.ID(0).getText());
			MessagePrinter.printUserMessage(String.format("Tabla %s creada en base de datos %s.", ctx.ID(0).getText(), currentDatabase.getDatabaseName()));

			// Obtener arreglo de id y de tipo columna         
			List<TerminalNode> listaId = ctx.ID();
			//List<TipoColumnaContext> listaTipoColumna = ctx.tipoColumna();

			int contador = 0;
			// Crear cada columna de la tabla
			for (TerminalNode id : listaId) {

				if (contador != 0) {
					String nombreColumna = id.getText();
					DataType tipoColumna = (DataType) visit(ctx.columnType(contador - 1));
					currentTable.addColumn(nombreColumna, tipoColumna);

					// Verifica si es necesario agregar una restriccion
					if (tipoColumna == DataType.CHAR) {
						int limiteChar = Integer.parseInt(ctx.columnType(contador - 1).int_literal().NUM().getText());

						/* NOTA: Utiliza UUID para asegurar que el nombre de la restricción sea único. */
						currentTable.addRestriction(UUID.randomUUID().toString(),
							new CharRestriction(
								motor.Util.getCualifiedName(currentTable.getTableName(), nombreColumna),
								limiteChar));
					}

					if (echo) {
						MessagePrinter.printUserMessage(String.format("Columna %s agregada a la tabla %s.", id.getText(), currentTable.getTableName()));
					}

				}
				contador++;

			}

			// Agregar restricciones
			List<ConstraintListContext> restricciones = ctx.constraintList();
			for (ConstraintListContext restriccion : restricciones) {

				visit(restriccion);

			}

		} else {
			MessagePrinter.printErrorMessage("No se encuentra ninguna base de datos en uso.");
			return false;
		}
		return true;

	}

	/**
	 * Método para visitar todas las restricciones
	 *
	 * @param ctx Contexto
	 * @return true
	 */
	@Override
	public Object visitConstraintList(SQLGrammarParser.ConstraintListContext ctx) {

		// Agregar todas las restricciones
		List<ConstraintTypeContext> restricciones = ctx.constraintType();

		for (ConstraintTypeContext restriccion : restricciones) {

			visit(restriccion);

		}

		return true;

	}

	/**
	 * Método para agregar restricciones
	 *
	 * @param ctx Contexto
	 * @return true si se agrega sin errores
	 */
	@Override
	public Object visitConstraintType(SQLGrammarParser.ConstraintTypeContext ctx) {

		// Verificar el tipo constraint
		if (ctx.PRIMARY() != null) {

			// Obtener las referencias y crear la restricción
			ArrayList<String> campos = (ArrayList<String>) visit(ctx.idList(0));
			ArrayList<String> camposCalificados = new ArrayList<>();

			for (String nc : campos) {

				camposCalificados.add(currentTable.getTableName() + "." + nc);

			}

			Restriction restriccionPrimaria = new PrimaryKeyRestriction(camposCalificados.toArray(new String[0]));

			// Agregar restricción
			currentTable.addRestriction(ctx.ID(0).getText(), restriccionPrimaria);
			MessagePrinter.printUserMessage(String.format("Restriccion %s agregada con éxito en tabla %s", ctx.ID(0).getText(), currentTable.getTableName()));

			return true;
		} else if (ctx.FOREIGN() != null) {

			// Pendiente de implementar
			return null;

		} // Caso check
		else {

			// Crear la restricción
			Condition condicionCheck = new Condition((Node) visit(ctx.expression()));
			Restriction restriccionCheck = new CheckRestriction(condicionCheck);

			// Agregar restricción
			currentTable.addRestriction(ctx.ID(0).getText(), restriccionCheck);
			MessagePrinter.printUserMessage(String.format("Restriccion %s agregada con éxito en tabla %s", ctx.ID(0).getText(), currentTable.getTableName()));

			return true;
		}

	}

	/**
	 * Método para determinar el tipo de columna
	 *
	 * @param ctx Contexto
	 * @return Tipo de columna
	 */
	@Override
	public Object visitColumnType(SQLGrammarParser.ColumnTypeContext ctx) {

		if (ctx.INT() != null) {
			return DataType.INT;
		} else if (ctx.FLOAT() != null) {
			return DataType.FLOAT;
		} else if (ctx.DATE() != null) {
			return DataType.DATE;
		} else {
			return DataType.CHAR;

			// TODO char debe agregar una nueva restricción
		}
	}

	/**
	 * Método para elminar una tabla de la base de datos en uso
	 *
	 * @param ctx Contexto
	 * @return true si se realizó sin errores.
	 */
	@Override
	public Object visitDropTable(SQLGrammarParser.DropTableContext ctx) {

		// Verificar que haya una base de datos en uso
		if (currentDatabase != null) {

			// Elminar la tabla de la base de datos
			currentDatabase.deleteTable(ctx.ID().getText());
			MessagePrinter.printUserMessage(String.format("La tabla %s ha sido eliminada de la base de datos %s.", ctx.ID().getText(), currentDatabase.getDatabaseName()));
		} else {
			MessagePrinter.printErrorMessage("No se encuentra ninguna base de datos en uso.");
			return false;
		}

		return true;
	}

	/**
	 * Método para mostrar todas las tablas de una base de datos
	 *
	 * @param ctx Contexto
	 * @return true si se realizó sin errores.
	 */
	@Override
	public Object visitShowTables(SQLGrammarParser.ShowTablesContext ctx) {

		// Verificar que haya una base de datos en uso
		if (currentDatabase != null) {

			MessagePrinter.printUserMessage(String.format("Tablas de %s:", currentDatabase.getDatabaseName()));

			// Mostrar las tablas de la base de datos
			Table[] tablas = currentDatabase.getTables();
			for (int i = 0; i < tablas.length; i++) {

				MessagePrinter.printUserMessage(tablas[i].getTableName());

			}
		} else {
			MessagePrinter.printErrorMessage("No se encuentra ninguna base de datos en uso.");
			return false;
		}
		return true;

	}

	/**
	 * Método para mostrar todas las columnas de una tabla
	 *
	 * @param ctx Contexto
	 * @return true si se realizó sin errores.
	 */
	@Override
	public Object visitShowColumns(SQLGrammarParser.ShowColumnsContext ctx) {

		currentTable = null;

		// Verificar que haya una base de datos en uso
		if (currentDatabase != null) {

			Table[] tablas = currentDatabase.getTables();

			// Encontrar la tabla de la que de desean mostrar las columnas
			for (int i = 0; i < tablas.length; i++) {

				// Encontrar la tabla de la que se desean visualizar las columnas
				if (tablas[i].getTableName().equals(ctx.ID().getText())) {
					currentTable = tablas[i];
				}

			}
			// Verificar que exista la tabla
			if (currentTable == null) {
				MessagePrinter.printErrorMessage(String.format("No existe la tabla %s.", ctx.ID().getText()));
				return false;
			} else {

				MessagePrinter.printUserMessage(String.format("Columnas de la tabla %s:", currentTable.getTableName()));

				// Mostrar todas las columnas
				HashMap<String, DataType> columnas = currentTable.getColumns();
				for (Map.Entry<String, DataType> columnaActual : columnas.entrySet()) {
					MessagePrinter.printUserMessage(String.format("%s %s \n", columnaActual.getKey(), columnaActual.getValue()));
				}
			}
		} else {
			MessagePrinter.printErrorMessage("No se encuentra ninguna base de datos en uso.");
			return false;
		}
		return true;

	}

	/**
	 * Método para modificar una tabla
	 *
	 * @param ctx Contexto
	 * @return true si se realizó sin errores.
	 */
	@Override
	public Object visitAlterTable(SQLGrammarParser.AlterTableContext ctx) {

		currentTable = null;

		// Verificar que haya una base de datos en uso
		if (currentDatabase != null) {

			// Verifificar si es una acción la que se desea realizar
			if (ctx.ID(1) != null) {
				currentDatabase.renameTable(ctx.ID(0).getText(), ctx.ID(1).getText());
				MessagePrinter.printUserMessage(String.format("Tabla %s cambió de nombre a %S.", ctx.ID(0).getText(), ctx.ID(1).getText()));
			} else {

				Table[] tablas = currentDatabase.getTables();

				// Encontrar la tabla que se desea modificar
				for (int i = 0; i < tablas.length; i++) {

					// Encontrar la tabla de la que se desean visualizar las columnas
					if (tablas[i].getTableName().equals(ctx.ID(0).getText())) {
						currentTable = tablas[i];
					}

				}

				// Verificar que exista la tabla
				if (currentTable == null) {
					MessagePrinter.printErrorMessage(String.format("No existe la tabla %s.", ctx.ID(0).getText()));
					return false;
				} else {

					List<ActionContext> acciones = ctx.action();

					// Recorrer todas las acciones
					for (ActionContext accion : acciones) {
						visit(accion);
					}

				}

			}
		} else {
			MessagePrinter.printErrorMessage("No se encuentra ninguna base de datos en uso.");
			return false;
		}
		return true;

	}

	/**
	 * Método para determinar la acción a realizar en la tabla actual
	 *
	 * @param ctx Contexto
	 * @return true si no hay errores
	 */
	@Override
	public Object visitAction(SQLGrammarParser.ActionContext ctx) {

		// Acción agregar columna
		if (ctx.ADD() != null && ctx.COLUMN() != null) {
			String nombreColumna = ctx.ID().getText();
			DataType tipoColumna = (DataType) visit(ctx.columnType());

			currentTable.addColumn(nombreColumna, tipoColumna);
			MessagePrinter.printUserMessage(String.format("Columna %s agregada a la tabla %s.", ctx.ID().getText(), currentTable.getTableName()));

			// Verifica si es necesario agregar una restriccion
			if (tipoColumna == DataType.CHAR) {
				int limiteChar = Integer.parseInt(ctx.columnType().int_literal().NUM().getText());

				/* NOTA: Utiliza UUID para asegurar que el nombre de la restricción sea único. */
				currentTable.addRestriction(UUID.randomUUID().toString(),
					new CharRestriction(
						motor.Util.getCualifiedName(currentTable.getTableName(), nombreColumna),
						limiteChar));
			}

			// Agregar restricciones
			if (ctx.constraintList() != null) {
				visit(ctx.constraintList());
			}

			return true;

		} else if (ctx.ADD() != null && ctx.CONSTRAINT() != null) {

			visit(ctx.constraintType());

			// Acción eliminar columna
		} else if (ctx.DROP() != null && ctx.COLUMN() != null) {

			currentTable.deleteColumn(ctx.ID().getText());
			MessagePrinter.printUserMessage(String.format("Columna %s eliminada de la tabla %s.", ctx.ID(), currentTable.getTableName()));
			return true;

			// Acción eliminar restricción
		} else if (ctx.DROP() != null && ctx.CONSTRAINT() != null) {

			currentTable.deleteRestriction(ctx.ID().getText());
			MessagePrinter.printUserMessage(String.format("Restricción %s eliminada de la tabla %s.", ctx.ID(), currentTable.getTableName()));
			return true;

		}

		return false;

	}

	/**
	 * Método utilizado para realizar inserciones
	 *
	 * @param ctx Contexto
	 * @return true si se realiza todo con éxito
	 */
	@Override
	public Object visitInsertInto(SQLGrammarParser.InsertIntoContext ctx) {

		currentTable = null;

		// Verificar que haya una base de datos en uso
		if (currentDatabase != null) {

			Table[] tablas = currentDatabase.getTables();

			// Encontrar la tabla que se desea modificar
			for (int i = 0; i < tablas.length; i++) {

				// Encontrar la tabla de la que se desean visualizar las columnas
				if (tablas[i].getTableName().equals(ctx.ID().getText())) {
					currentTable = tablas[i];
				}

			}

			// Verificar que exista la tabla
			if (currentTable == null) {
				MessagePrinter.printErrorMessage(String.format("No existe la tabla %s.", ctx.ID().getText()));
				return false;
			} else {

				// Datos a insertar en fila
				ArrayList<Data> datos = new ArrayList<>();
				//Dato[] datos;
				Data dato;

				// Valores que se van a insertar
				ArrayList<Object> valores = (ArrayList<Object>) visit(ctx.valueList());
				String[] nombreColumnas = currentTable.getColumnNames();

				// Caso en el que se especifican columnas
				if (ctx.idList() != null) {

					// Nombres de columnas identificadas
					ArrayList<String> nombres = (ArrayList<String>) visit(ctx.idList());

					// Verifica que existan suficients valores para la cantidad de columnas
					if (valores.size() != nombres.size()) {
						throw new DBMSException("No hay suficientes valores especificados para la cantidad de nombres dados.");
					}

					ArrayList<String> nombreColumnasArreglo = new ArrayList<>(Arrays.asList(nombreColumnas));

					for (String nombre : nombres) {
						if (!(nombreColumnasArreglo.contains(nombre))) {

							MessagePrinter.printErrorMessage(String.format("La columna %s no se encuentra en la tabla %S", nombre, currentTable.getTableName()));
							return false;

						}
					}

					// Recorrer todas las columnas de la tabla actual
					for (String nombreColumna : nombreColumnas) {

						int indice = nombres.indexOf(nombreColumna);

						// Verificar que se encuentre en el listado
						if (indice != -1) {
							dato = new Data(valores.get(indice));
						} else {
							dato = new Data(null);
						}
						datos.add(dato);
					}

					// Caso en el que no se especifican columnas
				} else {
					for (Object valor : valores) {
						dato = new Data(valor);
						datos.add(dato);
					}

				}

				//Construir la fila con los datos obtenidos y agregarla
				currentTable.addRow(new Row(datos.toArray(new Data[0])));
				if (echo) {
					MessagePrinter.printUserMessage(String.format("Insertados %s valores con éxito", datos.size()));
				} else {
					printInsert = true;
					insertCounter += datos.size();
				}
				return true;

			}

		} else {
			MessagePrinter.printErrorMessage("No se encuentra ninguna base de datos en uso.");
			return false;
		}

	}

	/**
	 * Método utilizado para obtener un listado de identificadores
	 *
	 * @param ctx Context
	 * @return ArrayList con los identificadores
	 */
	@Override
	public Object visitIdList(SQLGrammarParser.IdListContext ctx) {

		ArrayList<String> identificadores = new ArrayList<>();
		List<TerminalNode> ids = ctx.ID();

		// Recorrer los ID para agregar su texto
		for (TerminalNode id : ids) {
			identificadores.add(id.getText());
		}

		return identificadores;

	}

	/**
	 * Método que retorna los objetos que se obtienen de expression
	 *
	 * @param ctx Contexto
	 * @return Objetos que se obtienen de expression o null en caso de error
	 */
	@Override
	public Object visitValueList(SQLGrammarParser.ValueListContext ctx) {

		List<ExpressionContext> expresiones = ctx.expression();
		ArrayList<Object> objetos = new ArrayList<>();

		// recorrer todos verificando que cumplan
		for (ExpressionContext expresion : expresiones) {

			Node nodo = (Node) visit(expresion);

			Condition condicionPrueba = new Condition(nodo);
			if (condicionPrueba.getUsedColumns().length == 0) {
				objetos.add(nodo.evaluate(null));
			} else {
				throw new TableException(TableException.ErrorType.ErrorFatal, "No es posible utilizar una referencia para insertar en una tabla");
//                ImpresorMensajes.imprimirMensajeError("No es posible utilizar una referencia para insertar en una tabla");
//                return null;
			}

		}

		return objetos;

	}

	/**
	 * Método para retornar el nodo que se creó en location
	 *
	 * @param ctx Contexto
	 * @return NodoDato con el location
	 */
	@Override
	public Object visitLocationExpr(SQLGrammarParser.LocationExprContext ctx) {
		Node nodo = (Node) visit(ctx.location());
		return nodo;
	}

	/**
	 * Método para obtener un location
	 *
	 * @param ctx Contexto
	 * @return NodoDato con location
	 */
	@Override
	public Object visitLocation(SQLGrammarParser.LocationContext ctx) {

		DataNode nodoRetorno;

		// Verificar si ya es de la forma ID.ID
		if (ctx.ID(1) != null) {
			nodoRetorno = new DataNode(ctx.ID(0).getText() + "." + ctx.ID(1));
		} else {
			nodoRetorno = new DataNode(currentTable.getTableName() + "." + ctx.ID(0));
		}

		return nodoRetorno;

	}

	/**
	 * Método que maneja las literales enteras
	 *
	 * @param ctx Contexto
	 * @return Nodo con el valor de la literal
	 */
	@Override
	public Object visitInt_literal(SQLGrammarParser.Int_literalContext ctx) {
		//Dato valor = new Dato(Integer.valueOf(ctx.NUM().getText()));
		LiteralNode nodoRetorno = new LiteralNode(Integer.valueOf(ctx.NUM().getText()), LiteralType.INT);
		return nodoRetorno;
	}

	/**
	 * Metodo que maneja las litarales string
	 *
	 * @param ctx Contexto
	 * @return Nodo con el valor de la literal
	 */
	@Override
	public Object visitString_literal(SQLGrammarParser.String_literalContext ctx) {
		//Dato valor = new Dato(ctx.STRING().getText());
		String s = ctx.STRING().getText();
		LiteralNode nodoRetorno = new LiteralNode(s.substring(1, s.length() - 1), LiteralType.STRING);
		return nodoRetorno;
	}

	/**
	 * Método que maneja las litereas reales
	 *
	 * @param ctx Contexto
	 * @return Nodo con el valor de la literal
	 */
	@Override
	public Object visitReal_literal(SQLGrammarParser.Real_literalContext ctx) {
		//Dato valor = new Dato(Float.valueOf(ctx.REAL().getText()));
		LiteralNode nodoRetorno = new LiteralNode(Float.valueOf(ctx.REAL().getText()), LiteralType.FLOAT);
		return nodoRetorno;
	}

	/**
	 * Método que maneja las literales null
	 *
	 * @param ctx Contexto
	 * @return Nodo con el valor null
	 */
	@Override
	public Object visitNull_literal(SQLGrammarParser.Null_literalContext ctx) {
		LiteralNode nodoRetorno = new LiteralNode(null, LiteralType.NULL);
		return nodoRetorno;
	}

	/**
	 * Método para manejo de operador negativo
	 *
	 * @param ctx Contexto
	 * @return Nodo de operación 0-expresión
	 */
	@Override
	public Object visitNegExpr(SQLGrammarParser.NegExprContext ctx) {

		Node nodo = (Node) visit(ctx.expression());

		// Instancia de nodo literal
		if (nodo instanceof LiteralNode) {

			if (((LiteralNode) nodo).getType() != LiteralType.STRING) {
				LiteralNode nodoDummy = new LiteralNode(0, LiteralType.INT);
				OperationNode nodoRetorno = new OperationNode(OperationType.Subtraction, nodoDummy, nodo);
				return nodoRetorno;
			}

			// Instancia de nodo operacional
		} else if (nodo instanceof OperationNode) {

			LiteralNode nodoDummy = new LiteralNode(0, LiteralType.INT);
			OperationNode nodoRetorno = new OperationNode(OperationType.Subtraction, nodoDummy, nodo);
			return nodoRetorno;

		} else {

//            ImpresorMensajes.imprimirMensajeError("No se puede utilizar el operador '-' en resultados no numéricos.");
			throw new DBMSException("No se puede utilizar el operador '-' en resultados no numéricos.");
		}
		return null;

	}

	/**
	 * Método para manejo de nodo operacional de *, /, %.
	 *
	 * @param ctx Contexto
	 * @return Nodo operacional con la operación correspondiente
	 */
	@Override
	public Object visitMultdivExpr(SQLGrammarParser.MultdivExprContext ctx) {

		Node nodoIzq = (Node) visit(ctx.expression(0));
		Node nodoDer = (Node) visit(ctx.expression(1));

		// Determinar el tipo de operación que se debe realizar
		OperationType tipo;
		if (ctx.DIV_OP() != null) {
			tipo = OperationType.Division;
		} else if (ctx.MOD_OP() != null) {
			tipo = OperationType.Modulo;
		} else {
			tipo = OperationType.Multiplication;
		}

		boolean cumpleNodoIzq = true;
		boolean cumpleNodoDer = true;

		// Nodo izquierdo
		// Instancia de nodo literal
		if (nodoIzq instanceof LiteralNode) {

			if (((LiteralNode) nodoIzq).getType() == LiteralType.STRING) {
				cumpleNodoIzq = false;
			}

			// Instancia de nodo operacional
		} else if (!(nodoIzq instanceof OperationNode) && !(nodoIzq instanceof DataNode)) {
			cumpleNodoIzq = false;
		}

		// Nodo derecho
		// Instancia de nodo literal
		if (nodoDer instanceof LiteralNode) {

			if (((LiteralNode) nodoDer).getType() == LiteralType.STRING) {
				cumpleNodoDer = false;
			}

			// Instancia de nodo operacional
		} else if (!(nodoDer instanceof OperationNode) && !(nodoDer instanceof DataNode)) {
			cumpleNodoDer = false;
		}

		// Verificar que ambos cumplan para retornar
		if (cumpleNodoIzq && cumpleNodoDer) {
			Node nodoRetorno = new OperationNode(tipo, nodoIzq, nodoDer);
			return nodoRetorno;
		} else {
			throw new DBMSException(String.format("No se puede aplicar '%s' a valores no numéricos.", tipo));
//            ImpresorMensajes.imprimirMensajeError(String.format("No se puede aplicar '%s' a valores no numéricos.", tipo));
//            return null;
		}

	}

	/**
	 * Método para maneje de nodo operacional de +, -.
	 *
	 * @param ctx Contexto
	 * @return Nodo operacional con la operación correspondiente.
	 */
	@Override
	public Object visitAddsubExpr(SQLGrammarParser.AddsubExprContext ctx) {

		Node nodoIzq = (Node) visit(ctx.expression(0));
		Node nodoDer = (Node) visit(ctx.expression(1));

		// Determinar el tipo de operación que se debe realizar
		OperationType tipo;
		if (ctx.SUM_OP() != null) {
			tipo = OperationType.Sum;
		} else {
			tipo = OperationType.Subtraction;
		}

		boolean cumpleNodoIzq = true;
		boolean cumpleNodoDer = true;

		// Nodo izquierdo
		// Instancia de nodo literal
		if (nodoIzq instanceof LiteralNode) {

			if (((LiteralNode) nodoIzq).getType() == LiteralType.STRING) {
				cumpleNodoIzq = false;
			}

			// Instancia de nodo operacional
		} else if (!(nodoIzq instanceof OperationNode) && !(nodoIzq instanceof DataNode)) {
			cumpleNodoIzq = false;
		}

		// Nodo derecho
		// Instancia de nodo literal
		if (nodoDer instanceof LiteralNode) {

			if (((LiteralNode) nodoDer).getType() == LiteralType.STRING) {
				cumpleNodoDer = false;
			}

			// Instancia de nodo operacional
		} else if (!(nodoDer instanceof OperationNode) && !(nodoDer instanceof DataNode)) {
			cumpleNodoDer = false;
		}

		// Verificar que ambos cumplan para retornar
		if (cumpleNodoIzq && cumpleNodoDer) {
			Node nodoRetorno = new OperationNode(tipo, nodoIzq, nodoDer);
			return nodoRetorno;
		} else {
			throw new DBMSException(String.format("No se puede aplicar '%s' a valores no numéricos.", tipo));
//            ImpresorMensajes.imprimirMensajeError(String.format("No se puede aplicar '%s' a valores no numéricos.", tipo));
//            return null;
		}

	}

	/**
	 * Metodo para el manejo de condición not
	 *
	 * @param ctx Contexto
	 * @return NodoRelacional correspondiente a la negación del nodo recibido
	 */
	@Override
	public Object visitNotExpr(SQLGrammarParser.NotExprContext ctx) {

		Node nodo = (Node) visit(ctx.expression());

		// Instancia de nodo literal
		if (nodo instanceof RelationNode || nodo instanceof ConditionalNode) {

			ConditionalNode nodoRetorno = new ConditionalNode(nodo);
			return nodoRetorno;

		} else {
			throw new DBMSException("No se puede utilizar el operador '!' en resultados no booleanos.");
//            ImpresorMensajes.imprimirMensajeError("No se puede utilizar el operador '!' en resultados no booleanos.");

		}
//        return null;

	}

	/**
	 * Método para el manejo de operaciones relacionales menor, mayor,
	 * menorigual, mayorigual
	 *
	 * @param ctx Context
	 * @return Nodo relacional con la operación correspondiente
	 */
	@Override
	public Object visitRelExpr(SQLGrammarParser.RelExprContext ctx) {

		Node nodoIzq = (Node) visit(ctx.expression(0));
		Node nodoDer = (Node) visit(ctx.expression(1));

		// Determinar el tipo de operación que se debe realizar
		RelationType tipo;
		String operador = ctx.REL_OP().getText();

		if (operador.equals("<")) {
			tipo = RelationType.Less;
		} else if (operador.equals(">")) {
			tipo = RelationType.Greater;
		} else if (operador.equals("<=")) {
			tipo = RelationType.LessEqual;
		} else {
			tipo = RelationType.GreaterEqual;
		}

		boolean cumpleNodoIzq = true;
		boolean cumpleNodoDer = true;

		// Nodo izquierdo
		// Instancia de nodo literal
		if (nodoIzq instanceof LiteralNode) {

//            if (((NodoLiteral)nodoIzq).obtenerTipo() == TipoLiteral.STRING){
//                cumpleNodoIzq = false;
//            }
			// Instancia de nodo operacional
		} else if (!(nodoIzq instanceof OperationNode) && !(nodoIzq instanceof DataNode)) {
			cumpleNodoIzq = false;
		}

		// Nodo derecho
		// Instancia de nodo literal
		if (nodoDer instanceof LiteralNode) {

//            if (((NodoLiteral)nodoDer).obtenerTipo() == TipoLiteral.STRING){
//                cumpleNodoDer = false;
//            }
			// Instancia de nodo operacional
		} else if (!(nodoDer instanceof OperationNode) && !(nodoDer instanceof DataNode)) {
			cumpleNodoDer = false;
		}

		// Verificar que ambos cumplan para retornar
		if (cumpleNodoIzq && cumpleNodoDer) {
			Node nodoRetorno = new RelationNode(tipo, nodoIzq, nodoDer);
			return nodoRetorno;
		} else {
			throw new DBMSException(String.format("No se puede aplicar '%s' a valores no numéricos.", tipo));
//            ImpresorMensajes.imprimirMensajeError(String.format("No se puede aplicar '%s' a valores no numéricos.", tipo));
//            return null;
		}

	}

	/**
	 * Método para manejo de operadores relacionales igual, diferente
	 *
	 * @param ctx Context
	 * @return Nodo relacional que representa la operación correspondinte
	 */
	@Override
	public Object visitEqExpr(SQLGrammarParser.EqExprContext ctx) {

		Node nodoIzq = (Node) visit(ctx.expression(0));
		Node nodoDer = (Node) visit(ctx.expression(1));

		// Determinar el tipo de operación que se debe realizar
		RelationType tipo;

		if (ctx.eq_op().EQUALITY_OP() != null) {
			tipo = RelationType.Equal;
		} else {
			tipo = RelationType.Inequal;
		}

		boolean cumpleNodoIzq = true;
		boolean cumpleNodoDer = true;

		// Nodo izquierdo            
		// Instancia de nodo operacional o literal
		if (!(nodoIzq instanceof OperationNode) && !(nodoIzq instanceof LiteralNode) && !(nodoIzq instanceof DataNode)) {
			cumpleNodoIzq = false;
		}

		// Nodo derecho            
		// Instancia de nodo operacional o literal
		if (!(nodoDer instanceof OperationNode) && !(nodoDer instanceof LiteralNode) && !(nodoDer instanceof DataNode)) {
			cumpleNodoDer = false;
		}

		// Verificar que ambos cumplan para retornar
		if (cumpleNodoIzq && cumpleNodoDer) {
			Node nodoRetorno = new RelationNode(tipo, nodoIzq, nodoDer);
			return nodoRetorno;
		} else {
			throw new DBMSException(String.format("No se puede aplicar '%s' a valores no numéricos o string.", tipo));
//            ImpresorMensajes.imprimirMensajeError(String.format("No se puede aplicar '%s' a valores no numéricos o string.", tipo));
//            return null;
		}

	}

	/**
	 * Visita las expresiones con paréntesis.
	 *
	 * @param ctx
	 * @return
	 */
	@Override
	public Object visitParenthesisExpr(SQLGrammarParser.ParenthesisExprContext ctx) {
		return visit(ctx.expression());
	}

	/**
	 * Método que maneja operaciones de tipo AND
	 *
	 * @param ctx Contexto
	 * @return NodoCondicional que representa la operación correspondiente
	 */
	@Override
	public Object visitAndExpr(SQLGrammarParser.AndExprContext ctx) {

		Node nodoIzq = (Node) visit(ctx.expression(0));
		Node nodoDer = (Node) visit(ctx.expression(1));

		boolean cumpleNodoIzq = true;
		boolean cumpleNodoDer = true;

		// Nodo izquierdo            
		// Instancia de nodo operacional o literal
		if (!(nodoIzq instanceof RelationNode) && !(nodoIzq instanceof ConditionalNode) && !(nodoIzq instanceof DataNode)) {
			cumpleNodoIzq = false;
		}

		// Nodo derecho            
		// Instancia de nodo operacional o literal
		if (!(nodoDer instanceof RelationNode) && !(nodoDer instanceof ConditionalNode) && !(nodoDer instanceof DataNode)) {
			cumpleNodoDer = false;
		}

		// Verificar que ambos cumplan para retornar
		if (cumpleNodoIzq && cumpleNodoDer) {
			Node nodoRetorno = new ConditionalNode(ConditionalOperationType.AND, nodoIzq, nodoDer);
			return nodoRetorno;
		} else {
			throw new DBMSException(String.format("No se puede aplicar '%s' a valores no booleanos.", "AND"));
//            ImpresorMensajes.imprimirMensajeError(String.format("No se puede aplicar '%s' a valores no booleanos.", "AND"));
//            return null;
		}

	}

	/**
	 * Método para el manejo de operaciones OR
	 *
	 * @param ctx Contexto
	 * @return Nodo condicional con la operación corresponiente
	 */
	@Override
	public Object visitOrExpr(SQLGrammarParser.OrExprContext ctx) {

		Node nodoIzq = (Node) visit(ctx.expression(0));
		Node nodoDer = (Node) visit(ctx.expression(1));

		boolean cumpleNodoIzq = true;
		boolean cumpleNodoDer = true;

		// Nodo izquierdo            
		// Instancia de nodo operacional o literal
		if (!(nodoIzq instanceof RelationNode) && !(nodoIzq instanceof ConditionalNode) && !(nodoIzq instanceof DataNode)) {
			cumpleNodoIzq = false;
		}

		// Nodo derecho            
		// Instancia de nodo operacional o literal
		if (!(nodoDer instanceof RelationNode) && !(nodoDer instanceof ConditionalNode) && !(nodoDer instanceof DataNode)) {
			cumpleNodoDer = false;
		}

		// Verificar que ambos cumplan para retornar
		if (cumpleNodoIzq && cumpleNodoDer) {
			Node nodoRetorno = new ConditionalNode(ConditionalOperationType.OR, nodoIzq, nodoDer);
			return nodoRetorno;
		} else {
			throw new DBMSException(String.format("No se puede aplicar '%s' a valores no booleanos.", "OR"));
//            ImpresorMensajes.imprimirMensajeError(String.format("No se puede aplicar '%s' a valores no booleanos.", "OR"));
//            return null;
		}

	}

	/**
	 * Método para actualizar tabla
	 *
	 * @param ctx Contexto
	 * @return
	 */
	@Override
	public Object visitUpdate(SQLGrammarParser.UpdateContext ctx) {

		currentTable = null;

		// Verificar que haya una base de datos en uso
		if (currentDatabase != null) {

			Table[] tablas = currentDatabase.getTables();

			// Encontrar la tabla que se desea modificar
			for (int i = 0; i < tablas.length; i++) {

				// Encontrar la tabla de la que se desean visualizar las columnas
				if (tablas[i].getTableName().equals(ctx.ID().getText())) {
					currentTable = tablas[i];
				}

			}

			// Verificar que exista la tabla
			if (currentTable == null) {
				MessagePrinter.printErrorMessage(String.format("No existe la tabla %s.", ctx.ID().getText()));
				return false;
			} else {

				// Obtener la lista de valores
				HashMap<String, Expression> mapa = (HashMap<String, Expression>) visit(ctx.idValueList());

				// Verificar que tenga un where
				if (ctx.WHERE() != null) {

					Condition condicion = new Condition((Node) visit(ctx.expression()));
					int cambios = currentTable.updateRows(mapa, condicion);

					// Imprime mensaje de cambios
					MessagePrinter.printUserMessage(String.format("Se actualizaron %d filas.", cambios));

					return true;

				} else {

					LiteralNode nodoDummy = new LiteralNode("true", LiteralType.STRING);
					Condition condicion = new TrueCondition(nodoDummy);
					int cambios = currentTable.updateRows(mapa, condicion);

					// Imprime mensaje de cambios
					MessagePrinter.printUserMessage(String.format("Se actualizaron %d filas.", cambios));

					return true;

				}

			}

		} else {
			MessagePrinter.printErrorMessage("No se encuentra ninguna base de datos en uso.");
			return false;
		}

	}

	/**
	 * Métood para obtener el listadod e mapas String - Expresion
	 *
	 * @param ctx Contexto
	 * @return Mapa con todos los valores correspondientes
	 */
	@Override
	public Object visitIdValueList(SQLGrammarParser.IdValueListContext ctx) {

		HashMap<String, Expression> mapa = new HashMap<>();

		// Visitar id value y agregar al mapa acutal todos los elementos que retorna
		for (IdValueContext valor : ctx.idValue()) {
			mapa.putAll((HashMap<String, Expression>) visit(valor));
		}

		return mapa;

	}

	/**
	 * Métod para obtener un mapa String - Expresion
	 *
	 * @param ctx Contexto
	 * @return Mapa con los valores correspondientes
	 */
	@Override
	public Object visitIdValue(SQLGrammarParser.IdValueContext ctx) {

		HashMap<String, Expression> mapa = new HashMap<>();
		Expression expresion = new Expression((Node) visit(ctx.expression()));
		mapa.put(currentTable.getTableName() + "." + ctx.ID().getText(), expresion);

		return mapa;

	}

	/**
	 * Método para eliminar filas
	 *
	 * @param ctx Contexto
	 * @return true si se elminan
	 */
	@Override
	public Object visitDeleteFrom(SQLGrammarParser.DeleteFromContext ctx) {

		currentTable = null;

		// Verificar que haya una base de datos en uso
		if (currentDatabase != null) {

			Table[] tablas = currentDatabase.getTables();

			// Encontrar la tabla que se desea modificar
			for (int i = 0; i < tablas.length; i++) {

				// Encontrar la tabla de la que se desean visualizar las columnas
				if (tablas[i].getTableName().equals(ctx.ID().getText())) {
					currentTable = tablas[i];
				}

			}

			// Verificar que exista la tabla
			if (currentTable == null) {
				MessagePrinter.printErrorMessage(String.format("No existe la tabla %s.", ctx.ID().getText()));
				return false;
			} else {

				// Verificar que tenga un where
				if (ctx.WHERE() != null) {

					Condition condicion = new Condition((Node) visit(ctx.expression()));
					int cantidadEliminado = currentTable.deleteRows(condicion);
					MessagePrinter.printUserMessage(String.format("Se han eliminado de la tabla %s %d fila(s).", currentTable.getTableName(), cantidadEliminado));
					return true;

				} else {

					LiteralNode nodoDummy = new LiteralNode("true", LiteralType.STRING);
					Condition condicion = new TrueCondition(nodoDummy);
					currentTable.deleteRows(condicion);
					MessagePrinter.printUserMessage(String.format("Se han elminado todas las filas de la tabla %s", currentTable.getTableName()));
					return true;

				}

			}

		} else {
			MessagePrinter.printErrorMessage("No se encuentra ninguna base de datos en uso.");
			return false;
		}

	}

	/**
	 * Método para realizar un select
	 *
	 * @param ctx Contexto
	 * @return true si se ejecuta con éxito
	 */
	@Override
	public Object visitSelect(SQLGrammarParser.SelectContext ctx) {

		currentTable = null;
		Relation relacionFinal;

		// Verificar que haya una base de datos en uso
		if (currentDatabase != null) {

			ArrayList<String> tablas;
			boolean all = true;

			// Operar el from
			if (ctx.idList(1) != null) {
				tablas = (ArrayList<String>) visit(ctx.idList(1));
				all = false;
			} else {
				tablas = (ArrayList<String>) visit(ctx.idList(0));
			}
			ArrayList<Relation> relacionesTablas = new ArrayList<>();
			ArrayList<String[]> columnasTabla = new ArrayList<>();

			Relation relacionFrom = null;
			Relation relacionWhere = null;

			Table[] tablasTotales = currentDatabase.getTables();

			for (String tablaActualCiclo : tablas) {

				Relation relacionTemporal = null;

				// Encontrar la tabla que se desea modificar
				for (int i = 0; i < tablasTotales.length; i++) {

					// Encontrar la tabla de la que se desean visualizar las columnas
					if (tablasTotales[i].getTableName().equals(tablaActualCiclo)) {
						relacionTemporal = tablasTotales[i].getRelation();
						currentTable = tablasTotales[i];
						columnasTabla.add(tablasTotales[i].getColumnNames());
					}

				}

				if (relacionTemporal == null) {
					throw new TableException(TableException.ErrorType.TableDoesNotExist, tablaActualCiclo);
//                    ImpresorMensajes.imprimirMensajeError(String.format("No existe la tabla %s.", tablaActualCiclo));
//                    return null;
				} else {

					// Llenar el arreglo de relaciones con la relación temporal
					relacionesTablas.add(relacionTemporal);

				}

			}

			// Cuando es relación única
			if (relacionesTablas.size() == 1) {
				relacionFrom = relacionesTablas.get(0);
			} else {

				int cont = 1;
				boolean fin = false;
				CrossProductRelation relTemp = null;

				while (!fin) {

					if (cont == 1) {
						relTemp = new CrossProductRelation(relacionesTablas.get(cont - 1), relacionesTablas.get(cont));
					} else {
						relTemp = new CrossProductRelation(relTemp, relacionesTablas.get(cont));
					}

					cont++;

					// Verificar si ya ese pasó del índice para deter el ciclo
					if (cont >= relacionesTablas.size()) {
						fin = true;
						relacionFrom = relTemp;
					}

				}

			}

			// Operar el where
			if (ctx.WHERE() != null) {

				// Contruir la relación de where si existe
				Condition condicion = new Condition((Node) visit(ctx.expression()));
				relacionWhere = new FilteredRelation(relacionFrom, condicion);

			} else {

				relacionWhere = relacionFrom;

			}

			// Operar el select 
			ArrayList<String> columnasSelect = new ArrayList<>();

			boolean existe = true;

			if (!all) {

				ArrayList<String> col = (ArrayList<String>) visit(ctx.idList(0));

				// Recorrer cada una de las columnas
				for (String columna : col) {
					int c = 0;

					// Recorrer
					for (String[] tabla : columnasTabla) {
						for (int e = 0; e < tabla.length; e++) {

							if (tabla[e].equals(columna)) {
								// Agregra tabla.columna
								columnasSelect.add(tablas.get(c) + "." + columna);
								break;
							}

						}
						c++;
					}
				}

				if (col.size() != columnasSelect.size()) {

					MessagePrinter.printErrorMessage("Las columnas especificadas no existen en la relación");
					return false;

				}

				relacionFinal = new ProjectedRelation(relacionWhere, columnasSelect.toArray(new String[0]));
				//ImpresorMensajes.imprimirRelacion(relacionFinal);

				//return true;
				// caso select *    
			} else {

				relacionFinal = relacionWhere;
				//ImpresorMensajes.imprimirRelacion(relacionWhere);
				//return true;
			}

			// Operar el order
			if (ctx.ORDER() != null) {

				LinkedHashMap<String, OrderType> mapa = (LinkedHashMap<String, OrderType>) visit(ctx.orderList());
				Relation relacionOrdenamiento = new OrderedRelation(relacionFinal, mapa);
				MessagePrinter.imprimirRelacion(relacionOrdenamiento);
				return true;

			} else {

				MessagePrinter.imprimirRelacion(relacionFinal);
				return true;

			}

			//return true;
		} else {
			MessagePrinter.printErrorMessage("No se encuentra ninguna base de datos en uso.");
			return false;
		}

	}

	/**
	 * Método que obtiene el listado de ordenamientos
	 *
	 * @param ctx Contexto
	 * @return Listado con LinkedHashMaps correspondientes
	 */
	@Override
	public Object visitOrderList(SQLGrammarParser.OrderListContext ctx) {

		LinkedHashMap<String, OrderType> mapa = new LinkedHashMap<>();

		// Visitar id value y agregar al mapa acutal todos los elementos que retorna
		for (OrderExpContext valor : ctx.orderExp()) {
			mapa.putAll((HashMap<String, OrderType>) visit(valor));
		}

		return mapa;
	}

	/**
	 * Método que obtiene un LinkedHashMap representando el orden
	 *
	 * @param ctx Contexto
	 * @return LinkedHashMap con los datos correspondientes
	 */
	@Override
	public Object visitOrderExp(SQLGrammarParser.OrderExpContext ctx) {

		HashMap<String, OrderType> mapa = new HashMap<>();
		String campo;
		Node nodo = (Node) visit(ctx.expression());

		// Verificar que sea un campo de tabla
		if (nodo instanceof DataNode) {

			campo = ((DataNode) nodo).toString();

		} else {

			throw new DatabaseException("Solamente se puede ordernar por medio de campos de la tabla");

		}

		// Verificar tipo de ordenamiento
		OrderType tipoOrdenamiento;
		if (ctx.orderType() != null) {
			if (ctx.orderType().DESC() != null) {
				tipoOrdenamiento = OrderType.DESCENDING;
			} else {
				tipoOrdenamiento = OrderType.ASCENDING;
			}
		} else {
			tipoOrdenamiento = OrderType.ASCENDING;
		}

		//LinkedHashMap a retornar
		mapa.put(campo, tipoOrdenamiento);

		return mapa;

	}

	/**
	 * Método para manejar activación/desactivación de echo
	 *
	 * @param ctx Contexto
	 * @return true si se activa, false si se desactiva
	 */
	@Override
	public Object visitEcho(SQLGrammarParser.EchoContext ctx) {
		if (ctx.ENABLED() != null) {
			echo = true;
			return true;
		} else {
			echo = false;
			return false;
		}
	}

}
