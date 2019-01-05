package motor.restriccion;

import condicion.Condicion;
import excepciones.ExcepcionTabla;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import motor.Dato;
import motor.relacion.Fila;
import motor.relacion.Relacion;

/**
 *
 * @author Jorge
 */
public class RestriccionCheck extends Restriccion{
    /** Condicion a aplicar a cada fila */
    private Condicion condicionAplicar;
    private transient String[] camposCondicion;
    
    /**
     * Obtiene la restricción a aplicar.
     * @param condicionAplicar 
     */
    public RestriccionCheck(Condicion condicionAplicar) {
        this.condicionAplicar = condicionAplicar;
    }
    
    /**
     * Devuelve los campos referenciados.
     * @return 
     */
    public ArrayList<String> obtenerCamposReferenciados(){
        return new ArrayList<>(Arrays.asList(condicionAplicar.obtenerColumnasUtilizadas()));
    }
    
    /**
     * Evalua la restricción en una fila dada.
     * @param filaEvaluar 
     */
    public void evaluarRestriccion( Relacion relacion, Fila filaEvaluar ){
        // Obtiene los campos de la condicion
        if( camposCondicion == null )
            camposCondicion = condicionAplicar.obtenerColumnasUtilizadas();
        
        // Llena el mapa de datos
        HashMap<String, Dato> mapaDatos = new HashMap<>();
        
        ArrayList<String> camposRelacion = relacion.obtenerTodosNombreCalificados();
        for (String campoActual : camposRelacion) {
            int indice = camposRelacion.indexOf( campoActual );
            if( indice != -1 )
                mapaDatos.put(campoActual, filaEvaluar.obtenerDato(indice) );
        }
        
        if( !condicionAplicar.evaluar(mapaDatos) )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.FalloRestriccion, "No se cumple la restricción CHECK " + condicionAplicar.toString());
    }
    
    /**
     * Evalua una restriccion con todas las filas de una relación.
     * @param relacion 
     */
    public void evaluarRestriccion( Relacion relacion ) throws ExcepcionTabla{
        for (Fila filaActual : relacion) {
            evaluarRestriccion(relacion, filaActual);
        }
    }

    /**
     * Cambia el valor de los nombres calificados de la restricción.
     * @param nuevoNombre Nuevo nombre de la tabla.
     */
    @Override
    public void cambiarNombreTabla(String nuevoNombre) {
        condicionAplicar.cambiarNombreTabla(null, nuevoNombre);
    }
}
