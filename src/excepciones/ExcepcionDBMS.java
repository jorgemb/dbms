package excepciones;

/**
 *
 * @author Jorge
 */
public class ExcepcionDBMS extends RuntimeException {
    /**
     * Constructor con mensaje de error.
     * @param string 
     */
    public ExcepcionDBMS(String string) {
        super(string);
    }
}
