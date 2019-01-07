package motor;

import motor.restriction.Restriction;
import condition.Condition;
import condition.ConditionEvaluator;
import condition.ExpressionEvaluator;
import condition.Expression;
import exceptions.TableException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import motor.relation.Schema;
import motor.relation.Row;
import motor.relation.Relation;
import motor.relation.TemporaryRowRelation;
import motor.relation.LeafRelation;
import motor.restriction.CharRestriction;
import motor.restriction.CheckRestriction;
import motor.restriction.PrimaryKeyRestriction;

/**
 * Represents single table in the database.
 *
 * @author Jorge
 */
public class Table {

	private String tableName;
	private LinkedHashMap<String, DataType> columns;
	private HashMap<String, Restriction> restrictions;

	private LeafRelation data;
	private File tableFile;

	/**
	 * Verifies if the primary key is incremental or not
	 */
	transient private boolean incrementalPrimaryKey = false;

	/**
	 * Verifies if there are changes to the table
	 */
	transient private boolean withChanges = false;

	/**
	 * Checks if the table is valid
	 */
	private transient boolean isValid = true;

	/**
	 * Adds a column at the end of the table with the name and data type.
	 *
	 * @param newColumnName New column name
	 * @param newColumnType New column type
	 * @throws TableException
	 */
	public void addColumn(String newColumnName, DataType newColumnType) throws TableException {
		// Verifies if column exists
		if (columns.containsKey(newColumnName)) {
			throw new TableException(TableException.ErrorType.ColumnAlreadyExists, newColumnName);
		}

		// Checks that data type is not NULL
		if (newColumnType == DataType.NULL) {
			throw new TableException(TableException.ErrorType.InvalidSchema,
				"NULL is not a valid data type for a column.");
		}

		// Create the new relation
		Relation oldData = data;
		Schema newSchema = Schema.addType(data.getSchema(), newColumnType);

		LeafRelation newRelation = new LeafRelation(newSchema);
		newRelation.associateTable(this);

		// Copies data from previous relation
		if (oldData.getRowNumber() != 0) {
			for (Row currentRow : oldData) {
				Row newRow = Row.addData(currentRow, Data.getDefaultValue(newColumnType));

				newRelation.addRow(newRow);
			}
		}

		// Adds the new column to the schema
		columns.put(newColumnName, newColumnType);

		data = newRelation;
		withChanges = true;
	}

	/**
	 * Adds a restriction to the table.
	 *
	 * @param restrictionName Restriction name
	 * @param newRestriction Restriction object
	 * @throws TableException
	 */
	public void addRestriction(String restrictionName, Restriction newRestriction) throws TableException {
		// Verifies restriction name
		if (restrictions.containsKey(restrictionName)) {
			throw new TableException(TableException.ErrorType.RestrictionAlreadyExists, restrictionName);
		}

		// Verifies that only one primary key restriction exists
		if (newRestriction instanceof PrimaryKeyRestriction) {
			for (Restriction currentRestriction : restrictions.values()) {
				if (currentRestriction instanceof PrimaryKeyRestriction) {
					throw new TableException(TableException.ErrorType.RestrictionAlreadyExists, "[PrimaryKey]");
				}
			}
		}

		// Verifies that the new restriction holds in the table
		verifyRestriction(data, newRestriction);

		this.restrictions.put(restrictionName, newRestriction);

		// Verifies if the PrimaryKey is incremental
		if (newRestriction instanceof PrimaryKeyRestriction) {
			incrementalPrimaryKey = true;
		}

		withChanges = true;
	}

