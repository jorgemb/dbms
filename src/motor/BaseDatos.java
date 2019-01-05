package motor;

import condition.Condition;
import condition.Expression;
import condition.ConditionalNode;
import condition.DataNode;
import condition.LiteralNode;
import condition.LiteralNode.LiteralType;
import condition.OperationNode;
import condition.RelationNode;
import excepciones.DatabaseException;
import excepciones.TableException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import motor.relacion.Row;
import motor.relacion.Relation;
import motor.relacion.RelacionFiltro;
import motor.relacion.RelacionOrdenamiento;
import motor.relacion.RelacionProductoCruz;
import motor.relacion.RelacionProyeccion;
import motor.restriccion.RestriccionChar;
import motor.restriccion.RestriccionCheck;
import motor.restriccion.RestriccionLlavePrimaria;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Jorge
 */
public class BaseDatos {
    // Variables estáticas
    
    /** Directorio raíz de todos los datos */
    private static File directorioRaiz = null;
    
    // Variables de instancia
    
    /** Nombre de la base de datos */
    private final String nombreBD;
    
    /** Tablas asociadas a la base de datos */
    private HashMap<String, Tabla> tablas;
    
    /** Directorio base de la base de datos actual */
    private final File directorioBase;
    
    /**
     * Crea una base de datos nueva. La operación puede fallar si existe ya
     * una base de datos con ese nombre, o el nombre no es un identificador
     * válido.
     * @param nombre Nombre de la base de datos.
     * @return Base de datos
     * @throws DatabaseException
     * @throws TableException 
     */
    public static BaseDatos crear( String nombre ) throws DatabaseException, TableException{
        // Verificar estado inicial
        verificarEstadoInicial();
        
        // Verifica que la base de datos no exista
        try {
            buscar(nombre);
            
            // Puesto que la base de datos se encontró, significa que ya existe
            // una con ese nombre.
            throw new DatabaseException(DatabaseException.TipoError.NombreYaExiste, nombre);
            
        } catch (DatabaseException excepcionBaseDatos) {
            if( excepcionBaseDatos.obtenerTipoError() != DatabaseException.TipoError.NoExiste )
                throw excepcionBaseDatos;
        }
        
        
        // Se crea la nueva base de datos
        File directorioBD = new File( directorioRaiz, nombre );
        if( !directorioBD.mkdir() )
            throw new DatabaseException( "No se pudo crear el directorio para la base de datos " + nombre );
        
        return new BaseDatos(nombre, directorioBD);
    }
    
    /**
     * Obtiene una base de datos con el nombre dado.
     * @param nombre Nombre de la base de datos a utilizar.
     * @throws DatabaseException 
     * @throws TableException
     */
    public static BaseDatos buscar( String nombre ) throws DatabaseException, TableException{
        // Verificar estado inicial
        verificarEstadoInicial();
        
        // Busca los directorios de la base de datos
        String[] archivos = directorioRaiz.list();
        
        File directorioBuscado = null;
        
        for (String nombreArchivo : archivos) {
            File archivoActual = new File(directorioRaiz, nombreArchivo);
            if( archivoActual.isDirectory() ){
            
                // Verifica si es la base de datos que se desea.
                if( archivoActual.getName().equals(nombre) ){
                    directorioBuscado = archivoActual;
                    break;
                }
            }
        }
        
        // Verifica si se encontró
        if( directorioBuscado == null )
            throw new DatabaseException(DatabaseException.TipoError.NoExiste, nombre);
        
        // Crear la instancia de la base de datos
        BaseDatos bd = new BaseDatos(nombre, directorioBuscado);
        
        return bd;
    }
    
    /**
     * Elimina la base de datos con el nombre dado. Nota que esta elimina
     * el sistema de archivos de la base de datos. Si se tiene una instancia
     * a la base de datos que se elimina dicha instancia queda invalidada
     * después de esta llamada.
     * @param nombre Nombre de la base de datos a generar.
     * @throws DatabaseException 
     */
    public static void eliminar( String nombre ) throws DatabaseException, TableException{
        // Verifica el estado inicial
        verificarEstadoInicial();
        
        // Obtiene la base de datos
        BaseDatos bd = buscar(nombre);
        
        // Elimina todos los archivos en el directorio
        File directorio = bd.directorioBase;
        try {
            FileUtils.deleteDirectory(directorio);
        } catch (IOException ex) {
            throw new DatabaseException( String.format("No se pudo eliminar la base de datos: %s [%s]", 
                    nombre, ex.getMessage()) );
        }
    }
    
