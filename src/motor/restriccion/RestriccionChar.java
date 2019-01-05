package motor.restriccion;

import exceptions.TableException;
import java.util.ArrayList;
import motor.Data;
import motor.DataType;
import motor.relacion.Row;
import motor.relacion.Relation;

/**
 *
 * @author Jorge
 */
public class RestriccionChar extends Restriction{
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
    public void evaluateRestriction( Relation relacion, Row filaVerificar ) throws TableException{
        // Obtiene el esquema y los indices
        ArrayList<String> nombreColumnas = relacion.obtenerTodosNombreCalificados();
        
        // Obtiene el índice de la columna referenciada
        int indiceColumna = nombreColumnas.indexOf( nombreCampo );
        if( indiceColumna == -1 )
            throw new TableException(TableException.ErrorType.ColumnDoesNotExist, nombreCampo);
        
        
        Data datoVerificar = filaVerificar.getDatum(indiceColumna);
        if( datoVerificar.obtenerTipo() != DataType.CHAR )
            throw new TableException("No se puede aplicar una restricción CHAR a un dato de tipo " 
                    + datoVerificar.obtenerTipo().name());
        
        String strDato = (String)datoVerificar.getValue();
        if( strDato.length() > cantidadCaracteres )
            throw new TableException(TableException.ErrorType.FalloRestriccion, 
                    String.format( "No se puede asignar un string de tamaño %d a un dato de tipo CHAR(%d).",
                    strDato.length(), cantidadCaracteres) );
    }
    
    /**
     * Evalúa la restricción en una relación completa.
     * @param relacion Relación a evaluar.
     */
    public void evaluateRestriction( Relation relacion ){
        for (Row filaActual : relacion) {
            evaluateRestriction( relacion, filaActual );
        }
    }
    
    
    /**
     * @return Devuelve el nombre del campo referenciado.
     */
    public String getReferencedField(){
        return nombreCampo;
    }
    
    /**
     * Cambia el nombre de la tabla.
     * @param nuevoNombre 
     */
    @Override
    public void changeTableName(String nuevoNombre) {
        String campo = motor.Util.getFieldName(nombreCampo);
        nombreCampo = motor.Util.getCualifiedName(nuevoNombre, campo);
    }
    
}