	/**
	 * Deletes the column with the given name.
	 *
	 * @param columnName Name of the column.
	 * @throws TableException
	 */
	public void deleteColumn(String columnName) throws TableException {
		// Verify if column exists
		if (!this.columns.containsKey(columnName)) {
			throw new TableException(TableException.ErrorType.ColumnDoesNotExist, columnName);
		}

		// Verify that no restriction depends on this column
		String columnQualifiedName = Util.getCualifiedName(tableName, columnName);
		for (String restrictionName : restrictions.keySet()) {
			Restriction restriction = restrictions.get(restrictionName);
			// Primary key
			if (restriction instanceof PrimaryKeyRestriction) {
				PrimaryKeyRestriction primaryKey = (PrimaryKeyRestriction) restriction;
				if (primaryKey.getReferencedFields().contains(columnQualifiedName)) {
					throw new TableException(TableException.ErrorType.ReferenceError,
						String.format("Primary key %s deppends on column %s.", restrictionName, columnName));
				}
			} else if (restriction instanceof CheckRestriction) {
				// CHECK
				CheckRestriction check = (CheckRestriction) restriction;
				if (check.getReferencedFields().contains(columnQualifiedName)) {
					throw new TableException(TableException.ErrorType.ReferenceError,
						String.format("The check restriction %s deppends on column %s.", restrictionName, columnName));
				}
			} else {
				// TODO: Add any other restriction
			}
		}

		// Find the column ID
		ArrayList<String> allColumns = new ArrayList<>(columns.keySet());
		int columnId = allColumns.indexOf(columnName);

		// Creates the new relation
		Relation oldRelation = data;

		Schema newSchema = Schema.deleteType(oldRelation.getSchema(), columnId);
		LeafRelation newRelation = new LeafRelation(newSchema);
		newRelation.associateTable(this);
		columns.remove(columnName);

		// Copies all the data
		for (Row currentRow : oldRelation) {
			Row newRow = Row.deleteData(currentRow, columnId);
			newRelation.addRow(newRow);
		}
		for (Iterator<String> it = restrictions.keySet().iterator(); it.hasNext();) {
			Restriction currentRestriction = restrictions.get(it.next());
			if (currentRestriction instanceof CharRestriction) {
				CharRestriction charRestriction = (CharRestriction) currentRestriction;

				if (charRestriction.getReferencedField().equals(columnQualifiedName)) {
					it.remove();
				}
			}
		}

		// Reconstruct primary key
		incrementalPrimaryKey = false;

		// Associates new data
		data = newRelation;
		data.associateTable(this);

		withChanges = true;
	}

	/**
	 * Eliminates a restriction with the given name.
	 *
	 * @param restrictionName
	 * @throws TableException
	 */
	public void deleteRestriction(String restrictionName) throws TableException {
		this.restrictions.remove(restrictionName);

		withChanges = true;
	}

	/**
	 * Adds a new row to the relation.
	 *
	 * @param row Row to add
	 */
	public void addRow(Row row) throws TableException {
		try {
			verifyRestrictionsInRow(row);
		} catch (TableException ex) {
			// Marks primary key reevaluation
			incrementalPrimaryKey = false;
			throw ex;
		}
		this.data.addRow(row);
		withChanges = true;
	}

	/**
	 * Eliminates all files that hold the condition.
	 *
	 * @param condition Condition to check
	 * @return Amount of deleted rows.
	 * @throws TableException
	 */
	public int deleteRows(Condition condition) throws TableException {
		ConditionEvaluator evaluator = new ConditionEvaluator(condition, data);

		Iterator<Row> iterator = data.iterator();
		int amount = 0;
		while (iterator.hasNext()) {
			if (evaluator.evaluateRow(iterator.next())) {
				iterator.remove();
				++amount;
			}
		}

		// Primary key cannot be evaluated incrementally becase a row was
		// deleted.
		incrementalPrimaryKey = false;
		withChanges = true;

		return amount;
	}

