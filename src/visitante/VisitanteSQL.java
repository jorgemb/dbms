
package visitante;

import condicion.Condicion;
import condicion.CondicionTrue;
import condicion.Expresion;
import condicion.Nodo;
import condicion.NodoCondicional;
import condicion.NodoCondicional.TipoOperacionCondicional;
import condicion.NodoDato;
import condicion.NodoLiteral;
import condicion.NodoLiteral.TipoLiteral;
import condicion.NodoOperacional;
import condicion.NodoOperacional.TipoOperacionOperacional;
import condicion.NodoRelacional;
import condicion.NodoRelacional.TipoOperacionRelacional;
import excepciones.ExcepcionBaseDatos;
import excepciones.ExcepcionDBMS;
import excepciones.ExcepcionTabla;
import gramatica.GramaticaSQLBaseVisitor;
import gramatica.GramaticaSQLParser;
import gramatica.GramaticaSQLParser.AccionContext;
import gramatica.GramaticaSQLParser.ExpOrderContext;
import gramatica.GramaticaSQLParser.ExpressionContext;
import gramatica.GramaticaSQLParser.IdValueContext;
import gramatica.GramaticaSQLParser.ListaConstraintContext;
import gramatica.GramaticaSQLParser.TipoConstraintContext;
import interfazUsuario.ImpresorMensajes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import motor.BaseDatos;
import motor.Dato;
import motor.restriccion.Restriccion;
import motor.Tabla;
import motor.TipoDato;
import motor.relacion.Fila;
import motor.relacion.Relacion;
import motor.relacion.RelacionFiltro;
import motor.relacion.RelacionOrdenamiento;
import motor.relacion.RelacionOrdenamiento.TipoOrdenamiento;
import motor.relacion.RelacionProductoCruz;
import motor.relacion.RelacionProyeccion;
import motor.restriccion.RestriccionChar;
import motor.restriccion.RestriccionCheck;
import motor.restriccion.RestriccionLlavePrimaria;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author eddycastro
 */
public class VisitanteSQL extends GramaticaSQLBaseVisitor<Object>{
    
    // Contiene la Base de Datos que se encuentra en uso
    private BaseDatos baseDatosActual = null;
    // Contiene la última Tabla utilizada, para agregarle columnas y restricciones
    private Tabla tablaActual;
    
    // Booleano para manejar echo
    private boolean echo = true;
    private boolean impresionInsert = false;
    private int contadorInsert = 0;

    /**
     * Constructor de la clase
     */
    public VisitanteSQL() {
        
        // Agregar funcionalidad si hace falta
        
    }
    
    /**
     * Método para realizar un commit
     * @param ctx Contexto
     * @return 
     */
    @Override
    public Object visitProgram(GramaticaSQLParser.ProgramContext ctx){
        

        visitChildren(ctx);
        
        
        // Cuando hay echo disabled, imprimir el resumen.
        if (impresionInsert){       
            ImpresorMensajes.imprimirMensajeUsuario(String.format("Se insertaron %s valores con éxito.", contadorInsert));
            contadorInsert = 0;
            impresionInsert = false;
        }
        
        // Commit
        if( baseDatosActual != null )
            baseDatosActual.guardarCambios();
        return true;               
        
    }
    
    /**
     * Método que visita crear base de datos
     * @param ctx Contexto
     * @return true si se realizó sin errores.
     */
    @Override
    public Object visitCreateDatabase(GramaticaSQLParser.CreateDatabaseContext ctx){
        
        BaseDatos.crear(ctx.ID().getText());
        ImpresorMensajes.imprimirMensajeUsuario(String.format("Base de datos %s creada con éxito.", ctx.ID().getText()));

        return true;
               
    }

    /**
     * Método que renombra una base de datos
     * @param ctx Contexto
     * @return true si se realizó sin errores.
     */
    @Override
    public Object visitAlterDatabase(GramaticaSQLParser.AlterDatabaseContext ctx){
        
        // Cambia el nombre de la base de datos
        BaseDatos.renombrar(ctx.ID(0).getText(), ctx.ID(1).getText());
        ImpresorMensajes.imprimirMensajeUsuario(String.format("Base de datos %s cambió de nombre a %s.", ctx.ID(0).getText(), ctx.ID(1).getText()));

        return true;
                
    }
    
    /**
     * Método que elimina una base de datos
     * @param ctx Contexto
     * @return true si se realizó sin errores.
     */
    @Override
    public Object visitDropDatabase(GramaticaSQLParser.DropDatabaseContext ctx){
        
        // Encontrar la cantidad de registros en una base de datos
        Tabla[] tablasTotales = BaseDatos.buscar(ctx.ID().getText()).obtenerTablas();
        int cantidadRegistros = 0;
        
        for (int i=0; i<tablasTotales.length; i++){
            cantidadRegistros += tablasTotales[i].obtenerRelacion().obtenerCantidadFilas();
        }
        
        
        // Pedir confirmación al usuario
        boolean borrar = ImpresorMensajes.obtenerConfirmacion(String.format("¿Desea eliminar la base de datos %s con %s registros?", ctx.ID().getText(), cantidadRegistros));

        if (borrar){
            // Elimina la base de datos
            BaseDatos.eliminar(ctx.ID().getText());
            ImpresorMensajes.imprimirMensajeUsuario(String.format("Base de datos %s eliminada con éxito.", ctx.ID().getText()));

            return true;
        } else{
            
            return false;
            
        }
    }
    
    /**
     * Método que muestra todas las bases de datos almacenadas
     * @param ctx Contexto
     * @return true si se realizó sin errores.
     */
    @Override
    public Object visitShowDatabases(GramaticaSQLParser.ShowDatabasesContext ctx){

        ImpresorMensajes.imprimirMensajeUsuario("Bases de datos: ");
        
        // Muestra todas las bases de datos
        ArrayList<String> basesDatos = BaseDatos.mostrar();            
        for (String baseDatos : basesDatos) {

            ImpresorMensajes.imprimirMensajeUsuario(baseDatos);

        }

        return true;
        
    }
    
    /**
     * Método que selecciona una base de datos a utilizar
     * @param ctx Contexto
     * @return true si se realizó sin errores.
     */
    @Override
    public Object visitUseDatabase(GramaticaSQLParser.UseDatabaseContext ctx){
        
        // Modificar el String que almancena el nombre de la base de datos actual
        baseDatosActual = BaseDatos.buscar(ctx.ID().getText());
        ImpresorMensajes.imprimirMensajeUsuario(String.format("Base de datos %s ahora en uso.", ctx.ID().getText()));

        return true;
        
    }
    
