
package userInterface;

import java.util.HashMap;
import motor.relacion.Row;
import motor.relacion.Relation;

/**
 *
 * @author eddycastro
 */
public class MessagePrinter {
    private static int idSiguienteImpresor = 0;
    private static HashMap<Integer, Printer> impresores = new HashMap<>();
    
    /**
     * Método estático utilizado para imprimir los mensajes del usuario
     * @param mensaje Mensaje a imprimir.
     */
    public static void imprimirMensajeUsuario(String mensaje){
        if( impresores.isEmpty() )
            System.out.println(mensaje);
        else{
            for (Printer impresor : impresores.values()) {
                impresor.printMessage(mensaje);
            }
        }
    }
    
    /**
     * Método estático utilizado para imrpimir mensajes de error
     * @param mensaje 
     */
    public static void printErrorMessage(String mensaje){
        if( impresores.isEmpty() )
            System.err.println(mensaje);
        else{
            for (Printer impresor : impresores.values()) {
                impresor.printError(mensaje);
            }
        }
    }
    
    /**
     * Método estático utilizado para imprimir relaciones.
     * @param relacionP 
     */
    public static void imprimirRelacion(Relation relacionP){
        if( impresores.isEmpty() ){
            for (Row fila : relacionP) {
                System.out.println(fila);
            }
        }else{
            for (Printer impresor : impresores.values()) {
                impresor.printRelation(relacionP);
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
            for (Printer impresor : impresores.values()) {
                if( impresor.getConfirmation(mensaje) != true )
                    return false;
            }
            
            return true;
        }
    }
    
    
    /**
     * Permite registrar un nuevo impresor que capture todos los datos impresos.
     * @param nuevoImpresor 
     */
    public static int registerPrinter( Printer nuevoImpresor ){
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