    /**
     * Obtiene el nombre de las bases de datos.
     * @return Arreglo con los nombres.
     */
    public static ArrayList<String> mostrar() throws DatabaseException{
        // Verifica estado inicial
        verificarEstadoInicial();
        
        ArrayList<String> retorno = new ArrayList<>();
        
        // Obtiene todas las bases de datos
        for (String nombreArchivo : directorioRaiz.list()) {
            File archivoActual = new File(directorioRaiz, nombreArchivo);
            if( archivoActual.isDirectory() )
                retorno.add(nombreArchivo);
        }
        
        return retorno;
    }
    
    /**
     * Renombrea una base de datos. Nota que esta función cambia el nombre
     * el sistema de archivos de la base de datos. Si se tiene una instancia
     * a la base de datos que se renombra dicha instancia queda invalidada
     * después de esta llamada.
     * @param nombreActual Nombre de la base de datos que se renombrará.
     * @param nombreNuevo Nombre con el cual se renombrará la base de datos.
     * @throws DatabaseException 
     */
    public static void renombrar( String nombreActual, String nombreNuevo ) throws DatabaseException{
        // Verifica el estado inicial
        verificarEstadoInicial();
        
        // Busca la base de datos
        ArrayList<String> bdActuales = mostrar();
        if( !bdActuales.contains(nombreActual) )
            throw new DatabaseException(DatabaseException.TipoError.NoExiste, nombreActual);
        
        if( bdActuales.contains(nombreNuevo) )
            throw new DatabaseException(DatabaseException.TipoError.NombreYaExiste, nombreNuevo);
        
        // Renombra la base de datos
        File antiguo = new File(directorioRaiz, nombreActual);
        File nuevo = new File(directorioRaiz, nombreNuevo);
        if( !antiguo.renameTo(nuevo) ){
            throw new DatabaseException(String.format("No se pudo renombrar la base de datos %s a %s.", 
                    nombreActual, nombreNuevo) );
        }
    }
    
    
    
    
    /**
     * Cambia el nombre de una tabla.
     * @param nombreTabla Nombre de la tabla.
     * @param nuevoNombre Nuevo nombre de la tabla.
     * @throws DatabaseException
     * @throws TableException 
     */
    public void renombrarTabla( String nombreTabla, String nuevoNombre ) throws DatabaseException, TableException{
        // Verifica que la tabla a renombrar exista y que no exista una tabla con el nuevo nombre.
        if( !this.tablas.containsKey(nombreTabla) )
            throw new TableException(TableException.TipoError.TablaNoExiste, nombreTabla);
        if( this.tablas.containsKey(nuevoNombre) )
            throw new TableException(TableException.TipoError.TablaYaExiste, nuevoNombre);
        
        // Cambia el nombre de la tabla
        if( tablas.get(nombreTabla).existeEnArchivo() )
            Tabla.renombrarTabla(nombreTabla, nuevoNombre, directorioBase);
        
        this.tablas.remove(nombreTabla);
        
        // Cambia el nombre dentro de la tabla
        Tabla tablaRenombrada = Tabla.leerTabla(nuevoNombre, directorioBase);
        tablaRenombrada.cambiarNombre(nuevoNombre);
        
        this.tablas.put(nuevoNombre, tablaRenombrada );
    }
    
    /**
     * Elimina la tabla con el nombre dado.
     * @param nombreTabla Nombre de la tabla a eliminar.
     * @throws DatabaseException
     * @throws TableException 
     */
    public void eliminarTabla( String nombreTabla ) throws DatabaseException, TableException{
        // Verifica que la tabla exista
        if( !this.tablas.containsKey(nombreTabla) )
            throw new TableException(TableException.TipoError.TablaNoExiste, nombreTabla);
        
        // Verifica si se puede eliminar la tabla físicamente
        if( tablas.get(nombreTabla).existeEnArchivo() )
            Tabla.eliminarTabla(nombreTabla, directorioBase);
        
        // Invalida y remueve la tabla
        this.tablas.get(nombreTabla).invalidarTabla();
        this.tablas.remove(nombreTabla);
    }

    /**
     * @return Devuelve el nombre del a base de datos.
     */
    public String obtenerNombre() {
        return nombreBD;
    }

    /**
     * @return Devuelve un arreglo con todas las tablas de la base de datos.
     */
    public Tabla[] obtenerTablas() {
        return tablas.values().toArray(new Tabla[0]);
    }
    