    /**
     * Método para crear una tabla en la base de datos actual
     * @param ctx Contexto
     * @return true si se realizó sin errores.
     */
    @Override
    public Object visitCreateTable(GramaticaSQLParser.CreateTableContext ctx){
        
        // Verificar que haya una base de datos en uso
        if (baseDatosActual != null){
            
            // Crear la tabla en la base de datos
            tablaActual = baseDatosActual.agregarTabla(ctx.ID(0).getText());
            ImpresorMensajes.imprimirMensajeUsuario(String.format("Tabla %s creada en base de datos %s.", ctx.ID(0).getText(), baseDatosActual.obtenerNombre()));
            
            // Obtener arreglo de id y de tipo columna         
            List<TerminalNode> listaId = ctx.ID();
            //List<TipoColumnaContext> listaTipoColumna = ctx.tipoColumna();
            
            int contador = 0;
            // Crear cada columna de la tabla
            for (TerminalNode id : listaId){
                
                if (contador != 0){
                    String nombreColumna = id.getText();
                    TipoDato tipoColumna = (TipoDato)visit(ctx.tipoColumna(contador-1));
                    tablaActual.agregarColumna(nombreColumna, tipoColumna);
                    
                    // Verifica si es necesario agregar una restriccion
                    if( tipoColumna == TipoDato.CHAR ){
                        int limiteChar = Integer.parseInt( ctx.tipoColumna(contador-1).int_literal().NUM().getText() );
                        
                        /* NOTA: Utiliza UUID para asegurar que el nombre de la restricción sea único. */
                        tablaActual.agregarRestriccion( UUID.randomUUID().toString() , 
                                new RestriccionChar(
                                    motor.Util.obtenerNombreCalificado(tablaActual.obtenerNombre(), nombreColumna),
                                    limiteChar));
                    }
                    
                    if (echo){
                        ImpresorMensajes.imprimirMensajeUsuario(String.format("Columna %s agregada a la tabla %s.", id.getText(), tablaActual.obtenerNombre()));
                    }
                    
                }
                contador++;
                
            }
            
            // Agregar restricciones
            List<ListaConstraintContext> restricciones = ctx.listaConstraint();
            for (ListaConstraintContext restriccion : restricciones){
                
                visit(restriccion);
                
            }
            
            
        } else{
            ImpresorMensajes.imprimirMensajeError("No se encuentra ninguna base de datos en uso.");
            return false;
        }
        return true;
        
    }
    
    /**
     * Método para visitar todas las restricciones
     * @param ctx Contexto
     * @return true
     */
    @Override
    public Object visitListaConstraint(GramaticaSQLParser.ListaConstraintContext ctx){
        
        // Agregar todas las restricciones
        List<TipoConstraintContext> restricciones = ctx.tipoConstraint();
        
        for (TipoConstraintContext restriccion : restricciones){
            
            visit(restriccion);
            
        }
        
        return true;
        
    }
    
    /**
     * Método para agregar restricciones
     * @param ctx Contexto
     * @return true si se agrega sin errores
     */
    @Override
    public Object visitTipoConstraint(GramaticaSQLParser.TipoConstraintContext ctx){
        
        // Verificar el tipo constraint
        if (ctx.PRIMARY() != null){
            
            // Obtener las referencias y crear la restricción
            ArrayList<String> campos = (ArrayList<String>)visit(ctx.listaID(0)); 
            ArrayList<String> camposCalificados = new ArrayList<>();
            
            for (String nc : campos){
                
                camposCalificados.add(tablaActual.obtenerNombre()+"."+nc);
                
            }
            
            Restriccion restriccionPrimaria = new RestriccionLlavePrimaria(camposCalificados.toArray(new String[0]));
            
            // Agregar restricción
            tablaActual.agregarRestriccion(ctx.ID(0).getText(), restriccionPrimaria);
            ImpresorMensajes.imprimirMensajeUsuario(String.format("Restriccion %s agregada con éxito en tabla %s",ctx.ID(0).getText(), tablaActual.obtenerNombre()));
            
            return true;
        } else if (ctx.FOREIGN() != null){
            
            // Pendiente de implementar
            return null;
            
        }
        // Caso check
        else{
            
            // Crear la restricción
            Condicion condicionCheck = new Condicion((Nodo)visit(ctx.expression()));
            Restriccion restriccionCheck = new RestriccionCheck(condicionCheck);
            
            // Agregar restricción
            tablaActual.agregarRestriccion(ctx.ID(0).getText(), restriccionCheck);
            ImpresorMensajes.imprimirMensajeUsuario(String.format("Restriccion %s agregada con éxito en tabla %s",ctx.ID(0).getText(), tablaActual.obtenerNombre()));
            
            return true;
        }
        
    }
    
    /**
     * Método para determinar el tipo de columna 
     * @param ctx Contexto
     * @return Tipo de columna
     */
    @Override
    public Object visitTipoColumna(GramaticaSQLParser.TipoColumnaContext ctx){
        
        if (ctx.INT() != null){
            return TipoDato.INT;
        } else if (ctx.FLOAT() != null){
            return TipoDato.FLOAT;
        } else if (ctx.DATE() != null){
           return TipoDato.DATE;
        } else {
           return TipoDato.CHAR;
           
           // TODO char debe agregar una nueva restricción
           
        }
    }
    
    /**
     * Método para elminar una tabla de la base de datos en uso
     * @param ctx Contexto
     * @return true si se realizó sin errores.
     */
    @Override
    public Object visitDropTable(GramaticaSQLParser.DropTableContext ctx){
        
        // Verificar que haya una base de datos en uso
        if (baseDatosActual != null){
            
            // Elminar la tabla de la base de datos
            baseDatosActual.eliminarTabla(ctx.ID().getText());
            ImpresorMensajes.imprimirMensajeUsuario(String.format("La tabla %s ha sido eliminada de la base de datos %s.", ctx.ID().getText(), baseDatosActual.obtenerNombre()));
        } else{
            ImpresorMensajes.imprimirMensajeError("No se encuentra ninguna base de datos en uso.");
            return false;
        }

        return true;  
    }
    
    /**
     * Método para mostrar todas las tablas de una base de datos
     * @param ctx Contexto
     * @return true si se realizó sin errores.
     */
    @Override
    public Object visitShowTables(GramaticaSQLParser.ShowTablesContext ctx){
        
        // Verificar que haya una base de datos en uso
        if (baseDatosActual != null){
            
            ImpresorMensajes.imprimirMensajeUsuario(String.format("Tablas de %s:", baseDatosActual.obtenerNombre()));
            
            // Mostrar las tablas de la base de datos
            Tabla[] tablas = baseDatosActual.obtenerTablas();
            for (int i = 0; i<tablas.length; i++){
                
                ImpresorMensajes.imprimirMensajeUsuario(tablas[i].obtenerNombre());
                
            }
        } else{
            ImpresorMensajes.imprimirMensajeError("No se encuentra ninguna base de datos en uso.");
            return false;
        }
        return true;
        
    }
    
