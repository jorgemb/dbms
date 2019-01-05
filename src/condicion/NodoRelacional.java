
package condicion;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import motor.Dato;
import motor.TipoDato;

/**
 *
 * @author eddycastro
 */
public class NodoRelacional implements Nodo, java.io.Serializable {

    private TipoOperacionRelacional tipo;
    private Nodo hijoDer;
    private Nodo hijoIzq;
    
    public enum TipoOperacionRelacional{
        
        Menor,
        MenorIgual,
        Mayor,
        MayorIgual,
        Igual,
        Diferente
        
    }

    /**
     * Constructor de la clase
     * @param tipo
     * @param hijoIzq 
     * @param hijoDer
     */
    public NodoRelacional(TipoOperacionRelacional tipo, Nodo hijoIzq, Nodo hijoDer) {
        this.tipo = tipo;
        this.hijoIzq = hijoIzq;
        this.hijoDer = hijoDer;
    }
    
    /**
     * 
     * @param <T> Tipo genérico
     * @param izq Valor nodo izquierdo
     * @param der Valor nodo derecho
     * @return True o False dependiendo del resultado de la operación
     */
    private <T extends Comparable> boolean evaluarOperacion(T izq, T der){
        
        switch (tipo) {
            case Menor:
                return izq.compareTo(der) < 0;
            case MenorIgual:
                return izq.compareTo(der) <= 0;
            case Mayor:
                return izq.compareTo(der) > 0;
            case MayorIgual:
                return izq.compareTo(der) >= 0;
            case Igual:
                return izq.compareTo(der) == 0;
            case Diferente:
                return izq.compareTo(der) != 0;
            default:
                throw new AssertionError();
        }
        
    }
    
    /**
     * 
     * @param mapaDatos
     * @return 
     */
    @Override
    public Object evaluar(HashMap<String, Dato> mapaDatos) {
        
        Object izq = hijoIzq.evaluar(mapaDatos);
        Object der = hijoDer.evaluar(mapaDatos);
        
        // Verifica null
        if( izq == null || der == null ){
            // Verifica la única opción posible
            Dato datoIzq = new Dato(izq), datoDer = new Dato(der);
            return evaluarOperacion(datoIzq, datoDer);
        }
        
        // Ambos enteros
        if (izq instanceof Integer && der instanceof Integer) 
            return evaluarOperacion((Integer)izq, (Integer)der);
        
        // Ambos flotantes
        else if (izq instanceof Float || der instanceof Float || izq instanceof Double || der instanceof Double) 
            return evaluarOperacion(((Number)izq).floatValue(), ((Number)der).floatValue());
        
        // Ambos String
        else{
            // Verifica si se pueden convertir a fechas.
            Dato datoIzq = new Dato(izq);
            Dato datoDer = new Dato(der);
            
            if( datoIzq.obtenerTipo() == TipoDato.DATE && datoDer.obtenerTipo() == TipoDato.DATE ){
                Date fechaIzq = Dato.obtenerFechaDato(datoIzq);
                Date fechaDer = Dato.obtenerFechaDato(datoDer);
                return evaluarOperacion(fechaIzq, fechaDer);
            }
            
            return evaluarOperacion(izq.toString(), der.toString());
        }

        
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
            case Menor:
                simbolo = "<";
                break;
            case MenorIgual:
                simbolo = "<=";
                break;
            case Mayor:
                simbolo = ">";
                break;
            case MayorIgual:
                simbolo = ">=";
                break;
            case Igual:
                simbolo = "=";
                break;
            case Diferente:
                simbolo = "<>";
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
