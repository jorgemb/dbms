package motor.restriccion;

/**
 *
 * @author Jorge
 */
public abstract class Restriccion implements java.io.Serializable{
//    /**
//     * Evalúa la restricción en la(s) tabla(s) necesarias.
//     * @param relaciones Relaciones sobre las que se evaluarán las restricciones.
//     * @throws ExcepcionTabla Lanzada en caso de un error, o haber un fallo en
//     * la restricción.
//     */
//    public abstract void evaluarRestriccion( RelacionTerminal ... relaciones ) throws ExcepcionTabla;
    
    /**
     * Cambia el nombre de todos los datos asociados a la restricción, cambiando
     * los nombres calificados para que se adecúen a la nueva tabla.
     * @param nuevoNombre Nuevo nombre de la tabla.
     */
    public abstract void cambiarNombreTabla( String nuevoNombre );
}
