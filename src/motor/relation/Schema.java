package motor.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import motor.DataType;

/**
 * Represents a schema for a row, a relation or a table.
 *
 * @author Jorge
 */
public class Schema implements java.io.Serializable {

	private DataType[] schemaTypes;

	/**
	 * @param schemaTypes
	 */
	public Schema(DataType[] schemaTypes) {
		this.schemaTypes = schemaTypes;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + Arrays.deepHashCode(this.schemaTypes);
		return hash;
	}

	/**
	 * Verifies if two schemas are the same.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Schema other = (Schema) obj;

		// Checks if schemas are the same size
		if (this.schemaTypes.length != other.schemaTypes.length) {
			return false;
		}

		// Verifies each element
		for (int i = 0; i < schemaTypes.length; i++) {
			if (this.schemaTypes[i] != other.schemaTypes[i]) {
				// Schemas are comparable if one of the two is NULL
				if (this.schemaTypes[i] != DataType.NULL
					&& other.schemaTypes[i] != DataType.NULL) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * String representation
	 */
	@Override
	public String toString() {
		StringBuilder schemaString = new StringBuilder();
		for (DataType dataType : schemaTypes) {
			// First datum
			if (schemaString.length() == 0) {
				schemaString.append(dataType.name());
			} else {
				schemaString.append(", ").append(dataType.name());
			}
		}

		return '{' + schemaString.toString() + '}';
	}

	/**
	 * Returns the types associated to the schema.
	 *
	 * @return DataType[] with data.
	 */
	public DataType[] getTypes() {
		return (DataType[]) schemaTypes.clone();
	}

	/**
	 * Returns the size of the schema.
	 *
	 * @return Size of the schema
	 */
	public int getSize() {
		return schemaTypes.length;
	}

	/**
	 * Returns a new schema from the combination of two
	 *
	 * @param a First schema
	 * @param b Second schema
	 * @return Schema with combined values
	 */
	public static Schema combineSchemas(Schema a, Schema b) {
		ArrayList<DataType> schemaTypes = new ArrayList<>(Arrays.asList(a.getTypes()));
		schemaTypes.addAll(Arrays.asList(b.getTypes()));

		return new Schema(schemaTypes.toArray(new DataType[0]));
	}

	/**
	 * Creates a new schema with an additional type at the end.
	 *
	 * @param baseSchema Base schema
	 * @param typesToAdd Types to add
	 * @return New schema with the added types.
	 */
	public static Schema addType(Schema baseSchema, DataType... typesToAdd) {
		Schema tempSchema = new Schema(typesToAdd);
		return combineSchemas(baseSchema, tempSchema);
	}

	/**
	 * Creates a new schema without the given indices.
	 *
	 * @param baseSchema Base schema
	 * @param indicesToRemove Indices of the removed types.
	 * @return New schema
	 */
	public static Schema deleteType(Schema baseSchema, Integer... indicesToRemove) {
		ArrayList<Integer> indicesList = new ArrayList<>(Arrays.asList(indicesToRemove));
		ArrayList<DataType> schemaTypes = new ArrayList<>(Arrays.asList(baseSchema.getTypes()));

		Iterator<DataType> iterator = schemaTypes.iterator();
		int currentIndex = 0;
		while (iterator.hasNext()) {
			iterator.next();

			if (indicesList.contains(currentIndex)) {
				iterator.remove();
			}

			++currentIndex;
		}

		return new Schema(schemaTypes.toArray(new DataType[0]));
	}
}
