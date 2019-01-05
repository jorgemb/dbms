package motor.relacion;

import exceptions.TableException;
import java.util.ArrayList;

/**
 *
 * @author Jorge
 */
public abstract class Relation implements Iterable<Row>{

    /**
     * @return Esquema de la relación.
     */
    public abstract Schema getSchema();
    
    /**
     * @return Devuelve la cantidad de filas.
     */
    public abstract int getRowNumber();
    
    /**
     * Devuelve el nombre calificado de la columna con el índice dado.
     * @param indiceColumna Indice de la columna.
     * @return String con el nombre calificado
     */
    public abstract String getQualifiedName( int indiceColumna ) throws TableException;
    
    /**
     * @return Devuelve un arreglo con todos los nombres calificados.
     */
    public ArrayList<String> obtenerTodosNombreCalificados(){
        ArrayList<String> retorno = new ArrayList<>();
        for (int i = 0; i < getSchema().getSize(); i++) {
            retorno.add(getQualifiedName(i) );
        }
        
        return retorno;
    }
}
