package condicion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import motor.Dato;

/**
 *
 * @author Jorge
 */
public class Expresion {
    private Nodo nodoRaiz;

    /**
     * Constructor para la expresión.
     * @param nodoRaiz 
     */
    public Expresion(Nodo nodoRaiz) {
        this.nodoRaiz = nodoRaiz;
    }
    
    /**
     * Evalua la expresión con los datos dados.
     * @param datos
     * @return 
     */
    public Dato evaluarExpresion( HashMap<String, Dato> datos ){
        return new Dato( nodoRaiz.evaluar(datos) );
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
}
