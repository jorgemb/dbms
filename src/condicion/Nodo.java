
package condicion;

import java.util.HashMap;
import java.util.Set;
import motor.Dato;

/**
 *
 * @author eddycastro
 */
public interface Nodo {
    /**
     * Evalúa el nodo en base a un mapa de datos.
     * @param mapaDatos Mapa de datos.
     * @return 
     */
    public Object evaluar(HashMap<String, Dato> mapaDatos);

    /**
     * Agrega los nombres utilizados en los nodos de datos de la rama actual.
     * @param nombres Lugar donde se agregan los nuevos nombres.
     */
    public void agregarNombresUtilizados( Set<String> nombres );
    
    
    /**
     * Cambia el nombre de la tabla en los nombre calificados.
     * @param nombreAnterior Nombre anterior de la tabla.
     * @param nombreNuevo Nombre nuevo de la tabla.
     */
    public void cambiarNombreTabla( String nombreAnterior, String nombreNuevo );
}
