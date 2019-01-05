
package condicion;

import java.util.HashMap;
import java.util.Set;
import motor.Dato;

/**
 *
 * @author eddycastro
 */
public class NodoCondicional implements Nodo, java.io.Serializable {
    
    private TipoOperacionCondicional tipo;
    private Nodo hijoIzq;
    private Nodo hijoDer;

    public enum TipoOperacionCondicional{
        
        AND,
        OR,
        NOT
        
    }

    /**
     * Costructor para cualquier tipo de operacón -{NOT}
     * @param tipo Tipo de operación
     * @param hijoIzq Nodo izquierdo
     * @param hijoDer Nodo derecho
     */
    public NodoCondicional(TipoOperacionCondicional tipo, Nodo hijoIzq, Nodo hijoDer) {
        this.tipo = tipo;
        this.hijoIzq = hijoIzq;
        this.hijoDer = hijoDer;
    }

    /**
     * Constructor para NOT
     * @param hijoIzq Nodo izquierdo
     */
    public NodoCondicional(Nodo hijoIzq) {
        this.tipo = TipoOperacionCondicional.NOT;
        this.hijoIzq = hijoIzq;
        this.hijoDer = null;
    }
    
    @Override
    public Object evaluar(HashMap<String, Dato> mapaDatos){
        
        boolean izq = (Boolean)hijoIzq.evaluar(mapaDatos);
        boolean der = hijoDer != null ? (Boolean)hijoDer.evaluar(mapaDatos) : false;
        
        switch (tipo) {
            case AND:
                return izq && der;
            case OR:
                return izq || der;
            case NOT:
                return !izq;
            default:
                throw new AssertionError();
        }
        
    }
    
    @Override
    public void agregarNombresUtilizados(Set<String> nombres) {
        hijoIzq.agregarNombresUtilizados(nombres);
        
        if( hijoDer != null )
            hijoDer.agregarNombresUtilizados(nombres);
    }

    /**
     * @return Representación string del nodo.
     */
    @Override
    public String toString() {
        String simbolo = "";
        switch (tipo) {
            case AND:
                simbolo = "AND";
                break;
            case OR:
                simbolo = "OR";
                break;
            case NOT:
                return String.format("NOT (%s)", hijoIzq.toString());
            default:
                throw new AssertionError();
        }
        return String.format("%s %s %s", hijoIzq.toString(), simbolo, hijoDer.toString());
    }
    
    /**
     * Cambia el nombre de la tabla en los nombre calificados.
     * @param nombreAnterior Nombre anterior de la tabla.
     * @param nombreNuevo Nombre nuevo de la tabla.
     */
    @Override
    public void cambiarNombreTabla(String nombreAnterior, String nombreNuevo) {
        hijoIzq.cambiarNombreTabla(nombreAnterior, nombreNuevo);
        if( hijoDer != null )
            hijoDer.cambiarNombreTabla(nombreAnterior, nombreNuevo);
    }
}
