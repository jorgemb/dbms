package interfazUsuario;

import motor.relacion.Relation;

/**
 *
 * @author Jorge
 */
public interface Printer {
    /**
     * Imprime un mensaje normal.
     * @param txtMensaje 
     */
    public void printMessage( String txtMensaje );
    
    /**
     * Imprime un mensaje de error.
     * @param txtError 
     */
    public void printError( String txtError );
    
    /**
     * Imprime una relación completa.
     * @param relacion 
     */
    public void printRelation( Relation relacion );
    
    /**
     * Obtiene algún tipo de confirmación.
     * @param mensaje
     * @return 
     */
    public boolean getConfirmation( String mensaje );
}
