package motor.relation;

import exceptions.TableException;
import java.util.ArrayList;

/**
 * Interface for any relation
 *
 * @author Jorge
 */
public abstract class Relation implements Iterable<Row> {

	/**
	 * @return Returns the schema of the relation.
	 */
	public abstract Schema getSchema();

	/**
	 * @return Returns the number of rows in relation.
	 */
	public abstract int getRowNumber();

	/**
	 * Returns the qualified name of the column with the given index.
	 *
	 * @param columnIndex Column index
	 * @return String with qualified name
	 */
	public abstract String getQualifiedName(int columnIndex) throws TableException;

	/**
	 * @return Returns an array with all the qualified names.
	 */
	public ArrayList<String> getAllQualifiedNames() {
		ArrayList<String> ret = new ArrayList<>();
		for (int i = 0; i < getSchema().getSize(); i++) {
			ret.add(getQualifiedName(i));
		}

		return ret;
	}
}
