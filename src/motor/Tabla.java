package motor;

import motor.restriccion.Restriccion;
import condition.Condition;
import condition.ConditionEvaluator;
import condition.ExpressionEvaluator;
import condition.Expression;
import excepciones.TableException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import motor.relacion.Schema;
import motor.relacion.Row;
import motor.relacion.Relation;
import motor.relacion.RelacionTemporalFila;
import motor.relacion.RelacionTerminal;
import motor.restriccion.RestriccionChar;
import motor.restriccion.RestriccionCheck;
import motor.restriccion.RestriccionLlavePrimaria;

/**
 *
 * @author Jorge
 */
public class Tabla {
    private String nombreTabla;
    private LinkedHashMap<String, DataType> columnas;
    private HashMap<String, Restriccion> restricciones;
    
    private RelacionTerminal datos;
    private File archivoTabla;
    
    /** Verifica si se puede realizar la verificación de una llave primaria de forma incremental o no */
    transient private boolean llavePrimariaIncremental = false;
    
    /** Verifica si hay cambios o no en la tabla */
    transient private boolean hayCambios = false;
    
    /** Contiene si la tabla es válida */
    private transient boolean esValida = true;
    
    /**
     * Agrega una columna al final de la tabla con el nombre y tipo dados.
     * @param nombreNuevaColumna Nombre de la columna
     * @param tipoNuevaColumna Tipo de la columna
     * @throws TableException 
     */
    public void agregarColumna( String nombreNuevaColumna, DataType tipoNuevaColumna ) throws TableException{
        // Verifica que no exista la columna
        if( columnas.containsKey(nombreNuevaColumna) )
            throw new TableException(TableException.TipoError.ColumnaYaExiste, nombreNuevaColumna);
        
        // Verifica que la columna no sea null
        if( tipoNuevaColumna == DataType.NULL )
            throw new TableException(TableException.TipoError.EsquemaInvalido, 
                    "No se puede utilizar NULL para el esquema de una columna." );
        
        // Crea la nueva relación
        Relation datosAntiguos = datos;
        Schema nuevoEsquema = Schema.agregarTipo(datos.getSchema(), tipoNuevaColumna);
        
        RelacionTerminal nuevaRelacion = new RelacionTerminal(nuevoEsquema);
        nuevaRelacion.asociarTabla(this);
        
        // Copia los datos de la relación antigua
        if( datosAntiguos.obtenerCantidadFilas() != 0 ){
            for (Row filaActual: datosAntiguos) {
                Row nuevaFila = Row.agregarDatos(filaActual, Data.obtenerValorPorDefecto(tipoNuevaColumna) );
                
                nuevaRelacion.agregarFila( nuevaFila );
            }
        }
        
        // Agrega la nueva columna al esquema
        columnas.put(nombreNuevaColumna, tipoNuevaColumna);
        
        datos = nuevaRelacion;
        hayCambios = true;
    }
    
    /**
     * Agrea una restricción a la tabla.
     * @param nombreRestriccion Nombre de la restricción.
     * @param nuevaRestriccion Restricción
     * @throws TableException 
     */
    public void agregarRestriccion( String nombreRestriccion, Restriccion nuevaRestriccion ) throws TableException{
        // Verifica el nombre de la restriccion
        if( restricciones.containsKey(nombreRestriccion) )
            throw new TableException(TableException.TipoError.RestriccionYaExiste, nombreRestriccion);
        
        // Verifica que no exista otra restriccion de llave primaria
        if( nuevaRestriccion instanceof RestriccionLlavePrimaria ){
            for (Restriccion restriccionActual : restricciones.values()) {
                if( restriccionActual instanceof RestriccionLlavePrimaria )
                    throw new TableException(TableException.TipoError.RestriccionYaExiste, "[Llave Primaria]");
            }
        }
        
        // Verifica que la nueva restriccion se cumpla
        verificarRestriccion(datos, nuevaRestriccion);
        
        this.restricciones.put(nombreRestriccion, nuevaRestriccion);
        
        // Verifica si se pueden hacer revisiones incrementales de llave primaria.
        if( nuevaRestriccion instanceof RestriccionLlavePrimaria )
            llavePrimariaIncremental = true;
        
        hayCambios = true;
    }

