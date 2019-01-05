package motor;

import condicion.Condicion;
import condicion.Expresion;
import condicion.NodoCondicional;
import condicion.NodoDato;
import condicion.NodoLiteral;
import condicion.NodoLiteral.TipoLiteral;
import condicion.NodoOperacional;
import condicion.NodoRelacional;
import excepciones.ExcepcionBaseDatos;
import excepciones.ExcepcionTabla;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import motor.relacion.Fila;
import motor.relacion.Relacion;
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
     * @throws ExcepcionBaseDatos
     * @throws ExcepcionTabla 
     */
    public static BaseDatos crear( String nombre ) throws ExcepcionBaseDatos, ExcepcionTabla{
        // Verificar estado inicial
        verificarEstadoInicial();
        
        // Verifica que la base de datos no exista
        try {
            buscar(nombre);
            
            // Puesto que la base de datos se encontró, significa que ya existe
            // una con ese nombre.
            throw new ExcepcionBaseDatos(ExcepcionBaseDatos.TipoError.NombreYaExiste, nombre);
            
        } catch (ExcepcionBaseDatos excepcionBaseDatos) {
            if( excepcionBaseDatos.obtenerTipoError() != ExcepcionBaseDatos.TipoError.NoExiste )
                throw excepcionBaseDatos;
        }
        
        
        // Se crea la nueva base de datos
        File directorioBD = new File( directorioRaiz, nombre );
        if( !directorioBD.mkdir() )
            throw new ExcepcionBaseDatos( "No se pudo crear el directorio para la base de datos " + nombre );
        
        return new BaseDatos(nombre, directorioBD);
    }
    
    /**
     * Obtiene una base de datos con el nombre dado.
     * @param nombre Nombre de la base de datos a utilizar.
     * @throws ExcepcionBaseDatos 
     * @throws ExcepcionTabla
     */
    public static BaseDatos buscar( String nombre ) throws ExcepcionBaseDatos, ExcepcionTabla{
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
            throw new ExcepcionBaseDatos(ExcepcionBaseDatos.TipoError.NoExiste, nombre);
        
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
     * @throws ExcepcionBaseDatos 
     */
    public static void eliminar( String nombre ) throws ExcepcionBaseDatos, ExcepcionTabla{
        // Verifica el estado inicial
        verificarEstadoInicial();
        
        // Obtiene la base de datos
        BaseDatos bd = buscar(nombre);
        
        // Elimina todos los archivos en el directorio
        File directorio = bd.directorioBase;
        try {
            FileUtils.deleteDirectory(directorio);
        } catch (IOException ex) {
            throw new ExcepcionBaseDatos( String.format("No se pudo eliminar la base de datos: %s [%s]", 
                    nombre, ex.getMessage()) );
        }
    }
    
    /**
     * Obtiene el nombre de las bases de datos.
     * @return Arreglo con los nombres.
     */
    public static ArrayList<String> mostrar() throws ExcepcionBaseDatos{
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
     * @throws ExcepcionBaseDatos 
     */
    public static void renombrar( String nombreActual, String nombreNuevo ) throws ExcepcionBaseDatos{
        // Verifica el estado inicial
        verificarEstadoInicial();
        
        // Busca la base de datos
        ArrayList<String> bdActuales = mostrar();
        if( !bdActuales.contains(nombreActual) )
            throw new ExcepcionBaseDatos(ExcepcionBaseDatos.TipoError.NoExiste, nombreActual);
        
        if( bdActuales.contains(nombreNuevo) )
            throw new ExcepcionBaseDatos(ExcepcionBaseDatos.TipoError.NombreYaExiste, nombreNuevo);
        
        // Renombra la base de datos
        File antiguo = new File(directorioRaiz, nombreActual);
        File nuevo = new File(directorioRaiz, nombreNuevo);
        if( !antiguo.renameTo(nuevo) ){
            throw new ExcepcionBaseDatos(String.format("No se pudo renombrar la base de datos %s a %s.", 
                    nombreActual, nombreNuevo) );
        }
    }
    
    
    
    
    /**
     * Cambia el nombre de una tabla.
     * @param nombreTabla Nombre de la tabla.
     * @param nuevoNombre Nuevo nombre de la tabla.
     * @throws ExcepcionBaseDatos
     * @throws ExcepcionTabla 
     */
    public void renombrarTabla( String nombreTabla, String nuevoNombre ) throws ExcepcionBaseDatos, ExcepcionTabla{
        // Verifica que la tabla a renombrar exista y que no exista una tabla con el nuevo nombre.
        if( !this.tablas.containsKey(nombreTabla) )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.TablaNoExiste, nombreTabla);
        if( this.tablas.containsKey(nuevoNombre) )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.TablaYaExiste, nuevoNombre);
        
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
     * @throws ExcepcionBaseDatos
     * @throws ExcepcionTabla 
     */
    public void eliminarTabla( String nombreTabla ) throws ExcepcionBaseDatos, ExcepcionTabla{
        // Verifica que la tabla exista
        if( !this.tablas.containsKey(nombreTabla) )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.TablaNoExiste, nombreTabla);
        
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
    public Tabla obtenerTabla( String nombreTabla ) throws ExcepcionTabla{
        // Verifica que exista
        if( !this.tablas.containsKey(nombreTabla) )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.TablaNoExiste, nombreTabla );
        
        return this.tablas.get(nombreTabla);
    }
    
    /**
     * Crea y agrega una nueva tabla a la base de datos.
     * @param nombreTabla
     * @return Tabla recien creada.
     */
    public Tabla agregarTabla( String nombreTabla ) throws ExcepcionTabla, ExcepcionBaseDatos{
        // Verifica estado inicial
        verificarEstadoInicial();
        
        // Verifica que no exista la tabla
        if( this.tablas.containsKey(nombreTabla) )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.TablaYaExiste, nombreTabla);
        
        
        // Crea la nueva tabla y la agrega al listado de tablas
        Tabla nuevaTabla = Tabla.crearTabla(nombreTabla, directorioBase);
        this.tablas.put(nombreTabla, nuevaTabla);
        
        return nuevaTabla;
    }
    
    /**
     * Guarda todos los cambios realizados en la base de datos, incluidas 
     * todas las tablas.
     * @throws ExcepcionBaseDatos
     * @throws ExcepcionTabla 
     */
    public void guardarCambios() throws ExcepcionBaseDatos, ExcepcionTabla{
        for (Map.Entry<String, Tabla> parTabla : tablas.entrySet()) {
            parTabla.getValue().guardarCambios();
        }
    }

    /**
     * Elimina la instanciación.
     */
    private BaseDatos( String nombre, File directorioBase ) throws ExcepcionTabla{
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
    private static void verificarEstadoInicial() throws ExcepcionBaseDatos{
        if( directorioRaiz != null )
            return;
        
        directorioRaiz = new File(Configuracion.obtenerDato(Configuracion.DIRECTORIO_BASEDATOS));
        
        // Crea el directorio raiz
        if( !directorioRaiz.exists() ){
            if( !directorioRaiz.mkdir() )
                throw new ExcepcionBaseDatos( "No se pudo crear el directorio raíz para el gestor de bases de datos.");
        } else {
            if( !directorioRaiz.isDirectory() )
                throw new ExcepcionBaseDatos( "No se pudo crear el directorio raíz para el gestor de bases de datos.");
        }
    }
    
    
    
    
    
    public static void main( String[] args ) throws ExcepcionBaseDatos, ExcepcionTabla{
//        BaseDatos bd = crear("BDPruebas");
        BaseDatos bd = buscar("BDPruebas");
        
        NodoOperacional A_0 = new NodoOperacional(NodoOperacional.TipoOperacionOperacional.Modulo, 
                new NodoDato("TablaA.A-2"), new NodoLiteral(2, NodoLiteral.TipoLiteral.INT));
        NodoRelacional A_1 = new NodoRelacional(NodoRelacional.TipoOperacionRelacional.Diferente, 
                A_0, new NodoLiteral(1, NodoLiteral.TipoLiteral.INT) );
        Condicion condCheck = new Condicion(A_1);
        
        Tabla A = bd.agregarTabla("TablaA");
        A.agregarColumna("A-1", TipoDato.INT);
        A.agregarColumna("A-2", TipoDato.INT);
        A.agregarColumna("A-3", TipoDato.INT);
        A.agregarColumna("A-4", TipoDato.CHAR);
        
        A.agregarRestriccion("PK_Llave", new RestriccionLlavePrimaria("TablaA.A-1"));
        A.agregarRestriccion("CH_pares", new RestriccionCheck(condCheck));
        A.agregarRestriccion("STR_1", new RestriccionChar("TablaA.A-4", 10));
        
        for (int i = 0; i < 1000; i++) {
            A.agregarFila( new Fila( new Dato(Integer.toString(i)), new Dato(2*i), new Dato(i*i), new Dato("" + i + "+" + 2*i)) );
        }
        
//        Tabla A = bd.obtenerTabla("TablaA");
//        A.agregarFila( new Fila(new Dato(10), new Dato(15), new Dato(24), new Dato( "Hola" ) ) );
        
        
        Tabla B = bd.agregarTabla("TablaB");
        B.agregarColumna("B-1", TipoDato.FLOAT);
        B.agregarColumna("B-2", TipoDato.FLOAT);
        B.agregarColumna("B-3", TipoDato.DATE);
        for (int i = 0; i < 500; i++) {
            B.agregarFila( new Fila(new Dato(i*3.14f), new Dato(null), new Dato( "1-1-2014" ) ) );
        }
//        Tabla B = bd.obtenerTabla("TablaB");
        
        NodoRelacional nodoA = new NodoRelacional(NodoRelacional.TipoOperacionRelacional.Menor, 
                new NodoDato("TablaA.A-1"), new NodoLiteral(5, TipoLiteral.INT));
        NodoRelacional nodoB = new NodoRelacional(NodoRelacional.TipoOperacionRelacional.Igual,
                new NodoLiteral("3+6",TipoLiteral.STRING), new NodoDato("TablaA.A-4"));
        NodoCondicional nodoC = new NodoCondicional(NodoCondicional.TipoOperacionCondicional.AND, nodoA, nodoB);
        Condicion condicion = new Condicion(nodoC);
        System.out.println("Condicion: " + Arrays.toString( condicion.obtenerColumnasUtilizadas()));
        
//        A.eliminarFilas(new Condicion(nodoA));
        
        Relacion rel = new RelacionProductoCruz(A.obtenerRelacion(), B.obtenerRelacion());
        rel = new RelacionProyeccion( rel, "TablaA.A-1", "TablaB.B-1", "TablaA.A-3", "TablaA.A-4", "TablaB.B-3", "TablaB.B-2" );
        rel = new RelacionFiltro(rel, condicion);
        
        LinkedHashMap<String, RelacionOrdenamiento.TipoOrdenamiento> camposOrdenar = new LinkedHashMap<>();
        camposOrdenar.put("TablaA.A-1", RelacionOrdenamiento.TipoOrdenamiento.ASCENDENTE);
        camposOrdenar.put("TablaB.B-3", RelacionOrdenamiento.TipoOrdenamiento.ASCENDENTE);
        rel = new RelacionOrdenamiento(rel, camposOrdenar);
        
        System.out.println( rel.obtenerEsquema() );
        for (int i = 0; i < rel.obtenerEsquema().obtenerTamaño(); i++) {
            System.out.println( i + ":" + rel.obtenerNombreCalificado(i) );
        }
        
        NodoRelacional updateA = new NodoRelacional( NodoRelacional.TipoOperacionRelacional.Igual,
                new NodoDato("TablaB.B-3"), new NodoLiteral("1-1-2014", TipoLiteral.STRING) );
        NodoRelacional updateB = new NodoRelacional( NodoRelacional.TipoOperacionRelacional.Menor,
                new NodoDato("TablaB.B-1"), new NodoLiteral( 100.0f, TipoLiteral.FLOAT) );
        NodoCondicional updateC = new NodoCondicional(NodoCondicional.TipoOperacionCondicional.AND, updateA, updateB);
        NodoLiteral updateCambio = new NodoLiteral("1-1-2012", TipoLiteral.STRING);
        
        HashMap<String, Expresion> cambios = new HashMap<>();
        cambios.put("TablaB.B-3", new Expresion(updateCambio));
        B.actualizarFilas(cambios, new Condicion(updateC) );
        
        int cantidad = 0; 
        for (Fila filaActual : rel) {
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
        for (Fila fila : A.obtenerRelacion()) {
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