    /**
     * Método para mostrar todas las columnas de una tabla
     * @param ctx Contexto
     * @return true si se realizó sin errores.
     */
    @Override
    public Object visitShowColumns(GramaticaSQLParser.ShowColumnsContext ctx){
        
        tablaActual= null;
        
        // Verificar que haya una base de datos en uso
        if (baseDatosActual != null){
            
            Tabla[] tablas = baseDatosActual.obtenerTablas();
            
            // Encontrar la tabla de la que de desean mostrar las columnas
            for (int i = 0; i<tablas.length; i++){
                
                // Encontrar la tabla de la que se desean visualizar las columnas
                if (tablas[i].obtenerNombre().equals(ctx.ID().getText())){
                    tablaActual = tablas[i];
                }
                
            }
            // Verificar que exista la tabla
            if (tablaActual == null){
                ImpresorMensajes.imprimirMensajeError(String.format("No existe la tabla %s.", ctx.ID().getText()));
                return false;
            } else{
                
                ImpresorMensajes.imprimirMensajeUsuario(String.format("Columnas de la tabla %s:", tablaActual.obtenerNombre()));
                
                // Mostrar todas las columnas
                HashMap<String, TipoDato> columnas = tablaActual.obtenerColumnas();
                for (Map.Entry<String, TipoDato> columnaActual : columnas.entrySet()) {
                    ImpresorMensajes.imprimirMensajeUsuario(String.format("%s %s \n", columnaActual.getKey(), columnaActual.getValue()));
                }
            }
        } else{
            ImpresorMensajes.imprimirMensajeError("No se encuentra ninguna base de datos en uso.");
            return false;
        }
        return true;
               
    }
    
    /**
     * Método para modificar una tabla
     * @param ctx Contexto
     * @return true si se realizó sin errores.
     */
    @Override
    public Object visitAlterTable(GramaticaSQLParser.AlterTableContext ctx){
        
        tablaActual = null;
        
        // Verificar que haya una base de datos en uso
        if (baseDatosActual != null){
            
            // Verifificar si es una acción la que se desea realizar
            if (ctx.ID(1) != null){
                baseDatosActual.renombrarTabla(ctx.ID(0).getText(), ctx.ID(1).getText());
                ImpresorMensajes.imprimirMensajeUsuario(String.format("Tabla %s cambió de nombre a %S.", ctx.ID(0).getText(), ctx.ID(1).getText()));
            } else{

                Tabla[] tablas = baseDatosActual.obtenerTablas();
                
                // Encontrar la tabla que se desea modificar
                for (int i = 0; i<tablas.length; i++){

                    // Encontrar la tabla de la que se desean visualizar las columnas
                    if (tablas[i].obtenerNombre().equals(ctx.ID(0).getText())){
                        tablaActual = tablas[i];
                    }

                }
                
                // Verificar que exista la tabla
                if (tablaActual == null){
                    ImpresorMensajes.imprimirMensajeError(String.format("No existe la tabla %s.", ctx.ID(0).getText()));
                    return false;
                } else{
                    
                    List<AccionContext> acciones =  ctx.accion();
                    
                    // Recorrer todas las acciones
                    for (AccionContext accion : acciones){
                        visit(accion);
                    }
                                  
                }
            
            }
        } else{
            ImpresorMensajes.imprimirMensajeError("No se encuentra ninguna base de datos en uso.");
            return false;
        }
        return true;
                   
    }
    
    /**
     * Método para determinar la acción a realizar en la tabla actual
     * @param ctx Contexto
     * @return true si no hay errores
     */
    @Override
    public Object visitAccion(GramaticaSQLParser.AccionContext ctx){
        
        // Acción agregar columna
        if (ctx.ADD() != null && ctx.COLUMN() != null){
            String nombreColumna = ctx.ID().getText();
            TipoDato tipoColumna = (TipoDato)visit(ctx.tipoColumna());
            
            tablaActual.agregarColumna(nombreColumna, tipoColumna);
            ImpresorMensajes.imprimirMensajeUsuario(String.format("Columna %s agregada a la tabla %s.", ctx.ID().getText(), tablaActual.obtenerNombre()));

            // Verifica si es necesario agregar una restriccion
            if( tipoColumna == TipoDato.CHAR ){
                int limiteChar = Integer.parseInt( ctx.tipoColumna().int_literal().NUM().getText() );

                /* NOTA: Utiliza UUID para asegurar que el nombre de la restricción sea único. */
                tablaActual.agregarRestriccion( UUID.randomUUID().toString() , 
                    new RestriccionChar(
                        motor.Util.obtenerNombreCalificado(tablaActual.obtenerNombre(), nombreColumna),
                        limiteChar));
            }
            
            // Agregar restricciones
            if (ctx.listaConstraint() != null){
                visit(ctx.listaConstraint());
            }
           
            return true;

        } else if(ctx.ADD() != null && ctx.CONSTRAINT() != null){

            visit(ctx.tipoConstraint());

        // Acción eliminar columna
        } else if(ctx.DROP() != null && ctx.COLUMN() != null){

            tablaActual.eliminarColumna(ctx.ID().getText());
            ImpresorMensajes.imprimirMensajeUsuario(String.format("Columna %s eliminada de la tabla %s.", ctx.ID(), tablaActual.obtenerNombre()));
            return true;

        // Acción eliminar restricción
        } else if(ctx.DROP() != null && ctx.CONSTRAINT() != null){

            tablaActual.eliminarRestriccion(ctx.ID().getText());
            ImpresorMensajes.imprimirMensajeUsuario(String.format("Restricción %s eliminada de la tabla %s.", ctx.ID(), tablaActual.obtenerNombre()));
            return true;

        }
        
        return false;
        
    }
    
