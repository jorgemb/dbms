package motor.restriccion;

import exceptions.TableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import motor.Data;
import motor.relacion.Row;
import motor.relacion.Relation;

/**
 * Representa una restricción de llave primaria, lo que significa que el o 
 * los campos en la tabla deben de ser únicos para toda la tabla.
 * @author Jorge
 */
public class PrimaryKeyRestriction extends Restriction {
    /** Campos referenciados por la llave primaria */
    private String[] camposReferenciados;
    transient private HashSet<Row> llavesActuales;
    transient private ArrayList<Integer> indicesReferenciados;
    
    /**
     * Constructor con los campos referenciados.
     * @param camposReferenciados Nombre calificado de los campos referenciados por la
     * llave primaria.
     */
    public PrimaryKeyRestriction(String ... camposReferenciados) {
        this.camposReferenciados = camposReferenciados;
    }

    /**
     * Devuelve los campos referenciados por la llave primaria.
     * @return 
     */
    public ArrayList<String> getReferencedFields(){
        return new ArrayList<>( Arrays.asList(camposReferenciados) );
    }
    
    /**
     * Evalúa la restricción sobre una relación.
     * @param relacion
     * @throws TableException 
     */
    public void evaluateRestriction(Relation relacion) throws TableException {
        // Calcula los indices referenciados
        calcularIndicesReferenciados(relacion);
        
        // Verifica la restricción por cada fila
        llavesActuales = new HashSet<>();
        
        for (Row filaActual : relacion) {
            evaluateRowIncrementally(filaActual);
        }
    }
    
    
    /**
     * Evalua una fila de forma incremental, asumiendo que ya se evaluó la
     * restricción una vez antes.
     * @param fila 
     */
    public void evaluateRowIncrementally( Row fila ){
        // Construye las llaves si no existen
        if( llavesActuales == null )
            throw new TableException(TableException.ErrorType.RestriccionErrorEnParametros, 
                    "No se puede evaluar la restricción de llave primaria de forma incremental si no se ha evaluado una vez con toda la relación.");
        
        // Construye la fila nueva
        Data[] datosFila = new Data[camposReferenciados.length];
        for (int i = 0; i < camposReferenciados.length; i++) {
            datosFila[i] = fila.getDatum( indicesReferenciados.get(i) );
        }

        Row filaLlave = new Row(datosFila);
        if( !llavesActuales.add(filaLlave) )
            throw new TableException(TableException.ErrorType.FalloRestriccion,
                    String.format("Ya existe la llave %s en la tabla", filaLlave.toString()) );
    }
    
    
    /**
     * Calcula los indices referenciados en la relación por parte de la 
     * llave primaria.
     * @param relacion 
     */
    private void calcularIndicesReferenciados( Relation relacion ){
        // Verifica que la relación tenga todos los campos necesarios.
        ArrayList<Object> camposRelacion = new ArrayList<>();
        indicesReferenciados = new ArrayList<>();
        
        for (int i = 0; i < relacion.getSchema().getSize(); i++) {
            camposRelacion.add(relacion.getQualifiedName(i));
        }
        
        for (int i = 0; i < camposReferenciados.length; i++) {
            int indiceActual = camposRelacion.indexOf(camposReferenciados[i]);
            if( indiceActual == -1 )
                throw new TableException(TableException.ErrorType.RestriccionErrorEnParametros, 
                        "La relación no posee todos los argumentos necesarios para la evaluación de la llave primaria." );
            else
                indicesReferenciados.add( indiceActual );
        }
    }

    /**
     * Cambia el nombre calificado de los valores asociados.
     * @param nuevoNombre Nuevo nombre de la tabla.
     */
    @Override
    public void changeTableName(String nuevoNombre) {
        String[] nuevosCamposReferenciados = new String[camposReferenciados.length];
        
        for (int i = 0; i < camposReferenciados.length; i++) {
            String nombreCampo = motor.Util.getFieldName(camposReferenciados[i]);
            nuevosCamposReferenciados[i] = String.format("%s.%s", nuevoNombre, nombreCampo);
        }
        
        camposReferenciados = nuevosCamposReferenciados;
    }
    
    
}
