package motor.restriccion;

import condition.Condition;
import exceptions.TableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import motor.Data;
import motor.relacion.Row;
import motor.relacion.Relation;

/**
 *
 * @author Jorge
 */
public class CheckRestriction extends Restriction{
    /** Condicion a aplicar a cada fila */
    private Condition condicionAplicar;
    private transient String[] camposCondicion;
    
    /**
     * Obtiene la restricción a aplicar.
     * @param condicionAplicar 
     */
    public CheckRestriction(Condition condicionAplicar) {
        this.condicionAplicar = condicionAplicar;
    }
    
    /**
     * Devuelve los campos referenciados.
     * @return 
     */
    public ArrayList<String> getReferencedFields(){
        return new ArrayList<>(Arrays.asList(condicionAplicar.getUsedColumns()));
    }
    
    /**
     * Evalua la restricción en una fila dada.
     * @param filaEvaluar 
     */
    public void evaluateRestriction( Relation relacion, Row filaEvaluar ){
        // Obtiene los campos de la condicion
        if( camposCondicion == null )
            camposCondicion = condicionAplicar.getUsedColumns();
        
        // Llena el mapa de datos
        HashMap<String, Data> mapaDatos = new HashMap<>();
        
        ArrayList<String> camposRelacion = relacion.obtenerTodosNombreCalificados();
        for (String campoActual : camposRelacion) {
            int indice = camposRelacion.indexOf( campoActual );
            if( indice != -1 )
                mapaDatos.put(campoActual, filaEvaluar.getDatum(indice) );
        }
        
        if( !condicionAplicar.evaluate(mapaDatos) )
            throw new TableException(TableException.ErrorType.FalloRestriccion, "No se cumple la restricción CHECK " + condicionAplicar.toString());
    }
    
    /**
     * Evalua una restriccion con todas las filas de una relación.
     * @param relacion 
     */
    public void evaluateRestriction( Relation relacion ) throws TableException{
        for (Row filaActual : relacion) {
            evaluateRestriction(relacion, filaActual);
        }
    }

    /**
     * Cambia el valor de los nombres calificados de la restricción.
     * @param nuevoNombre Nuevo nombre de la tabla.
     */
    @Override
    public void changeTableName(String nuevoNombre) {
        condicionAplicar.changeTableName(null, nuevoNombre);
    }
}
