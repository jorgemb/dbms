package motor.relation;

import exceptions.TableException;
import java.util.ArrayList;
import java.util.Iterator;
import motor.Table;

/**
 * Represents a base relation with raw data.
 *
 * @author Jorge
 */
public class LeafRelation extends Relation implements java.io.Serializable {

	private ArrayList<Row> rows;
	private Schema schema;
	private transient Table associatedTable;

	/**
	 * Schema constructor
	 *
	 * @param schema
	 */
	public LeafRelation(Schema schema) {
		this.schema = schema;
		this.rows = new ArrayList<>();
	}

	/**
	 * Adds a new row to the relation. The row needs to have the same schema.
	 *
	 * @param row Row to add
	 */
	public void addRow(Row row) throws TableException {
		// Verifies the row schema
		if (getSchema().getSize() != row.getSchema().getSize()) {
			throw new TableException(TableException.ErrorType.SchemaDoesNotMatch,
				String.format("Unable to add row with %s schema to relation with %s schema.",
					row.getSchema(), getSchema()));
		}

		// Tries to convert row
		if (!getSchema().equals(row.getSchema())) {
			row = Row.changeSchema(getSchema(), row);
		}

		rows.add(row);
	}

	@Override
	public Iterator<Row> iterator() {
		return rows.iterator();
	}

	@Override
	public Schema getSchema() {
		return this.schema;
	}

	@Override
	public int getRowNumber() {
		return this.rows.size();
	}

	@Override
	public String getQualifiedName(int columnIndex) throws TableException {
		// Verifies index
		if (columnIndex < 0 || columnIndex >= this.schema.getSize()) {
			throw new TableException(TableException.ErrorType.ColumnDoesNotExist, "IDX" + columnIndex);
		}

		// Gets the qualified name
		String columnName = associatedTable.getTableName() + ".";
		columnName += associatedTable.getColumnNames()[columnIndex];

		return columnName;
	}

	/**
	 * Associates a table to the relation.
	 *
	 * @param associatedTable Associated table
	 */
	public void associateTable(Table associatedTable) {
		this.associatedTable = associatedTable;
	}
}
