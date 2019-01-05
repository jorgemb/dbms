package motor.relacion;

import excepciones.ExcepcionTabla;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Relacion utilizada para evaluar temporalmente una relación con una fila
 * ingresada.
 * @author Jorge
 */
public class RelacionTemporalFila extends Relacion{
    RelacionTerminal relacionContenida;
    Fila filaExtra;

    /**
     * Constructor con relación y fila.
     * @param relacionContenida Relación base.
     * @param filaExtra Fila extra a utilizar.
     */
    public RelacionTemporalFila(RelacionTerminal relacionContenida, Fila filaExtra) {
        this.relacionContenida = relacionContenida;
        this.filaExtra = filaExtra;
    }
    
    /**
     * Devuelve el esquema de la tabla.
     * @return Esquema de la relación.
     */
    @Override
    public Esquema obtenerEsquema() {
        return relacionContenida.obtenerEsquema();
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
     * @throws ExcepcionTabla 
     */
    @Override
    public String obtenerNombreCalificado(int indiceColumna) throws ExcepcionTabla {
        return relacionContenida.obtenerNombreCalificado(indiceColumna);
    }

    
    @Override
    public Iterator<Fila> iterator() {
        return new Iterador();
    }
    
    
    
    
    /**
     * Clase iterador.
     */
    public class Iterador implements Iterator<Fila>{
        private Iterator<Fila> iteradorRelacion;
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
        public Fila next() {
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
