package condition;

import excepciones.TableException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import motor.Data;
import motor.relacion.Row;
import motor.relacion.Relation;

/**
 * Allows to evaluate a condition in a relation.
 * @author Jorge
 */
public class ConditionEvaluator {
    private ArrayList<Integer> usedIndices;
    private Condition condition;
    private LinkedHashMap<String, Data> dataMap;

    /**
     * Calculates the indices based on the necessary columns to fulfill the relation.
     * @param condition Condition to evaluate
     * @param relation Relation to construct the evaluator
     */
    public ConditionEvaluator(Condition condition, Relation relation ) {
        this.condition = condition;
        this.usedIndices = new ArrayList<>();
        this.dataMap = new LinkedHashMap<>();
        
        // Gets the data from the relation columns
        ArrayList<String> columnNames = new ArrayList<>();
        for (int i = 0; i < relation.getSchema().getSize(); i++) {
            columnNames.add(relation.getQualifiedName(i));
        }
        
	// Gets the indices to use
        String[] usedColumns = condition.getUsedColumns();
        for (int i = 0; i < usedColumns.length; i++) {
            int schemaIndex = columnNames.indexOf( usedColumns[i] );
            if( schemaIndex == -1 )
                throw new TableException(TableException.TipoError.ColumnDoesNotExist, usedColumns[i] );
            
            usedIndices.add(schemaIndex);
            dataMap.put(columnNames.get(schemaIndex), null);
        }
    }
    
    /**
     * Evaluates the condition in the given row only if is from the initial relation.
     * @param row Row to evaluate
     * @return True if the condition is evaluated successfully in the row. 
     */
    public boolean evaluateRow( Row row ){
	// Iterates on every key
        int i = 0;
        for (String currentKey : dataMap.keySet()) {
            dataMap.put(currentKey, row.getDatum( usedIndices.get(i) ));
            ++i;
        }
        
        return condition.evaluate(dataMap);
    }
}
