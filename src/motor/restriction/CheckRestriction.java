package motor.restriction;

import condition.Condition;
import exceptions.TableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import motor.Data;
import motor.relation.Row;
import motor.relation.Relation;

/**
 * Checker for a restriction.
 *
 * @author Jorge
 */
public class CheckRestriction extends Restriction {

	private Condition condition;
	private transient String[] fields;

	/**
	 * Constructor
	 *
	 * @param condition
	 */
	public CheckRestriction(Condition condition) {
		this.condition = condition;
	}

	/**
	 * Returns referenced fields.
	 *
	 * @return
	 */
	public ArrayList<String> getReferencedFields() {
		return new ArrayList<>(Arrays.asList(condition.getUsedColumns()));
	}

	/**
	 * Evaluates restriction on a single row.
	 *
	 * @param row
	 */
	public void evaluateRestriction(Relation relation, Row row) {
		if (fields == null) {
			fields = condition.getUsedColumns();
		}

		// Fills the data map
		HashMap<String, Data> dataMap = new HashMap<>();

		ArrayList<String> relationFields = relation.getAllQualifiedNames();
		for (String currentField : relationFields) {
			int index = relationFields.indexOf(currentField);
			if (index != -1) {
				dataMap.put(currentField, row.getDatum(index));
			}
		}

		if (!condition.evaluate(dataMap)) {
			throw new TableException(TableException.ErrorType.RestrictionFailure, "CHECK restriction does not hold: " + condition.toString());
		}
	}

	/**
	 * Evaluates a restriction in every row of a relation.
	 *
	 * @param relation
	 */
	public void evaluateRestriction(Relation relation) throws TableException {
		for (Row currentRow : relation) {
			evaluateRestriction(relation, currentRow);
		}
	}

	@Override
	public void changeTableName(String newName) {
		condition.changeTableName(null, newName);
	}
}
