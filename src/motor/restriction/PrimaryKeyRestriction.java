package motor.restriction;

import exceptions.TableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import motor.Data;
import motor.relation.Row;
import motor.relation.Relation;

/**
 * Represents a primary key restriction, meaning that the field must be unique
 * along the whole Table.
 *
 * @author Jorge
 */
public class PrimaryKeyRestriction extends Restriction {

	private String[] referencedFields;
	transient private HashSet<Row> currentKeys;
	transient private ArrayList<Integer> referencedIndices;

	/**
	 * Constructor with referenced fields
	 *
	 * @param referencedFields Qualified names of the referenced fields.
	 */
	public PrimaryKeyRestriction(String... referencedFields) {
		this.referencedFields = referencedFields;
	}

	public ArrayList<String> getReferencedFields() {
		return new ArrayList<>(Arrays.asList(referencedFields));
	}

	/**
	 * Evaluates a restriction over a relation
	 *
	 * @param relation
	 * @throws TableException
	 */
	public void evaluateRestriction(Relation relation) throws TableException {
		calculateReferencedIndices(relation);

		// Verifies the restriction in every row
		currentKeys = new HashSet<>();

		for (Row currentRow : relation) {
			evaluateRowIncrementally(currentRow);
		}
	}

	/**
	 * Evaluates incrementally a row, assuming that the restriction passed the
	 * previous time.
	 *
	 * @param row
	 */
	public void evaluateRowIncrementally(Row row) {
		// Builds the keys if they not exist
		if (currentKeys == null) {
			throw new TableException(TableException.ErrorType.ErrorInRestrictionParameters,
				"Cannot evaluate restriction of Primary Key if it hasn't been evaluated on the whole relation at least once.");
		}

		// Builds the new row
		Data[] rowData = new Data[referencedFields.length];
		for (int i = 0; i < referencedFields.length; i++) {
			rowData[i] = row.getDatum(referencedIndices.get(i));
		}

		Row keyRow = new Row(rowData);
		if (!currentKeys.add(keyRow)) {
			throw new TableException(TableException.ErrorType.RestrictionFailure,
				String.format("Primary key already exist in the relation with name: %s", keyRow.toString()));
		}
	}

	/**
	 * Calculates the referenced indices for a primary key.
	 *
	 * @param relation
	 */
	private void calculateReferencedIndices(Relation relation) {
		// Verifies that the relation has the necessary fields.
		ArrayList<Object> relationFields = new ArrayList<>();
		referencedIndices = new ArrayList<>();

		for (int i = 0; i < relation.getSchema().getSize(); i++) {
			relationFields.add(relation.getQualifiedName(i));
		}

		for (int i = 0; i < referencedFields.length; i++) {
			int currentIndex = relationFields.indexOf(referencedFields[i]);
			if (currentIndex == -1) {
				throw new TableException(TableException.ErrorType.ErrorInRestrictionParameters,
					"The relation doesn not have all the necessary data for the primary key evaluation.");
			} else {
				referencedIndices.add(currentIndex);
			}
		}
	}

	@Override
	public void changeTableName(String newName) {
		String[] newReferencedFields = new String[referencedFields.length];

		for (int i = 0; i < referencedFields.length; i++) {
			String fieldName = motor.Util.getFieldName(referencedFields[i]);
			newReferencedFields[i] = String.format("%s.%s", newName, fieldName);
		}

		referencedFields = newReferencedFields;
	}

}
