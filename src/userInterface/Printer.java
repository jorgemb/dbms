package userInterface;

import motor.relation.Relation;

/**
 * Printer interface
 *
 * @author Jorge
 */
public interface Printer {

	/**
	 * Prints a normal message
	 *
	 * @param msg
	 */
	public void printMessage(String msg);

	/**
	 * Prints an error message
	 *
	 * @param msg
	 */
	public void printError(String msg);

	/**
	 * Prints a relation
	 *
	 * @param relation
	 */
	public void printRelation(Relation relation);

	/**
	 * Gets user confirmation
	 *
	 * @param msg
	 * @return
	 */
	public boolean getConfirmation(String msg);
}
