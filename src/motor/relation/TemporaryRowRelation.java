package motor.relation;

import exceptions.TableException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Relation used for a the temporary evaluation of a row at the end of a Relation.
 * @author Jorge
 */
public class TemporaryRowRelation extends Relation{
    LeafRelation innerRelation;
    Row extraRow;

    /**
	 * Constructor
     * @param innerRelation Base relation
     * @param extraRow Extra row
     */
    public TemporaryRowRelation(LeafRelation innerRelation, Row extraRow) {
        this.innerRelation = innerRelation;
        this.extraRow = extraRow;
    }
    
    @Override
    public Schema getSchema() {
        return innerRelation.getSchema();
    }

    @Override
    public int getRowNumber() {
        return innerRelation.getRowNumber() + 1;
    }

    @Override
    public String getQualifiedName(int indiceColumna) throws TableException {
        return innerRelation.getQualifiedName(indiceColumna);
    }

    @Override
    public Iterator<Row> iterator() {
        return new RelationIterator();
    }
    
    /**
     * Iterator class for a temporary relation
     */
    public class RelationIterator implements Iterator<Row>{
        private Iterator<Row> relationIterator;
        private boolean continueIterator;

        /**
		 * Initial iterator contructor
         */
        public RelationIterator() {
            relationIterator = innerRelation.iterator();
            continueIterator = true;
        }

        
        
        @Override
        public boolean hasNext() {
            return relationIterator.hasNext() || continueIterator;
        }

        @Override
        public Row next() {
            if( relationIterator.hasNext() )
                return relationIterator.next();
            else if( continueIterator ){
                continueIterator = false;
                return extraRow;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove elements from Temporary Relation");
        }
        
    }
}
