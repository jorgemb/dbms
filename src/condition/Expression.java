package condition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import motor.Data;

/**
 * Defines an expression
 *
 * @author Jorge
 */
public class Expression {

	private Node rootNode;

	/**
	 * Constructor
	 *
	 * @param rootNode
	 */
	public Expression(Node rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * Evaluates an expression with the given data.
	 *
	 * @param data
	 * @return
	 */
	public Data evaluateExpression(HashMap<String, Data> data) {
		return new Data(rootNode.evaluate(data));
	}

	/**
	 * Returns the names of the columns used by the condition.
	 *
	 * @return String[] with the qualified names of the columns.
	 */
	public String[] getUsedColumns() {
		Set<String> usedNames = new HashSet<>();
		rootNode.addUsedNames(usedNames);

		return usedNames.toArray(new String[0]);
	}
}