    /**
     * Elimina la columna con el nombre dado.
     * @param nombreColumnaEliminar Nombre de la columna.
     * @throws TableException 
     */
    public void eliminarColumna( String nombreColumnaEliminar ) throws TableException{
        // Verifica que la columna exista
        if( !this.columnas.containsKey(nombreColumnaEliminar) )
            throw new TableException(TableException.TipoError.ColumnDoesNotExist, nombreColumnaEliminar);
        
        // Verifica que ninguna restriccion dependa de esta columna
        String nombreCalificadoColumna = Util.obtenerNombreCalificado(nombreTabla, nombreColumnaEliminar);
        for (String nombreRestriccion : restricciones.keySet()) {
            Restriccion restriccion = restricciones.get(nombreRestriccion);
            // LLAVE PRIMARIA
            if( restriccion instanceof RestriccionLlavePrimaria ){
                RestriccionLlavePrimaria llavePrimaria = (RestriccionLlavePrimaria) restriccion;
                if( llavePrimaria.obtenerCamposReferenciados().contains(nombreCalificadoColumna) )
                    throw new TableException(TableException.TipoError.ErrorReferencia, 
                            String.format("La llave primaria %s depende de la columna %s.", nombreRestriccion, nombreColumnaEliminar));
            } else if (restriccion instanceof RestriccionCheck){
                // CHECK
                RestriccionCheck check = (RestriccionCheck) restriccion;
                if( check.obtenerCamposReferenciados().contains(nombreCalificadoColumna) )
                    throw new TableException(TableException.TipoError.ErrorReferencia, 
                            String.format("La restricción check %s depende de la columna %s.", nombreRestriccion, nombreColumnaEliminar));
            } else {
                // TODO: Agregar las demás restricciones.
            }
        }
        
        // Encuentra el id de la columna
        ArrayList<String> nombreColumnas = new ArrayList<>( columnas.keySet() );
        int idColumna = nombreColumnas.indexOf( nombreColumnaEliminar );
        
        // Crea la nueva relación
        Relation relacionAntigua = datos;
        
        Schema nuevoEsquema = Schema.eliminarTipos(relacionAntigua.getSchema(), idColumna);
        RelacionTerminal nuevaRelacion = new RelacionTerminal(nuevoEsquema);
        nuevaRelacion.asociarTabla(this);
        columnas.remove(nombreColumnaEliminar);
        
        
        // Agrega los datos
        for (Row filaActual : relacionAntigua) {
            Row nuevaFila = Row.eliminarDatos(filaActual, idColumna);
            nuevaRelacion.agregarFila( nuevaFila );
        }
        for (Iterator<String> it = restricciones.keySet().iterator(); it.hasNext();) {
            Restriccion restriccionActual = restricciones.get(it.next());
            if( restriccionActual instanceof RestriccionChar ){
                RestriccionChar restriccionChar = (RestriccionChar)restriccionActual;
                
                if( restriccionChar.obtenerCampoReferenciado().equals(nombreCalificadoColumna) )
                    it.remove();
            }
        }
        
        // Marca que se reconstruya la llave primaria
        llavePrimariaIncremental = false;
        
        // Realiza los cambios
        datos = nuevaRelacion;
        datos.asociarTabla(this);
        
        hayCambios = true;
    }
    
    /**
     * Elimina una restriccion con el nombre dado.
     * @param nombreRestriccion
     * @throws TableException 
     */
    public void eliminarRestriccion( String nombreRestriccion ) throws TableException{
        this.restricciones.remove(nombreRestriccion);
        
        hayCambios = true;
    }
    
