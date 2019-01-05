package motor.relation;

import condition.Condition;
import condition.ConditionEvaluator;
import exceptions.TableException;
import java.util.Iterator;

/**
 * Represents a relation where a filter is applied (WHERE clause)
 *
 * @author Jorge
 */
public class FilteredRelation extends Relation {

	private Relation baseRelation;
	private Condition filter;
	private ConditionEvaluator evaluator;

	/**
	 * Constructor
	 *
	 * @param baseRelation
	 * @param filter
	 */
	public FilteredRelation(Relation baseRelation, Condition filter) {
		this.baseRelation = baseRelation;
		this.filter = filter;
		this.evaluator = new ConditionEvaluator(filter, baseRelation);
	}

	@Override
	public Schema getSchema() {
		return baseRelation.getSchema();
	}

	@Override
	public int getRowNumber() {
		int amount = 0;

		for (Row currentRow : baseRelation) {
			if (evaluator.evaluateRow(currentRow)) {
				++amount;
			}
		}
		return amount;
	}

	@Override
	public String getQualifiedName(int columndIndex) throws TableException {
		return baseRelation.getQualifiedName(columndIndex);
	}

	@Override
	public Iterator<Row> iterator() {
		return new RelationIterator();
	}

	/**
	 * Relation iterator
	 */
	public class RelationIterator implements Iterator<Row> {

		private Row nextRow;
		private Iterator<Row> rowIterator;

		/**
		 * Constructor.
		 */
		public RelationIterator() {
			this.rowIterator = baseRelation.iterator();
			findNext();
		}

		/**
		 * Finds the next row that holds the condition.
		 */
		private void findNext() {
			while (rowIterator.hasNext()) {
				Row currentRow = rowIterator.next();
				if (evaluator.evaluateRow(currentRow)) {
					nextRow = currentRow;
					return;
				}
			}
			nextRow = null;
		}

		@Override
		public boolean hasNext() {
			return nextRow != null;
		}

		@Override
		public Row next() {
			Row ret = nextRow;
			findNext();

			return ret;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Elements cannot be removed from a Filter Relation.");
		}

	}
}
