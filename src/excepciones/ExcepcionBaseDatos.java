package excepciones;

/**
 *
 * @author Jorge
 */
public class ExcepcionBaseDatos extends ExcepcionDBMS{
    public enum TipoError{
        NoExiste,
        IdentificadorInvalido,
        NombreYaExiste,
        ErrorFatal,
    }
    
    private TipoError tipoError;
    private String nombreBaseDatos;
    private String mensajeErrorFatal;

    /**
     * Constructor con tipo y nombre de base de datos.
     * @param tipoError Tipo del error generado.
     * @param nombreBaseDatos Nombre de la base de datos que generó el error.
     */
    public ExcepcionBaseDatos(TipoError tipoError, String nombreBaseDatos) {
        super("Error en base de datos: " + nombreBaseDatos);
        this.tipoError = tipoError;
        this.nombreBaseDatos = nombreBaseDatos;
    }
    
    /**
     * Constructor para un error fatal.
     * @param mensaje Mensaje a mostrar al usuario.
     */
    public ExcepcionBaseDatos(String mensaje){
        super(mensaje);
        this.tipoError = TipoError.ErrorFatal;
        this.mensajeErrorFatal = mensaje;
    }

    /**
     * Devuelve el mensaje asociado a la excepción.
     * @return 
     */
    @Override
    public String getMessage() {
        switch (tipoError) {
            case NoExiste:
                return String.format("La base de datos %s no existe.", this.nombreBaseDatos);
            case IdentificadorInvalido:
                return String.format("El nombre %s no es un identificador válido.", this.nombreBaseDatos);
            case NombreYaExiste:
                return String.format("Ya existe una base de datos con el nombre %s.", this.nombreBaseDatos);
            case ErrorFatal:
                return String.format("ERROR FATAL: %s", this.mensajeErrorFatal);
            default:
                return "Error no identificado.";
        }
    }

    /**
     * Devuelve el tipo de error.
     * @return TipoError de la excepción.
     */
    public TipoError obtenerTipoError() {
        return tipoError;
    }
}
