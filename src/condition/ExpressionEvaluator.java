package condition;

import exceptions.TableException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import motor.Data;
import motor.relation.Row;
import motor.relation.Relation;

/**
 * Evaluates a compound expression.
 *
 * @author Jorge
 */
public class ExpressionEvaluator {

	private ArrayList<Integer> usedIndices;
	private Expression expression;
	private LinkedHashMap<String, Data> dataMap;

	/**
	 * Gets the indices of the necessary columns to fulfill the relation.
	 *
	 * @param condicion Evaluated condition
	 * @param relation Relation from which the evaluator is constructed.
	 */
	public ExpressionEvaluator(Expression expression, Relation relation) {
		this.expression = expression;
		this.usedIndices = new ArrayList<>();
		this.dataMap = new LinkedHashMap<>();

		//Gets the data from the columns in the relation.
		ArrayList<String> columnNames = new ArrayList<>();
		for (int i = 0; i < relation.getSchema().getSize(); i++) {
			columnNames.add(relation.getQualifiedName(i));
		}

		// Gets the indices to use
		String[] usedColumns = expression.getUsedColumns();
		for (int i = 0; i < usedColumns.length; i++) {
			int schemaIndex = columnNames.indexOf(usedColumns[i]);
			if (schemaIndex == -1) {
				throw new TableException(TableException.ErrorType.ColumnDoesNotExist, usedColumns[i]);
			}

			usedIndices.add(schemaIndex);
			dataMap.put(columnNames.get(schemaIndex), null);
		}
	}

	/**
	 * Evaluates the condition on the given row only if it is from the initial
	 * relation.
	 *
	 * @param row Row to evaluate
	 * @return True if the row fulfills the condition.
	 */
	public Data evaluateRow(Row row) {
		// Iterates on every key
		int i = 0;
		for (String currentKey : dataMap.keySet()) {
			dataMap.put(currentKey, row.getDatum(usedIndices.get(i)));
			++i;
		}

		return expression.evaluateExpression(dataMap);
	}
}
