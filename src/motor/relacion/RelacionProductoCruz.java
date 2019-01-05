package motor.relacion;

import exceptions.TableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import motor.Data;

/**
 * Representa a la relación resultante de realizar el producto cruz de dos
 * relaciones.
 * @author Jorge
 */
public class RelacionProductoCruz extends Relation{
    /** Relaciones encapsuladas por la relacion actual */
    private Relation relacionIzq, relacionDer;

    /**
     * Constructor de la relacion.
     * @param relacionIzq Parte izquierda del producto cruzado.
     * @param relacionDer Parte derecha del producto cruzado.
     */
    public RelacionProductoCruz(Relation relacionIzq, Relation relacionDer) {
        this.relacionIzq = relacionIzq;
        this.relacionDer = relacionDer;
    }
    
    /**
     * @return Esquema de la relación.
     */
    @Override
    public Schema getSchema() {
        return Schema.combinarEsquemas(relacionIzq.getSchema(), relacionDer.getSchema());
    }

    /**
     * @return Cantidad de filas de la relación.
     */
    @Override
    public int getRowNumber() {
        return relacionIzq.getRowNumber() * relacionDer.getRowNumber();
    }

    /**
     * Devuelve el nombre calificado de la columna dada.
     * @param indiceColumna Índice de la columna a consultar.
     * @return String con el nombre calificado.
     * @throws TableException 
     */
    @Override
    public String getQualifiedName(int indiceColumna) throws TableException {
        int columnasIzq = relacionIzq.getSchema().getSize();
        if( indiceColumna < columnasIzq )
            return relacionIzq.getQualifiedName(indiceColumna);
        else
            return relacionDer.getQualifiedName(indiceColumna-columnasIzq);
    }
    
    /**
     * @return Iterador sobre la relacion
     */
    @Override
    public Iterator<Row> iterator() {
        return new Iterador();
    }
    
    
    /**
     * Iterador sobre la relación.
     */
    public class Iterador implements Iterator<Row>{
        private Iterator<Row> iterIzquierdo, iterDerecho;
        private ArrayList<Data> filaIzquierdaActual;
        
        /**
         * Constructor por defecto.
         */
        public Iterador() {
            this.iterIzquierdo = relacionIzq.iterator();
            this.iterDerecho = relacionDer.iterator();
            
            if( iterIzquierdo.hasNext() )
                filaIzquierdaActual = new ArrayList<>( Arrays.asList( iterIzquierdo.next().getData() ) );
        }
        
        /**
         * @return True si aún existen filas para iterar.
         */
        @Override
        public boolean hasNext() {
            if( relacionIzq.getRowNumber() * relacionDer.getRowNumber() == 0 )
                return false;
            
            return (iterIzquierdo.hasNext() || iterDerecho.hasNext());
        }

        /**
         * @return Devuelve la siguiente fila, y avanza la iteración.
         */
        @Override
        public Row next() {
            // Crea la nueva fila
            if( iterDerecho.hasNext() ){
                ArrayList<Data> datosFila = new ArrayList<>( filaIzquierdaActual );
                datosFila.addAll( Arrays.asList( iterDerecho.next().getData() ) );
                return new Row( datosFila.toArray(new Data[0] ) );
            } else if( iterIzquierdo.hasNext() ){
                filaIzquierdaActual = new ArrayList<>( Arrays.asList( iterIzquierdo.next().getData() ) );
                iterDerecho = relacionDer.iterator();
                return this.next();
            } else {
                throw new java.util.NoSuchElementException("La relación no tiene más elementos.");
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("No se pueden remover elementos de la relación.");
        }
    }
}
