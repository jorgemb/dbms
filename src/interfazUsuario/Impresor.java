package interfazUsuario;

import motor.relacion.Relacion;

/**
 *
 * @author Jorge
 */
public interface Impresor {
    /**
     * Imprime un mensaje normal.
     * @param txtMensaje 
     */
    public void imprimirMensaje( String txtMensaje );
    
    /**
     * Imprime un mensaje de error.
     * @param txtError 
     */
    public void imprimirError( String txtError );
    
    /**
     * Imprime una relación completa.
     * @param relacion 
     */
    public void imprimirRelacion( Relacion relacion );
    
    /**
     * Obtiene algún tipo de confirmación.
     * @param mensaje
     * @return 
     */
    public boolean obtenerConfirmacion( String mensaje );
}
