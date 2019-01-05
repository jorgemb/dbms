package motor.restriccion;

import excepciones.ExcepcionTabla;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import motor.Dato;
import motor.relacion.Fila;
import motor.relacion.Relacion;

/**
 * Representa una restricción de llave primaria, lo que significa que el o 
 * los campos en la tabla deben de ser únicos para toda la tabla.
 * @author Jorge
 */
public class RestriccionLlavePrimaria extends Restriccion {
    /** Campos referenciados por la llave primaria */
    private String[] camposReferenciados;
    transient private HashSet<Fila> llavesActuales;
    transient private ArrayList<Integer> indicesReferenciados;
    
    /**
     * Constructor con los campos referenciados.
     * @param camposReferenciados Nombre calificado de los campos referenciados por la
     * llave primaria.
     */
    public RestriccionLlavePrimaria(String ... camposReferenciados) {
        this.camposReferenciados = camposReferenciados;
    }

    /**
     * Devuelve los campos referenciados por la llave primaria.
     * @return 
     */
    public ArrayList<String> obtenerCamposReferenciados(){
        return new ArrayList<>( Arrays.asList(camposReferenciados) );
    }
    
    /**
     * Evalúa la restricción sobre una relación.
     * @param relacion
     * @throws ExcepcionTabla 
     */
    public void evaluarRestriccion(Relacion relacion) throws ExcepcionTabla {
        // Calcula los indices referenciados
        calcularIndicesReferenciados(relacion);
        
        // Verifica la restricción por cada fila
        llavesActuales = new HashSet<>();
        
        for (Fila filaActual : relacion) {
            evaluarFilaIncremental(filaActual);
        }
    }
    
    
    /**
     * Evalua una fila de forma incremental, asumiendo que ya se evaluó la
     * restricción una vez antes.
     * @param fila 
     */
    public void evaluarFilaIncremental( Fila fila ){
        // Construye las llaves si no existen
        if( llavesActuales == null )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.RestriccionErrorEnParametros, 
                    "No se puede evaluar la restricción de llave primaria de forma incremental si no se ha evaluado una vez con toda la relación.");
        
        // Construye la fila nueva
        Dato[] datosFila = new Dato[camposReferenciados.length];
        for (int i = 0; i < camposReferenciados.length; i++) {
            datosFila[i] = fila.obtenerDato( indicesReferenciados.get(i) );
        }

        Fila filaLlave = new Fila(datosFila);
        if( !llavesActuales.add(filaLlave) )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.FalloRestriccion,
                    String.format("Ya existe la llave %s en la tabla", filaLlave.toString()) );
    }
    
    
    /**
     * Calcula los indices referenciados en la relación por parte de la 
     * llave primaria.
     * @param relacion 
     */
    private void calcularIndicesReferenciados( Relacion relacion ){
        // Verifica que la relación tenga todos los campos necesarios.
        ArrayList<Object> camposRelacion = new ArrayList<>();
        indicesReferenciados = new ArrayList<>();
        
        for (int i = 0; i < relacion.obtenerEsquema().obtenerTamaño(); i++) {
            camposRelacion.add(relacion.obtenerNombreCalificado(i));
        }
        
        for (int i = 0; i < camposReferenciados.length; i++) {
            int indiceActual = camposRelacion.indexOf(camposReferenciados[i]);
            if( indiceActual == -1 )
                throw new ExcepcionTabla(ExcepcionTabla.TipoError.RestriccionErrorEnParametros, 
                        "La relación no posee todos los argumentos necesarios para la evaluación de la llave primaria." );
            else
                indicesReferenciados.add( indiceActual );
        }
    }

    /**
     * Cambia el nombre calificado de los valores asociados.
     * @param nuevoNombre Nuevo nombre de la tabla.
     */
    @Override
    public void cambiarNombreTabla(String nuevoNombre) {
        String[] nuevosCamposReferenciados = new String[camposReferenciados.length];
        
        for (int i = 0; i < camposReferenciados.length; i++) {
            String nombreCampo = motor.Util.obtenerNombreCampo(camposReferenciados[i]);
            nuevosCamposReferenciados[i] = String.format("%s.%s", nuevoNombre, nombreCampo);
        }
        
        camposReferenciados = nuevosCamposReferenciados;
    }
    
    
}