    /**
     * Método utilizado para realizar inserciones
     * @param ctx Contexto
     * @return true si se realiza todo con éxito
     */
    @Override
    public Object visitInsertInto(GramaticaSQLParser.InsertIntoContext ctx){
        
        tablaActual = null;
        
        // Verificar que haya una base de datos en uso
        if (baseDatosActual != null){
            
            Tabla[] tablas = baseDatosActual.obtenerTablas();
                
            // Encontrar la tabla que se desea modificar
            for (int i = 0; i<tablas.length; i++){

                // Encontrar la tabla de la que se desean visualizar las columnas
                if (tablas[i].obtenerNombre().equals(ctx.ID().getText())){
                    tablaActual = tablas[i];
                }

            }
            
            // Verificar que exista la tabla
            if (tablaActual == null){
                ImpresorMensajes.imprimirMensajeError(String.format("No existe la tabla %s.", ctx.ID().getText()));
                return false;
            } else{
                
                // Datos a insertar en fila
                ArrayList<Dato> datos = new ArrayList<>();
                //Dato[] datos;
                Dato dato;
                
                // Valores que se van a insertar
                ArrayList<Object> valores = (ArrayList<Object>)visit(ctx.listaValue());
                String[] nombreColumnas = tablaActual.obtenerNombreColumnas();
                 
                // Caso en el que se especifican columnas
                if (ctx.listaID() != null){
                    
                    // Nombres de columnas identificadas
                    ArrayList<String> nombres = (ArrayList<String>)visit(ctx.listaID());
                    
                    // Verifica que existan suficients valores para la cantidad de columnas
                    if( valores.size() != nombres.size() )
                        throw new ExcepcionDBMS("No hay suficientes valores especificados para la cantidad de nombres dados.");

                    ArrayList<String> nombreColumnasArreglo = new ArrayList<>(Arrays.asList(nombreColumnas));
                    
                    for (String nombre : nombres){
                        if (!(nombreColumnasArreglo.contains(nombre))){
                            
                            ImpresorMensajes.imprimirMensajeError(String.format("La columna %s no se encuentra en la tabla %S", nombre, tablaActual.obtenerNombre()));
                            return false;
                            
                        }
                    }
                    
                    
                    // Recorrer todas las columnas de la tabla actual
                    for (String nombreColumna : nombreColumnas){

                         int indice = nombres.indexOf(nombreColumna);

                        // Verificar que se encuentre en el listado
                        if (indice != -1){
                            dato = new Dato(valores.get(indice));
                        } else{
                            dato = new Dato(null);
                        }
                        datos.add(dato);
                    }
                    
                // Caso en el que no se especifican columnas
                } else {                    
                    for (Object valor : valores){
                        dato = new Dato(valor);
                        datos.add(dato);
                    }
                    
                }
                
                
                //Construir la fila con los datos obtenidos y agregarla
                tablaActual.agregarFila(new Fila(datos.toArray(new Dato[0])));
                if (echo){
                    ImpresorMensajes.imprimirMensajeUsuario(String.format("Insertados %s valores con éxito", datos.size()));
                } else{
                    impresionInsert = true;
                    contadorInsert += datos.size();
                }
                return true;
                
            }
            
        } else{
            ImpresorMensajes.imprimirMensajeError("No se encuentra ninguna base de datos en uso.");
            return false;
        }
        
    }
    
    /**
     * Método utilizado para obtener un listado de identificadores
     * @param ctx Context
     * @return ArrayList con los identificadores
     */
    @Override
    public Object visitListaID(GramaticaSQLParser.ListaIDContext ctx){
        
        ArrayList<String> identificadores = new ArrayList<>();
        List<TerminalNode> ids = ctx.ID();
        
        // Recorrer los ID para agregar su texto
        for (TerminalNode id : ids){
            identificadores.add(id.getText());
        }
        
        return identificadores;
        
    }
    
    /**
     * Método que retorna los objetos que se obtienen de expression
     * @param ctx Contexto
     * @return Objetos que se obtienen de expression o null en caso de error
     */
    @Override
    public Object visitListaValue(GramaticaSQLParser.ListaValueContext ctx){
      
        List<ExpressionContext> expresiones = ctx.expression();
        ArrayList<Object> objetos = new ArrayList<>();
        
        // recorrer todos verificando que cumplan
        for (ExpressionContext expresion : expresiones){
            
            Nodo nodo = (Nodo)visit(expresion);
            
            Condicion condicionPrueba = new Condicion(nodo);
            if (condicionPrueba.obtenerColumnasUtilizadas().length == 0){
                objetos.add(nodo.evaluar(null));
            } else{
                throw new ExcepcionTabla(ExcepcionTabla.TipoError.ErrorFatal, "No es posible utilizar una referencia para insertar en una tabla");
//                ImpresorMensajes.imprimirMensajeError("No es posible utilizar una referencia para insertar en una tabla");
//                return null;
            }
            
        }
        
        return objetos;
        
    }
    
    /**
     * Método para retornar el nodo que se creó en location
     * @param ctx Contexto
     * @return NodoDato con el location
     */
    @Override
    public Object visitLocationExpr(GramaticaSQLParser.LocationExprContext ctx){
        Nodo nodo = (Nodo)visit(ctx.location());
        return nodo;
    }
    
    /**
     * Método para obtener un location
     * @param ctx Contexto
     * @return NodoDato con location
     */
    @Override
    public Object visitLocation(GramaticaSQLParser.LocationContext ctx){
        
        NodoDato nodoRetorno;
        
        // Verificar si ya es de la forma ID.ID
        if (ctx.ID(1) != null){
            nodoRetorno = new NodoDato(ctx.ID(0).getText()+"."+ctx.ID(1));
        } else {
            nodoRetorno = new NodoDato(tablaActual.obtenerNombre()+"."+ctx.ID(0));
        }
        
        return nodoRetorno;
        
    }
    
    /**
     * Método que maneja las literales enteras
     * @param ctx Contexto
     * @return Nodo con el valor de la literal
     */
    @Override
    public Object visitInt_literal(GramaticaSQLParser.Int_literalContext ctx){
        //Dato valor = new Dato(Integer.valueOf(ctx.NUM().getText()));
        NodoLiteral nodoRetorno = new NodoLiteral(Integer.valueOf(ctx.NUM().getText()), TipoLiteral.INT);
        return nodoRetorno;
    }
    
    /**
     * Metodo que maneja las litarales string
     * @param ctx Contexto
     * @return Nodo con el valor de la literal
     */
    @Override
    public Object visitString_literal(GramaticaSQLParser.String_literalContext ctx){
        //Dato valor = new Dato(ctx.STRING().getText());
        String s = ctx.STRING().getText();
        NodoLiteral nodoRetorno = new NodoLiteral(s.substring(1, s.length()-1), TipoLiteral.STRING);
        return nodoRetorno;
    }
    
    /**
     * Método que maneja las litereas reales
     * @param ctx Contexto
     * @return Nodo con el valor de la literal
     */
    @Override
    public Object visitReal_literal(GramaticaSQLParser.Real_literalContext ctx){
        //Dato valor = new Dato(Float.valueOf(ctx.REAL().getText()));
        NodoLiteral nodoRetorno = new NodoLiteral(Float.valueOf(ctx.REAL().getText()), TipoLiteral.FLOAT);
        return nodoRetorno;
    }
    
    /**
     * Método que maneja las literales null
     * @param ctx Contexto
     * @return Nodo con el valor null
     */
    @Override
    public Object visitNull_literal(GramaticaSQLParser.Null_literalContext ctx){
        NodoLiteral nodoRetorno = new NodoLiteral(null, TipoLiteral.NULL);
        return nodoRetorno;
    }
    
