package motor.relacion;

import excepciones.TableException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Relacion utilizada para evaluar temporalmente una relación con una fila
 * ingresada.
 * @author Jorge
 */
public class RelacionTemporalFila extends Relation{
    RelacionTerminal relacionContenida;
    Row filaExtra;

    /**
     * Constructor con relación y fila.
     * @param relacionContenida Relación base.
     * @param filaExtra Fila extra a utilizar.
     */
    public RelacionTemporalFila(RelacionTerminal relacionContenida, Row filaExtra) {
        this.relacionContenida = relacionContenida;
        this.filaExtra = filaExtra;
    }
    
    /**
     * Devuelve el esquema de la tabla.
     * @return Esquema de la relación.
     */
    @Override
    public Schema getSchema() {
        return relacionContenida.getSchema();
    }

    /**
     * Devuelve la cantidad de filas.
     * @return 
     */
    @Override
    public int obtenerCantidadFilas() {
        return relacionContenida.obtenerCantidadFilas() + 1;
    }

    /**
     * Devuelve el nombre calificado.
     * @param indiceColumna
     * @return
     * @throws TableException 
     */
    @Override
    public String getQualifiedName(int indiceColumna) throws TableException {
        return relacionContenida.getQualifiedName(indiceColumna);
    }

    
    @Override
    public Iterator<Row> iterator() {
        return new Iterador();
    }
    
    
    
    
    /**
     * Clase iterador.
     */
    public class Iterador implements Iterator<Row>{
        private Iterator<Row> iteradorRelacion;
        private boolean continuar;

        /**
         * Construye el iterador inicial.
         */
        public Iterador() {
            iteradorRelacion = relacionContenida.iterator();
            continuar = true;
        }

        
        
        @Override
        public boolean hasNext() {
            return iteradorRelacion.hasNext() || continuar;
        }

        @Override
        public Row next() {
            if( iteradorRelacion.hasNext() )
                return iteradorRelacion.next();
            else if( continuar ){
                continuar = false;
                return filaExtra;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("No se puede remover elementos de la relación.");
        }
        
    }
}
