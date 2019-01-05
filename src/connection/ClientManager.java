package connection;

import exceptions.DatabaseException;
import exceptions.DBMSException;
import exceptions.TableException;
import grammar.SQLGrammarLexer;
import grammar.SQLGrammarParser;
import grammar.SQLGrammarVisitor;
import userInterface.MessagePrinter;
import userInterface.VerboseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import visitante.SQLVisitor;

/**
 *
 * @author Jorge
 */
public class ClientManager {
    private BufferedReader input;
    private BufferedWriter output;

    /**
	 * Data stream constructor
     * @param input
     * @param output 
     */
    public ClientManager(BufferedReader input, BufferedWriter output) {
        this.input = input;
        this.output = output;
    }
    
    /**
     * Manages database data
     */
    public void manageClient() throws IOException{
        long maxTime = 5000;
        long startTime = new Date().getTime();
        
        String clientQuery = "";
        
		// Reads query
        while( new Date().getTime() - startTime < maxTime ){
            if( input.ready() ){
                String line = input.readLine();
                if( line.equals("<FIN>") )
                    maxTime = 0;
                else 
                    clientQuery += line;
            }
        }
        
        try{
            // ANTLR
            CharStream input = CharStreams.fromString(clientQuery);
            SQLGrammarLexer lexer = new SQLGrammarLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SQLGrammarParser parser = new SQLGrammarParser(tokens);

			// Errors
            parser.removeErrorListeners();
            parser.addErrorListener(new VerboseListener());

			// Parser
            ParseTree tree = parser.program();

			// Visitor
            SQLGrammarVisitor visitor = new SQLVisitor();
            visitor.visit(tree);
        } catch (DatabaseException databaseException){
            MessagePrinter.printErrorMessage(databaseException.getMessage());
        } catch (TableException tableException){
            MessagePrinter.printErrorMessage(tableException.getMessage());
        } catch( DBMSException ex ){
            MessagePrinter.printErrorMessage(ex.getMessage());
        }
    }
}
