package motor;

import excepciones.ExcepcionDBMS;

/**
 *
 * @author Jorge
 */
public class Util {
    /**
     * Devuelve el nombre de la tabla a partir de una nombre calificado.
     * @param nombreCalificado Nombre calificado
     * @return Nombre de la tabla.
     * @throws ExcepcionDBMS
     */
    public static String obtenerNombreTabla( String nombreCalificado ) throws ExcepcionDBMS{
        int indicePunto = nombreCalificado.indexOf(".");
        
        // Verifica que sea un nombre calificado
        if( indicePunto == -1 )
            throw new ExcepcionDBMS( "No se tiene un nombre calificado." );
        
        return nombreCalificado.substring(0, indicePunto);
    }
    
    /**
     * Devuelve el nombre dle campo a partir de un nombre calificado.
     * @param nombreCalificado Nombre calificado
     * @return Nombre del campo
     * @throws ExcepcionDBMS 
     */
    public static String obtenerNombreCampo( String nombreCalificado ) throws ExcepcionDBMS{
        int indicePunto = nombreCalificado.indexOf(".");
        
        // Verifica que sea un nombre calificado
        if( indicePunto == -1 )
            throw new ExcepcionDBMS( "No se tiene un nombre calificado." );
        
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
