package exceptions;

/**
 *
 * @author Jorge
 */
public class DatabaseException extends DBMSException{
    public enum ErrorType{
        NonExistent,
        InvalidIdentifier,
        NameAlreadyExists,
        FatalError,
    }
    
    private ErrorType errorType;
    private String databaseName;
    private String fatalErrorMessage;

    /**
	 * Constructor with type and database name
     * @param errorType Error type
     * @param databaseName Name of the database that generated the error.
     */
    public DatabaseException(ErrorType errorType, String databaseName) {
        super("Error in database: " + databaseName);
        this.errorType = errorType;
        this.databaseName = databaseName;
    }
    
    /**
	 * Fatal error constructor
     * @param message Message to user
     */
    public DatabaseException(String message){
        super(message);
        this.errorType = ErrorType.FatalError;
        this.fatalErrorMessage = message;
    }

    /**
	 * Returns the message
     * @return 
     */
    @Override
    public String getMessage() {
        switch (errorType) {
            case NonExistent:
                return String.format("Database %s does not exist.", this.databaseName);
            case InvalidIdentifier:
                return String.format("Name '%s' is not a valid identifier.", this.databaseName);
            case NameAlreadyExists:
                return String.format("Database with name '%s' already exists.", this.databaseName);
            case FatalError:
                return String.format("FATAL ERROR: %s", this.fatalErrorMessage);
            default:
                return "Unidentified error.";
        }
    }

    /**
	 * Returns the error type
     * @return Exception type
     */
    public ErrorType getErrorType() {
        return errorType;
    }
}