    /**
     * Agrega una nueva fila a la relacion.
     * @param nuevaFila Fila a agregar.
     */
    public void agregarFila( Row nuevaFila ) throws TableException{
        try{
            verificarRestriccionesEnFila(nuevaFila);
        }catch( TableException ex ){
            // Marca que al siguiente paso se reevalúe la llave primaria
            llavePrimariaIncremental = false;
            throw ex;
        }
        this.datos.agregarFila(nuevaFila);
        hayCambios = true;
    }
    
    /**
     * Elimina todas las filas que cumplan la condicion dada.
     * @param condicion Condicion a verificar.
     * @return Cantidad de filas que fueron eliminadas.
     * @throws TableException 
     */
    public int eliminarFilas( Condition condicion ) throws TableException{
        ConditionEvaluator evaluador = new ConditionEvaluator(condicion, datos);
        
        Iterator<Row> iterador = datos.iterator();
        int cantidad = 0;
        while( iterador.hasNext() ){
            if( evaluador.evaluateRow(iterador.next()) ){
                iterador.remove();
                ++cantidad;
            }
        }
        
        // Puesto que se eliminó una fila, ya no se puede evaluar la llave
        // primaria de forma incremental.
        llavePrimariaIncremental = false;
        hayCambios = true;
        
        return cantidad;
    }
    
    /**
     * Permite actualizar las filas dadas con los valores utilizados.
     * @param expresiones Expresiones para cada campo.
     * @param condicion Condicion a evaluar.
     * @return 
     */
    public int actualizarFilas( HashMap<String, Expression> expresiones, Condition condicion ){
        ConditionEvaluator evaluador = new ConditionEvaluator(condicion, datos);
        
        // Crea todos los evaluadores
        HashMap<String, ExpressionEvaluator> evaluadores = new HashMap<>();
        for (String campoActual : expresiones.keySet()) {
            Expression exp = expresiones.get(campoActual);
            evaluadores.put(campoActual, new ExpressionEvaluator(exp, datos));
            
            /* Verifica que los campos existan en la tabla */
            if( !columnas.containsKey(Util.getFieldName(campoActual) ) )
                throw new TableException(TableException.TipoError.ColumnDoesNotExist, campoActual);
        }
        
        // Obtiene el índice de cada campo
        HashMap<String, Integer> indiceCampos = new HashMap<>();
        int tamEsquema = datos.getSchema().getSize();
        for (int i = 0; i < tamEsquema; i++)
            indiceCampos.put(datos.getQualifiedName(i), i);
        
        // Arreglo de las filas a agregar
        ArrayList<Row> filasModificadas = new ArrayList<>();
        
        // Analiza cada una de las filas
        int modificaciones = 0;
        Iterator<Row> iteradorFilas = datos.iterator();
        while( iteradorFilas.hasNext() ){
            Row filaActual = iteradorFilas.next();
            Data[] datosActuales = filaActual.obtenerDatos();
            
            if( evaluador.evaluateRow(filaActual) ){
                // Elimina la fila actual, la recalcula y la inserta
                iteradorFilas.remove();
                llavePrimariaIncremental = false;
                
                Data[] datosNuevos = new Data[tamEsquema];
                System.arraycopy(datosActuales, 0, datosNuevos, 0, datosActuales.length);
                
                /* .. cambiar campos */
                for (String campoCambiar : evaluadores.keySet()) {
                    Data datoCalculado = evaluadores.get(campoCambiar).evaluateRow(filaActual);
                    datosNuevos[indiceCampos.get(campoCambiar)] = datoCalculado;
                }
                
                // Trata de insertar la nueva fila
//                agregarFila( new Fila( datosNuevos ) );
                filasModificadas.add(new Row(datosNuevos) );
                ++modificaciones;
            }
        }
        
        // Agrega las filas modificadas
        for (Row filaActual : filasModificadas) {
            agregarFila( filaActual );
        }
        
        hayCambios = true;
        return modificaciones;
    }
    
