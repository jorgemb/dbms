package motor.relacion;

import exceptions.TableException;
import java.util.ArrayList;
import java.util.Iterator;
import motor.Table;

/**
 *
 * @author Jorge
 */
public class LeafRelation extends Relation implements java.io.Serializable {
    private ArrayList<Row> datosRelacion;
    private Schema esquemaRelacion;
    private transient Table tablaAsociada;

    /**
     * Constructor con esquema.
     * @param esquemaRelacion 
     */
    public LeafRelation(Schema esquemaRelacion) {
        this.esquemaRelacion = esquemaRelacion;
        this.datosRelacion = new ArrayList<>();
    }
    
    /**
     * Agrega una fila a la relación. La fila debe tener el mismo esquema que
     * la relación.
     * @param filaAgregar Fila a agregar.
     * @return True si la operación fue satisfactoria.
     */
    public void addRow( Row filaAgregar ) throws TableException{
        // Verifica el esquema de la fila
        if( getSchema().getSize() != filaAgregar.obtenerEsquema().getSize() ){
            throw new TableException(TableException.ErrorType.EsquemaNoCoincide,
            String.format("No se puede agregar la fila con esquema %s a la relación con esquema %s.",
            filaAgregar.obtenerEsquema(), getSchema()));
        }
        
        // Trata de realizar la conversion
        if( !getSchema().equals(filaAgregar.obtenerEsquema()) ){
            // Trata de convertir la fila
            filaAgregar = Row.cambiarEsquema(getSchema(), filaAgregar);
        }
        
        datosRelacion.add(filaAgregar);
    }
    
    /**
     * Permite iterar por la relación.
     * @return Iterador para recorrer la lista de filas.
     */
    @Override
    public Iterator<Row> iterator() {
        return datosRelacion.iterator();
    }
    
    /**
     * Devuelve el esquema de la relación.
     * @return 
     */
    @Override
    public Schema getSchema() {
        return this.esquemaRelacion;
    }

    /**
     * Devuelve la cantidad filas.
     * @return 
     */
    @Override
    public int getRowNumber() {
        return this.datosRelacion.size();
    }

    /**
     * Obtiene el nombre calificado de la columna dada.
     * @param indiceColumna Indice de la columna
     * @return Nombre calificado
     */
    @Override
    public String getQualifiedName(int indiceColumna) throws TableException{
        // Verifica el índice
        if( indiceColumna < 0 || indiceColumna >= this.esquemaRelacion.getSize() )
            throw new TableException(TableException.ErrorType.ColumnDoesNotExist, "IDX" + indiceColumna);
        
        // Obtiene el nombre calificado
        String nombreColumna = tablaAsociada.getTableName() + ".";
        nombreColumna += tablaAsociada.getColumnNames()[indiceColumna];
        
        return nombreColumna;
    }
    
    /**
     * Asocia una tabla a la relacion.
     * @param tablaAsociada Tabla asociada a la relación.
     */
    public void associateTable( Table tablaAsociada ){
        this.tablaAsociada = tablaAsociada;
    }
}
