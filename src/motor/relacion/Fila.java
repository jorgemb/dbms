package motor.relacion;

import excepciones.ExcepcionTabla;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import motor.Dato;
import motor.TipoDato;
import org.stringtemplate.v4.misc.ArrayIterator;

/**
 *
 * @author Jorge
 */
public class Fila implements java.io.Serializable, Iterable<Dato>{
    /** Datos asociados a la fila */
    private final Dato[] datos;
    
    /** Esquema asociado de la fila */
    private transient Esquema esquemaFila;
    

    /**
     * Constructor con datos.
     * @param datos Datos asociados
     */
    public Fila(Dato ... datos) {
        this.datos = datos;
    }
    
    /**
     * Obtiene el esquema de la fila.
     * @return Arreglo con el tipo de dato de cada parte.
     * @throws ExcepcionTabla Lanzada si algún dato es inválido.
     */
    public Esquema obtenerEsquema() throws ExcepcionTabla{
        if( esquemaFila != null )
            return esquemaFila;
        
        TipoDato[] esquema = new TipoDato[this.datos.length];
        for (int i = 0; i != datos.length; i++) {
            if( (esquema[i] = datos[i].obtenerTipo()) == null )
                throw new ExcepcionTabla(ExcepcionTabla.TipoError.EsquemaInvalido,
                        String.format( "La fila posee un dato no soportado (%s).",
                        datos[i].obtenerValor().getClass() ) );
        }
        esquemaFila = new Esquema(esquema);
        
        return esquemaFila;
    }
    
    /**
     * Iterador de la fila.
     * @return 
     */
    @Override
    public Iterator<Dato> iterator() {
        return (Iterator)new ArrayIterator( datos );
    }
    
    /**
     * @return Devuelve un arreglo con los datos.
     */
    public Dato[] obtenerDatos(){
        return datos;
    }
    
    /**
     * @return Devuelve el dato en la posición dada.
     */
    public Dato obtenerDato( int indice ){
        return datos[indice];
    }
    
    /**
     * @return Retorna un string con la representación de la fila.
     */
    @Override
    public String toString() {
        StringBuilder stringFila = new StringBuilder("(");
        boolean primero = true;
        
        for (Dato datoActual : datos) {
            if( primero ){
                stringFila.append(datoActual.representacion());
                primero = false;
            } else {
                stringFila.append(", ").append(datoActual.representacion());
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
        final Fila other = (Fila) obj;
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
    public static Fila cambiarEsquema(Esquema esquema, Fila fila){
        TipoDato[] listaTipos = esquema.obtenerTipos();
        Dato[] listaDatos = fila.obtenerDatos();
        
        // Verifica
        if( listaTipos.length != listaDatos.length )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.EsquemaNoCoincide, 
                    "No se puede realizar cast a un esquema de distinto tamaño.");
        
        // Crea los datos
        ArrayList<Dato> datosRetorno = new ArrayList<>();
        for (int i=0; i<listaTipos.length; i++){
            datosRetorno.add( listaDatos[i].convertirA(listaTipos[i]) );
        }
        
        // Retorno de nueva fila
        Fila filaRetorno = new Fila(datosRetorno.toArray(new Dato[0]));
        return filaRetorno;
    }
    
    /**
     * Devuelve una nueva fila con los datos de dos filas combinados.
     * @param a
     * @param b
     * @return Nueva fila
     */
    public static Fila combinarFilas( Fila a, Fila b ){
        ArrayList<Dato> datosFilas = new ArrayList<>(Arrays.asList(a.obtenerDatos()));
        datosFilas.addAll(Arrays.asList(b.obtenerDatos()));
        
        return new Fila( datosFilas.toArray(new Dato[0] ) );
    }
    
    /**
     * Crea una nueva fila con los datos dados agregados.
     * @param fila Fila base
     * @param datosAgregar Datos a agregar.
     * @return Nueva fila
     */
    public static Fila agregarDatos( Fila fila, Dato ... datosAgregar ){
        Fila filaTemporal = new Fila(datosAgregar);
        
        return combinarFilas( fila, filaTemporal );
    }
    
    
    /**
     * Devuelve una nueva fila con las posiciones dadas por los indices eliminadas.
     * @param fila Fila base
     * @param indicesEliminar Posiciones a eliminar.
     * @return Nueva fila
     */
    public static Fila eliminarDatos( Fila fila, Integer ... indicesEliminar ){
        ArrayList<Integer> listaIndices = new ArrayList<>( Arrays.asList(indicesEliminar) );
        ArrayList<Dato> datosFila = new ArrayList<>( Arrays.asList(fila.obtenerDatos()) );
        
        Iterator<Dato> iterador = datosFila.iterator();
        int indiceActual = 0;
        while( iterador.hasNext() ){
            iterador.next();
            
            // Verifica si es un índice a eliminar
            if( listaIndices.contains(indiceActual) )
                iterador.remove();
            
            ++indiceActual;
        }
        
        return new Fila( datosFila.toArray(new Dato[0] ) );
    }
}