    /**
     * Método para manejo de operador negativo
     * @param ctx Contexto
     * @return Nodo de operación 0-expresión
     */
    @Override
    public Object visitNegExpr(GramaticaSQLParser.NegExprContext ctx){
        
        Nodo nodo = (Nodo)visit(ctx.expression());
        
        // Instancia de nodo literal
        if (nodo instanceof NodoLiteral){
            
            if (((NodoLiteral)nodo).obtenerTipo() != TipoLiteral.STRING){
                NodoLiteral nodoDummy = new NodoLiteral(0, TipoLiteral.INT);
                NodoOperacional nodoRetorno = new NodoOperacional(TipoOperacionOperacional.Resta, nodoDummy, nodo);
                return nodoRetorno;
            }
            
        // Instancia de nodo operacional
        } else if (nodo instanceof NodoOperacional) {
            
            NodoLiteral nodoDummy = new NodoLiteral(0, TipoLiteral.INT);
            NodoOperacional nodoRetorno = new NodoOperacional(TipoOperacionOperacional.Resta, nodoDummy, nodo);
            return nodoRetorno;
            
        } else {
            
//            ImpresorMensajes.imprimirMensajeError("No se puede utilizar el operador '-' en resultados no numéricos.");
            throw new ExcepcionDBMS("No se puede utilizar el operador '-' en resultados no numéricos.");
        }
        return null;
        
    }
    
    /**
     * Método para manejo de nodo operacional de *, /, %.
     * @param ctx Contexto
     * @return Nodo operacional con la operación correspondiente
     */
    @Override
    public Object visitMultdivExpr(GramaticaSQLParser.MultdivExprContext ctx){
        
        Nodo nodoIzq = (Nodo)visit(ctx.expression(0));
        Nodo nodoDer = (Nodo)visit(ctx.expression(1));
        
        // Determinar el tipo de operación que se debe realizar
        TipoOperacionOperacional tipo;
        if (ctx.DIV() != null){
            tipo = TipoOperacionOperacional.Division;
        } else if (ctx.MOD() != null){
            tipo = TipoOperacionOperacional.Modulo;
        } else {
            tipo = TipoOperacionOperacional.Multiplicacion;
        }
        
        boolean cumpleNodoIzq = true;
        boolean cumpleNodoDer = true;
        
        // Nodo izquierdo
        // Instancia de nodo literal
        if (nodoIzq instanceof NodoLiteral){
            
            if (((NodoLiteral)nodoIzq).obtenerTipo() == TipoLiteral.STRING){
                cumpleNodoIzq = false;
            }
            
        // Instancia de nodo operacional
        } else if (!(nodoIzq instanceof NodoOperacional)  && !(nodoIzq instanceof NodoDato)) {
            cumpleNodoIzq = false;
        }
        
        // Nodo derecho
        // Instancia de nodo literal
        if (nodoDer instanceof NodoLiteral){
            
            if (((NodoLiteral)nodoDer).obtenerTipo() == TipoLiteral.STRING){
                cumpleNodoDer = false;
            }
            
        // Instancia de nodo operacional
        } else if (!(nodoDer instanceof NodoOperacional)  && !(nodoDer instanceof NodoDato)) {
            cumpleNodoDer = false;
        }
        
        // Verificar que ambos cumplan para retornar
        if (cumpleNodoIzq && cumpleNodoDer){
            Nodo nodoRetorno = new NodoOperacional(tipo, nodoIzq, nodoDer);
            return nodoRetorno;
        } else{
            throw new ExcepcionDBMS(String.format("No se puede aplicar '%s' a valores no numéricos.", tipo));
//            ImpresorMensajes.imprimirMensajeError(String.format("No se puede aplicar '%s' a valores no numéricos.", tipo));
//            return null;
        }
        
    }
    
    /**
     * Método para maneje de nodo operacional de +, -.
     * @param ctx Contexto
     * @return Nodo operacional con la operación correspondiente.
     */
    @Override
    public Object visitAddsubExpr(GramaticaSQLParser.AddsubExprContext ctx){

        Nodo nodoIzq = (Nodo)visit(ctx.expression(0));
        Nodo nodoDer = (Nodo)visit(ctx.expression(1));
        
        // Determinar el tipo de operación que se debe realizar
        TipoOperacionOperacional tipo;
        if (ctx.SUMA() != null){
            tipo = TipoOperacionOperacional.Suma;
        } else {
            tipo = TipoOperacionOperacional.Resta;
        }
        
        boolean cumpleNodoIzq = true;
        boolean cumpleNodoDer = true;
        
        // Nodo izquierdo
        // Instancia de nodo literal
        if (nodoIzq instanceof NodoLiteral){
            
            if (((NodoLiteral)nodoIzq).obtenerTipo() == TipoLiteral.STRING){
                cumpleNodoIzq = false;
            }
            
        // Instancia de nodo operacional
        } else if (!(nodoIzq instanceof NodoOperacional)  && !(nodoIzq instanceof NodoDato)) {
            cumpleNodoIzq = false;
        }
        
        // Nodo derecho
        // Instancia de nodo literal
        if (nodoDer instanceof NodoLiteral){
            
            if (((NodoLiteral)nodoDer).obtenerTipo() == TipoLiteral.STRING){
                cumpleNodoDer = false;
            }
            
        // Instancia de nodo operacional
        } else if (!(nodoDer instanceof NodoOperacional)  && !(nodoDer instanceof NodoDato)) {
            cumpleNodoDer = false;
        }
        
        // Verificar que ambos cumplan para retornar
        if (cumpleNodoIzq && cumpleNodoDer){
            Nodo nodoRetorno = new NodoOperacional(tipo, nodoIzq, nodoDer);
            return nodoRetorno;
        } else{
            throw new ExcepcionDBMS(String.format("No se puede aplicar '%s' a valores no numéricos.", tipo));
//            ImpresorMensajes.imprimirMensajeError(String.format("No se puede aplicar '%s' a valores no numéricos.", tipo));
//            return null;
        }        
        
    }
     
    /**
     * Metodo para el manejo de condición not
     * @param ctx Contexto
     * @return NodoRelacional correspondiente a la negación del nodo recibido
     */
    @Override
    public Object visitNotExpr(GramaticaSQLParser.NotExprContext ctx){
        
        Nodo nodo = (Nodo)visit(ctx.expression());
        
        // Instancia de nodo literal
        if (nodo instanceof NodoRelacional || nodo instanceof NodoCondicional){
            
            NodoCondicional nodoRetorno = new NodoCondicional(nodo);
            return nodoRetorno;
            
        } else {
            throw new ExcepcionDBMS("No se puede utilizar el operador '!' en resultados no booleanos.");
//            ImpresorMensajes.imprimirMensajeError("No se puede utilizar el operador '!' en resultados no booleanos.");
            
        }
//        return null;
        
    }
      
