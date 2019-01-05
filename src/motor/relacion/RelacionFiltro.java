package motor.relacion;

import condicion.Condicion;
import condicion.EvaluadorCondicion;
import excepciones.ExcepcionTabla;
import java.util.ArrayList;
import java.util.Iterator;
import motor.Dato;

/**
 * Representa una relación donde se aplica un filtro (WHERE).
 * @author Jorge
 */
public class RelacionFiltro extends Relacion{
    private Relacion relacionContenida;
    private Condicion filtro;
    private EvaluadorCondicion evaluador;

    /**
     * Constructor para la relación.
     * @param relacionContenida
     * @param filtro 
     */
    public RelacionFiltro(Relacion relacionContenida, Condicion filtro) {
        this.relacionContenida = relacionContenida;
        this.filtro = filtro;
        this.evaluador = new EvaluadorCondicion(filtro, relacionContenida);
    }

    /**
     * @return Devuelve el esquema de la relación.
     */
    @Override
    public Esquema obtenerEsquema() {
        return relacionContenida.obtenerEsquema();
    }

    /**
     * Calcula la cantidad de filas aplicando la condición a toda la relación
     * contenida.
     * @return Cantidad de filas.
     */
    @Override
    public int obtenerCantidadFilas() {
        int cantidad = 0;
        // Itera por cada fila de la relación
        for (Fila filaActual : relacionContenida) {
            if( evaluador.evaluarFila(filaActual) )
                ++cantidad;
        }
        
        return cantidad;
    }

    /**
     * Devuelve el nombre calificado de la columna dada.
     * @param indiceColumna Indice de la columna.
     * @return Nombre calificado de la columna.
     * @throws ExcepcionTabla 
     */
    @Override
    public String obtenerNombreCalificado(int indiceColumna) throws ExcepcionTabla {
        return relacionContenida.obtenerNombreCalificado(indiceColumna);
    }

    @Override
    public Iterator<Fila> iterator() {
        // Materializa la relacion
//        ArrayList<Fila> filasRelacion = new ArrayList<>();
//        
//        
//        for (Fila filaActual : relacionContenida) {
//            if( evaluador.evaluarFila(filaActual) )
//                filasRelacion.add(filaActual);
//        }
//        
//        return filasRelacion.iterator();
        
        return new Iterador();
    }
    
    
    
    /**
     * Clase para iterar por la relación de filtro.
     */
    public class Iterador implements Iterator<Fila>{
        private Fila filaSiguiente;
        private Iterator<Fila> iteradorRelacion;

        /**
         * Constructor.
         */
        public Iterador() {
            this.iteradorRelacion = relacionContenida.iterator();
            buscarSiguiente();
        }
        
        /**
         * Busca la siguiente fila que cumpla la condición.
         */
        private void buscarSiguiente(){
            while( iteradorRelacion.hasNext() ){
                Fila filaActual = iteradorRelacion.next();
                if( evaluador.evaluarFila(filaActual) ){
                    filaSiguiente = filaActual;
                    return;
                }
            }
            filaSiguiente = null;
        }
        
        @Override
        public boolean hasNext() {
            return filaSiguiente != null;
        }

        @Override
        public Fila next() {
            Fila retorno = filaSiguiente;
            buscarSiguiente();
            
            return retorno;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("No se pueden remover elementos de la relación.");
        }
        

        
    }
}
