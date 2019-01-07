package motor;

import condition.Condition;
import condition.Expression;
import condition.ConditionalNode;
import condition.DataNode;
import condition.LiteralNode;
import condition.LiteralNode.LiteralType;
import condition.OperationNode;
import condition.RelationNode;
import exceptions.DatabaseException;
import exceptions.TableException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import motor.relation.Row;
import motor.relation.Relation;
import motor.relation.FilteredRelation;
import motor.relation.OrderedRelation;
import motor.relation.CrossProductRelation;
import motor.relation.ProjectedRelation;
import motor.restriction.CharRestriction;
import motor.restriction.CheckRestriction;
import motor.restriction.PrimaryKeyRestriction;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Jorge
 */
public class Database {
	// Variables estáticas

	/**
	 * Root directory for data
	 */
	private static File rootDirectory = null;

	/**
	 * Database name
	 */
	private final String databaseName;

	/**
	 * Associated tables
	 */
	private HashMap<String, Table> tables;

	/**
	 * Current database directory
	 */
	private final File baseDirectory;

	/**
	 * Creates a new database. Operation can fail if a database with the given
	 * name already exists, or if the name is not a valid identifier.
	 *
	 * @param name Database name
	 * @return Database instance
	 * @throws DatabaseException
	 * @throws TableException
	 */
	public static Database create(String name) throws DatabaseException, TableException {
		// Verifies initial state
		verifyInitialState();

		// Verifies that the name does not exist
		try {
			findDatabase(name);

			// Database already exists
			throw new DatabaseException(DatabaseException.ErrorType.NameAlreadyExists, name);

		} catch (DatabaseException databaseException) {
			if (databaseException.getErrorType() != DatabaseException.ErrorType.NonExistent) {
				throw databaseException;
			}
		}

		// New database created
		File databaseDirectory = new File(rootDirectory, name);
		if (!databaseDirectory.mkdir()) {
			throw new DatabaseException(String.format("Unable to create directory for database '%s'.", name));
		}

		return new Database(name, databaseDirectory);
	}

	/**
	 * Returns a database with the given name
	 *
	 * @param name Name of the database
	 * @return Database instance
	 * @throws DatabaseException
	 * @throws TableException
	 */
	public static Database findDatabase(String name) throws DatabaseException, TableException {
		// Verify initial state
		verifyInitialState();

		// Looks for the database directory
		String[] directories = rootDirectory.list();

		File searchedDirectory = null;

		for (String directoryName : directories) {
			File currentFile = new File(rootDirectory, directoryName);
			if (currentFile.isDirectory()) {

				// Check if the database is the one we are looking for
				if (currentFile.getName().equals(name)) {
					searchedDirectory = currentFile;
					break;
				}
			}
		}

		// Verify if found
		if (searchedDirectory == null) {
			throw new DatabaseException(DatabaseException.ErrorType.NonExistent, name);
		}

		// Loads the database
		Database bd = new Database(name, searchedDirectory);

		return bd;
	}

	/**
	 * Eliminates the database with the given name. All the file system is
	 * deleted. Any instance to the database is invalidated.
	 *
	 * @param name Database name to delete
	 * @throws DatabaseException
	 */
	public static void deleteDatabase(String name) throws DatabaseException, TableException {
		verifyInitialState();

		// Finds the database
		Database database;
		try {
			database = findDatabase(name);
		} catch (DatabaseException databaseException) {
			if (databaseException.getErrorType() == DatabaseException.ErrorType.NonExistent) // Nothing to delete
			{
				return;
			} else {
				throw databaseException;
			}
		}

		// Eliminates every file in the directory
		File directory = database.baseDirectory;
		try {
			FileUtils.deleteDirectory(directory);
		} catch (IOException ex) {
			throw new DatabaseException(String.format("Unable to delete database: %s [%s]",
				name, ex.getMessage()));
		}
	}

	/**
	 * Returns the name of the existent databases
	 *
	 * @return Array with names
	 */
	public static ArrayList<String> getDatabaseNames() throws DatabaseException {
		verifyInitialState();

		ArrayList<String> ret = new ArrayList<>();

		// Gets every database name
		for (String fileName : rootDirectory.list()) {
			File currentFile = new File(rootDirectory, fileName);
			if (currentFile.isDirectory()) {
				ret.add(fileName);
			}
		}

		return ret;
	}

