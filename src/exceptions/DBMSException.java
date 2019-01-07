package exceptions;

/**
 *
 * @author Jorge
 */
public class DBMSException extends RuntimeException {

	/**
	 * Constructor with message
	 *
	 * @param message
	 */
	public DBMSException(String message) {
		super(message);
	}
}
