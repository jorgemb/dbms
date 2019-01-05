package condition;

import java.util.HashMap;
import java.util.Set;
import motor.Data;

/**
 * Implements a node with conditional operators.
 *
 * @author eddycastro
 */
public class ConditionalNode implements Node, java.io.Serializable {

	private ConditionalOperationType type;
	private Node leftChild;
	private Node rightChild;

	public enum ConditionalOperationType {
		AND,
		OR,
		NOT

	}

	/**
	 * Constructor for any type of operation except NOT
	 * @param type Operation type
	 * @param leftChild Left node
	 * @param rightChild Right node
	 */
	public ConditionalNode(ConditionalOperationType type, Node leftChild, Node rightChild) {
		this.type = type;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	/**
	 * Constructor for NOT operation
	 *
	 * @param leftChild Left node
	 */
	public ConditionalNode(Node leftChild) {
		this.type = ConditionalOperationType.NOT;
		this.leftChild = leftChild;
		this.rightChild = null;
	}

	@Override
	public Object evaluate(HashMap<String, Data> dataMap) {

		boolean left = (Boolean) leftChild.evaluate(dataMap);
		boolean right = rightChild != null ? (Boolean) rightChild.evaluate(dataMap) : false;

		switch (type) {
			case AND:
				return left && right;
			case OR:
				return left || right;
			case NOT:
				return !left;
			default:
				throw new AssertionError();
		}

	}

	@Override
	public void addUsedNames(Set<String> names) {
		leftChild.addUsedNames(names);

		if (rightChild != null) {
			rightChild.addUsedNames(names);
		}
	}

	/**
	 * @return String representation
	 */
	@Override
	public String toString() {
		String symbol = "";
		switch (type) {
			case AND:
				symbol = "AND";
				break;
			case OR:
				symbol = "OR";
				break;
			case NOT:
				return String.format("NOT (%s)", leftChild.toString());
			default:
				throw new AssertionError();
		}
		return String.format("%s %s %s", leftChild.toString(), symbol, rightChild.toString());
	}

	@Override
	public void changeTableName(String previousName, String newName) {
		leftChild.changeTableName(previousName, newName);
		if (rightChild != null) {
			rightChild.changeTableName(previousName, newName);
		}
	}
}
