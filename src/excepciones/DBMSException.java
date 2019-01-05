package excepciones;

/**
 *
 * @author Jorge
 */
public class DBMSException extends RuntimeException {
    /**
     * Constructor con mensaje de error.
     * @param string 
     */
    public DBMSException(String string) {
        super(string);
    }
}