    /**
     * Método para el manejo de operaciones relacionales menor, mayor, menorigual, mayorigual
     * @param ctx Context
     * @return  Nodo relacional con la operación correspondiente
     */
    @Override
    public Object visitRelExpr(GramaticaSQLParser.RelExprContext ctx){
        
        Nodo nodoIzq = (Nodo)visit(ctx.expression(0));
        Nodo nodoDer = (Nodo)visit(ctx.expression(1));
        
        // Determinar el tipo de operación que se debe realizar
        TipoOperacionRelacional tipo;
        String operador = ctx.REL_OP().getText();
        
        if (operador.equals("<")){
            tipo = TipoOperacionRelacional.Menor;
        } else  if (operador.equals(">")){
            tipo = TipoOperacionRelacional.Mayor;
        } else  if (operador.equals("<=")){
            tipo = TipoOperacionRelacional.MenorIgual;
        } else{
            tipo = TipoOperacionRelacional.MayorIgual;
        }
        
        boolean cumpleNodoIzq = true;
        boolean cumpleNodoDer = true;
        
        // Nodo izquierdo
        // Instancia de nodo literal
        if (nodoIzq instanceof NodoLiteral){
            
//            if (((NodoLiteral)nodoIzq).obtenerTipo() == TipoLiteral.STRING){
//                cumpleNodoIzq = false;
//            }
            
        // Instancia de nodo operacional
        } else if (!(nodoIzq instanceof NodoOperacional) && !(nodoIzq instanceof NodoDato)) {
            cumpleNodoIzq = false;
        }
        
        // Nodo derecho
        // Instancia de nodo literal
        if (nodoDer instanceof NodoLiteral){
            
//            if (((NodoLiteral)nodoDer).obtenerTipo() == TipoLiteral.STRING){
//                cumpleNodoDer = false;
//            }
            
        // Instancia de nodo operacional
        } else if (!(nodoDer instanceof NodoOperacional) && !(nodoDer instanceof NodoDato)) {
            cumpleNodoDer = false;
        }
        
        // Verificar que ambos cumplan para retornar
        if (cumpleNodoIzq && cumpleNodoDer){
            Nodo nodoRetorno = new NodoRelacional(tipo, nodoIzq, nodoDer);
            return nodoRetorno;
        } else{
            throw new ExcepcionDBMS(String.format("No se puede aplicar '%s' a valores no numéricos.", tipo));
//            ImpresorMensajes.imprimirMensajeError(String.format("No se puede aplicar '%s' a valores no numéricos.", tipo));
//            return null;
        }  
        
    }
      
    /**
     * Método para manejo de operadores relacionales igual, diferente
     * @param ctx Context
     * @return Nodo relacional que representa la operación correspondinte
     */
    @Override
    public Object visitEqExpr(GramaticaSQLParser.EqExprContext ctx){
        
        Nodo nodoIzq = (Nodo)visit(ctx.expression(0));
        Nodo nodoDer = (Nodo)visit(ctx.expression(1));
        
        // Determinar el tipo de operación que se debe realizar
        TipoOperacionRelacional tipo;
        
        if (ctx.eq_op().IGUAL() != null){
            tipo = TipoOperacionRelacional.Igual;
        }  else{
            tipo = TipoOperacionRelacional.Diferente;
        }
        
        boolean cumpleNodoIzq = true;
        boolean cumpleNodoDer = true;
        
        // Nodo izquierdo            
        // Instancia de nodo operacional o literal
        if (!(nodoIzq instanceof NodoOperacional) && !(nodoIzq instanceof NodoLiteral) && !(nodoIzq instanceof NodoDato)) {
            cumpleNodoIzq = false;
        }
        
        // Nodo derecho            
        // Instancia de nodo operacional o literal
        if (!(nodoDer instanceof NodoOperacional) && !(nodoDer instanceof NodoLiteral) && !(nodoDer instanceof NodoDato)) {
            cumpleNodoDer = false;
        }
        
        // Verificar que ambos cumplan para retornar
        if (cumpleNodoIzq && cumpleNodoDer){
            Nodo nodoRetorno = new NodoRelacional(tipo, nodoIzq, nodoDer);
            return nodoRetorno;
        } else{
            throw new ExcepcionDBMS(String.format("No se puede aplicar '%s' a valores no numéricos o string.", tipo));
//            ImpresorMensajes.imprimirMensajeError(String.format("No se puede aplicar '%s' a valores no numéricos o string.", tipo));
//            return null;
        } 
        
    }

    /**
     * Visita las expresiones con paréntesis.
     * @param ctx
     * @return 
     */
    @Override
    public Object visitParenthesisExpr(GramaticaSQLParser.ParenthesisExprContext ctx) {
        return visit(ctx.expression());
    }
    
    
    
    /**
     * Método que maneja operaciones de tipo AND
     * @param ctx Contexto
     * @return NodoCondicional que representa la operación correspondiente
     */
    @Override
    public Object visitAndExpr(GramaticaSQLParser.AndExprContext ctx){
        
        Nodo nodoIzq = (Nodo)visit(ctx.expression(0));
        Nodo nodoDer = (Nodo)visit(ctx.expression(1));
        
        boolean cumpleNodoIzq = true;
        boolean cumpleNodoDer = true;
        
        // Nodo izquierdo            
        // Instancia de nodo operacional o literal
        if (!(nodoIzq instanceof NodoRelacional) && !(nodoIzq instanceof NodoCondicional) && !(nodoIzq instanceof NodoDato)) {
            cumpleNodoIzq = false;
        }
        
        // Nodo derecho            
        // Instancia de nodo operacional o literal
        if (!(nodoDer instanceof NodoRelacional) && !(nodoDer instanceof NodoCondicional) && !(nodoDer instanceof NodoDato)) {
            cumpleNodoDer = false;
        }
        
        // Verificar que ambos cumplan para retornar
        if (cumpleNodoIzq && cumpleNodoDer){
            Nodo nodoRetorno = new NodoCondicional(TipoOperacionCondicional.AND, nodoIzq, nodoDer);
            return nodoRetorno;
        } else{
            throw new ExcepcionDBMS(String.format("No se puede aplicar '%s' a valores no booleanos.", "AND"));
//            ImpresorMensajes.imprimirMensajeError(String.format("No se puede aplicar '%s' a valores no booleanos.", "AND"));
//            return null;
        } 
        
    }
        
