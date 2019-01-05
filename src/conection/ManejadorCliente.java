package conection;

import excepciones.DatabaseException;
import excepciones.DBMSException;
import excepciones.TableException;
import grammar.SQLGrammarLexer;
import grammar.SQLGrammarParser;
import grammar.SQLGrammarVisitor;
import interfazUsuario.MessagePrinter;
import interfazUsuario.VerboseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import visitante.SQLVisitor;

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
            SQLGrammarLexer lexer = new SQLGrammarLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SQLGrammarParser parser = new SQLGrammarParser(tokens);

            // Manejo de errores
            parser.removeErrorListeners();
            parser.addErrorListener(new VerboseListener());

            // Ejecución de parser
            ParseTree arbol = parser.program();

            // Visitar
            SQLGrammarVisitor visitante = new SQLVisitor();
            visitante.visit(arbol);
        } catch (DatabaseException excepcionBD){
            MessagePrinter.printErrorMessage(excepcionBD.getMessage());
        } catch (TableException excepcionTabla){
            MessagePrinter.printErrorMessage(excepcionTabla.getMessage());
        } catch( DBMSException ex ){
            MessagePrinter.printErrorMessage(ex.getMessage());
        }
    }
}