	/**
	 * Renames a database. Note that this changes the file system name. Every
	 * instance of the database is invalidated after this call.
	 *
	 * @param currentName Current name of the database
	 * @param newName New name of the database
	 * @throws DatabaseException
	 */
	public static void renameDatabase(String currentName, String newName) throws DatabaseException {
		verifyInitialState();

		// Find database
		ArrayList<String> currentDatabases = getDatabaseNames();
		if (!currentDatabases.contains(currentName)) {
			throw new DatabaseException(DatabaseException.ErrorType.NonExistent, currentName);
		}

		if (currentDatabases.contains(newName)) {
			throw new DatabaseException(DatabaseException.ErrorType.NameAlreadyExists, newName);
		}

		File oldDirectory = new File(rootDirectory, currentName);
		File newDirectory = new File(rootDirectory, newName);
		if (!oldDirectory.renameTo(newDirectory)) {
			throw new DatabaseException(String.format("Couldn't rename database from %s ro %s.",
				currentName, newName));
		}
	}

	/**
	 * Changes a table name
	 *
	 * @param previousName Previous table name
	 * @param newName New table name
	 * @throws DatabaseException
	 * @throws TableException
	 */
	public void renameTable(String previousName, String newName) throws DatabaseException, TableException {
		// Verify if table exists
		if (!this.tables.containsKey(previousName)) {
			throw new TableException(TableException.ErrorType.TableDoesNotExist, previousName);
		}
		if (this.tables.containsKey(newName)) {
			throw new TableException(TableException.ErrorType.TableAlreadyExists, newName);
		}

		// Changes table name
		if (tables.get(previousName).existsInFile()) {
			Table.renameTable(previousName, newName, baseDirectory);
		}

		this.tables.remove(previousName);

		// Changes the name inside the table
		Table renamedTable = Table.loadTable(newName, baseDirectory);
		renamedTable.changeName(newName);

		this.tables.put(newName, renamedTable);
	}

	/**
	 * Deletes the table with the given name
	 *
	 * @param tableName Name of the table to delete
	 * @throws DatabaseException
	 * @throws TableException
	 */
	public void deleteTable(String tableName) throws DatabaseException, TableException {
		// Verifies that the table exists
		if (!this.tables.containsKey(tableName)) {
			throw new TableException(TableException.ErrorType.TableDoesNotExist, tableName);
		}

		// Verifies if file exists
		if (tables.get(tableName).existsInFile()) {
			Table.deleteTable(tableName, baseDirectory);
		}

		// Invalidates and deletes table
		this.tables.get(tableName).invalidateTable();
		this.tables.remove(tableName);
	}

	/**
	 * @return Returns the database name
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * @return Returns array with the name of every table
	 */
	public Table[] getTables() {
		return tables.values().toArray(new Table[0]);
	}

	/**
	 * Returns the table with the given name.
	 *
	 * @param tableName Name of the table
	 * @return Table
	 */
	public Table getTableByName(String tableName) throws TableException {
		// Verify existance
		if (!this.tables.containsKey(tableName)) {
			throw new TableException(TableException.ErrorType.TableDoesNotExist, tableName);
		}

		return this.tables.get(tableName);
	}

	/**
	 * Creates and adds a new table to the database.
	 *
	 * @param tableName
	 * @return Created table
	 */
	public Table addNewTable(String tableName) throws TableException, DatabaseException {
		verifyInitialState();

		// Verifies if the table exists
		if (this.tables.containsKey(tableName)) {
			throw new TableException(TableException.ErrorType.TableAlreadyExists, tableName);
		}

		// Creates the new table
		Table newTable = Table.createTable(tableName, baseDirectory);
		this.tables.put(tableName, newTable);

		return newTable;
	}

	/**
	 * Saves all changes to disk.
	 *
	 * @throws DatabaseException
	 * @throws TableException
	 */
	public void saveChanges() throws DatabaseException, TableException {
		for (Map.Entry<String, Table> tablePair : tables.entrySet()) {
			tablePair.getValue().saveChanges();
		}
	}

	/**
	 * Disable instancing.
	 */
	private Database(String name, File baseDirectory) throws TableException {
		this.databaseName = name;
		this.baseDirectory = baseDirectory;
		this.tables = new HashMap<>();

		// Adds existing tables
		ArrayList<String> tableNames = Table.getTables(baseDirectory);
		tableNames.forEach((currentTableName) -> {
			Table currentTable = Table.loadTable(currentTableName, baseDirectory);
			this.tables.put(currentTableName, currentTable);
		});
	}

	/**
	 * Verifies initial state of directories in database.
	 */
	private static void verifyInitialState() throws DatabaseException {
		if (rootDirectory != null) {
			return;
		}

		rootDirectory = new File(Configuration.getValue(Configuration.DIRECTORY_DATABASE));

		// Creates root directory
		if (!rootDirectory.exists()) {
			if (!rootDirectory.mkdir()) {
				throw new DatabaseException("Couldn't create root directory for database.");
			}
		} else {
			if (!rootDirectory.isDirectory()) {
				throw new DatabaseException("Couldn't create root directory for database.");
			}
		}
	}
}
