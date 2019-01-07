package visitor;

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

			// Create table in current database
			currentTable = currentDatabase.addNewTable(ctx.ID(0).getText());
			MessagePrinter.printUserMessage(String.format("Table %s created in database %s.", ctx.ID(0).getText(), currentDatabase.getDatabaseName()));

			// Get the column types
			List<TerminalNode> idList = ctx.ID();

			int counter = 0;
			// Creates every column in the table
			for (TerminalNode id : idList) {

				if (counter != 0) {
					String columnName = id.getText();
					DataType columnType = (DataType) visit(ctx.columnType(counter - 1));
					currentTable.addColumn(columnName, columnType);

					// Checks if it is necessary to add a restriction
					if (columnType == DataType.CHAR) {
						int charLimit = Integer.parseInt(ctx.columnType(counter - 1).int_literal().NUM().getText());

						/* NOTE: Uses an UUID to make sure that the restriction name is unique */
						currentTable.addRestriction(UUID.randomUUID().toString(),
							new CharRestriction(
								motor.Util.getCualifiedName(currentTable.getTableName(), columnName),
								charLimit));
					}

					if (echo) {
						MessagePrinter.printUserMessage(String.format("Column %s added to the table %s.", id.getText(), currentTable.getTableName()));
					}

				}
				counter++;

			}

			// Add restrictions
			List<ConstraintListContext> restrictions = ctx.constraintList();
			for (ConstraintListContext currentRestriction : restrictions) {
				visit(currentRestriction);
			}

		} else {
			MessagePrinter.printErrorMessage("No database selected.");
			return false;
		}
		return true;

	}

	@Override
	public Object visitConstraintList(SQLGrammarParser.ConstraintListContext ctx) {
		// Adds all restrictions
		List<ConstraintTypeContext> restrictions = ctx.constraintType();

		for (ConstraintTypeContext currentRestriction : restrictions) {
			visit(currentRestriction);
		}

		return true;
	}

	@Override
	public Object visitConstraintType(SQLGrammarParser.ConstraintTypeContext ctx) {

		// Check constraint type
		if (ctx.PRIMARY() != null) {

			// Get references and create the restriction
			ArrayList<String> fields = (ArrayList<String>) visit(ctx.idList(0));
			ArrayList<String> qualifiedNames = new ArrayList<>();

			for (String currentField : fields) {
				qualifiedNames.add(currentTable.getTableName() + "." + currentField);
			}

			Restriction primaryKeyRestriction = new PrimaryKeyRestriction(qualifiedNames.toArray(new String[0]));

			// Adds the restriction
			currentTable.addRestriction(ctx.ID(0).getText(), primaryKeyRestriction);
			MessagePrinter.printUserMessage(String.format("Restriction %s added to the table %s.", ctx.ID(0).getText(), currentTable.getTableName()));

			return true;
		} else if (ctx.FOREIGN() != null) {

			// TODO
			return null;

		} // CHECK
		else {
			// Create and add restriction
			Condition checkCondition = new Condition((Node) visit(ctx.expression()));
			Restriction checkRestriction = new CheckRestriction(checkCondition);

			currentTable.addRestriction(ctx.ID(0).getText(), checkRestriction);
			MessagePrinter.printUserMessage(String.format("Restriction %s added to the table %s.", ctx.ID(0).getText(), currentTable.getTableName()));

			return true;
		}

	}

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

			// TODO: Char must add a new restriction
		}
	}

	@Override
	public Object visitDropTable(SQLGrammarParser.DropTableContext ctx) {

		// Check for selected database
		if (currentDatabase != null) {

			// Deletes the table
			currentDatabase.deleteTable(ctx.ID().getText());
			MessagePrinter.printUserMessage(String.format("Table %s eliminated from database 5s.", ctx.ID().getText(), currentDatabase.getDatabaseName()));
		} else {
			MessagePrinter.printErrorMessage("No database selected.");
			return false;
		}

		return true;
	}

	@Override
	public Object visitShowTables(SQLGrammarParser.ShowTablesContext ctx) {
		if (currentDatabase != null) {
			MessagePrinter.printUserMessage(String.format("Tables of %s:", currentDatabase.getDatabaseName()));

			// Show tables
			Table[] tables = currentDatabase.getTables();
			for (int i = 0; i < tables.length; i++) {

				MessagePrinter.printUserMessage(tables[i].getTableName());

			}
		} else {
			MessagePrinter.printErrorMessage("No database selected.");
			return false;
		}
		return true;

	}

	@Override
	public Object visitShowColumns(SQLGrammarParser.ShowColumnsContext ctx) {
		currentTable = null;
		if (currentDatabase != null) {

			Table[] tables = currentDatabase.getTables();

			// Find current table
			for (int i = 0; i < tables.length; i++) {
				if (tables[i].getTableName().equals(ctx.ID().getText())) {
					currentTable = tables[i];
				}
			}

			// Check that table exists
			if (currentTable == null) {
				MessagePrinter.printErrorMessage(String.format("Unexistent table %s.", ctx.ID().getText()));
				return false;
			} else {
				MessagePrinter.printUserMessage(String.format("Columns in table %s:", currentTable.getTableName()));

				// Get all columns and print
				HashMap<String, DataType> columnas = currentTable.getColumns();
				for (Map.Entry<String, DataType> columnaActual : columnas.entrySet()) {
					MessagePrinter.printUserMessage(String.format("\t%s %s \n", columnaActual.getKey(), columnaActual.getValue()));
				}
			}
		} else {
			MessagePrinter.printErrorMessage("No database selected.");
			return false;
		}
		return true;

	}

	@Override
	public Object visitAlterTable(SQLGrammarParser.AlterTableContext ctx) {
		currentTable = null;
		if (currentDatabase != null) {

			// Check action to make
			if (ctx.ID(1) != null) {
				currentDatabase.renameTable(ctx.ID(0).getText(), ctx.ID(1).getText());
				MessagePrinter.printUserMessage(String.format("Table %s renamed to %S.", ctx.ID(0).getText(), ctx.ID(1).getText()));
			} else {

				Table[] tablas = currentDatabase.getTables();

				// Find table to modify
				for (int i = 0; i < tablas.length; i++) {
					if (tablas[i].getTableName().equals(ctx.ID(0).getText())) {
						currentTable = tablas[i];
					}
				}

				// Verify that table exists and get the actions
				if (currentTable == null) {
					MessagePrinter.printErrorMessage(String.format("Unexistent table %s", ctx.ID(0).getText()));
					return false;
				} else {
					List<ActionContext> actions = ctx.action();
					for (ActionContext accion : actions) {
						visit(accion);
					}

				}

			}
		} else {
			MessagePrinter.printErrorMessage("No database selected.");
			return false;
		}
		return true;

	}

	@Override
	public Object visitAction(SQLGrammarParser.ActionContext ctx) {
		// Add column action
		if (ctx.ADD() != null && ctx.COLUMN() != null) {
			String columnName = ctx.ID().getText();
			DataType columnType = (DataType) visit(ctx.columnType());

			currentTable.addColumn(columnName, columnType);
			MessagePrinter.printUserMessage(String.format("Column %s added to the table %s.", ctx.ID().getText(), currentTable.getTableName()));

			// Limit char
			if (columnType == DataType.CHAR) {
				int charLimit = Integer.parseInt(ctx.columnType().int_literal().NUM().getText());

				/* NOTE: Uses UUID to make the restriction name unique */
				currentTable.addRestriction(UUID.randomUUID().toString(),
					new CharRestriction(
						motor.Util.getCualifiedName(currentTable.getTableName(), columnName),
						charLimit));
			}

			// Add constraint
			if (ctx.constraintList() != null) {
				visit(ctx.constraintList());
			}

			return true;

		} else if (ctx.ADD() != null && ctx.CONSTRAINT() != null) {

			visit(ctx.constraintType());

			// Eliminate column action
		} else if (ctx.DROP() != null && ctx.COLUMN() != null) {
			currentTable.deleteColumn(ctx.ID().getText());
			MessagePrinter.printUserMessage(String.format("Column %s eliminated from table %s.", ctx.ID(), currentTable.getTableName()));
			return true;

			// Eliminate contraint
		} else if (ctx.DROP() != null && ctx.CONSTRAINT() != null) {
			currentTable.deleteRestriction(ctx.ID().getText());
			MessagePrinter.printUserMessage(String.format("Constraint %s eliminated from table %s.", ctx.ID(), currentTable.getTableName()));
			return true;
		}
		return false;

	}

	@Override
	public Object visitInsertInto(SQLGrammarParser.InsertIntoContext ctx) {
		currentTable = null;

		// Verififies selected database
		if (currentDatabase != null) {
			Table[] tables = currentDatabase.getTables();

			// Find the table to modify
			for (int i = 0; i < tables.length; i++) {
				if (tables[i].getTableName().equals(ctx.ID().getText())) {
					currentTable = tables[i];
				}
			}

			// Check that table exists
			if (currentTable == null) {
				MessagePrinter.printErrorMessage(String.format("Table %s does not exist.", ctx.ID().getText()));
				return false;
			} else {
				// Data to instert to the row
				ArrayList<Data> data = new ArrayList<>();
				Data datum;

				// Values to insert
				ArrayList<Object> values = (ArrayList<Object>) visit(ctx.valueList());
				String[] tableColumnNames = currentTable.getColumnNames();

				// Case: Column names were specified
				if (ctx.idList() != null) {
					// Column names
					ArrayList<String> specifiedColumnNames = (ArrayList<String>) visit(ctx.idList());

					// Check if enough values were specified
					if (values.size() != specifiedColumnNames.size()) {
						throw new DBMSException("Not enough values specified for the given columsn.");
					}

					ArrayList<String> tableColumnNamesList = new ArrayList<>(Arrays.asList(tableColumnNames));

					for (String currentName : specifiedColumnNames) {
						if (!(tableColumnNamesList.contains(currentName))) {
							MessagePrinter.printErrorMessage(String.format("Column %s not found in table %S.", currentName, currentTable.getTableName()));
							return false;
						}
					}

					// Recorrer todas las columnas de la tabla actual
					// Check columns in current table
					for (String currentColumn : tableColumnNames) {
						int index = specifiedColumnNames.indexOf(currentColumn);

						// Check that the name exists in the list
						if (index != -1) {
							datum = new Data(values.get(index));
						} else {
							datum = new Data(null);
						}
						data.add(datum);
					}

					// Case: Columns were not specified
				} else {
					for (Object currentValue : values) {
						datum = new Data(currentValue);
						data.add(datum);
					}

				}

				// Construct the row with the given values
				currentTable.addRow(new Row(data.toArray(new Data[0])));
				if (echo) {
					MessagePrinter.printUserMessage(String.format("%s values were insterted.", data.size()));
				} else {
					printInsert = true;
					insertCounter += data.size();
				}
				return true;
			}
		} else {
			MessagePrinter.printErrorMessage("No database selected.");
			return false;
		}

	}

	@Override
	public Object visitIdList(SQLGrammarParser.IdListContext ctx) {
		ArrayList<String> identifiers = new ArrayList<>();
		List<TerminalNode> ids = ctx.ID();

		// Traverse ids to get associated text
		for (TerminalNode id : ids) {
			identifiers.add(id.getText());
		}

		return identifiers;
	}

	@Override
	public Object visitValueList(SQLGrammarParser.ValueListContext ctx) {
		List<ExpressionContext> expressions = ctx.expression();
		ArrayList<Object> objetos = new ArrayList<>();

		for (ExpressionContext expresion : expressions) {
			Node node = (Node) visit(expresion);

			Condition testCondition = new Condition(node);
			if (testCondition.getUsedColumns().length == 0) {
				objetos.add(node.evaluate(null));
			} else {
				throw new TableException(TableException.ErrorType.ErrorFatal, "Reference cannot be inserted in a table.");
			}
		}

		return objetos;
	}

	@Override
	public Object visitLocationExpr(SQLGrammarParser.LocationExprContext ctx) {
		Node node = (Node) visit(ctx.location());
		return node;
	}

	@Override
	public Object visitLocation(SQLGrammarParser.LocationContext ctx) {
		DataNode returnNode;

		// Verify if it is a qualified name
		if (ctx.ID(1) != null) {
			returnNode = new DataNode(ctx.ID(0).getText() + "." + ctx.ID(1));
		} else {
			returnNode = new DataNode(currentTable.getTableName() + "." + ctx.ID(0));
		}

		return returnNode;
	}

	@Override
	public Object visitInt_literal(SQLGrammarParser.Int_literalContext ctx) {
		LiteralNode returnNode = new LiteralNode(Integer.valueOf(ctx.NUM().getText()), LiteralType.INT);
		return returnNode;
	}

	@Override
	public Object visitString_literal(SQLGrammarParser.String_literalContext ctx) {
		String s = ctx.STRING().getText();
		LiteralNode returnNode = new LiteralNode(s.substring(1, s.length() - 1), LiteralType.STRING);
		return returnNode;
	}

	@Override
	public Object visitReal_literal(SQLGrammarParser.Real_literalContext ctx) {
		LiteralNode returnNode = new LiteralNode(Float.valueOf(ctx.REAL().getText()), LiteralType.FLOAT);
		return returnNode;
	}

	@Override
	public Object visitNull_literal(SQLGrammarParser.Null_literalContext ctx) {
		LiteralNode returnNode = new LiteralNode(null, LiteralType.NULL);
		return returnNode;
	}

	@Override
	public Object visitNegExpr(SQLGrammarParser.NegExprContext ctx) {
		Node node = (Node) visit(ctx.expression());

		// Literal node instance
		if (node instanceof LiteralNode) {
			if (((LiteralNode) node).getType() != LiteralType.STRING) {
				LiteralNode dummyNode = new LiteralNode(0, LiteralType.INT);
				OperationNode returnNode = new OperationNode(OperationType.Subtraction, dummyNode, node);
				return returnNode;
			}

			// Operational node instance
		} else if (node instanceof OperationNode) {
			LiteralNode dummyNode = new LiteralNode(0, LiteralType.INT);
			OperationNode returnNode = new OperationNode(OperationType.Subtraction, dummyNode, node);
			return returnNode;
		} else {
			throw new DBMSException("Cannot use the operator '-' in not numeric results.");
		}
		return null;

	}

	@Override
	public Object visitMultdivExpr(SQLGrammarParser.MultdivExprContext ctx) {
		Node leftNode = (Node) visit(ctx.expression(0));
		Node rightNode = (Node) visit(ctx.expression(1));

		// Verify type of operation to make
		OperationType operationType;
		if (ctx.DIV_OP() != null) {
			operationType = OperationType.Division;
		} else if (ctx.MOD_OP() != null) {
			operationType = OperationType.Modulo;
		} else {
			operationType = OperationType.Multiplication;
		}

		boolean leftNodeOK = true;
		boolean rightNodeOK = true;

		// LEFT NODE
		// Literal node instance
		if (leftNode instanceof LiteralNode) {
			if (((LiteralNode) leftNode).getType() == LiteralType.STRING) {
				leftNodeOK = false;
			}

			// Operation node instance
		} else if (!(leftNode instanceof OperationNode) && !(leftNode instanceof DataNode)) {
			leftNodeOK = false;
		}

		// RIGHT NODE
		// Literal node instance
		if (rightNode instanceof LiteralNode) {

			if (((LiteralNode) rightNode).getType() == LiteralType.STRING) {
				rightNodeOK = false;
			}

			// Operation node instance
		} else if (!(rightNode instanceof OperationNode) && !(rightNode instanceof DataNode)) {
			rightNodeOK = false;
		}

		// Verify that both are ok in order to continue
		if (leftNodeOK && rightNodeOK) {
			Node returnNode = new OperationNode(operationType, leftNode, rightNode);
			return returnNode;
		} else {
			throw new DBMSException(String.format("Cannot do '%s' in non numeric data.", operationType));
		}
	}

	@Override
	public Object visitAddsubExpr(SQLGrammarParser.AddsubExprContext ctx) {
		Node leftNode = (Node) visit(ctx.expression(0));
		Node rightNode = (Node) visit(ctx.expression(1));

		// Check operation type
		OperationType tipo;
		if (ctx.SUM_OP() != null) {
			tipo = OperationType.Sum;
		} else {
			tipo = OperationType.Subtraction;
		}

		boolean leftNodeOK = true;
		boolean rightNodeOK = true;

		// LEFT NODE
		// Literal node instance
		if (leftNode instanceof LiteralNode) {

			if (((LiteralNode) leftNode).getType() == LiteralType.STRING) {
				leftNodeOK = false;
			}

			// Operation node instance
		} else if (!(leftNode instanceof OperationNode) && !(leftNode instanceof DataNode)) {
			leftNodeOK = false;
		}

		// RIGHT NODE
		// Literal node instance
		if (rightNode instanceof LiteralNode) {
			if (((LiteralNode) rightNode).getType() == LiteralType.STRING) {
				rightNodeOK = false;
			}

			// Operation node instance
		} else if (!(rightNode instanceof OperationNode) && !(rightNode instanceof DataNode)) {
			rightNodeOK = false;
		}

		// Verify that both are OK
		if (leftNodeOK && rightNodeOK) {
			Node returnNode = new OperationNode(tipo, leftNode, rightNode);
			return returnNode;
		} else {
			throw new DBMSException(String.format("Cannot do '%s' in non numeric data.", tipo));
		}
	}

	@Override
	public Object visitNotExpr(SQLGrammarParser.NotExprContext ctx) {
		Node node = (Node) visit(ctx.expression());

		if (node instanceof RelationNode || node instanceof ConditionalNode) {
			ConditionalNode returnNode = new ConditionalNode(node);
			return returnNode;
		} else {
			throw new DBMSException("Cannot do '!' over non boolean data.");
		}
	}

	@Override
	public Object visitRelExpr(SQLGrammarParser.RelExprContext ctx) {
		Node leftNode = (Node) visit(ctx.expression(0));
		Node rightNode = (Node) visit(ctx.expression(1));

		// Check operation type
		RelationType type;
		String operator = ctx.REL_OP().getText();

		if (operator.equals("<")) {
			type = RelationType.Less;
		} else if (operator.equals(">")) {
			type = RelationType.Greater;
		} else if (operator.equals("<=")) {
			type = RelationType.LessEqual;
		} else {
			type = RelationType.GreaterEqual;
		}

		boolean leftNodeOK = true;
		boolean rightNodeOK = true;

		// LEFT NODE
		// Literal node instance
		if (leftNode instanceof LiteralNode) {
			// Operation node instance
		} else if (!(leftNode instanceof OperationNode) && !(leftNode instanceof DataNode)) {
			leftNodeOK = false;
		}

		// RIGHT NODE
		// Literal node instance
		if (rightNode instanceof LiteralNode) {

			// Operation node instance
		} else if (!(rightNode instanceof OperationNode) && !(rightNode instanceof DataNode)) {
			rightNodeOK = false;
		}

		// Verify that both are ok
		if (leftNodeOK && rightNodeOK) {
			Node returnNode = new RelationNode(type, leftNode, rightNode);
			return returnNode;
		} else {
			throw new DBMSException(String.format("Cannot do '%s' over non numeric data.", type));
		}
	}

	@Override
	public Object visitEqExpr(SQLGrammarParser.EqExprContext ctx) {
		Node leftNode = (Node) visit(ctx.expression(0));
		Node rightNode = (Node) visit(ctx.expression(1));

		// Check relation type
		RelationType type;

		if (ctx.eq_op().EQUALITY_OP() != null) {
			type = RelationType.Equal;
		} else {
			type = RelationType.Inequal;
		}

		boolean leftNodeOK = true;
		boolean rightNodeOK = true;

		// LEFT NODE
		// Operation or literal
		if (!(leftNode instanceof OperationNode) && !(leftNode instanceof LiteralNode) && !(leftNode instanceof DataNode)) {
			leftNodeOK = false;
		}

		// RIGHT NODE
		// Operation or literal
		if (!(rightNode instanceof OperationNode) && !(rightNode instanceof LiteralNode) && !(rightNode instanceof DataNode)) {
			rightNodeOK = false;
		}

		// Check that both are ok
		if (leftNodeOK && rightNodeOK) {
			Node returnNode = new RelationNode(type, leftNode, rightNode);
			return returnNode;
		} else {
			throw new DBMSException(String.format("Cannot do '%s' over non numeric data.", type));
		}

	}

	@Override
	public Object visitParenthesisExpr(SQLGrammarParser.ParenthesisExprContext ctx) {
		return visit(ctx.expression());
	}

	@Override
	public Object visitAndExpr(SQLGrammarParser.AndExprContext ctx) {
		Node leftNode = (Node) visit(ctx.expression(0));
		Node rightNode = (Node) visit(ctx.expression(1));

		boolean leftNodeOK = true;
		boolean rightNodeOK = true;

		// LEFT NODE 
		// Operation or literal
		if (!(leftNode instanceof RelationNode) && !(leftNode instanceof ConditionalNode) && !(leftNode instanceof DataNode)) {
			leftNodeOK = false;
		}

		// RIGHT NODE
		// Operation or literal
		if (!(rightNode instanceof RelationNode) && !(rightNode instanceof ConditionalNode) && !(rightNode instanceof DataNode)) {
			rightNodeOK = false;
		}

		// Verify both ok
		if (leftNodeOK && rightNodeOK) {
			Node returnNode = new ConditionalNode(ConditionalOperationType.AND, leftNode, rightNode);
			return returnNode;
		} else {
			throw new DBMSException(String.format("Cannot do '%s' over non boolean data.", "AND"));
		}

	}

	@Override
	public Object visitOrExpr(SQLGrammarParser.OrExprContext ctx) {
		Node leftNode = (Node) visit(ctx.expression(0));
		Node rightNode = (Node) visit(ctx.expression(1));

		boolean leftNodeOK = true;
		boolean rightNodeOK = true;

		// LEFT NODE
		// Operation or literal
		if (!(leftNode instanceof RelationNode) && !(leftNode instanceof ConditionalNode) && !(leftNode instanceof DataNode)) {
			leftNodeOK = false;
		}

		// RIGHT NODE
		// Operation or literal
		if (!(rightNode instanceof RelationNode) && !(rightNode instanceof ConditionalNode) && !(rightNode instanceof DataNode)) {
			rightNodeOK = false;
		}

		// Verify both ok
		if (leftNodeOK && rightNodeOK) {
			Node returnNode = new ConditionalNode(ConditionalOperationType.OR, leftNode, rightNode);
			return returnNode;
		} else {
			throw new DBMSException(String.format("Cannot do '%s' over non boolean data.", "OR"));
		}

	}

	@Override
	public Object visitUpdate(SQLGrammarParser.UpdateContext ctx) {
		currentTable = null;

		if (currentDatabase != null) {

			Table[] tables = currentDatabase.getTables();

			// Find table to modify
			for (int i = 0; i < tables.length; i++) {
				if (tables[i].getTableName().equals(ctx.ID().getText())) {
					currentTable = tables[i];
				}
			}

			// Verify that table exists
			if (currentTable == null) {
				MessagePrinter.printErrorMessage(String.format("Unexistent table %s.", ctx.ID().getText()));
				return false;
			} else {
				// Retrieve value list
				HashMap<String, Expression> mapa = (HashMap<String, Expression>) visit(ctx.idValueList());

				// Verify WHERE clause
				if (ctx.WHERE() != null) {
					Condition condition = new Condition((Node) visit(ctx.expression()));
					int cambios = currentTable.updateRows(mapa, condition);

					MessagePrinter.printUserMessage(String.format("%d rows were updated.", cambios));

					return true;
				} else {
					LiteralNode dummyNode = new LiteralNode("true", LiteralType.STRING);
					Condition condition = new TrueCondition(dummyNode);
					int changes = currentTable.updateRows(mapa, condition);

					MessagePrinter.printUserMessage(String.format("%d rows where updated.", changes));

					return true;
				}
			}
		} else {
			MessagePrinter.printErrorMessage("No database selected.");
			return false;
		}
	}

	@Override
	public Object visitIdValueList(SQLGrammarParser.IdValueListContext ctx) {
		HashMap<String, Expression> map = new HashMap<>();

		// Visit id and value and add it to the map
		for (IdValueContext currentValue : ctx.idValue()) {
			map.putAll((HashMap<String, Expression>) visit(currentValue));
		}

		return map;

	}

	@Override
	public Object visitIdValue(SQLGrammarParser.IdValueContext ctx) {
		HashMap<String, Expression> map = new HashMap<>();
		Expression expression = new Expression((Node) visit(ctx.expression()));
		map.put(currentTable.getTableName() + "." + ctx.ID().getText(), expression);

		return map;
	}

	@Override
	public Object visitDeleteFrom(SQLGrammarParser.DeleteFromContext ctx) {
		currentTable = null;

		if (currentDatabase != null) {
			Table[] tables = currentDatabase.getTables();

			// Find table to modify
			for (int i = 0; i < tables.length; i++) {
				if (tables[i].getTableName().equals(ctx.ID().getText())) {
					currentTable = tables[i];
				}
			}

			// Verify that table exists
			if (currentTable == null) {
				MessagePrinter.printErrorMessage(String.format("Unexistent table %s.", ctx.ID().getText()));
				return false;
			} else {
				// Check that WHERE clause exists
				if (ctx.WHERE() != null) {
					Condition condition = new Condition((Node) visit(ctx.expression()));
					int eliminatedRows = currentTable.deleteRows(condition);
					MessagePrinter.printUserMessage(String.format("%d row(s) were eliminated from table %s.", eliminatedRows, currentTable.getTableName()));
					return true;
				} else {
					LiteralNode dummyNode = new LiteralNode("true", LiteralType.STRING);
					Condition condition = new TrueCondition(dummyNode);
					currentTable.deleteRows(condition);
					MessagePrinter.printUserMessage(String.format("All rows were eliminated from table %s.", currentTable.getTableName()));
					return true;
				}
			}
		} else {
			MessagePrinter.printErrorMessage("No database selected.");
			return false;
		}
	}

	@Override
	public Object visitSelect(SQLGrammarParser.SelectContext ctx) {
		currentTable = null;
		Relation finalRelation;

		if (currentDatabase != null) {

			ArrayList<String> tables;
			boolean all = true;

			// Operate FROM clause
			if (ctx.idList(1) != null) {
				tables = (ArrayList<String>) visit(ctx.idList(1));
				all = false;
			} else {
				tables = (ArrayList<String>) visit(ctx.idList(0));
			}
			ArrayList<Relation> tableRelations = new ArrayList<>();
			ArrayList<String[]> tableColumns = new ArrayList<>();

			Relation fromRelation = null;
			Relation whereRelation = null;

			Table[] allTables = currentDatabase.getTables();

			for (String currentTableCycle : tables) {
				Relation tempRelation = null;

				// Find table to modify
				for (int i = 0; i < allTables.length; i++) {
					// Find table to visualize
					if (allTables[i].getTableName().equals(currentTableCycle)) {
						tempRelation = allTables[i].getRelation();
						currentTable = allTables[i];
						tableColumns.add(allTables[i].getColumnNames());
					}
				}

				if (tempRelation == null) {
					throw new TableException(TableException.ErrorType.TableDoesNotExist, currentTableCycle);
				} else {
					tableRelations.add(tempRelation);
				}
			}

			// If is an unique relation
			if (tableRelations.size() == 1) {
				fromRelation = tableRelations.get(0);
			} else {
				int counter = 1;
				boolean end = false;
				CrossProductRelation temporaryRelation = null;

				while (!end) {
					if (counter == 1) {
						temporaryRelation = new CrossProductRelation(tableRelations.get(counter - 1), tableRelations.get(counter));
					} else {
						temporaryRelation = new CrossProductRelation(temporaryRelation, tableRelations.get(counter));
					}

					counter++;

					// Check index
					if (counter >= tableRelations.size()) {
						end = true;
						fromRelation = temporaryRelation;
					}
				}
			}

			// WHERE clause
			if (ctx.WHERE() != null) {
				// Construct where relation
				Condition condition = new Condition((Node) visit(ctx.expression()));
				whereRelation = new FilteredRelation(fromRelation, condition);
			} else {
				whereRelation = fromRelation;
			}

			// SELECT clause
			ArrayList<String> selectedColumns = new ArrayList<>();

			if (!all) {
				ArrayList<String> allColumns = (ArrayList<String>) visit(ctx.idList(0));

				// Check all columns
				for (String currentColumn : allColumns) {
					int counter = 0;

					// Traverse
					for (String[] table : tableColumns) {
						for (int e = 0; e < table.length; e++) {
							if (table[e].equals(currentColumn)) {
								// Add column
								selectedColumns.add(tables.get(counter) + "." + currentColumn);
								break;
							}
						}
						counter++;
					}
				}

				if (allColumns.size() != selectedColumns.size()) {
					MessagePrinter.printErrorMessage("Specified columns dont' exist in relation.");
					return false;
				}

				finalRelation = new ProjectedRelation(whereRelation, selectedColumns.toArray(new String[0]));
			} else {
				finalRelation = whereRelation;
			}

			// ORDER clause
			if (ctx.ORDER() != null) {
				LinkedHashMap<String, OrderType> map = (LinkedHashMap<String, OrderType>) visit(ctx.orderList());
				Relation orderedRelation = new OrderedRelation(finalRelation, map);
				MessagePrinter.printRelation(orderedRelation);
				return true;
			} else {
				MessagePrinter.printRelation(finalRelation);
				return true;
			}
		} else {
			MessagePrinter.printErrorMessage("No database selected.");
			return false;
		}
	}

	@Override
	public Object visitOrderList(SQLGrammarParser.OrderListContext ctx) {
		LinkedHashMap<String, OrderType> map = new LinkedHashMap<>();

		// Visit id value and aadd to current map.
		for (OrderExpContext value : ctx.orderExp()) {
			map.putAll((HashMap<String, OrderType>) visit(value));
		}

		return map;
	}

	@Override
	public Object visitOrderExp(SQLGrammarParser.OrderExpContext ctx) {
		HashMap<String, OrderType> map = new HashMap<>();
		String field;
		Node node = (Node) visit(ctx.expression());

		// Check that is a field in the table
		if (node instanceof DataNode) {
			field = ((DataNode) node).toString();
		} else {
			throw new DatabaseException("Ordering can only be done over fields in the table.");
		}

		// Check ordering type
		OrderType orderType;
		if (ctx.orderType() != null) {
			if (ctx.orderType().DESC() != null) {
				orderType = OrderType.DESCENDING;
			} else {
				orderType = OrderType.ASCENDING;
			}
		} else {
			orderType = OrderType.ASCENDING;
		}

		map.put(field, orderType);

		return map;
	}

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
