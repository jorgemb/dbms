package exceptions;

/**
 * Represents various table exceptions.
 * @author Jorge
 */
public class TableException extends DBMSException {

	public enum ErrorType {
		InvalidData,
		InvalidSchema,
		SchemaDoesNotMatch,
		TableDoesNotExist,
		TableAlreadyExists,
		InvalidTable,
		ColumnAlreadyExists,
		ColumnDoesNotExist,
		RestrictionAlreadyExists,
		RestrictionDoesNotExist,
		RestrictionFailure,
		ErrorInRestrictionParameters,
		ReferenceError,
		FatalError
	}

	private ErrorType errorType;
	private String errorDetails;

	/**
	 * Fatal error constructor
	 *
	 * @param errorDetails
	 */
	public TableException(String errorDetails) {
		super(errorDetails);
		this.errorDetails = errorDetails;
		this.errorType = ErrorType.FatalError;
	}

	/**
	 * Exception with details
	 *
	 * @param errorType
	 * @param errorDetails
	 */
	public TableException(ErrorType errorType, String errorDetails) {
		super(errorDetails);
		this.errorType = errorType;
		this.errorDetails = errorDetails;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	@Override
	public String getMessage() {
		switch (errorType) {
			case TableAlreadyExists:
				return String.format("Table with name %s already exists.", errorDetails);
			case TableDoesNotExist:
				return String.format("Table %s does not exist.", errorDetails);
			case InvalidTable:
				return String.format("Invalid table %s.", errorDetails);
			case ColumnAlreadyExists:
				return String.format("Column %s already exists.", errorDetails);
			case ColumnDoesNotExist:
				return String.format("Column %s does not exist.", errorDetails);
			case RestrictionAlreadyExists:
				return String.format("Restriction %s already exists.", errorDetails);
			case RestrictionDoesNotExist:
				return String.format("Restriction %s does not exist.", errorDetails);
			default:
				return this.errorDetails;
		}
	}

}
