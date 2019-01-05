
package interfazUsuario;

import java.util.HashMap;
import motor.relacion.Fila;
import motor.relacion.Relacion;

/**
 *
 * @author eddycastro
 */
public class ImpresorMensajes {
    private static int idSiguienteImpresor = 0;
    private static HashMap<Integer, Impresor> impresores = new HashMap<>();
    
    /**
     * Método estático utilizado para imprimir los mensajes del usuario
     * @param mensaje Mensaje a imprimir.
     */
    public static void imprimirMensajeUsuario(String mensaje){
        if( impresores.isEmpty() )
            System.out.println(mensaje);
        else{
            for (Impresor impresor : impresores.values()) {
                impresor.imprimirMensaje(mensaje);
            }
        }
    }
    
    /**
     * Método estático utilizado para imrpimir mensajes de error
     * @param mensaje 
     */
    public static void imprimirMensajeError(String mensaje){
        if( impresores.isEmpty() )
            System.err.println(mensaje);
        else{
            for (Impresor impresor : impresores.values()) {
                impresor.imprimirError(mensaje);
            }
        }
    }
    
    /**
     * Método estático utilizado para imprimir relaciones.
     * @param relacionP 
     */
    public static void imprimirRelacion(Relacion relacionP){
        if( impresores.isEmpty() ){
            for (Fila fila : relacionP) {
                System.out.println(fila);
            }
        }else{
            for (Impresor impresor : impresores.values()) {
                impresor.imprimirRelacion(relacionP);
            }
        }
    }
    
    /**
     * Obtiene la confirmación del usuario.
     * @param mensaje
     * @return 
     */
    public static boolean obtenerConfirmacion( String mensaje ){
        if( impresores.isEmpty() ){
            return true;
        } else {
            for (Impresor impresor : impresores.values()) {
                if( impresor.obtenerConfirmacion(mensaje) != true )
                    return false;
            }
            
            return true;
        }
    }
    
    
    /**
     * Permite registrar un nuevo impresor que capture todos los datos impresos.
     * @param nuevoImpresor 
     */
    public static int registrarImpresor( Impresor nuevoImpresor ){
        impresores.put(idSiguienteImpresor, nuevoImpresor);
        return idSiguienteImpresor++;
    }
    
    /**
     * Elimina el impresor con el id dado.
     * @param idImpresor Id del impresor a eliminar.
     * @return True si la eliminación fue exitosa.
     */
    public static boolean eliminarImpresor( int idImpresor ){
        if( impresores.containsKey(idImpresor) ){
            impresores.remove(idImpresor);
            return true;
        }
        return false;
    }
}