    /**
     * Devuelve la tabla con el nombre dado.
     * @param nombreTabla Nombre de la tabla.
     * @return Tabla
     */
    public Tabla obtenerTabla( String nombreTabla ) throws TableException{
        // Verifica que exista
        if( !this.tablas.containsKey(nombreTabla) )
            throw new TableException(TableException.TipoError.TablaNoExiste, nombreTabla );
        
        return this.tablas.get(nombreTabla);
    }
    
    /**
     * Crea y agrega una nueva tabla a la base de datos.
     * @param nombreTabla
     * @return Tabla recien creada.
     */
    public Tabla agregarTabla( String nombreTabla ) throws TableException, DatabaseException{
        // Verifica estado inicial
        verificarEstadoInicial();
        
        // Verifica que no exista la tabla
        if( this.tablas.containsKey(nombreTabla) )
            throw new TableException(TableException.TipoError.TablaYaExiste, nombreTabla);
        
        
        // Crea la nueva tabla y la agrega al listado de tablas
        Tabla nuevaTabla = Tabla.crearTabla(nombreTabla, directorioBase);
        this.tablas.put(nombreTabla, nuevaTabla);
        
        return nuevaTabla;
    }
    
    /**
     * Guarda todos los cambios realizados en la base de datos, incluidas 
     * todas las tablas.
     * @throws DatabaseException
     * @throws TableException 
     */
    public void guardarCambios() throws DatabaseException, TableException{
        for (Map.Entry<String, Tabla> parTabla : tablas.entrySet()) {
            parTabla.getValue().guardarCambios();
        }
    }

    /**
     * Elimina la instanciación.
     */
    private BaseDatos( String nombre, File directorioBase ) throws TableException{
        this.nombreBD = nombre;
        this.directorioBase = directorioBase;
        this.tablas = new HashMap<>();
        
        // Agrega las tablas existentes
        ArrayList<String> nombreTablas = Tabla.obtenerTablas(directorioBase);
        for (String nombreTablaActual : nombreTablas) {
            Tabla tablaActual = Tabla.leerTabla(nombreTablaActual, directorioBase);
            this.tablas.put(nombreTablaActual, tablaActual);
        }
    }
    
    /**
     * Verifica el estado inicial de los directorios de la base de datos.
     */
    private static void verificarEstadoInicial() throws DatabaseException{
        if( directorioRaiz != null )
            return;
        
        directorioRaiz = new File(Configuracion.obtenerDato(Configuracion.DIRECTORIO_BASEDATOS));
        
        // Crea el directorio raiz
        if( !directorioRaiz.exists() ){
            if( !directorioRaiz.mkdir() )
                throw new DatabaseException( "No se pudo crear el directorio raíz para el gestor de bases de datos.");
        } else {
            if( !directorioRaiz.isDirectory() )
                throw new DatabaseException( "No se pudo crear el directorio raíz para el gestor de bases de datos.");
        }
    }
    
    
    
    
    
