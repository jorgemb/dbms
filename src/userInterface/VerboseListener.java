
package userInterface;

import exceptions.DBMSException;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;


/**
 * Prints ANTLR error messages.
 * @author eddycastro
 */
public class VerboseListener extends BaseErrorListener{
    
    /**
	 * Prints a syntax error message from ANTLR.
     * @param recognizer
     * @param offendingSymbol
     * @param line
     * @param charPositionInLine
     * @param msg
     * @param e 
     */
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg,
                            RecognitionException e)
    {
        List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        
        MessagePrinter.printErrorMessage(String.format("Rule %s", stack));
        MessagePrinter.printErrorMessage(String.format("Line %s: %s en %s: %s", line, charPositionInLine, offendingSymbol, msg));
        
        throw new DBMSException("NON RECUPERABLE ERRORS");
    }

}