    /**
     * Método para el manejo de operaciones OR
     * @param ctx Contexto
     * @return Nodo condicional con la operación corresponiente
     */
    @Override
    public Object visitOrExpr(GramaticaSQLParser.OrExprContext ctx){
        
        Nodo nodoIzq = (Nodo)visit(ctx.expression(0));
        Nodo nodoDer = (Nodo)visit(ctx.expression(1));
        
        boolean cumpleNodoIzq = true;
        boolean cumpleNodoDer = true;
        
        // Nodo izquierdo            
        // Instancia de nodo operacional o literal
        if (!(nodoIzq instanceof NodoRelacional) && !(nodoIzq instanceof NodoCondicional) && !(nodoIzq instanceof NodoDato)) {
            cumpleNodoIzq = false;
        }
        
        // Nodo derecho            
        // Instancia de nodo operacional o literal
        if (!(nodoDer instanceof NodoRelacional) && !(nodoDer instanceof NodoCondicional) && !(nodoDer instanceof NodoDato)) {
            cumpleNodoDer = false;
        }
        
        // Verificar que ambos cumplan para retornar
        if (cumpleNodoIzq && cumpleNodoDer){
            Nodo nodoRetorno = new NodoCondicional(TipoOperacionCondicional.OR, nodoIzq, nodoDer);
            return nodoRetorno;
        } else{
            throw new ExcepcionDBMS(String.format("No se puede aplicar '%s' a valores no booleanos.", "OR"));
//            ImpresorMensajes.imprimirMensajeError(String.format("No se puede aplicar '%s' a valores no booleanos.", "OR"));
//            return null;
        } 
        
    }
    
    /**
     * Método para actualizar tabla
     * @param ctx Contexto
     * @return 
     */
    @Override
    public Object visitUpdate(GramaticaSQLParser.UpdateContext ctx){
        
        tablaActual = null;
        
        // Verificar que haya una base de datos en uso
        if (baseDatosActual != null){
            
            Tabla[] tablas = baseDatosActual.obtenerTablas();
                
            // Encontrar la tabla que se desea modificar
            for (int i = 0; i<tablas.length; i++){

                // Encontrar la tabla de la que se desean visualizar las columnas
                if (tablas[i].obtenerNombre().equals(ctx.ID().getText())){
                    tablaActual = tablas[i];
                }

            }
            
            // Verificar que exista la tabla
            if (tablaActual == null){
                ImpresorMensajes.imprimirMensajeError(String.format("No existe la tabla %s.", ctx.ID().getText()));
                return false;
            } else{
                
                
                // Obtener la lista de valores
                HashMap<String, Expresion> mapa = (HashMap<String, Expresion>)visit(ctx.listaIDValue());
                
                // Verificar que tenga un where
                if (ctx.WHERE() != null){
                    
                    Condicion condicion = new Condicion((Nodo)visit(ctx.expression()));
                    int cambios = tablaActual.actualizarFilas(mapa, condicion);
                    
                    // Imprime mensaje de cambios
                    ImpresorMensajes.imprimirMensajeUsuario(String.format("Se actualizaron %d filas.", cambios));
                    
                    return true;
                    
                } else{ 
                    
                    NodoLiteral nodoDummy = new NodoLiteral("true", TipoLiteral.STRING);
                    Condicion condicion = new CondicionTrue(nodoDummy);
                    int cambios = tablaActual.actualizarFilas(mapa, condicion);
                    
                    // Imprime mensaje de cambios
                    ImpresorMensajes.imprimirMensajeUsuario(String.format("Se actualizaron %d filas.", cambios));
                    
                    return true;
                    
                }
                
            }
            
        } else{
            ImpresorMensajes.imprimirMensajeError("No se encuentra ninguna base de datos en uso.");
            return false;
        }
        
    }
    
    /**
     * Métood para obtener el listadod e mapas String - Expresion
     * @param ctx Contexto
     * @return Mapa con todos los valores correspondientes
     */
    @Override
    public Object visitListaIDValue(GramaticaSQLParser.ListaIDValueContext ctx){
        
        HashMap<String, Expresion> mapa = new HashMap<>();
        
        // Visitar id value y agregar al mapa acutal todos los elementos que retorna
        for (IdValueContext valor : ctx.idValue()){
            mapa.putAll((HashMap<String, Expresion>)visit(valor)); 
        }
        
        return mapa;
        
    }
    
    /**
     * Métod para obtener un mapa String - Expresion
     * @param ctx Contexto
     * @return Mapa con los valores correspondientes
     */
    @Override
    public Object visitIdValue(GramaticaSQLParser.IdValueContext ctx){
        
        HashMap<String, Expresion> mapa = new HashMap<>();
        Expresion expresion = new Expresion((Nodo)visit(ctx.expression()));
        mapa.put(tablaActual.obtenerNombre()+"."+ctx.ID().getText(), expresion);
        
        return mapa;
        
    }
    
    /**
     * Método para eliminar filas 
     * @param ctx Contexto
     * @return true si se elminan
     */
    @Override
    public Object visitDeleteFrom(GramaticaSQLParser.DeleteFromContext ctx){
        
        tablaActual = null;
        
        // Verificar que haya una base de datos en uso
        if (baseDatosActual != null){
            
            Tabla[] tablas = baseDatosActual.obtenerTablas();
                
            // Encontrar la tabla que se desea modificar
            for (int i = 0; i<tablas.length; i++){

                // Encontrar la tabla de la que se desean visualizar las columnas
                if (tablas[i].obtenerNombre().equals(ctx.ID().getText())){
                    tablaActual = tablas[i];
                }

            }
            
            // Verificar que exista la tabla
            if (tablaActual == null){
                ImpresorMensajes.imprimirMensajeError(String.format("No existe la tabla %s.", ctx.ID().getText()));
                return false;
            } else{
                
                // Verificar que tenga un where
                if (ctx.WHERE() != null){
                    
                    Condicion condicion = new Condicion((Nodo)visit(ctx.expression()));
                    int cantidadEliminado = tablaActual.eliminarFilas(condicion);
                    ImpresorMensajes.imprimirMensajeUsuario(String.format("Se han eliminado de la tabla %s %d fila(s).",tablaActual.obtenerNombre(), cantidadEliminado));
                    return true;
                    
                } else{
                    
                    NodoLiteral nodoDummy = new NodoLiteral("true", TipoLiteral.STRING);
                    Condicion condicion = new CondicionTrue(nodoDummy);
                    tablaActual.eliminarFilas(condicion);
                    ImpresorMensajes.imprimirMensajeUsuario(String.format("Se han elminado todas las filas de la tabla %s", tablaActual.obtenerNombre()));
                    return true;
                    
                }
                
            }
            
        } else{
            ImpresorMensajes.imprimirMensajeError("No se encuentra ninguna base de datos en uso.");
            return false;
        }
        
    }
    
