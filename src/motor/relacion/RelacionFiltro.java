package motor.relacion;

import condition.Condition;
import condition.ConditionEvaluator;
import exceptions.TableException;
import java.util.ArrayList;
import java.util.Iterator;
import motor.Data;

/**
 * Representa una relación donde se aplica un filtro (WHERE).
 * @author Jorge
 */
public class RelacionFiltro extends Relation{
    private Relation relacionContenida;
    private Condition filtro;
    private ConditionEvaluator evaluador;

    /**
     * Constructor para la relación.
     * @param relacionContenida
     * @param filtro 
     */
    public RelacionFiltro(Relation relacionContenida, Condition filtro) {
        this.relacionContenida = relacionContenida;
        this.filtro = filtro;
        this.evaluador = new ConditionEvaluator(filtro, relacionContenida);
    }

    /**
     * @return Devuelve el esquema de la relación.
     */
    @Override
    public Schema getSchema() {
        return relacionContenida.getSchema();
    }

    /**
     * Calcula la cantidad de filas aplicando la condición a toda la relación
     * contenida.
     * @return Cantidad de filas.
     */
    @Override
    public int getRowNumber() {
        int cantidad = 0;
        // Itera por cada fila de la relación
        for (Row filaActual : relacionContenida) {
            if( evaluador.evaluateRow(filaActual) )
                ++cantidad;
        }
        
        return cantidad;
    }

    /**
     * Devuelve el nombre calificado de la columna dada.
     * @param indiceColumna Indice de la columna.
     * @return Nombre calificado de la columna.
     * @throws TableException 
     */
    @Override
    public String getQualifiedName(int indiceColumna) throws TableException {
        return relacionContenida.getQualifiedName(indiceColumna);
    }

    @Override
    public Iterator<Row> iterator() {
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
    public class Iterador implements Iterator<Row>{
        private Row filaSiguiente;
        private Iterator<Row> iteradorRelacion;

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
                Row filaActual = iteradorRelacion.next();
                if( evaluador.evaluateRow(filaActual) ){
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
        public Row next() {
            Row retorno = filaSiguiente;
            buscarSiguiente();
            
            return retorno;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("No se pueden remover elementos de la relación.");
        }
        

        
    }
}
