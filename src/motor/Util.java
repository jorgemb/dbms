package motor;

import excepciones.DBMSException;

/**
 *
 * @author Jorge
 */
public class Util {
    /**
     * Devuelve el nombre de la tabla a partir de una nombre calificado.
     * @param nombreCalificado Nombre calificado
     * @return Nombre de la tabla.
     * @throws DBMSException
     */
    public static String getTableName( String nombreCalificado ) throws DBMSException{
        int indicePunto = nombreCalificado.indexOf(".");
        
        // Verifica que sea un nombre calificado
        if( indicePunto == -1 )
            throw new DBMSException( "No se tiene un nombre calificado." );
        
        return nombreCalificado.substring(0, indicePunto);
    }
    
    /**
     * Devuelve el nombre dle campo a partir de un nombre calificado.
     * @param nombreCalificado Nombre calificado
     * @return Nombre del campo
     * @throws DBMSException 
     */
    public static String getFieldName( String nombreCalificado ) throws DBMSException{
        int indicePunto = nombreCalificado.indexOf(".");
        
        // Verifica que sea un nombre calificado
        if( indicePunto == -1 )
            throw new DBMSException( "No se tiene un nombre calificado." );
        
        return nombreCalificado.substring(indicePunto+1);
    }
    
    /**
     * Devuelve el nombre calificado a partir del nombre de la tabla y el campo.
     * @param nombreTabla
     * @param nombreCampo
     * @return 
     */
    public static String obtenerNombreCalificado( String nombreTabla, String nombreCampo ){
        return String.format("%s.%s", nombreTabla, nombreCampo);
    }
}
