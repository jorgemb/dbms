package motor.relacion;

import excepciones.TableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import motor.Data;
import motor.DataType;
import org.stringtemplate.v4.misc.ArrayIterator;

/**
 *
 * @author Jorge
 */
public class Row implements java.io.Serializable, Iterable<Data>{
    /** Datos asociados a la fila */
    private final Data[] datos;
    
    /** Esquema asociado de la fila */
    private transient Schema esquemaFila;
    

    /**
     * Constructor con datos.
     * @param datos Datos asociados
     */
    public Row(Data ... datos) {
        this.datos = datos;
    }
    
    /**
     * Obtiene el esquema de la fila.
     * @return Arreglo con el tipo de dato de cada parte.
     * @throws TableException Lanzada si algún dato es inválido.
     */
    public Schema obtenerEsquema() throws TableException{
        if( esquemaFila != null )
            return esquemaFila;
        
        DataType[] esquema = new DataType[this.datos.length];
        for (int i = 0; i != datos.length; i++) {
            if( (esquema[i] = datos[i].obtenerTipo()) == null )
                throw new TableException(TableException.TipoError.EsquemaInvalido,
                        String.format( "La fila posee un dato no soportado (%s).",
                        datos[i].getValue().getClass() ) );
        }
        esquemaFila = new Schema(esquema);
        
        return esquemaFila;
    }
    
    /**
     * Iterador de la fila.
     * @return 
     */
    @Override
    public Iterator<Data> iterator() {
        return (Iterator)new ArrayIterator( datos );
    }
    
    /**
     * @return Devuelve un arreglo con los datos.
     */
    public Data[] obtenerDatos(){
        return datos;
    }
    
    /**
     * @return Devuelve el dato en la posición dada.
     */
    public Data getDatum( int indice ){
        return datos[indice];
    }
    
    /**
     * @return Retorna un string con la representación de la fila.
     */
    @Override
    public String toString() {
        StringBuilder stringFila = new StringBuilder("(");
        boolean primero = true;
        
        for (Data datoActual : datos) {
            if( primero ){
                stringFila.append(datoActual.representation());
                primero = false;
            } else {
                stringFila.append(", ").append(datoActual.representation());
            }
        }
        stringFila.append(")");
        
        return stringFila.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Arrays.deepHashCode(this.datos);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Row other = (Row) obj;
        if (!Arrays.deepEquals(this.datos, other.datos)) {
            return false;
        }
        return true;
    }
    
    
    /**
     * Método para realizar el casteo, si no es posible realizarlo lanza una excepción
     * @param esquema Exquema de la tabla donde se desea insertar
     * @param fila Fila que se desea cambiarEsquema
     * @return Nueva fila que cumple con el esquema solicitado
     */
    public static Row cambiarEsquema(Schema esquema, Row fila){
        DataType[] listaTipos = esquema.getTypes();
        Data[] listaDatos = fila.obtenerDatos();
        
        // Verifica
        if( listaTipos.length != listaDatos.length )
            throw new TableException(TableException.TipoError.EsquemaNoCoincide, 
                    "No se puede realizar cast a un esquema de distinto tamaño.");
        
        // Crea los datos
        ArrayList<Data> datosRetorno = new ArrayList<>();
        for (int i=0; i<listaTipos.length; i++){
            datosRetorno.add( listaDatos[i].convertirA(listaTipos[i]) );
        }
        
        // Retorno de nueva fila
        Row filaRetorno = new Row(datosRetorno.toArray(new Data[0]));
        return filaRetorno;
    }
    
    /**
     * Devuelve una nueva fila con los datos de dos filas combinados.
     * @param a
     * @param b
     * @return Nueva fila
     */
    public static Row combinarFilas( Row a, Row b ){
        ArrayList<Data> datosFilas = new ArrayList<>(Arrays.asList(a.obtenerDatos()));
        datosFilas.addAll(Arrays.asList(b.obtenerDatos()));
        
        return new Row( datosFilas.toArray(new Data[0] ) );
    }
    
    /**
     * Crea una nueva fila con los datos dados agregados.
     * @param fila Fila base
     * @param datosAgregar Datos a agregar.
     * @return Nueva fila
     */
    public static Row agregarDatos( Row fila, Data ... datosAgregar ){
        Row filaTemporal = new Row(datosAgregar);
        
        return combinarFilas( fila, filaTemporal );
    }
    
    
    /**
     * Devuelve una nueva fila con las posiciones dadas por los indices eliminadas.
     * @param fila Fila base
     * @param indicesEliminar Posiciones a eliminar.
     * @return Nueva fila
     */
    public static Row eliminarDatos( Row fila, Integer ... indicesEliminar ){
        ArrayList<Integer> listaIndices = new ArrayList<>( Arrays.asList(indicesEliminar) );
        ArrayList<Data> datosFila = new ArrayList<>( Arrays.asList(fila.obtenerDatos()) );
        
        Iterator<Data> iterador = datosFila.iterator();
        int indiceActual = 0;
        while( iterador.hasNext() ){
            iterador.next();
            
            // Verifica si es un índice a eliminar
            if( listaIndices.contains(indiceActual) )
                iterador.remove();
            
            ++indiceActual;
        }
        
        return new Row( datosFila.toArray(new Data[0] ) );
    }
}
