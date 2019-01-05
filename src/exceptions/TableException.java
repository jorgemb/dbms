package exceptions;

/**
 *
 * @author Jorge
 */
public class TableException extends DBMSException {
    public enum ErrorType{
        DatoInvalido,
        InvalidSchema,
        SchemaDoesNotMatch,
        TableDoesNotExist,
        TableAlreadyExists,
        TablaNoEsValida,
        ColumnAlreadyExists,
        ColumnDoesNotExist,
        RestrictionAlreadyExists,
        RestriccionNoExiste,
        RestrictionFailure,
        ErrorInRestrictionParameters,
        ReferenceError,
        ErrorFatal
    }
    
    private ErrorType tipoError;
    private String detallesError;
    
    
    /**
     * Constructor con error fatal.
     * @param detallesError 
     */
    public TableException(String detallesError) {
        super(detallesError);
        this.detallesError = detallesError;
        this.tipoError = ErrorType.ErrorFatal;
    }

    /**
     * Ingresa un error con los detalles dados.
     * @param tipoError
     * @param detallesError 
     */
    public TableException(ErrorType tipoError, String detallesError) {
        super(detallesError);
        this.tipoError = tipoError;
        this.detallesError = detallesError;
    }

    /**
     * @return Devuelve el tipo de error.
     */
    public ErrorType obtenerTipoError() {
        return tipoError;
    }

    /**
     * Devuelve el mensaje de error.
     * @return Mensaje de error.
     */
    @Override
    public String getMessage() {
        switch (tipoError) {
            case TableAlreadyExists:
                return String.format("Ya existe una tabla con nombre: %s", detallesError);
            case TableDoesNotExist:
                return String.format("No existe la tabla con nombre: %s", detallesError);
            case TablaNoEsValida:
                return String.format("La tabla %s no es válida.", detallesError);
            case ColumnAlreadyExists:
                return String.format("Ya existe una columna con nombre: %s", detallesError);
            case ColumnDoesNotExist:
                return String.format("No existe la columna con nombre: %s", detallesError);
            case RestrictionAlreadyExists:
                return String.format("Ya existe una restricción con nombre: %s", detallesError);
            case RestriccionNoExiste:
                return String.format("No existe la restricción con nombre: %s", detallesError);
            default:
                return this.detallesError;
        }
    }
    
    
}
