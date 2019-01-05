package excepciones;

/**
 *
 * @author Jorge
 */
public class ExcepcionTabla extends ExcepcionDBMS {
    public enum TipoError{
        DatoInvalido,
        EsquemaInvalido,
        EsquemaNoCoincide,
        TablaNoExiste,
        TablaYaExiste,
        TablaNoEsValida,
        ColumnaYaExiste,
        ColumnaNoExiste,
        RestriccionYaExiste,
        RestriccionNoExiste,
        FalloRestriccion,
        RestriccionErrorEnParametros,
        ErrorReferencia,
        ErrorFatal
    }
    
    private TipoError tipoError;
    private String detallesError;
    
    
    /**
     * Constructor con error fatal.
     * @param detallesError 
     */
    public ExcepcionTabla(String detallesError) {
        super(detallesError);
        this.detallesError = detallesError;
        this.tipoError = TipoError.ErrorFatal;
    }

    /**
     * Ingresa un error con los detalles dados.
     * @param tipoError
     * @param detallesError 
     */
    public ExcepcionTabla(TipoError tipoError, String detallesError) {
        super(detallesError);
        this.tipoError = tipoError;
        this.detallesError = detallesError;
    }

    /**
     * @return Devuelve el tipo de error.
     */
    public TipoError obtenerTipoError() {
        return tipoError;
    }

    /**
     * Devuelve el mensaje de error.
     * @return Mensaje de error.
     */
    @Override
    public String getMessage() {
        switch (tipoError) {
            case TablaYaExiste:
                return String.format("Ya existe una tabla con nombre: %s", detallesError);
            case TablaNoExiste:
                return String.format("No existe la tabla con nombre: %s", detallesError);
            case TablaNoEsValida:
                return String.format("La tabla %s no es válida.", detallesError);
            case ColumnaYaExiste:
                return String.format("Ya existe una columna con nombre: %s", detallesError);
            case ColumnaNoExiste:
                return String.format("No existe la columna con nombre: %s", detallesError);
            case RestriccionYaExiste:
                return String.format("Ya existe una restricción con nombre: %s", detallesError);
            case RestriccionNoExiste:
                return String.format("No existe la restricción con nombre: %s", detallesError);
            default:
                return this.detallesError;
        }
    }
    
    
}
