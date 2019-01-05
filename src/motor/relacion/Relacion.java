package motor.relacion;

import excepciones.ExcepcionTabla;
import java.util.ArrayList;

/**
 *
 * @author Jorge
 */
public abstract class Relacion implements Iterable<Fila>{

    /**
     * @return Esquema de la relación.
     */
    public abstract Esquema obtenerEsquema();
    
    /**
     * @return Devuelve la cantidad de filas.
     */
    public abstract int obtenerCantidadFilas();
    
    /**
     * Devuelve el nombre calificado de la columna con el índice dado.
     * @param indiceColumna Indice de la columna.
     * @return String con el nombre calificado
     */
    public abstract String obtenerNombreCalificado( int indiceColumna ) throws ExcepcionTabla;
    
    /**
     * @return Devuelve un arreglo con todos los nombres calificados.
     */
    public ArrayList<String> obtenerTodosNombreCalificados(){
        ArrayList<String> retorno = new ArrayList<>();
        for (int i = 0; i < obtenerEsquema().obtenerTamaño(); i++) {
            retorno.add( obtenerNombreCalificado(i) );
        }
        
        return retorno;
    }
}
