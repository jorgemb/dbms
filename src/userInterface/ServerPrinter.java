package userInterface;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import motor.Data;
import motor.relation.Row;
import motor.relation.Relation;

/**
 * Allows to print message to a console.
 * @author Jorge
 */
public class ServerPrinter implements Printer, Closeable{
    private int printerId;
    private BufferedWriter output;
    
    /**
	 * Registers the printer.
     */
    public ServerPrinter( BufferedWriter output ) {
        printerId = MessagePrinter.registerPrinter(this);
        this.output = output;
    }

    /**
	 * Removes printer
     * @throws IOException 
     */
    @Override
    public void close() throws IOException {
        MessagePrinter.deletePrinter(printerId);
    }
    
    
    
    public final static String FIELD_ERROR = "Error";
    public final static String FIELD_TYPE = "Type";
    public final static String FIELD_CONTENT = "Content";
    
    public final static String FIELD_SCHEMA = "Schema";
    public final static String FIELD_TYPES = "Types";
    public final static String FIELD_NAMES = "Names";
    public final static String FIELD_DATA = "Data";
    

    public final static String TYPE_MESSAGE = "Message";
    public final static String TYPE_RELATION = "Relation";
    
    @Override
    public void printMessage(String message) {
        try {
            // Crea un nuevo JSON
            JsonObject jsonMensaje = new JsonObject();
            jsonMensaje.addProperty(FIELD_ERROR, false );
            jsonMensaje.addProperty(FIELD_TYPE, TYPE_MESSAGE );
            jsonMensaje.addProperty(FIELD_CONTENT, message );
            
            output.append(jsonMensaje.toString());
            output.newLine();
        } catch (IOException ex) {
            System.err.println( "ERROR PRINTER: " + ex.getMessage() );
        }
    }

    @Override
    public void printError(String txtError) {
        try {
            // Crea un nuevo JSON
            JsonObject jsonError = new JsonObject();
            jsonError.addProperty(FIELD_ERROR, true );
            jsonError.addProperty(FIELD_TYPE, TYPE_MESSAGE );
            jsonError.addProperty(FIELD_CONTENT, txtError );
            
            output.append(jsonError.toString());
            output.newLine();
        } catch (IOException ex) {
            System.err.println( "ERROR PRINTER: " + ex.getMessage() );
        }
    }

    @Override
    public void printRelation(Relation relacion) {
        Gson gson = new Gson();
        try{
			// Builds the output json
            output.append("{");
            output.append(String.format("\"%s\": false,", FIELD_ERROR) );
            output.newLine();
            output.append(String.format("\"%s\": \"%s\",", FIELD_TYPE, TYPE_RELATION) );
            output.newLine();
            
			// Schema
            output.append(String.format("\"%s\":{", FIELD_SCHEMA ) );
            output.append(String.format("\"%s\":%s,", FIELD_NAMES,
                    gson.toJson(relacion.getAllQualifiedNames())));
            output.append(String.format("\"%s\":%s},", FIELD_TYPES, 
                    gson.toJson( relacion.getSchema().getTypes() )));
            output.newLine();
            
			// Relation data
            output.append(String.format("\"%s\":[", FIELD_DATA) );
            
            boolean first = true;
            for (Row row : relacion) {
                if( !first ){
                    output.append(",");
                    output.newLine();
                } else 
                    first = false;
                
				// Prints the row
                ArrayList<Object> datos = new ArrayList<>();
                for (Data dato : row.getData()) {
                    datos.add(dato.getValue());
                }
                output.append( gson.toJson(datos) );
            }
            
            // end
            output.append("]}");
        } catch (IOException ex) {
            System.err.println( "ERROR PRINTER: " + ex.getMessage() );
        }
    }

    @Override
    public boolean getConfirmation(String message) {
        // TODO
        return true;
    }
    
}
