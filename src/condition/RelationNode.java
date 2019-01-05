package condition;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import motor.Data;
import motor.DataType;

/**
 * Node that represents a relation
 *
 * @author eddycastro
 */
public class RelationNode implements Node, java.io.Serializable {

	private RelationType type;
	private Node rightChild;
	private Node leftChild;

	public enum RelationType {
		Less,
		LessEqual,
		Greater,
		GreaterEqual,
		Equal,
		Inequal
	}

	/**
	 * Constructor
	 *
	 * @param type
	 * @param leftChild
	 * @param rightChild
	 */
	public RelationNode(RelationType type, Node leftChild, Node rightChild) {
		this.type = type;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	/**
	 *
	 * @param <T> Generic type
	 * @param leftValue Left node value
	 * @param rightValue Right node value
	 * @return True or False
	 */
	private <T extends Comparable> boolean evaluateOperation(T leftValue, T rightValue) {

		switch (type) {
			case Less:
				return leftValue.compareTo(rightValue) < 0;
			case LessEqual:
				return leftValue.compareTo(rightValue) <= 0;
			case Greater:
				return leftValue.compareTo(rightValue) > 0;
			case GreaterEqual:
				return leftValue.compareTo(rightValue) >= 0;
			case Equal:
				return leftValue.compareTo(rightValue) == 0;
			case Inequal:
				return leftValue.compareTo(rightValue) != 0;
			default:
				throw new AssertionError();
		}

	}

	/**
	 * @param dataMap
	 * @return
	 */
	@Override
	public Object evaluate(HashMap<String, Data> dataMap) {

		Object left = leftChild.evaluate(dataMap);
		Object right = rightChild.evaluate(dataMap);

		// Verifies null
		if (left == null || right == null) {
			// Verifies only option
			Data leftDatum = new Data(left);
			Data rightDatum = new Data(right);
			return evaluateOperation(leftDatum, rightDatum);
		}

		// Both integer
		if (left instanceof Integer && right instanceof Integer) {
			return evaluateOperation((Integer) left, (Integer) right);
		} // Both floating point or mixed with int
		else if (left instanceof Float || right instanceof Float || left instanceof Double || right instanceof Double) {
			return evaluateOperation(((Number) left).floatValue(), ((Number) right).floatValue());
		} // Both string
		else {
			// Verifies if they can be converted to date
			Data datoIzq = new Data(left);
			Data datoDer = new Data(right);

			if (datoIzq.obtenerTipo() == DataType.DATE && datoDer.obtenerTipo() == DataType.DATE) {
				Date leftDate = Data.getDate(datoIzq);
				Date rightDate = Data.getDate(datoDer);
				return evaluateOperation(leftDate, rightDate);
			}

			return evaluateOperation(left.toString(), right.toString());
		}

	}

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
			case Less:
				symbol = "<";
				break;
			case LessEqual:
				symbol = "<=";
				break;
			case Greater:
				symbol = ">";
				break;
			case GreaterEqual:
				symbol = ">=";
				break;
			case Equal:
				symbol = "=";
				break;
			case Inequal:
				symbol = "<>";
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
