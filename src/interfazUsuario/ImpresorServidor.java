package interfazUsuario;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import motor.Data;
import motor.relacion.Row;
import motor.relacion.Relation;

/**
 * Permite imprimir los mensajes de salida de queries a la consola.
 * @author Jorge
 */
public class ImpresorServidor implements Printer, Closeable{
    private int idImpresor;
    private BufferedWriter salida;
    
    /**
     * Registra el impresor de mensajes.
     */
    public ImpresorServidor( BufferedWriter salida ) {
        idImpresor = MessagePrinter.registerPrinter(this);
        this.salida = salida;
    }

    /**
     * Elimina el impresor de mensajes.
     * @throws IOException 
     */
    @Override
    public void close() throws IOException {
        MessagePrinter.eliminarImpresor(idImpresor);
    }
    
    
    
    public final static String CAMPO_ERROR = "Error";
    public final static String CAMPO_TIPO = "Tipo";
    public final static String CAMPO_CONTENIDO = "Contenido";
    
    public final static String CAMPO_ESQUEMA = "Esquema";
    public final static String CAMPO_TIPOS = "Tipos";
    public final static String CAMPO_NOMBRES = "Nombres";
    public final static String CAMPO_DATOS = "Datos";
    

    public final static String TIPO_MENSAJE = "Mensaje";
    public final static String TIPO_RELACION = "Relacion";
    
    @Override
    public void printMessage(String txtMensaje) {
        try {
            // Crea un nuevo JSON
            JsonObject jsonMensaje = new JsonObject();
            jsonMensaje.addProperty( CAMPO_ERROR, false );
            jsonMensaje.addProperty( CAMPO_TIPO, TIPO_MENSAJE );
            jsonMensaje.addProperty( CAMPO_CONTENIDO, txtMensaje );
            
            salida.append(jsonMensaje.toString());
            salida.newLine();
        } catch (IOException ex) {
            System.err.println( "ERROR IMPRESOR: " + ex.getMessage() );
        }
    }

    @Override
    public void printError(String txtError) {
        try {
            // Crea un nuevo JSON
            JsonObject jsonError = new JsonObject();
            jsonError.addProperty( CAMPO_ERROR, true );
            jsonError.addProperty( CAMPO_TIPO, TIPO_MENSAJE );
            jsonError.addProperty( CAMPO_CONTENIDO, txtError );
            
            salida.append(jsonError.toString());
            salida.newLine();
        } catch (IOException ex) {
            System.err.println( "ERROR IMPRESOR: " + ex.getMessage() );
        }
    }

    @Override
    public void printRelation(Relation relacion) {
        Gson gson = new Gson();
        try{
            // Construye poco a poco el json correspondiente
            salida.append("{");
            salida.append( String.format("\"%s\": false,", CAMPO_ERROR) );
            salida.newLine();
            salida.append( String.format("\"%s\": \"%s\",", CAMPO_TIPO, TIPO_RELACION) );
            salida.newLine();
            
            // Ingresa los datos del esquema
            salida.append( String.format("\"%s\":{", CAMPO_ESQUEMA ) );
            salida.append( String.format("\"%s\":%s,", CAMPO_NOMBRES,
                    gson.toJson(relacion.obtenerTodosNombreCalificados())));
            salida.append( String.format("\"%s\":%s},", CAMPO_TIPOS, 
                    gson.toJson( relacion.getSchema().getTypes() )));
            salida.newLine();
            
            // Ingresa los datos de la relación
            salida.append( String.format("\"%s\":[", CAMPO_DATOS) );
            
            boolean primero = true;
            for (Row fila : relacion) {
                // Verifica si es la primera fila
                if( !primero ){
                    salida.append(",");
                    salida.newLine();
                } else 
                    primero = false;
                
                // Imprime la fila
                ArrayList<Object> datos = new ArrayList<>();
                for (Data dato : fila.obtenerDatos()) {
                    datos.add(dato.getValue());
                }
                salida.append( gson.toJson(datos) );
            }
            
            // Finaliza
            salida.append("]}");
        } catch (IOException ex) {
            System.err.println( "ERROR IMPRESOR: " + ex.getMessage() );
        }
    }

    @Override
    public boolean getConfirmation(String mensaje) {
        // TODO
        return true;
    }
    
}
