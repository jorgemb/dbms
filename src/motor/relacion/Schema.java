package motor.relacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import motor.DataType;

/**
 *
 * @author Jorge
 */
public class Schema implements java.io.Serializable {
    private DataType[] tiposEsquema;

    /**
     * Constructor con arreglo de tipos.
     * @param tiposEsquema 
     */
    public Schema(DataType[] tiposEsquema) {
        this.tiposEsquema = tiposEsquema;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Arrays.deepHashCode(this.tiposEsquema);
        return hash;
    }

    /**
     * Permite comparar si dos esquemas son iguales.
     * @param obj
     * @return True si los objetos representan al mismo esquema.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Schema other = (Schema) obj;
        
        // Revisa que sean del mismo tamaño
        if( this.tiposEsquema.length != other.tiposEsquema.length )
            return false;
        
        // Verifica cada uno de los elementos
        for (int i = 0; i < tiposEsquema.length; i++) {
            if( this.tiposEsquema[i] != other.tiposEsquema[i] ){
                // Son comparables si alguno de los dos es null 
                if( this.tiposEsquema[i] != DataType.NULL &&
                        other.tiposEsquema[i] != DataType.NULL )
                    return false;
            }
        }
        
//        if (!Arrays.deepEquals(this.tiposEsquema, other.tiposEsquema)) {
//            return false;
//        }
        return true;
    }

    /**
     * Representación string del esquema.
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder stringEsquema = new StringBuilder();
        for (DataType tipoDato : tiposEsquema) {
            // Es el primer dato
            if( stringEsquema.length() == 0 )
                stringEsquema.append(tipoDato.name());
            else
                stringEsquema.append(", ").append(tipoDato.name());
        }
        
        return '{' + stringEsquema.toString() + '}';
    }
    
    /**
     * Devuelve los tipos asociados al esquema.
     * @return TipoDato[] con los datos.
     */
    public DataType[] getTypes(){
        return (DataType[])tiposEsquema.clone();
    }
    
    /**
     * Devuelve el tamaño del esquema.
     * @return Cantidad de tipos.
     */
    public int getSize(){
        return tiposEsquema.length;
    }
    
    /**
     * Devuelve un esquema con la combinación de los dos anteriores.
     * @param a Primer esquema
     * @param b Segundo esquema
     * @return Nuevo esquema con la combinación.
     */
    public static Schema combinarEsquemas( Schema a, Schema b ){
        ArrayList<DataType> tiposEsquema = new ArrayList<>(Arrays.asList(a.getTypes()));
        tiposEsquema.addAll(Arrays.asList(b.getTypes()));
        
        return new Schema( tiposEsquema.toArray(new DataType[0] ) );
    }
    
    /**
     * Crea un nuevo esquema con el tipo dado puesto al final.
     * @param esquema Esquema a partir del que se crea el nuevo esquema
     * @param tiposAgregar Tipos a agregar al final del esquema.
     * @return Nuevo esquema con los tipos dados.
     */
    public static Schema addType( Schema esquema, DataType ... tiposAgregar ){
        // Obtiene todos los tipos referenciados 
        Schema esquemaTemporal = new Schema(tiposAgregar);
        return combinarEsquemas( esquema, esquemaTemporal );
    }
    
    /**
     * Crea un nuevo esquema con los indices eliminados.
     * @param esquema Esquema a partir del que se crea el nuevo esquema.
     * @param indicesTiposEliminar Indices de los tipos a eliminar.
     * @return Nuevo esquema con los tipos eliminados.
     */
    public static Schema deleteType( Schema esquema, Integer ... indicesTiposEliminar ){
        ArrayList<Integer> listaIndicesEliminar = new ArrayList<>( Arrays.asList(indicesTiposEliminar) );
        ArrayList<DataType> tiposEsquema = new ArrayList<>(Arrays.asList(esquema.getTypes()));
        
        Iterator<DataType> iterador = tiposEsquema.iterator();
        int indiceActual = 0;
        while( iterador.hasNext() ){
            iterador.next();
            
            // Verifica si es un índice a eliminar
            if( listaIndicesEliminar.contains(indiceActual) )
                iterador.remove();
            
            ++indiceActual;
        }
        
        return new Schema( tiposEsquema.toArray(new DataType[0] ) );
    }
}
