package conexion;

import excepciones.ExcepcionBaseDatos;
import excepciones.ExcepcionDBMS;
import excepciones.ExcepcionTabla;
import gramatica.GramaticaSQLLexer;
import gramatica.GramaticaSQLParser;
import gramatica.GramaticaSQLVisitor;
import interfazUsuario.ImpresorMensajes;
import interfazUsuario.VerboseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import visitante.VisitanteSQL;

/**
 *
 * @author Jorge
 */
public class ManejadorCliente {
    private BufferedReader entrada;
    private BufferedWriter salida;

    /**
     * Constructor con los streams de datos.
     * @param entrada
     * @param salida 
     */
    public ManejadorCliente(BufferedReader entrada, BufferedWriter salida) {
        this.entrada = entrada;
        this.salida = salida;
    }
    
    /**
     * Maneja con los datos de la base de datos.
     */
    public void manejarCliente() throws IOException{
        long tiempoMax = 5000;
        long tiempoInicio = new Date().getTime();
        
        String queryCliente = "";
        
        // Lee los datos del query
        while( new Date().getTime() - tiempoInicio < tiempoMax ){
            if( entrada.ready() ){
                String linea = entrada.readLine();
                if( linea.equals("<FIN>") )
                    tiempoMax = 0;
                else 
                    queryCliente += linea;
            }
        }
        
        try{
            // ANTLR
            ANTLRInputStream input = new ANTLRInputStream(queryCliente);
            GramaticaSQLLexer lexer = new GramaticaSQLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            GramaticaSQLParser parser = new GramaticaSQLParser(tokens);

            // Manejo de errores
            parser.removeErrorListeners();
            parser.addErrorListener(new VerboseListener());

            // Ejecución de parser
            ParseTree arbol = parser.program();

            // Visitar
            GramaticaSQLVisitor visitante = new VisitanteSQL();
            visitante.visit(arbol);
        } catch (ExcepcionBaseDatos excepcionBD){
            ImpresorMensajes.imprimirMensajeError(excepcionBD.getMessage());
        } catch (ExcepcionTabla excepcionTabla){
            ImpresorMensajes.imprimirMensajeError(excepcionTabla.getMessage());
        } catch( ExcepcionDBMS ex ){
            ImpresorMensajes.imprimirMensajeError(ex.getMessage());
        }
    }
}
