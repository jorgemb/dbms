package motor;

import exceptions.DBMSException;

/**
 * Utility functions.
 *
 * @author Jorge
 */
public class Util {

	/**
	 * Returns the name of a table from the qualified name.
	 *
	 * @param qualifiedName Qualified name
	 * @return Table name
	 * @throws DBMSException
	 */
	public static String getTableName(String qualifiedName) throws DBMSException {
		int dotIndex = qualifiedName.indexOf(".");

		// Verifies that is a qualified name
		if (dotIndex == -1) {
			throw new DBMSException("Qualified name not provided.");
		}

		return qualifiedName.substring(0, dotIndex);
	}

	/**
	 * Returns the name of the field from the qualified name.
	 *
	 * @param qualifiedName Qualified name
	 * @return Field name
	 * @throws DBMSException
	 */
	public static String getFieldName(String qualifiedName) throws DBMSException {
		int dotIndex = qualifiedName.indexOf(".");

		// Verifies that its a qualified name.
		if (dotIndex == -1) {
			throw new DBMSException("Qualified name not provided.");
		}

		return qualifiedName.substring(dotIndex + 1);
	}

	/**
	 * Returns the qualified name from the table name and the field.
	 *
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	public static String getCualifiedName(String tableName, String fieldName) {
		return String.format("%s.%s", tableName, fieldName);
	}
}
