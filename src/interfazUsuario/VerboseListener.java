
package interfazUsuario;

import excepciones.ExcepcionDBMS;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;


/**
 *
 * @author eddycastro
 */
public class VerboseListener extends BaseErrorListener{
    
    /**
     * Método que imprime mensajes de error de ANTLR
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
        
        ImpresorMensajes.imprimirMensajeError(String.format("Regla %s", stack));
        ImpresorMensajes.imprimirMensajeError(String.format("Linea %s: %s en %s: %s", line, charPositionInLine, offendingSymbol, msg));
        
        /*
        System.err.println("rule stack: "+stack);
        System.err.println("line "+line+":"+charPositionInLine+" at "+
                           offendingSymbol+": "+msg);
        */

        throw new ExcepcionDBMS("ERRORES NO RECUPERABLES");
    }

}