    public static void main( String[] args ) throws DatabaseException, TableException{
//        BaseDatos bd = crear("BDPruebas");
        BaseDatos bd = buscar("BDPruebas");
        
        OperationNode A_0 = new OperationNode(OperationNode.OperationType.Modulo, 
                new DataNode("TablaA.A-2"), new LiteralNode(2, LiteralNode.LiteralType.INT));
        RelationNode A_1 = new RelationNode(RelationNode.RelationType.Inequal, 
                A_0, new LiteralNode(1, LiteralNode.LiteralType.INT) );
        Condition condCheck = new Condition(A_1);
        
        Tabla A = bd.agregarTabla("TablaA");
        A.agregarColumna("A-1", DataType.INT);
        A.agregarColumna("A-2", DataType.INT);
        A.agregarColumna("A-3", DataType.INT);
        A.agregarColumna("A-4", DataType.CHAR);
        
        A.agregarRestriccion("PK_Llave", new RestriccionLlavePrimaria("TablaA.A-1"));
        A.agregarRestriccion("CH_pares", new RestriccionCheck(condCheck));
        A.agregarRestriccion("STR_1", new RestriccionChar("TablaA.A-4", 10));
        
        for (int i = 0; i < 1000; i++) {
            A.agregarFila(new Row( new Data(Integer.toString(i)), new Data(2*i), new Data(i*i), new Data("" + i + "+" + 2*i)) );
        }
        
//        Tabla A = bd.obtenerTabla("TablaA");
//        A.agregarFila( new Fila(new Dato(10), new Dato(15), new Dato(24), new Dato( "Hola" ) ) );
        
        
        Tabla B = bd.agregarTabla("TablaB");
        B.agregarColumna("B-1", DataType.FLOAT);
        B.agregarColumna("B-2", DataType.FLOAT);
        B.agregarColumna("B-3", DataType.DATE);
        for (int i = 0; i < 500; i++) {
            B.agregarFila(new Row(new Data(i*3.14f), new Data(null), new Data( "1-1-2014" ) ) );
        }
//        Tabla B = bd.obtenerTabla("TablaB");
        
        RelationNode nodoA = new RelationNode(RelationNode.RelationType.Less, 
                new DataNode("TablaA.A-1"), new LiteralNode(5, LiteralType.INT));
        RelationNode nodoB = new RelationNode(RelationNode.RelationType.Equal,
                new LiteralNode("3+6",LiteralType.STRING), new DataNode("TablaA.A-4"));
        ConditionalNode nodoC = new ConditionalNode(ConditionalNode.ConditionalOperationType.AND, nodoA, nodoB);
        Condition condicion = new Condition(nodoC);
        System.out.println("Condicion: " + Arrays.toString( condicion.getUsedColumns()));
        
//        A.eliminarFilas(new Condicion(nodoA));
        
        Relation rel = new RelacionProductoCruz(A.obtenerRelacion(), B.obtenerRelacion());
        rel = new RelacionProyeccion( rel, "TablaA.A-1", "TablaB.B-1", "TablaA.A-3", "TablaA.A-4", "TablaB.B-3", "TablaB.B-2" );
        rel = new RelacionFiltro(rel, condicion);
        
        LinkedHashMap<String, RelacionOrdenamiento.TipoOrdenamiento> camposOrdenar = new LinkedHashMap<>();
        camposOrdenar.put("TablaA.A-1", RelacionOrdenamiento.TipoOrdenamiento.ASCENDENTE);
        camposOrdenar.put("TablaB.B-3", RelacionOrdenamiento.TipoOrdenamiento.ASCENDENTE);
        rel = new RelacionOrdenamiento(rel, camposOrdenar);
        
        System.out.println( rel.getSchema() );
        for (int i = 0; i < rel.getSchema().getSize(); i++) {
            System.out.println( i + ":" + rel.getQualifiedName(i) );
        }
        
        RelationNode updateA = new RelationNode( RelationNode.RelationType.Equal,
                new DataNode("TablaB.B-3"), new LiteralNode("1-1-2014", LiteralType.STRING) );
        RelationNode updateB = new RelationNode( RelationNode.RelationType.Less,
                new DataNode("TablaB.B-1"), new LiteralNode( 100.0f, LiteralType.FLOAT) );
        ConditionalNode updateC = new ConditionalNode(ConditionalNode.ConditionalOperationType.AND, updateA, updateB);
        LiteralNode updateCambio = new LiteralNode("1-1-2012", LiteralType.STRING);
        
        HashMap<String, Expression> cambios = new HashMap<>();
        cambios.put("TablaB.B-3", new Expression(updateCambio));
        B.actualizarFilas(cambios, new Condition(updateC) );
        
        int cantidad = 0; 
        for (Row filaActual : rel) {
//            System.out.println( filaActual );
            ++cantidad;
        }
        System.out.println("Total: " + cantidad);
        
        // Elimina la columna con char
        System.out.println("ELIMINAR COLUMNA");
        A.eliminarColumna("A-4");
        for (String restriccion : A.obtenerRestricciones().keySet()) {
            System.out.println(restriccion);
        }
        
        int cantidad2 = 0;
        for (Row fila : A.obtenerRelacion()) {
//            System.out.println(fila);
            ++cantidad2;
        }
        System.out.println("TOTAL: " + cantidad2);
        
        
        // Prueba la condicion
//        System.out.println("\nEVALUAR CONDICION");
//        EvaluadorCondicion evaluador = new EvaluadorCondicion(condicion, rel);
//        cantidad = 0;
//        for (Fila filaActual : rel) {
//            if( evaluador.evaluarFila(filaActual) ){
//                System.out.println( filaActual );
//                ++cantidad;
//            }
//        }
//        System.out.println("Total: " + cantidad);
        
//        bd.guardarCambios();
    }
}
