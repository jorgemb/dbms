package condicion;

import excepciones.ExcepcionTabla;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import motor.Dato;
import motor.relacion.Fila;
import motor.relacion.Relacion;

/**
 *
 * @author Jorge
 */
public class EvaluadorExpresion {
    private ArrayList<Integer> indicesUtilizar;
    private Expresion expresion;
    private LinkedHashMap<String, Dato> mapaDatos;

    /**
     * Obtiene los indices de las columnas necesarias para poder cumplir la relación.
     * @param condicion Condición a evaluar.
     * @param relacion Relación a partir de la cual se construye el evaluador.
     */
    public EvaluadorExpresion(Expresion expresion, Relacion relacion ) {
        this.expresion = expresion;
        this.indicesUtilizar = new ArrayList<>();
        this.mapaDatos = new LinkedHashMap<>();
        
        // Obtiene los datos de las columnas de la relación.
        ArrayList<String> nombreColumnas = new ArrayList<>();
        for (int i = 0; i < relacion.obtenerEsquema().obtenerTamaño(); i++) {
            nombreColumnas.add(relacion.obtenerNombreCalificado(i));
        }
        
        // Obtiene los indices a utilizar
        String[] columnasUtilizadas = expresion.obtenerColumnasUtilizadas();
        for (int i = 0; i < columnasUtilizadas.length; i++) {
            int indiceEsquema = nombreColumnas.indexOf( columnasUtilizadas[i] );
            if( indiceEsquema == -1 )
                throw new ExcepcionTabla(ExcepcionTabla.TipoError.ColumnaNoExiste, columnasUtilizadas[i] );
            
            indicesUtilizar.add(indiceEsquema);
            mapaDatos.put(nombreColumnas.get(indiceEsquema), null);
        }
    }
    
    /**
     * Evalua la condición en la fila dada, siempre y cuando sea de la relación inicial.
     * @param fila Fila a evaluar.
     * @return True si la condición es evaluada de forma satisfactoria con la fila.
     */
    public Dato evaluarFila( Fila fila ){
        // Itera por todas las llaves
        int i = 0;
        for (String llaveActual : mapaDatos.keySet()) {
            mapaDatos.put(llaveActual, fila.obtenerDato( indicesUtilizar.get(i) ));
            ++i;
        }
        
        return expresion.evaluarExpresion(mapaDatos);
    }
}
