package motor.restriction;

import exceptions.TableException;
import java.util.ArrayList;
import motor.Data;
import motor.DataType;
import motor.relation.Row;
import motor.relation.Relation;

/**
 * Represents restriction for the size of a char data.
 *
 * @author Jorge
 */
public class CharRestriction extends Restriction {

	private String fieldName;
	private int maxChars;

	/**
	 * @param fieldName Qualified name of the verified field.
	 * @param maxChars Maximum number of characters in field.
	 */
	public CharRestriction(String fieldName, int maxChars) {
		this.fieldName = fieldName;
		this.maxChars = maxChars;
	}

	/**
	 * Verifies if the row complies with the given relation.
	 *
	 * @param relation Relation to check
	 * @param row Row to check
	 */
	public void evaluateRestriction(Relation relation, Row row) throws TableException {
		// Gets schema and indices
		ArrayList<String> columnNames = relation.getAllQualifiedNames();

		int columnIndex = columnNames.indexOf(fieldName);
		if (columnIndex == -1) {
			throw new TableException(TableException.ErrorType.ColumnDoesNotExist, fieldName);
		}

		Data datumToVerify = row.getDatum(columnIndex);
		if (datumToVerify.getTypes() != DataType.CHAR) {
			throw new TableException("Cannot apply a CHAR restrition to a column of type: "
				+ datumToVerify.getTypes().name());
		}

		String strDatum = (String) datumToVerify.getValue();
		if (strDatum.length() > maxChars) {
			throw new TableException(TableException.ErrorType.RestrictionFailure,
				String.format("Cannot assign a string of size %d to a datum of type CHAR(%d).",
					strDatum.length(), maxChars));
		}
	}

	/**
	 * Evaluates the restriction in a whole relation.
	 *
	 * @param relation Relation to evaluate.
	 */
	public void evaluateRestriction(Relation relation) {
		for (Row currentRow : relation) {
			evaluateRestriction(relation, currentRow);
		}
	}

	public String getReferencedField() {
		return fieldName;
	}

	@Override
	public void changeTableName(String newName) {
		String campo = motor.Util.getFieldName(fieldName);
		fieldName = motor.Util.getCualifiedName(newName, campo);
	}

}
