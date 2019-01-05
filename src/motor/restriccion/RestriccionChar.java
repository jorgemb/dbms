package motor.restriccion;

import excepciones.ExcepcionTabla;
import java.util.ArrayList;
import motor.Dato;
import motor.TipoDato;
import motor.relacion.Fila;
import motor.relacion.Relacion;

/**
 *
 * @author Jorge
 */
public class RestriccionChar extends Restriccion{
    private String nombreCampo;
    private int cantidadCaracteres;

    /**
     * Verifica que un campo no se pase de la cantidad de caracteres dada.
     * @param nombreCampo Nombre calificado del campo a verificar.
     * @param cantidadCaracteres Cantidad de caracteres máximo del campo.
     */
    public RestriccionChar(String nombreCampo, int cantidadCaracteres) {
        this.nombreCampo = nombreCampo;
        this.cantidadCaracteres = cantidadCaracteres;
    }
    
    /**
     * Verifica si la fila se cumple con la relación dada.
     * @param relacion Relación de donde salen los campos.
     * @param filaVerificar Fila a verificar. 
     */
    public void evaluarRestriccion( Relacion relacion, Fila filaVerificar ) throws ExcepcionTabla{
        // Obtiene el esquema y los indices
        ArrayList<String> nombreColumnas = relacion.obtenerTodosNombreCalificados();
        
        // Obtiene el índice de la columna referenciada
        int indiceColumna = nombreColumnas.indexOf( nombreCampo );
        if( indiceColumna == -1 )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.ColumnaNoExiste, nombreCampo);
        
        
        Dato datoVerificar = filaVerificar.obtenerDato(indiceColumna);
        if( datoVerificar.obtenerTipo() != TipoDato.CHAR )
            throw new ExcepcionTabla("No se puede aplicar una restricción CHAR a un dato de tipo " 
                    + datoVerificar.obtenerTipo().name());
        
        String strDato = (String)datoVerificar.obtenerValor();
        if( strDato.length() > cantidadCaracteres )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.FalloRestriccion, 
                    String.format( "No se puede asignar un string de tamaño %d a un dato de tipo CHAR(%d).",
                    strDato.length(), cantidadCaracteres) );
    }
    
    /**
     * Evalúa la restricción en una relación completa.
     * @param relacion Relación a evaluar.
     */
    public void evaluarRestriccion( Relacion relacion ){
        for (Fila filaActual : relacion) {
            evaluarRestriccion( relacion, filaActual );
        }
    }
    
    
    /**
     * @return Devuelve el nombre del campo referenciado.
     */
    public String obtenerCampoReferenciado(){
        return nombreCampo;
    }
    
    /**
     * Cambia el nombre de la tabla.
     * @param nuevoNombre 
     */
    @Override
    public void cambiarNombreTabla(String nuevoNombre) {
        String campo = motor.Util.obtenerNombreCampo(nombreCampo);
        nombreCampo = motor.Util.obtenerNombreCalificado(nuevoNombre, campo);
    }
    
}
