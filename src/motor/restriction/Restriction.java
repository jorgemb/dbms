package motor.restriction;

/**
 *
 * @author Jorge
 */
public abstract class Restriction implements java.io.Serializable {

	/**
	 * Changes the table name in the qualified names of all the associated data.
	 *
	 * @param newName New table name
	 */
	public abstract void changeTableName(String newName);
}
