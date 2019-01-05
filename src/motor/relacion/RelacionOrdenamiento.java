package motor.relacion;

import excepciones.ExcepcionTabla;
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
public class RelacionOrdenamiento extends Relacion{
    public enum TipoOrdenamiento{ ASCENDENTE, DESCENDENTE };
    
    private Relacion relacionContenida;
    private LinkedHashMap<String, TipoOrdenamiento> ordenamientos;

    /**
     * Constructor de la relación de ordenamiento.
     * @param relacionContenida Relación a ordenar.
     * @param ordenamientos Tipos y campos de ordenamiento
     */
    public RelacionOrdenamiento(Relacion relacionContenida, LinkedHashMap<String, TipoOrdenamiento> ordenamientos) {
        this.relacionContenida = relacionContenida;
        this.ordenamientos = ordenamientos;
    }
    
    /**
     * @return Devuelve el esquema de la relación.
     */
    @Override
    public Esquema obtenerEsquema() {
        return this.relacionContenida.obtenerEsquema();
    }

    /**
     * @return Devuelve la cantidad de filas.
     */
    @Override
    public int obtenerCantidadFilas() {
        return this.relacionContenida.obtenerCantidadFilas();
    }

    /**
     * Devuelve el nombre calificado de un campo.
     * @param indiceColumna Indice de la columna
     * @return 
     * @throws ExcepcionTabla 
     */
    @Override
    public String obtenerNombreCalificado(int indiceColumna) throws ExcepcionTabla {
        return this.relacionContenida.obtenerNombreCalificado(indiceColumna);
    }

    @Override
    public Iterator<Fila> iterator() {
        // Materializa la relación
        ArrayList<Fila> relacion = new ArrayList<>();
        for (Fila filaActual : relacionContenida) {
            relacion.add(filaActual);
        }
        
        Collections.sort(relacion, new Comparador());
        return relacion.iterator();
    }
    
    
    
    /**
     * Permite realizar las comparaciones para ordenamiento.
     */
    class Comparador implements Comparator<Fila>{
        private HashMap<String, Integer> indicesColumnas;

        /**
         * Construye los indices de las columnas.
         */
        public Comparador() {
            indicesColumnas = new HashMap<>();
            
            // Obtiene el nombre de todas las columnas
            for (int i = 0; i < obtenerEsquema().obtenerTamaño(); i++) {
                indicesColumnas.put(obtenerNombreCalificado(i), i);
            }
        }
        
        /**
         * Compara dos filas.
         * @param izq
         * @param der
         * @return 
         */
        @Override
        public int compare(Fila izq, Fila der) {
            // Verifica con todas las columnas
            for (String campoActual : ordenamientos.keySet()) {
                int indice = indicesColumnas.get(campoActual);
                
                int comparacion = 0;
                if( ordenamientos.get(campoActual) == TipoOrdenamiento.ASCENDENTE ){
                    comparacion = izq.obtenerDato(indice).compareTo(der.obtenerDato(indice));
                } else {
                    comparacion = der.obtenerDato(indice).compareTo(izq.obtenerDato(indice));
                }
                
                if( comparacion != 0 )
                    return comparacion;
            }
            
            return 0;
        }
        
    }
}
