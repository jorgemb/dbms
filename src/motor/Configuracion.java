package motor;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import interfazUsuario.MessagePrinter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;


/**
 * Guarda la configuración inicial de todo el proyecto.
 * @author Jorge
 */
public class Configuracion {
    // Llaves para obtener datos globales.
    public static final String DIRECTORIO_BASEDATOS = "DIRECTORIO_BASE_DATOS";
    
    /**
     * Instancia del singleton.
     */
    private static Configuracion singleton;
    private static final String nombreArchivo = "Config.ini";
    
    /**
     * Constructor estático.
     */
    static{
        singleton = new Configuracion();
        
        
        // Lee los datos del archivo de configuración
        try {
            Gson gson = new Gson();
            String datosJson = FileUtils.readFileToString( new File(nombreArchivo), Charset.forName("UTF-8") );
            
            java.lang.reflect.Type tipoDatos = new TypeToken<HashMap<String, String>>() {
            }.getType();
            singleton.datos = (HashMap<String, String>) gson.fromJson(datosJson, tipoDatos);
            
        } catch (IOException | JsonIOException | JsonSyntaxException ex) {
            // Crea los datos con las versiones default
            singleton.datos = new HashMap<>();
            
            // Agrega todas las llaves
            File directorioTrabajo = new File( System.getProperty("user.dir") );
            
            /* DIRECTORIO BASE DATOS */
            File directorioBaseDatos = new File(directorioTrabajo, "DatosDBMS");
            singleton.datos.put(DIRECTORIO_BASEDATOS, directorioBaseDatos.getAbsolutePath());
            singleton.guardarCambios();
        }
    }
    
    
    /**
     * Agrega un dato a la configuración.
     * @param llave Llave con el cual se puede encontrar el dato.
     * @param dato Dato string a guardar.
     */
    public static void agregarDato( String llave, String dato ){
        singleton.datos.put(llave, dato);
        singleton.guardarCambios();
    }
    
    /**
     * Devuelve el dato asociado a una llave.
     * @param llave Llave a consultar.
     * @return String con el dato, o null si existe la llave.
     */
    public static String obtenerDato( String llave ){
        if( singleton.datos.containsKey(llave) )
            return singleton.datos.get(llave);
        else
            return null;
    }

    /**
     * Guarda los cambios realizados.
     */
    private boolean guardarCambios() {
        Gson gson = new Gson();
        
        try {
            String datosJson = gson.toJson(datos);
            FileUtils.writeStringToFile(new File(nombreArchivo), datosJson);
            
        } catch (IOException iOException) {
            MessagePrinter.imprimirMensajeError( "Error guardar configuración: " + iOException.getMessage() );
            return false;
        }
        return true;
    }
    
    /**
     * Datos almacenados.
     */
    private HashMap<String, String> datos;

    
    
    
    /**
     * Constructor marcado privado para mantener el singleton.
     */
    private Configuracion() {
        this.datos = new HashMap<>();
    }
    
}
