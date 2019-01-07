package condition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import motor.Data;

/**
 *
 * @author Jorge
 */
public class Condition implements java.io.Serializable {

	private Node rootNode;

	/**
	 * Constructor
	 *
	 * @param rootNode
	 */
	public Condition(Node rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * Evaluates the condition with the given value map.
	 *
	 * @param valueMap Data to be used for the evaluation
	 * @return True or False
	 */
	public boolean evaluate(HashMap<String, Data> valueMap) {
		return (Boolean) rootNode.evaluate(valueMap);
	}

	/**
	 * Returns the names of the columns used by the condition.
	 *
	 * @return String[] with qualified names of the used columns.
	 */
	public String[] getUsedColumns() {
		Set<String> usedNames = new HashSet<>();
		rootNode.addUsedNames(usedNames);

		return usedNames.toArray(new String[0]);
	}

	/**
	 * @return String representation of the condition
	 */
	@Override
	public String toString() {
		return "(" + rootNode.toString() + ")";
	}

	/**
	 * Changes the name of the table in the qualified names of the associated
	 * nodes in the condition.
	 *
	 * @param previousName
	 * @param newName
	 */
	public void changeTableName(String previousName, String newName) {
		rootNode.changeTableName(previousName, newName);
	}
}
