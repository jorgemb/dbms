
package condicion;

import java.util.HashMap;
import java.util.Set;
import motor.Dato;

/**
 *
 * @author eddycastro
 */
public class NodoDato implements Nodo, java.io.Serializable{
    
    private String nombreColumna;

    /**
     * Constructor de la clase
     * @param nombreColumna 
     */
    public NodoDato(String nombreColumna) {
        this.nombreColumna = nombreColumna;
    }

    @Override
    public Object evaluar(HashMap<String, Dato> mapaDatos) {
        return mapaDatos.get(nombreColumna).obtenerValor();
    }

    @Override
    public void agregarNombresUtilizados(Set<String> nombres) {
        nombres.add(nombreColumna);
    }

    /**
     * @return Representación string del nodo.
     */
    @Override
    public String toString() {
        return nombreColumna;
    }
    
    
    /**
     * Cambia el nombre de la tabla en los nombre calificados.
     * @param nombreAnterior Nombre anterior de la tabla.
     * @param nombreNuevo Nombre nuevo de la tabla.
     */
    @Override
    public void cambiarNombreTabla(String nombreAnterior, String nombreNuevo) {
        if( nombreAnterior != null ){
            if( motor.Util.obtenerNombreTabla(nombreColumna).equals(nombreAnterior) ){
                String nombreCampo = motor.Util.obtenerNombreCampo(nombreColumna);
                nombreColumna = String.format("%s.%s", nombreNuevo, nombreCampo);
            }
        } else {
            String nombreCampo = motor.Util.obtenerNombreCampo(nombreColumna);
            nombreColumna = String.format("%s.%s", nombreNuevo, nombreCampo);
        }
    }

}
