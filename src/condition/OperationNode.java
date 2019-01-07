package condition;

import exceptions.TableException;
import java.util.HashMap;
import java.util.Set;
import motor.Data;

/**
 * Node that represents an arithmetic operation.
 *
 * @author eddycastro
 */
public class OperationNode implements Node, java.io.Serializable {

	private OperationType type;
	private Node leftChild;
	private Node rightChild;

	public enum OperationType {
		Sum,
		Subtraction,
		Multiplication,
		Division,
		Modulo
	}

	/**
	 * Constructor
	 *
	 * @param type
	 * @param leftChild
	 * @param rightChild
	 */
	public OperationNode(OperationType type, Node leftChild, Node rightChild) {
		this.type = type;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	/**
	 * Evaluates returning an int
	 *
	 * @param leftValue Left node value
	 * @param rightValue Right node value
	 * @return Result
	 */
	private int evaluteInt(int leftValue, int rightValue) {

		switch (type) {
			case Sum:
				return leftValue + rightValue;
			case Subtraction:
				return leftValue - rightValue;
			case Multiplication:
				return leftValue * rightValue;
			case Division:
				return leftValue / rightValue;
			case Modulo:
				return leftValue % rightValue;
			default:
				throw new AssertionError();
		}

	}

	/**
	 * Evaluates returning a float
	 *
	 * @param leftValue Left node value
	 * @param rightValue Right node value
	 * @return Result
	 */
	private float evaluateFloat(float leftValue, float rightValue) {

		switch (type) {
			case Sum:
				return leftValue + rightValue;
			case Subtraction:
				return leftValue - rightValue;
			case Multiplication:
				return leftValue * rightValue;
			case Division:
				return leftValue / rightValue;
			case Modulo:
				return leftValue % rightValue;
			default:
				throw new AssertionError();
		}

	}

	@Override
	public Object evaluate(HashMap<String, Data> dataMap) {

		Number left = (Number) leftChild.evaluate(dataMap);
		Number right = (Number) rightChild.evaluate(dataMap);

		// Check for null
		if (left == null || right == null) {
			throw new TableException(TableException.ErrorType.DatoInvalido,
				"NULL values cannot be operated.");
		}

		// Both integer
		if (left instanceof Integer && right instanceof Integer) {
			return evaluteInt(left.intValue(), right.intValue());
		} // Both float or mixed
		else {
			return evaluateFloat(left.floatValue(), right.floatValue());
		}

	}

	/**
	 * Adds used names
	 *
	 * @param names
	 */
	@Override
	public void addUsedNames(Set<String> names) {
		leftChild.addUsedNames(names);
		rightChild.addUsedNames(names);
	}

	/**
	 * @return String representation
	 */
	@Override
	public String toString() {
		String symbol = "";
		switch (type) {
			case Sum:
				symbol = "+";
				break;
			case Subtraction:
				symbol = "-";
				break;
			case Multiplication:
				symbol = "*";
				break;
			case Division:
				symbol = "/";
				break;
			case Modulo:
				symbol = "%";
				break;
			default:
				throw new AssertionError();
		}

		return String.format("%s %s %s", leftChild.toString(), symbol, rightChild.toString());
	}

	@Override
	public void changeTableName(String previousName, String newName) {
		leftChild.changeTableName(previousName, newName);
		rightChild.changeTableName(previousName, newName);
	}

}
