package motor.relation;

import exceptions.TableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Represents a relation with an order (ORDER BY clause)
 *
 * @author Jorge
 */
public class OrderedRelation extends Relation {

	public enum OrderType {
		ASCENDING, DESCENDING
	};

	private Relation baseRelation;
	private LinkedHashMap<String, OrderType> orders;

	/**
	 * Constructor
	 *
	 * @param baseRelation Base relation
	 * @param orders Types and fields for ordering.
	 */
	public OrderedRelation(Relation baseRelation, LinkedHashMap<String, OrderType> orders) {
		this.baseRelation = baseRelation;
		this.orders = orders;
	}

	@Override
	public Schema getSchema() {
		return this.baseRelation.getSchema();
	}

	@Override
	public int getRowNumber() {
		return this.baseRelation.getRowNumber();
	}

	@Override
	public String getQualifiedName(int indiceColumna) throws TableException {
		return this.baseRelation.getQualifiedName(indiceColumna);
	}

	@Override
	public Iterator<Row> iterator() {
		// Materializes the relation
		ArrayList<Row> relation = new ArrayList<>();
		for (Row filaActual : baseRelation) {
			relation.add(filaActual);
		}

		Collections.sort(relation, new RelationComparator());
		return relation.iterator();
	}

	/**
	 * Compares columns for ordering
	 */
	class RelationComparator implements Comparator<Row> {

		private HashMap<String, Integer> columnIndices;

		/**
		 * Builds index for the columns
		 */
		public RelationComparator() {
			columnIndices = new HashMap<>();

			for (int i = 0; i < getSchema().getSize(); i++) {
				columnIndices.put(getQualifiedName(i), i);
			}
		}

		/**
		 * Compares two rows
		 *
		 * @param left
		 * @param right
		 * @return
		 */
		@Override
		public int compare(Row left, Row right) {
			for (String currentField : orders.keySet()) {
				int index = columnIndices.get(currentField);

				int comparison = 0;
				if (orders.get(currentField) == OrderType.ASCENDING) {
					comparison = left.getDatum(index).compareTo(right.getDatum(index));
				} else {
					comparison = right.getDatum(index).compareTo(left.getDatum(index));
				}

				if (comparison != 0) {
					return comparison;
				}
			}

			return 0;
		}

	}
}
