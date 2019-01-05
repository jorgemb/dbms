package motor.relation;

import exceptions.TableException;
import java.util.ArrayList;
import java.util.Iterator;
import motor.Data;
import motor.DataType;

/**
 * Represents the projection of a relation (SELECT clause)
 *
 * @author Jorge
 */
public class ProjectedRelation extends Relation {

	private Relation baseRelation;
	private ArrayList<Integer> projectionIndices;

	/**
	 * Constructs the relation from the given qualified names.
	 *
	 * @param projectionNames Used qualified names for projection.
	 */
	public ProjectedRelation(Relation baseRelation, String... projectionNames) throws TableException {
		this.baseRelation = baseRelation;
		this.projectionIndices = new ArrayList<>();

		// Gets the qualified names for all the columns.
		ArrayList<String> qualifiedNames = new ArrayList<>();
		for (int i = 0; i < baseRelation.getSchema().getSize(); i++) {
			qualifiedNames.add(baseRelation.getQualifiedName(i));
		}

		// Gets the index of each name
		for (String selectedName : projectionNames) {
			int indice = qualifiedNames.indexOf(selectedName);
			if (indice != -1) {
				projectionIndices.add(indice);
			} else {
				throw new TableException(TableException.ErrorType.ColumnDoesNotExist, selectedName);
			}
		}
	}

	@Override
	public Schema getSchema() {
		DataType[] completeSchema = baseRelation.getSchema().getTypes();

		ArrayList<DataType> currentSchema = new ArrayList<>();
		for (Integer index : projectionIndices) {
			currentSchema.add(completeSchema[index]);
		}

		return new Schema(currentSchema.toArray(new DataType[0]));
	}

	@Override
	public int getRowNumber() {
		return baseRelation.getRowNumber();
	}

	@Override
	public String getQualifiedName(int columnIndex) throws TableException {
		return baseRelation.getQualifiedName(projectionIndices.get(columnIndex));
	}

	@Override
	public Iterator<Row> iterator() {
		return new RelationIterator();
	}

	/**
	 * Iterator for projected relation
	 */
	public class RelationIterator implements Iterator<Row> {

		private Iterator<Row> rowIterator;

		public RelationIterator() {
			this.rowIterator = baseRelation.iterator();
		}

		@Override
		public boolean hasNext() {
			return rowIterator.hasNext();
		}

		@Override
		public Row next() {
			Row completeRow = rowIterator.next();
			Data[] rowData = completeRow.getData();

			// Projects the row
			Data[] projectedData = new Data[projectionIndices.size()];
			for (int i = 0; i < projectionIndices.size(); i++) {
				projectedData[i] = rowData[projectionIndices.get(i)];
			}

			return new Row(projectedData);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Cannot remove elements from ProjectedRelation.");
		}

	}
}