	/**
	 * Updates every row that holds the condition with the given values.
	 *
	 * @param expressions Expressions for each field
	 * @param condition Condition to evaluate
	 * @return
	 */
	public int updateRows(HashMap<String, Expression> expressions, Condition condition) {
		ConditionEvaluator evaluator = new ConditionEvaluator(condition, data);

		// Creates all evaluators
		HashMap<String, ExpressionEvaluator> expEvaluators = new HashMap<>();
		for (String currentField : expressions.keySet()) {
			Expression exp = expressions.get(currentField);
			expEvaluators.put(currentField, new ExpressionEvaluator(exp, data));

			// Verifies that the field exists in the table
			if (!columns.containsKey(Util.getFieldName(currentField))) {
				throw new TableException(TableException.ErrorType.ColumnDoesNotExist, currentField);
			}
		}

		// Gets the index of each field
		HashMap<String, Integer> fieldIndices = new HashMap<>();
		int schemaSize = data.getSchema().getSize();
		for (int i = 0; i < schemaSize; i++) {
			fieldIndices.put(data.getQualifiedName(i), i);
		}

		// Evaluates each row
		ArrayList<Row> modifiedRows = new ArrayList<>();
		int modifications = 0;
		Iterator<Row> rowIterator = data.iterator();
		while (rowIterator.hasNext()) {
			Row currentRow = rowIterator.next();
			Data[] currentData = currentRow.getData();

			if (evaluator.evaluateRow(currentRow)) {
				// Removes current row, recalculates and inserts
				rowIterator.remove();
				incrementalPrimaryKey = false;

				Data[] newData = new Data[schemaSize];
				System.arraycopy(currentData, 0, newData, 0, currentData.length);

				/* .. changes fields */
				for (String changedField : expEvaluators.keySet()) {
					Data datoCalculado = expEvaluators.get(changedField).evaluateRow(currentRow);
					newData[fieldIndices.get(changedField)] = datoCalculado;
				}

				// Tries to insert new row
				modifiedRows.add(new Row(newData));
				++modifications;
			}
		}

		// Adds the modified rows
		for (Row currentRow : modifiedRows) {
			addRow(currentRow);
		}

		withChanges = true;
		return modifications;
	}

	/**
	 * @return Returns a hashmap with all the data in the table.
	 */
	public HashMap<String, DataType> getColumns() {
		LinkedHashMap<String, DataType> ret = new LinkedHashMap<>();
		for (Map.Entry<String, DataType> currentRow : columns.entrySet()) {
			ret.put(currentRow.getKey(), currentRow.getValue());
		}

		return ret;
	}

	/**
	 * Returns the name of all the columns
	 *
	 * @return String[] with names.
	 */
	public String[] getColumnNames() {
		return columns.keySet().toArray(new String[0]);
	}

	/**
	 * @return Hashmap with all the restrictions.
	 */
	public HashMap<String, Restriction> getRestrictions() {
		HashMap<String, Restriction> ret = new HashMap<>();
		for (Map.Entry<String, Restriction> currentRestriction : restrictions.entrySet()) {
			ret.put(currentRestriction.getKey(), currentRestriction.getValue());
		}

		return ret;
	}

	/**
	 * Returns the table relation.
	 *
	 * @return Returns the physical relation of the table.
	 */
	public Relation getRelation() {
		return this.data;
	}

	/**
	 * @return Returns the table name.
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Changes the name of the table, recreating every restriction.
	 *
	 * @param newName New name
	 */
	void changeName(String newName) {
		tableName = newName;
		incrementalPrimaryKey = true;
		withChanges = true;

		// Reconstructs the restrictions
		for (Restriction currentRestriction : restrictions.values()) {
			currentRestriction.changeTableName(newName);
		}
	}

