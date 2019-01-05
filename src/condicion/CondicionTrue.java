
package condicion;

import java.util.HashMap;
import motor.Dato;

/**
 *
 * @author Eddy
 */
public class CondicionTrue extends Condicion {
    
    /**
     * Construcción de la clase
     * @param nodo 
     */    
    public CondicionTrue(Nodo nodo){
        super(nodo);
    }
    
    /**
     * Método override 
     * @return Arreglo de String vacío por defecto
     */
    @Override
    public String[] obtenerColumnasUtilizadas(){
        return new String[0];
    }
   
    /**
     * Método override
     * @param datos
     * @return true por defecto
     */
    @Override
    public boolean evaluar( HashMap<String, Dato> datos ){
        return true;
    }
    
}
