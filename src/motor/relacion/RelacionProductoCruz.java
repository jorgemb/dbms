package motor.relacion;

import excepciones.ExcepcionTabla;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import motor.Dato;

/**
 * Representa a la relación resultante de realizar el producto cruz de dos
 * relaciones.
 * @author Jorge
 */
public class RelacionProductoCruz extends Relacion{
    /** Relaciones encapsuladas por la relacion actual */
    private Relacion relacionIzq, relacionDer;

    /**
     * Constructor de la relacion.
     * @param relacionIzq Parte izquierda del producto cruzado.
     * @param relacionDer Parte derecha del producto cruzado.
     */
    public RelacionProductoCruz(Relacion relacionIzq, Relacion relacionDer) {
        this.relacionIzq = relacionIzq;
        this.relacionDer = relacionDer;
    }
    
    /**
     * @return Esquema de la relación.
     */
    @Override
    public Esquema obtenerEsquema() {
        return Esquema.combinarEsquemas(relacionIzq.obtenerEsquema(), relacionDer.obtenerEsquema());
    }

    /**
     * @return Cantidad de filas de la relación.
     */
    @Override
    public int obtenerCantidadFilas() {
        return relacionIzq.obtenerCantidadFilas() * relacionDer.obtenerCantidadFilas();
    }

    /**
     * Devuelve el nombre calificado de la columna dada.
     * @param indiceColumna Índice de la columna a consultar.
     * @return String con el nombre calificado.
     * @throws ExcepcionTabla 
     */
    @Override
    public String obtenerNombreCalificado(int indiceColumna) throws ExcepcionTabla {
        int columnasIzq = relacionIzq.obtenerEsquema().obtenerTamaño();
        if( indiceColumna < columnasIzq )
            return relacionIzq.obtenerNombreCalificado(indiceColumna);
        else
            return relacionDer.obtenerNombreCalificado(indiceColumna-columnasIzq);
    }
    
    /**
     * @return Iterador sobre la relacion
     */
    @Override
    public Iterator<Fila> iterator() {
        return new Iterador();
    }
    
    
    /**
     * Iterador sobre la relación.
     */
    public class Iterador implements Iterator<Fila>{
        private Iterator<Fila> iterIzquierdo, iterDerecho;
        private ArrayList<Dato> filaIzquierdaActual;
        
        /**
         * Constructor por defecto.
         */
        public Iterador() {
            this.iterIzquierdo = relacionIzq.iterator();
            this.iterDerecho = relacionDer.iterator();
            
            if( iterIzquierdo.hasNext() )
                filaIzquierdaActual = new ArrayList<>( Arrays.asList( iterIzquierdo.next().obtenerDatos() ) );
        }
        
        /**
         * @return True si aún existen filas para iterar.
         */
        @Override
        public boolean hasNext() {
            if( relacionIzq.obtenerCantidadFilas() * relacionDer.obtenerCantidadFilas() == 0 )
                return false;
            
            return (iterIzquierdo.hasNext() || iterDerecho.hasNext());
        }

        /**
         * @return Devuelve la siguiente fila, y avanza la iteración.
         */
        @Override
        public Fila next() {
            // Crea la nueva fila
            if( iterDerecho.hasNext() ){
                ArrayList<Dato> datosFila = new ArrayList<>( filaIzquierdaActual );
                datosFila.addAll( Arrays.asList( iterDerecho.next().obtenerDatos() ) );
                return new Fila( datosFila.toArray( new Dato[0] ) );
            } else if( iterIzquierdo.hasNext() ){
                filaIzquierdaActual = new ArrayList<>( Arrays.asList( iterIzquierdo.next().obtenerDatos() ) );
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