    /**
     * @return Devuelve un hash map con los datos de todas las columnas de la tabla.
     */
    public HashMap<String, DataType> obtenerColumnas(){
        LinkedHashMap<String, DataType> retorno = new LinkedHashMap<>();
        for (Map.Entry<String, DataType> columnaActual : columnas.entrySet()) {
            retorno.put(columnaActual.getKey(), columnaActual.getValue());
        }
        
        return retorno;
    }
    
    /**
     * Devuelve el nombre de todas las columnas.
     * @return String[] con los nombres.
     */
    public String[] obtenerNombreColumnas(){
        return columnas.keySet().toArray( new String[0] );
    }
    
    /**
     * @return Devuelve un hashmap con los datos de todas las restricciones.
     */
    public HashMap<String, Restriccion> obtenerRestricciones(){
        HashMap<String, Restriccion> retorno = new HashMap<>();
        for (Map.Entry<String, Restriccion> restriccionActual : restricciones.entrySet()) {
            retorno.put(restriccionActual.getKey(), restriccionActual.getValue());
        }
        
        return retorno;
    }
    
    /**
     * Devuelve la relación de la tabla.
     * @return Relación física de la tabla.
     */
    public Relation obtenerRelacion(){
        return this.datos;
    }
    
    /**
     * @return Retorna el nombre de la tabla
     */
    public String obtenerNombre(){
        return nombreTabla;        
    }
    
    /**
     * Cambia el nombre de la tabla, volviendo a crear todas las restricciones.
     * @param nuevoNombre Nuevo nombre de la tabla
     */
    void cambiarNombre( String nuevoNombre ){
        nombreTabla = nuevoNombre;
        llavePrimariaIncremental = true;
        hayCambios = true;

        // Reconstruye las restricciones
        for (Restriccion restriccionActual : restricciones.values()) {
            restriccionActual.cambiarNombreTabla(nuevoNombre);
        }
    }
    
    
    /**
     * Verifica que todas las filas de una columna cumplan con la restricción
     * dada.
     * @param restriccion Restricción a verificar.
     * @throws TableException
     */
    private void verificarRestriccion( Relation relacionPrueba, Restriccion restriccion ) throws TableException{
        
        // Verifica el tipo de restriccion
        if( restriccion instanceof RestriccionLlavePrimaria ){
            // LLAVE PRIMARIA
            ((RestriccionLlavePrimaria)restriccion).evaluarRestriccion(relacionPrueba);
            
        } else if( restriccion instanceof RestriccionCheck ){
            // CHECK
            ((RestriccionCheck)restriccion).evaluarRestriccion(relacionPrueba);
            
        } else if( restriccion instanceof RestriccionChar ){
            // CHAR
            ((RestriccionChar)restriccion).evaluarRestriccion(relacionPrueba);
            
        } else {
            throw new UnsupportedOperationException();
        }
    }
    
    /**
     * Verifica que se cumplan todas las restricciones en la tabla.
     * @throws TableException 
     */
    private void verificarTodasRestricciones( Relation relacionPrueba ) throws TableException{
        // Verifica todas las restricciones
        for (Restriccion restriccionActual : restricciones.values()) {
            verificarRestriccion(relacionPrueba, restriccionActual);
        }
    }
    
