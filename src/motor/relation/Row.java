package motor.relation;

import exceptions.TableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import motor.Data;
import motor.DataType;
import org.stringtemplate.v4.misc.ArrayIterator;

/**
 * Represent a single row in a table / relation
 *
 * @author Jorge
 */
public class Row implements java.io.Serializable, Iterable<Data> {

	private final Data[] data;
	private transient Schema schema;

	/**
	 * Constructor with data
	 *
	 * @param data Associated data
	 */
	public Row(Data... data) {
		this.data = data;
	}

	/**
	 * Returns the schema of the row
	 *
	 * @return Schema object
	 * @throws TableException
	 */
	public Schema getSchema() throws TableException {
		if (schema != null) {
			return schema;
		}

		// Calculate schema
		DataType[] dataTypes = new DataType[this.data.length];
		for (int i = 0; i != data.length; i++) {
			if ((dataTypes[i] = data[i].getTypes()) == null) {
				throw new TableException(TableException.ErrorType.InvalidSchema,
					String.format("Unsupported type in row (%s).",
						data[i].getValue().getClass()));
			}
		}
		schema = new Schema(dataTypes);

		return schema;
	}

	/**
	 * Row iterator
	 *
	 * @return
	 */
	@Override
	public Iterator<Data> iterator() {
		return (Iterator) new ArrayIterator(data);
	}

	/**
	 * @return Returns an array with internal data
	 */
	public Data[] getData() {
		return data;
	}

	/**
	 * @param index Index of the datum
	 * @return Return the datum in the given index
	 */
	public Data getDatum(int index) {
		return data[index];
	}

	/**
	 * @return Returns string representation
	 */
	@Override
	public String toString() {
		StringBuilder rowString = new StringBuilder("(");
		boolean first = true;

		for (Data currentDatum : data) {
			if (first) {
				rowString.append(currentDatum.representation());
				first = false;
			} else {
				rowString.append(", ").append(currentDatum.representation());
			}
		}
		rowString.append(")");

		return rowString.toString();
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + Arrays.deepHashCode(this.data);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Row other = (Row) obj;
		if (!Arrays.deepEquals(this.data, other.data)) {
			return false;
		}
		return true;
	}

	/**
	 * Tries to cast the row to a new schema.
	 *
	 * @param targetSchema Target schema
	 * @param row Row to cast
	 * @return New row with the target schema
	 */
	public static Row changeSchema(Schema targetSchema, Row row) {
		DataType[] typeList = targetSchema.getTypes();
		Data[] dataList = row.getData();

		// Verifies
		if (typeList.length != dataList.length) {
			throw new TableException(TableException.ErrorType.SchemaDoesNotMatch,
				"Cannot change row to a schema of different size.");
		}

		// Create return data
		ArrayList<Data> returnData = new ArrayList<>();
		for (int i = 0; i < typeList.length; i++) {
			returnData.add(dataList[i].convertTo(typeList[i]));
		}

		// Returns the new row
		Row returnRow = new Row(returnData.toArray(new Data[0]));
		return returnRow;
	}

	/**
	 * Returns a new row with the data of two combined rows
	 *
	 * @param firstRow First row
	 * @param secondRow Second row
	 * @return New row with combined data.
	 */
	public static Row combineRows(Row firstRow, Row secondRow) {
		ArrayList<Data> rowData = new ArrayList<>(Arrays.asList(firstRow.getData()));
		rowData.addAll(Arrays.asList(secondRow.getData()));

		return new Row(rowData.toArray(new Data[0]));
	}

	/**
	 * Creates a new row with the new data added.
	 *
	 * @param baseRow Base row
	 * @param dataToAdd Data to add
	 * @return New row
	 */
	public static Row addData(Row baseRow, Data... dataToAdd) {
		Row tempRow = new Row(dataToAdd);

		return combineRows(baseRow, tempRow);
	}

	/**
	 * Returns a new row without the positions in the indices.
	 *
	 * @param baseRow Base row
	 * @param indicesToRemove Position to remove
	 * @return New row
	 */
	public static Row deleteData(Row baseRow, Integer... indicesToRemove) {
		ArrayList<Integer> indices = new ArrayList<>(Arrays.asList(indicesToRemove));
		ArrayList<Data> rowData = new ArrayList<>(Arrays.asList(baseRow.getData()));

		Iterator<Data> iterator = rowData.iterator();
		int currentIndex = 0;
		while (iterator.hasNext()) {
			iterator.next();

			// Verifies if is an index to remove
			if (indices.contains(currentIndex)) {
				iterator.remove();
			}

			++currentIndex;
		}

		return new Row(rowData.toArray(new Data[0]));
	}
}
