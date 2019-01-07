package condition;

import java.util.HashMap;
import java.util.Set;
import motor.Data;

/**
 * Represents a node with data.
 *
 * @author eddycastro
 */
public class DataNode implements Node, java.io.Serializable {

	private String columnName;

	/**
	 * Constructor
	 *
	 * @param columnName
	 */
	public DataNode(String columnName) {
		this.columnName = columnName;
	}

	@Override
	public Object evaluate(HashMap<String, Data> dataMap) {
		return dataMap.get(columnName).getValue();
	}

	@Override
	public void addUsedNames(Set<String> names) {
		names.add(columnName);
	}

	/**
	 * @return String representation
	 */
	@Override
	public String toString() {
		return columnName;
	}

	@Override
	public void changeTableName(String previousName, String newName) {
		if (previousName != null) {
			if (motor.Util.getTableName(columnName).equals(previousName)) {
				String fieldName = motor.Util.getFieldName(columnName);
				columnName = String.format("%s.%s", newName, fieldName);
			}
		} else {
			String fieldName = motor.Util.getFieldName(columnName);
			columnName = String.format("%s.%s", newName, fieldName);
		}
	}

}