    /**
     * Verifica que la nueva fila pase todas las restricciones, tomando en cuenta
     * los datos actuales de la base de datos.
     * @param filaVerificar Fila a verificar
     */
    private void verificarRestriccionesEnFila( Row filaVerificar ) throws TableException{
        for (Restriccion restriccionActual : restricciones.values()) {
            // LLAVE PRIMARIA
            if( restriccionActual instanceof RestriccionLlavePrimaria){
                if( llavePrimariaIncremental ){
                    ((RestriccionLlavePrimaria)restriccionActual).evaluarFilaIncremental(filaVerificar);
                } else {
                    // Crea la Relación dummy
                    RelacionTemporalFila relacionDummy = new RelacionTemporalFila(datos, filaVerificar);
                    verificarRestriccion(relacionDummy, restriccionActual);
                }
            } else if( restriccionActual instanceof RestriccionCheck ){
                // CHECK
                ((RestriccionCheck)restriccionActual).evaluarRestriccion(datos, filaVerificar);
                
            } else if( restriccionActual instanceof RestriccionChar ) {
                // CHAR
                ((RestriccionChar)restriccionActual).evaluarRestriccion(datos, filaVerificar);
                
            } else {
                // TODO
                throw new UnsupportedOperationException();
            }
        }
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="SERIALIZACIÓN">
    
    /**
     * Guarda los cambios realizados en la tabla.
     * @throws TableException
     */
    void guardarCambios() throws TableException{
        if( hayCambios )
            this.guardarTabla();
    }
    
    /**
     * Verifica si la tabla existe físicamente en archivo.
     * @return True si la tabla existe físicamente.
     */
    boolean existeEnArchivo(){
        return archivoTabla.exists();
    }
    
    /**
     * Invalida la tabla actual.
     */
    void invalidarTabla(){
        esValida = false;
    }
    
    /**
     * Devuelve si la tabla es válida o no.
     * @return True si la tabla es válida.
     */
    boolean esValida(){
        return this.esValida;
    }
    
    /**
     * Guarda los detalles de la tabla en un archivo.
     * @throws TableException Si existe algún error en la escritura.
     */
    private void guardarTabla() throws TableException{
        // Verifica que la tabla sea válida
        if( !esValida() )
            throw new TableException(TableException.TipoError.TablaNoEsValida, nombreTabla);
        
        try (BufferedOutputStream buffer = new BufferedOutputStream(new FileOutputStream(archivoTabla));
                ObjectOutputStream salida = new ObjectOutputStream(buffer)) {
            
            // Guarda Columnas, Restricción y Datos en ese orden.
            salida.writeObject(columnas);
            salida.writeObject(restricciones);
            salida.writeObject(datos);
            
            salida.flush();
        }catch (IOException iOException) {
            throw new TableException( String.format("No se pudo guardar los datos de la tabla %s (%s)",
                    this.nombreTabla, iOException.getMessage() ) );
        }
    }
    
    /**
     * Lee los datos de una tabla.
     * @throws TableException
     */
    private void leerTabla() throws TableException{
        LinkedHashMap<String, DataType> columnasLeidas;
        HashMap<String, Restriccion> restriccionesLeidas;
        RelacionTerminal datosLeidos;
        try (BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(archivoTabla));
                ObjectInputStream entrada = new ObjectInputStream(buffer)) {
            columnasLeidas = (LinkedHashMap<String, DataType>) entrada.readObject();
            restriccionesLeidas = (HashMap<String, Restriccion>) entrada.readObject();
            datosLeidos = (RelacionTerminal) entrada.readObject();
            
            // Guarda los datos en la tabla
            this.columnas = columnasLeidas;
            this.restricciones = restriccionesLeidas;
            this.datos = datosLeidos;
            this.datos.asociarTabla(this);
        }catch (IOException | ClassNotFoundException ex) {
            throw new TableException(String.format( "No se pudo leer la tabla %s (%s).",
                    this.nombreTabla, ex.getMessage()) );
        }
    }
    //</editor-fold>
    
    
    /**
     * Crea una nueva tabla vacía con el nombre dado.
     * @param nombre Nombre de la tabla.
     * @param archivoTabla Archivo físico de la tabla.
     */
    private Tabla( String nombre, File archivoTabla ){
        this.nombreTabla = nombre;
        this.columnas = new LinkedHashMap<>();
        this.restricciones = new HashMap<>();
        this.restricciones = new HashMap<>();
        this.archivoTabla = archivoTabla;
        
        this.datos = new RelacionTerminal( new Schema( new DataType[0] ) );
        this.datos.asociarTabla(this);
    }
    
    
    
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS ESTÁTICOS">
    