	/**
	 * Verifies that all the rows in a relation pass the restriction.
	 *
	 * @param restriction Restriction to verify
	 * @throws TableException
	 */
	private void verifyRestriction(Relation testedRelation, Restriction restriction) throws TableException {

		// Verifies restriction type
		if (restriction instanceof PrimaryKeyRestriction) {
			// Primary Key
			((PrimaryKeyRestriction) restriction).evaluateRestriction(testedRelation);

		} else if (restriction instanceof CheckRestriction) {
			// CHECK
			((CheckRestriction) restriction).evaluateRestriction(testedRelation);

		} else if (restriction instanceof CharRestriction) {
			// CHAR
			((CharRestriction) restriction).evaluateRestriction(testedRelation);

		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Verifies that every restriction in the table passes.
	 *
	 * @throws TableException
	 */
	private void verifyAllRestrictions(Relation testedRelation) throws TableException {
		for (Restriction currentRestriction : restrictions.values()) {
			verifyRestriction(testedRelation, currentRestriction);
		}
	}

	/**
	 * Verifies that the new row passes all the restrictions, taking into
	 * account current data in the table.
	 *
	 * @param row Row to verify
	 */
	private void verifyRestrictionsInRow(Row row) throws TableException {
		for (Restriction currentRestriction : restrictions.values()) {
			if (currentRestriction instanceof PrimaryKeyRestriction) {
				if (incrementalPrimaryKey) {
					((PrimaryKeyRestriction) currentRestriction).evaluateRowIncrementally(row);
				} else {
					// Creates a dummy relation
					TemporaryRowRelation dummyRelation = new TemporaryRowRelation(data, row);
					verifyRestriction(dummyRelation, currentRestriction);
				}
			} else if (currentRestriction instanceof CheckRestriction) {
				// CHECK
				((CheckRestriction) currentRestriction).evaluateRestriction(data, row);

			} else if (currentRestriction instanceof CharRestriction) {
				// CHAR
				((CharRestriction) currentRestriction).evaluateRestriction(data, row);

			} else {
				// TODO
				throw new UnsupportedOperationException();
			}
		}
	}

	//<editor-fold defaultstate="collapsed" desc="Serialization">
	/**
	 * Saves changes in table
	 *
	 * @throws TableException
	 */
	void saveChanges() throws TableException {
		if (withChanges) {
			this.saveTable();
		}
	}

	/**
	 * Verifies if the table exists in the file
	 *
	 * @return
	 */
	boolean existsInFile() {
		return tableFile.exists();
	}

	/**
	 * Invalidates current table.
	 */
	void invalidateTable() {
		isValid = false;
	}

	/**
	 * Returns if the table is valid or not.
	 *
	 * @return
	 */
	boolean isValid() {
		return this.isValid;
	}

	/**
	 * Saves table in a file.
	 *
	 * @throws TableException If there's an error while writing.
	 */
	private void saveTable() throws TableException {
		if (!isValid()) {
			throw new TableException(TableException.ErrorType.InvalidTable, tableName);
		}

		try (BufferedOutputStream buffer = new BufferedOutputStream(new FileOutputStream(tableFile));
			ObjectOutputStream output = new ObjectOutputStream(buffer)) {

			// Saves columns, restrictions and data in that order
			output.writeObject(columns);
			output.writeObject(restrictions);
			output.writeObject(data);

			output.flush();
		} catch (IOException iOException) {
			throw new TableException(String.format("Error while saving data on table %s (%s)",
				this.tableName, iOException.getMessage()));
		}
	}

	/**
	 * Reads data from a table
	 *
	 * @throws TableException
	 */
	private void restoreTable() throws TableException {
		LinkedHashMap<String, DataType> readColumns;
		HashMap<String, Restriction> readRestrictions;
		LeafRelation readData;
		try (BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(tableFile));
			ObjectInputStream input = new ObjectInputStream(buffer)) {
			readColumns = (LinkedHashMap<String, DataType>) input.readObject();
			readRestrictions = (HashMap<String, Restriction>) input.readObject();
			readData = (LeafRelation) input.readObject();

			this.columns = readColumns;
			this.restrictions = readRestrictions;
			this.data = readData;
			this.data.associateTable(this);
		} catch (IOException | ClassNotFoundException ex) {
			throw new TableException(String.format("Error while restoring table %s (%s).",
				this.tableName, ex.getMessage()));
		}
	}
	//</editor-fold>

	/**
	 * Creates a new table with the given name.
	 *
	 * @param name Name of the table
	 * @param tableFile Physical file of the table
	 */
	private Table(String name, File tableFile) {
		this.tableName = name;
		this.columns = new LinkedHashMap<>();
		this.restrictions = new HashMap<>();
		this.restrictions = new HashMap<>();
		this.tableFile = tableFile;

		this.data = new LeafRelation(new Schema(new DataType[0]));
		this.data.associateTable(this);
	}

	/**
	 * Creates a new table in the directory.
	 *
	 * @param name Name of the table
	 * @param databaseDirectory Directory for the database
	 * @return Table with data
	 * @throws TableException
	 */
	static Table createTable(String name, File databaseDirectory) throws TableException {
		// Verifies if the table exists
		if (getTables(databaseDirectory).contains(name)) {
			throw new TableException(TableException.ErrorType.TableAlreadyExists, name);
		}

		// Creates the new table
		Table newTable = new Table(name, new File(databaseDirectory, name));

		// Table is not yet saved
		// newTable.saveTable();
		return newTable;
	}

	/**
	 * Restores the table with the given name.
	 *
	 * @param name Name of the tabje
	 * @param databaseDirectory Directory of database
	 * @return Table with data
	 * @throws TableException
	 */
	static Table loadTable(String name, File databaseDirectory) throws TableException {
		// Verifies that the table exists
		if (!getTables(databaseDirectory).contains(name)) {
			throw new TableException(TableException.ErrorType.TableDoesNotExist, name);
		}

		// Reads data for the Table
		File tableDirectory = new File(databaseDirectory, name);
		Table readTable = new Table(name, tableDirectory);
		readTable.restoreTable();

		return readTable;
	}

	/**
	 * Returns the name of all the tables in the directory.
	 *
	 * @param databaseDirectory Database directory
	 * @return Array with the name of all the tables
	 */
	static ArrayList<String> getTables(File databaseDirectory) {
		ArrayList<String> ret = new ArrayList<>();
		ret.addAll(Arrays.asList(databaseDirectory.list()));

		return ret;
	}

	/**
	 * Deletes the table with the given name.
	 *
	 * @param tableName Table name
	 * @param databaseDirectory Database directory
	 * @throws TableException
	 */
	static void deleteTable(String tableName, File databaseDirectory) throws TableException {
		// Verifies that the table exists
		if (!getTables(databaseDirectory).contains(tableName)) {
			throw new TableException(TableException.ErrorType.TableDoesNotExist, tableName);
		}

		// Deletes the table
		File tableFile = new File(databaseDirectory, tableName);
		try {
			tableFile.delete();
		} catch (Exception e) {
			throw new TableException(String.format("Couldn't delete table %s (%s).",
				tableName, e.getMessage()));
		}
	}

	/**
	 * Changes the name of the table.
	 *
	 * @param tableName Name of the table
	 * @param newTableName New name of the table
	 * @param databaseDirectory Database directory
	 * @throws TableException
	 */
	static void renameTable(String tableName, String newTableName, File databaseDirectory) throws TableException {
		// Verifies if the table exists and the new name does not
		ArrayList<String> tablas = getTables(databaseDirectory);
		if (!tablas.contains(tableName)) {
			throw new TableException(TableException.ErrorType.TableDoesNotExist, tableName);
		}
		if (tablas.contains(newTableName)) {
			throw new TableException(TableException.ErrorType.TableAlreadyExists, newTableName);
		}

		File oldFile = new File(databaseDirectory, tableName);
		File newFile = new File(databaseDirectory, newTableName);
		if (!oldFile.renameTo(newFile)) {
			throw new TableException(String.format("Couldn't rename the table %s to %s.",
				tableName, newTableName));
		}
	}
}
