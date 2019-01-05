package motor.relacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import motor.TipoDato;

/**
 *
 * @author Jorge
 */
public class Esquema implements java.io.Serializable {
    private TipoDato[] tiposEsquema;

    /**
     * Constructor con arreglo de tipos.
     * @param tiposEsquema 
     */
    public Esquema(TipoDato[] tiposEsquema) {
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
        final Esquema other = (Esquema) obj;
        
        // Revisa que sean del mismo tamaño
        if( this.tiposEsquema.length != other.tiposEsquema.length )
            return false;
        
        // Verifica cada uno de los elementos
        for (int i = 0; i < tiposEsquema.length; i++) {
            if( this.tiposEsquema[i] != other.tiposEsquema[i] ){
                // Son comparables si alguno de los dos es null 
                if( this.tiposEsquema[i] != TipoDato.NULL &&
                        other.tiposEsquema[i] != TipoDato.NULL )
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
        for (TipoDato tipoDato : tiposEsquema) {
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
    public TipoDato[] obtenerTipos(){
        return (TipoDato[])tiposEsquema.clone();
    }
    
    /**
     * Devuelve el tamaño del esquema.
     * @return Cantidad de tipos.
     */
    public int obtenerTamaño(){
        return tiposEsquema.length;
    }
    
    /**
     * Devuelve un esquema con la combinación de los dos anteriores.
     * @param a Primer esquema
     * @param b Segundo esquema
     * @return Nuevo esquema con la combinación.
     */
    public static Esquema combinarEsquemas( Esquema a, Esquema b ){
        ArrayList<TipoDato> tiposEsquema = new ArrayList<>(Arrays.asList(a.obtenerTipos()));
        tiposEsquema.addAll(Arrays.asList(b.obtenerTipos()));
        
        return new Esquema( tiposEsquema.toArray( new TipoDato[0] ) );
    }
    
    /**
     * Crea un nuevo esquema con el tipo dado puesto al final.
     * @param esquema Esquema a partir del que se crea el nuevo esquema
     * @param tiposAgregar Tipos a agregar al final del esquema.
     * @return Nuevo esquema con los tipos dados.
     */
    public static Esquema agregarTipo( Esquema esquema, TipoDato ... tiposAgregar ){
        // Obtiene todos los tipos referenciados 
        Esquema esquemaTemporal = new Esquema(tiposAgregar);
        return combinarEsquemas( esquema, esquemaTemporal );
    }
    
    /**
     * Crea un nuevo esquema con los indices eliminados.
     * @param esquema Esquema a partir del que se crea el nuevo esquema.
     * @param indicesTiposEliminar Indices de los tipos a eliminar.
     * @return Nuevo esquema con los tipos eliminados.
     */
    public static Esquema eliminarTipos( Esquema esquema, Integer ... indicesTiposEliminar ){
        ArrayList<Integer> listaIndicesEliminar = new ArrayList<>( Arrays.asList(indicesTiposEliminar) );
        ArrayList<TipoDato> tiposEsquema = new ArrayList<>(Arrays.asList(esquema.obtenerTipos()));
        
        Iterator<TipoDato> iterador = tiposEsquema.iterator();
        int indiceActual = 0;
        while( iterador.hasNext() ){
            iterador.next();
            
            // Verifica si es un índice a eliminar
            if( listaIndicesEliminar.contains(indiceActual) )
                iterador.remove();
            
            ++indiceActual;
        }
        
        return new Esquema( tiposEsquema.toArray( new TipoDato[0] ) );
    }
}
