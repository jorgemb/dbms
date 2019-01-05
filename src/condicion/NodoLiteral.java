
package condicion;

import java.util.HashMap;
import java.util.Set;
import motor.Dato;

/**
 *
 * @author eddycastro
 */
public class NodoLiteral implements Nodo, java.io.Serializable{

    private Object valor;
    
    private TipoLiteral tipo;
    public enum TipoLiteral{
        
        INT,
        FLOAT,
        STRING,
        NULL,
    }

    /**
     * Constructor de la clase
     * @param valor 
     * @param tipo
     */
    public NodoLiteral(Object valor, TipoLiteral tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }
    
    /**
     * Método para obtener el tipo de literal
     * @return El tipo de literal que es
     */
    public TipoLiteral obtenerTipo(){
        return this.tipo;
    }
    
    /**
     * 
     * @param mapaDatos
     * @return 
     */
    @Override
    public Object evaluar(HashMap<String, Dato> mapaDatos) {
    
        return valor;
        
    }

    @Override
    public void agregarNombresUtilizados(Set<String> nombres) {
    }

    /**
     * @return Representación string del nodo.
     */
    @Override
    public String toString() {
        return valor.toString();
    }

    /**
     * Cambia el nombre de la tabla en los nombre calificados.
     * @param nombreAnterior Nombre anterior de la tabla.
     * @param nombreNuevo Nombre nuevo de la tabla.
     */
    @Override
    public void cambiarNombreTabla(String nombreAnterior, String nombreNuevo) {
        return;
    }
    
    
}