    /**
     * Método para realizar un select
     * @param ctx Contexto
     * @return true si se ejecuta con éxito
     */
    @Override
    public Object visitSelect(GramaticaSQLParser.SelectContext ctx){
        
         tablaActual = null;
         Relacion relacionFinal;
        
        // Verificar que haya una base de datos en uso
        if (baseDatosActual != null){
            
            ArrayList<String> tablas;
            boolean all = true;
            
            // Operar el from
            if (ctx.listaID(1) != null){
                tablas = (ArrayList<String>)visit(ctx.listaID(1));
                all = false;
            } else{
                tablas = (ArrayList<String>)visit(ctx.listaID(0));
            }
            ArrayList<Relacion> relacionesTablas = new ArrayList<>();
            ArrayList<String[]> columnasTabla = new ArrayList<>();
            
            Relacion relacionFrom = null;
            Relacion relacionWhere = null;
            
            Tabla[] tablasTotales = baseDatosActual.obtenerTablas();
             
            for (String tablaActualCiclo : tablas){
                
                Relacion relacionTemporal = null;
                
                // Encontrar la tabla que se desea modificar
                for (int i = 0; i<tablasTotales.length; i++){

                    // Encontrar la tabla de la que se desean visualizar las columnas
                    if (tablasTotales[i].obtenerNombre().equals(tablaActualCiclo)){
                        relacionTemporal = tablasTotales[i].obtenerRelacion();
                        tablaActual = tablasTotales[i];
                        columnasTabla.add(tablasTotales[i].obtenerNombreColumnas());
                    }

                }
                
                if (relacionTemporal == null){
                    throw new ExcepcionTabla(ExcepcionTabla.TipoError.TablaNoExiste, tablaActualCiclo);
//                    ImpresorMensajes.imprimirMensajeError(String.format("No existe la tabla %s.", tablaActualCiclo));
//                    return null;
                } else{
                    
                    // Llenar el arreglo de relaciones con la relación temporal
                    relacionesTablas.add(relacionTemporal);
                    
                }
                
            }
            
            // Cuando es relación única
            if (relacionesTablas.size() == 1){
                relacionFrom = relacionesTablas.get(0);
            } else{
                
                int cont = 1;
                boolean fin = false;
                RelacionProductoCruz relTemp = null;
                
                while (!fin){
                    
                    if (cont == 1){
                        relTemp = new RelacionProductoCruz(relacionesTablas.get(cont-1),relacionesTablas.get(cont));
                    } else {
                        relTemp = new RelacionProductoCruz(relTemp, relacionesTablas.get(cont));
                    }
                    
                    cont++;
                    
                    // Verificar si ya ese pasó del índice para deter el ciclo
                    if (cont >= relacionesTablas.size()){
                        fin = true;
                        relacionFrom = relTemp;
                    }
                    
                    
                                        
                }
                
            }
            
            // Operar el where
            if (ctx.WHERE() != null){
                
                // Contruir la relación de where si existe
                Condicion condicion = new Condicion((Nodo)visit(ctx.expression()));
                relacionWhere = new RelacionFiltro(relacionFrom, condicion);
                
            } else {
                
                relacionWhere = relacionFrom;
                
            }
            
            // Operar el select 
            ArrayList<String> columnasSelect = new ArrayList<>();
            
            boolean existe = true;
            
            if (!all){
                
                ArrayList<String> col = (ArrayList<String>)visit(ctx.listaID(0));
                
                // Recorrer cada una de las columnas
                for (String columna : col){
                    int c = 0;
                    
                    
                    // Recorrer
                    for (String[] tabla : columnasTabla){
                        for (int e=0; e<tabla.length; e++){
                            
                            if (tabla[e].equals(columna)){
                                // Agregra tabla.columna
                                columnasSelect.add(tablas.get(c)+"."+columna);
                                break;                                
                            }                            
                            
                        }
                        c++;                       
                    }   
                }
                

                if (col.size() != columnasSelect.size()){
                    
                    
                    ImpresorMensajes.imprimirMensajeError("Las columnas especificadas no existen en la relación");
                    return false;
                    
                }
                
                
                relacionFinal = new RelacionProyeccion(relacionWhere, columnasSelect.toArray(new String[0]));
                //ImpresorMensajes.imprimirRelacion(relacionFinal);
                
                //return true;

            // caso select *    
            } else{
                
                relacionFinal = relacionWhere;
                //ImpresorMensajes.imprimirRelacion(relacionWhere);
                //return true;
            }
           
            // Operar el order
            if (ctx.ORDER() != null){
                
                LinkedHashMap<String, TipoOrdenamiento> mapa = (LinkedHashMap<String, TipoOrdenamiento>)visit(ctx.listaOrder());
                Relacion relacionOrdenamiento = new RelacionOrdenamiento(relacionFinal, mapa);
                ImpresorMensajes.imprimirRelacion(relacionOrdenamiento);
                return true;
                
            } else {
                
                ImpresorMensajes.imprimirRelacion(relacionFinal);
                return true;
                
            }
                
                
            //return true;
            
            
        } else{
            ImpresorMensajes.imprimirMensajeError("No se encuentra ninguna base de datos en uso.");
            return false;
        }
        
        
    }
    
    /**
     * Método que obtiene el listado de ordenamientos
     * @param ctx Contexto
     * @return Listado con LinkedHashMaps correspondientes
     */
    @Override
    public Object visitListaOrder(GramaticaSQLParser.ListaOrderContext ctx){
        
        LinkedHashMap<String, TipoOrdenamiento> mapa = new LinkedHashMap<>();
        
        // Visitar id value y agregar al mapa acutal todos los elementos que retorna
        for (ExpOrderContext valor : ctx.expOrder()){
            mapa.putAll((HashMap<String, TipoOrdenamiento>)visit(valor)); 
        }
        
        return mapa;
    }
    
    /**
     * Método que obtiene un LinkedHashMap representando el orden
     * @param ctx Contexto
     * @return LinkedHashMap con los datos correspondientes
     */
    @Override
    public Object visitExpOrder(GramaticaSQLParser.ExpOrderContext ctx){
        
        HashMap<String, TipoOrdenamiento> mapa = new HashMap<>();
        String campo;
        Nodo nodo = (Nodo)visit(ctx.expression());
        
        // Verificar que sea un campo de tabla
        if (nodo instanceof NodoDato){
            
            campo = ((NodoDato)nodo).toString();
            
        } else{
            
            throw new ExcepcionBaseDatos("Solamente se puede ordernar por medio de campos de la tabla");
            
        }
        
        // Verificar tipo de ordenamiento
        TipoOrdenamiento tipoOrdenamiento;
        if (ctx.tipoOrder() != null){
            if (ctx.tipoOrder().DESC() != null){
                tipoOrdenamiento = TipoOrdenamiento.DESCENDENTE;
            } else{
                tipoOrdenamiento = TipoOrdenamiento.ASCENDENTE;
            }
        } else{
            tipoOrdenamiento = TipoOrdenamiento.ASCENDENTE;
        }
        
        //LinkedHashMap a retornar
        mapa.put(campo, tipoOrdenamiento);
        
        return mapa;
        
    }
    
    /**
     * Método para manejar activación/desactivación de echo
     * @param ctx Contexto
     * @return true si se activa, false si se desactiva
     */
    @Override
    public Object visitEcho(GramaticaSQLParser.EchoContext ctx){
        if (ctx.ENABLED() != null){
            echo = true;
            return true;
        } else{
            echo = false;
            return false;
        }
    }
    
}
