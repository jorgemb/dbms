
package condicion;

import excepciones.ExcepcionTabla;
import java.util.HashMap;
import java.util.Set;
import motor.Dato;

/**
 *
 * @author eddycastro
 */
public class NodoOperacional implements Nodo, java.io.Serializable{
    
    private TipoOperacionOperacional tipo;
    private Nodo hijoIzq;
    private Nodo hijoDer;

    public enum TipoOperacionOperacional{
        
        Suma,
        Resta,
        Multiplicacion,
        Division,
        Modulo
        
    }
    
    /**
     * Constructor de la clase
     * @param tipo
     * @param hijoIzq
     * @param hijoDer 
     */
    public NodoOperacional(TipoOperacionOperacional tipo, Nodo hijoIzq, Nodo hijoDer) {
        this.tipo = tipo;
        this.hijoIzq = hijoIzq;
        this.hijoDer = hijoDer;
    }
    
    /**
     * 
     * @param izq Valor nodo izquierdo
     * @param der Valor nodo derecho
     * @return Resultado de la operacion
     */
    private int evaluarInt(int izq, int der){
        
        switch (tipo) {
            case Suma:
                return izq + der;
            case Resta:
                return izq - der;
            case Multiplicacion:
                return izq * der; 
            case Division:
                return izq / der;
            case Modulo:
                return izq % der;
            default:
                throw new AssertionError();
        }
        
    }
    
    /**
     * 
     * @param izq Valor odo izquierdo
     * @param der Valor nodo derecho
     * @return Resultado de la operación
     */
    private float evaluarFloat(float izq, float der){
        
        switch (tipo) {
            case Suma:
                return izq + der;
            case Resta:
                return izq - der;
            case Multiplicacion:
                return izq * der; 
            case Division:
                return izq / der;
            case Modulo:
                return izq % der;
            default:
                throw new AssertionError();
        }
        
    }
    
    @Override
    public Object evaluar(HashMap<String, Dato> mapaDatos) {
        
        Number izq = (Number)hijoIzq.evaluar(mapaDatos);
        Number der = (Number)hijoDer.evaluar(mapaDatos);
        
        // Comprueba null
        if( izq == null || der == null )
            throw new ExcepcionTabla( ExcepcionTabla.TipoError.DatoInvalido, 
                    "No se pueden operar valores de tipo NULL." );
        
        // Ambos enteros
        if (izq instanceof Integer && der instanceof Integer) 
            return evaluarInt(izq.intValue(), der.intValue());
        
        // Ambos flotantes
        else 
            return evaluarFloat(izq.floatValue(), der.floatValue());
        
    }
    
    /**
     * Agrega los nombres utilizados.
     * @param nombres 
     */
    @Override
    public void agregarNombresUtilizados(Set<String> nombres) {
        hijoIzq.agregarNombresUtilizados(nombres);
        hijoDer.agregarNombresUtilizados(nombres);
    }

    /**
     * @return Representación string del nodo.
     */
    @Override
    public String toString() {
        String simbolo = "";
        switch (tipo) {
            case Suma:
                simbolo = "+";
                break;
            case Resta:
                simbolo = "-";
                break;
            case Multiplicacion:
                simbolo = "*";
                break;
            case Division:
                simbolo = "/";
                break;
            case Modulo:
                simbolo = "%";
                break;
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
        hijoDer.cambiarNombreTabla(nombreAnterior, nombreNuevo);
    }

    
}
