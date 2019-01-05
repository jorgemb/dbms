package motor.relacion;

import exceptions.TableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 *
 * @author Jorge
 */
public class RelacionOrdenamiento extends Relation{
    public enum TipoOrdenamiento{ ASCENDENTE, DESCENDENTE };
    
    private Relation relacionContenida;
    private LinkedHashMap<String, TipoOrdenamiento> ordenamientos;

    /**
     * Constructor de la relación de ordenamiento.
     * @param relacionContenida Relación a ordenar.
     * @param ordenamientos Tipos y campos de ordenamiento
     */
    public RelacionOrdenamiento(Relation relacionContenida, LinkedHashMap<String, TipoOrdenamiento> ordenamientos) {
        this.relacionContenida = relacionContenida;
        this.ordenamientos = ordenamientos;
    }
    
    /**
     * @return Devuelve el esquema de la relación.
     */
    @Override
    public Schema getSchema() {
        return this.relacionContenida.getSchema();
    }

    /**
     * @return Devuelve la cantidad de filas.
     */
    @Override
    public int getRowNumber() {
        return this.relacionContenida.getRowNumber();
    }

    /**
     * Devuelve el nombre calificado de un campo.
     * @param indiceColumna Indice de la columna
     * @return 
     * @throws TableException 
     */
    @Override
    public String getQualifiedName(int indiceColumna) throws TableException {
        return this.relacionContenida.getQualifiedName(indiceColumna);
    }

    @Override
    public Iterator<Row> iterator() {
        // Materializa la relación
        ArrayList<Row> relacion = new ArrayList<>();
        for (Row filaActual : relacionContenida) {
            relacion.add(filaActual);
        }
        
        Collections.sort(relacion, new Comparador());
        return relacion.iterator();
    }
    
    
    
    /**
     * Permite realizar las comparaciones para ordenamiento.
     */
    class Comparador implements Comparator<Row>{
        private HashMap<String, Integer> indicesColumnas;

        /**
         * Construye los indices de las columnas.
         */
        public Comparador() {
            indicesColumnas = new HashMap<>();
            
            // Obtiene el nombre de todas las columnas
            for (int i = 0; i < getSchema().getSize(); i++) {
                indicesColumnas.put(getQualifiedName(i), i);
            }
        }
        
        /**
         * Compara dos filas.
         * @param izq
         * @param der
         * @return 
         */
        @Override
        public int compare(Row izq, Row der) {
            // Verifica con todas las columnas
            for (String campoActual : ordenamientos.keySet()) {
                int indice = indicesColumnas.get(campoActual);
                
                int comparacion = 0;
                if( ordenamientos.get(campoActual) == TipoOrdenamiento.ASCENDENTE ){
                    comparacion = izq.getDatum(indice).compareTo(der.getDatum(indice));
                } else {
                    comparacion = der.getDatum(indice).compareTo(izq.getDatum(indice));
                }
                
                if( comparacion != 0 )
                    return comparacion;
            }
            
            return 0;
        }
        
    }
}
