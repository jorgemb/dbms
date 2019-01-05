package condicion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import motor.Dato;

/**
 *
 * @author Jorge
 */
public class Condicion implements java.io.Serializable{
    
    private Nodo nodoRaiz;

    /**
     * Constructor de la clase
     * @param nodoRaiz 
     */
    public Condicion(Nodo nodoRaiz) {
        this.nodoRaiz = nodoRaiz;
    }
    
    /**
     * Evalua la condición con el mapa de datos dado.
     * @param datos Datos utilizados para la evaluación.
     * @return True o false, dependiendo de la evaluación.
     */
    public boolean evaluar( HashMap<String, Dato> datos ){
        return (Boolean)nodoRaiz.evaluar(datos);
    }
    
    /**
     * Devuelve los nombres de las columnas utilizadas por la condición.
     * @return String[] con los nombres calificados de las columnas utilizadas.
     */
    public String[] obtenerColumnasUtilizadas(){
        Set<String> nombresUtilizados = new HashSet<>();
        nodoRaiz.agregarNombresUtilizados(nombresUtilizados);
        
        return nombresUtilizados.toArray( new String[0] );
    }

    /**
     * @return Representación string de la condición.
     */
    @Override
    public String toString() {
        return "(" + nodoRaiz.toString() + ")";
    }
    
    /**
     * Cambia el nombre de la tabla en los nombre calificados de los nodos
     * dato asociados a la condicion.
     * @param nombreAnterior
     * @param nombreNuevo 
     */
    public void cambiarNombreTabla( String nombreAnterior, String nombreNuevo ){
        nodoRaiz.cambiarNombreTabla(nombreAnterior, nombreNuevo);
    }
}
