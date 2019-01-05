package motor.relacion;

import excepciones.ExcepcionTabla;
import java.util.ArrayList;
import java.util.Iterator;
import motor.Tabla;

/**
 *
 * @author Jorge
 */
public class RelacionTerminal extends Relacion implements java.io.Serializable {
    private ArrayList<Fila> datosRelacion;
    private Esquema esquemaRelacion;
    private transient Tabla tablaAsociada;

    /**
     * Constructor con esquema.
     * @param esquemaRelacion 
     */
    public RelacionTerminal(Esquema esquemaRelacion) {
        this.esquemaRelacion = esquemaRelacion;
        this.datosRelacion = new ArrayList<>();
    }
    
    /**
     * Agrega una fila a la relación. La fila debe tener el mismo esquema que
     * la relación.
     * @param filaAgregar Fila a agregar.
     * @return True si la operación fue satisfactoria.
     */
    public void agregarFila( Fila filaAgregar ) throws ExcepcionTabla{
        // Verifica el esquema de la fila
        if( obtenerEsquema().obtenerTamaño() != filaAgregar.obtenerEsquema().obtenerTamaño() ){
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.EsquemaNoCoincide,
            String.format("No se puede agregar la fila con esquema %s a la relación con esquema %s.",
            filaAgregar.obtenerEsquema(), obtenerEsquema()));
        }
        
        // Trata de realizar la conversion
        if( !obtenerEsquema().equals(filaAgregar.obtenerEsquema()) ){
            // Trata de convertir la fila
            filaAgregar = Fila.cambiarEsquema(obtenerEsquema(), filaAgregar);
        }
        
        datosRelacion.add(filaAgregar);
    }
    
    /**
     * Permite iterar por la relación.
     * @return Iterador para recorrer la lista de filas.
     */
    @Override
    public Iterator<Fila> iterator() {
        return datosRelacion.iterator();
    }
    
    /**
     * Devuelve el esquema de la relación.
     * @return 
     */
    @Override
    public Esquema obtenerEsquema() {
        return this.esquemaRelacion;
    }

    /**
     * Devuelve la cantidad filas.
     * @return 
     */
    @Override
    public int obtenerCantidadFilas() {
        return this.datosRelacion.size();
    }

    /**
     * Obtiene el nombre calificado de la columna dada.
     * @param indiceColumna Indice de la columna
     * @return Nombre calificado
     */
    @Override
    public String obtenerNombreCalificado(int indiceColumna) throws ExcepcionTabla{
        // Verifica el índice
        if( indiceColumna < 0 || indiceColumna >= this.esquemaRelacion.obtenerTamaño() )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.ColumnaNoExiste, "IDX" + indiceColumna);
        
        // Obtiene el nombre calificado
        String nombreColumna = tablaAsociada.obtenerNombre() + ".";
        nombreColumna += tablaAsociada.obtenerNombreColumnas()[indiceColumna];
        
        return nombreColumna;
    }
    
    /**
     * Asocia una tabla a la relacion.
     * @param tablaAsociada Tabla asociada a la relación.
     */
    public void asociarTabla( Tabla tablaAsociada ){
        this.tablaAsociada = tablaAsociada;
    }
}