    /**
     * Crea una tabla dada en el directorio.
     * @param nombre Nombre de la tabla nueva.
     * @param directorioBaseDatos Directorio donde se crea la tabla.
     * @return Tabla con los datos leídos.
     * @throws TableException
     */
    static Tabla crearTabla( String nombre, File directorioBaseDatos ) throws TableException{
        // Verifica que la tabla no exista
        if( obtenerTablas(directorioBaseDatos).contains(nombre) )
            throw new TableException(TableException.TipoError.TablaYaExiste, nombre);
        
        // Crea la nueva tabla
        Tabla nuevaTabla = new Tabla(nombre, new File(directorioBaseDatos, nombre) );
        
        // AUN NO GUARDA LA TABLA
        // nuevaTabla.guardarTabla();
        
        return nuevaTabla;
    }
    
    /**
     * Lee la tabla con el nombre dado.
     * @param nombre Nombre de la tabla
     * @param directorioBaseDatos Directorio de la base de datos.
     * @return Tabla con los datos.
     * @throws TableException
     */
    static Tabla leerTabla( String nombre, File directorioBaseDatos ) throws TableException{
        // Verifica que la tabla exista
        if( !obtenerTablas(directorioBaseDatos).contains(nombre) )
            throw new TableException(TableException.TipoError.TablaNoExiste, nombre);
        
        // Lee los datos de la tabla
        File directorioTabla = new File(directorioBaseDatos, nombre);
        Tabla tablaLeida = new Tabla(nombre, directorioTabla);
        tablaLeida.leerTabla();
        
        return tablaLeida;
    }
    
    /**
     * Lee todas las tablas en el directorio dado.
     * @param directorioBaseDatos Directorio de la base de datos.
     * @return Arreglo con los nombre de todas las tablas.
     */
    static ArrayList<String> obtenerTablas( File directorioBaseDatos ){
        ArrayList<String> retorno = new ArrayList<>();
        retorno.addAll(Arrays.asList(directorioBaseDatos.list()));
        
        return retorno;
    }
    
    /**
     * Elimina la tabla con el nombre dado.
     * @param nombre Nombre de la tabla a eliminar.
     * @param directorioBaseDatos Directorio de la base de datos.
     * @throws TableException
     */
    static void eliminarTabla( String nombre, File directorioBaseDatos ) throws TableException{
        // Verifica que la tabla exista
        if( !obtenerTablas(directorioBaseDatos).contains(nombre) )
            throw new TableException(TableException.TipoError.TablaNoExiste, nombre);
        
        
        // Elimina la tabla
        File archivoTabla = new File(directorioBaseDatos, nombre);
        try {
            archivoTabla.delete();
        } catch (Exception e) {
            throw new TableException(String.format( "No se pudo eliminar la tabla %s (%s).",
                    nombre, e.getMessage() ) );
        }
    }
    
    /**
     * Cambia el nombre de una tabla.
     * @param nombre Nombre de la tabla a renombrar.
     * @param nuevoNombre Nuevo nombre de la tabla.
     * @param directorioBaseDatos Directorio donde se encuentra la base de datos.
     * @throws TableException
     */
    static void renombrarTabla( String nombre, String nuevoNombre, File directorioBaseDatos ) throws TableException{
        // Verifica que la tabla exista y la nueva no
        ArrayList<String> tablas = obtenerTablas(directorioBaseDatos);
        if( !tablas.contains(nombre) )
            throw new TableException(TableException.TipoError.TablaNoExiste, nombre);
        if( tablas.contains(nuevoNombre) )
            throw new TableException(TableException.TipoError.TablaYaExiste, nuevoNombre);
        
        
        File antiguo = new File(directorioBaseDatos, nombre);
        File nuevo = new File(directorioBaseDatos, nuevoNombre);
        if( !antiguo.renameTo(nuevo) )
            throw new TableException(String.format("No se pudo renombrar la tabla %s a %s.",
                    nombre, nuevoNombre) );
    }
    //</editor-fold>
}
