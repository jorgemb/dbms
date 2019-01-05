package motor.restriccion;

/**
 *
 * @author Jorge
 */
public class RestriccionLlaveForanea {
    private String nombreTablaLocal;
    private String[] camposTablaLocal;
    private String nombreTablaForanea;
    private String[] camposTablaForanea;

    /**
     * Constructor de una llave foránea.
     * @param nombreTablaLocal Nombre de la tabla sobre la cual se aplica la restricción.
     * @param camposTablaLocal Campos actuales referenciados en la tabla local.
     * @param nombreTablaForanea Nombre de la tabla que posee los campos referenciados.
     * @param camposTablaForanea Campos referenciados en la tabla.
     */
    public RestriccionLlaveForanea(String nombreTablaLocal, String[] camposTablaLocal, String nombreTablaForanea, String[] camposTablaForanea) {
        this.nombreTablaLocal = nombreTablaLocal;
        this.camposTablaLocal = camposTablaLocal;
        this.nombreTablaForanea = nombreTablaForanea;
        this.camposTablaForanea = camposTablaForanea;
    }
}
