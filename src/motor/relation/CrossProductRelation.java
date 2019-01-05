package motor.relation;

import exceptions.TableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import motor.Data;

/**
 * Represents a relation resulting from the cross product of two relations.
 *
 * @author Jorge
 */
public class CrossProductRelation extends Relation {

	/**
	 * Encapsulated relations
	 */
	private Relation leftRelation, rightRelation;

	/**
	 * Constructor
	 *
	 * @param leftRelation
	 * @param rightRelation
	 */
	public CrossProductRelation(Relation leftRelation, Relation rightRelation) {
		this.leftRelation = leftRelation;
		this.rightRelation = rightRelation;
	}

	@Override
	public Schema getSchema() {
		return Schema.combineSchemas(leftRelation.getSchema(), rightRelation.getSchema());
	}

	@Override
	public int getRowNumber() {
		return leftRelation.getRowNumber() * rightRelation.getRowNumber();
	}

	@Override
	public String getQualifiedName(int columnIndex) throws TableException {
		int leftColumns = leftRelation.getSchema().getSize();
		if (columnIndex < leftColumns) {
			return leftRelation.getQualifiedName(columnIndex);
		} else {
			return rightRelation.getQualifiedName(columnIndex - leftColumns);
		}
	}

	@Override
	public Iterator<Row> iterator() {
		return new RelationIterator();
	}

	/**
	 * Iterator for cross product relation
	 */
	public class RelationIterator implements Iterator<Row> {

		private Iterator<Row> leftIterator, rightIterator;
		private ArrayList<Data> currentLeftRow;

		/**
		 * Constructor
		 */
		public RelationIterator() {
			this.leftIterator = leftRelation.iterator();
			this.rightIterator = rightRelation.iterator();

			if (leftIterator.hasNext()) {
				currentLeftRow = new ArrayList<>(Arrays.asList(leftIterator.next().getData()));
			}
		}

		@Override
		public boolean hasNext() {
			if (leftRelation.getRowNumber() * rightRelation.getRowNumber() == 0) {
				return false;
			}

			return (leftIterator.hasNext() || rightIterator.hasNext());
		}

		@Override
		public Row next() {
			// Creates the new row
			if (rightIterator.hasNext()) {
				ArrayList<Data> rowData = new ArrayList<>(currentLeftRow);
				rowData.addAll(Arrays.asList(rightIterator.next().getData()));
				return new Row(rowData.toArray(new Data[0]));
			} else if (leftIterator.hasNext()) {
				currentLeftRow = new ArrayList<>(Arrays.asList(leftIterator.next().getData()));
				rightIterator = rightRelation.iterator();
				return this.next();
			} else {
				throw new java.util.NoSuchElementException("The relation doesn't have more elements.");
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Cannot remove elements from cross product relation.");
		}
	}
}
