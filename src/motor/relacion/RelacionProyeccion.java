package motor.relacion;

import excepciones.ExcepcionTabla;
import java.util.ArrayList;
import java.util.Iterator;
import motor.Dato;
import motor.TipoDato;

/**
 * Representa una proyección (SELECT) de la relación contenida.
 * @author Jorge
 */
public class RelacionProyeccion extends Relacion{
    /** Relacion contenida */
    private Relacion relacionContenida;
    private ArrayList<Integer> indicesProyeccion;

    /**
     * Construye la relación de proyeccion a partir de los nombres calificados
     * dados.
     * @param nombresProyeccion Nombres calificados a tomar en cuenta.
     */
    public RelacionProyeccion( Relacion relacionContenida, String ... nombresProyeccion ) throws ExcepcionTabla {
        this.relacionContenida = relacionContenida;
        this.indicesProyeccion = new ArrayList<>();
        
        // Obtiene los nombres calificados de todas las columnas
        ArrayList<String> nombresCalificados = new ArrayList<>();
        for (int i = 0; i < relacionContenida.obtenerEsquema().obtenerTamaño(); i++) {
            nombresCalificados.add( relacionContenida.obtenerNombreCalificado(i) );
        }
        
        // Obtiene los indices de cada uno de los nombres.
        for (String nombreSeleccionar : nombresProyeccion) {
            int indice = nombresCalificados.indexOf( nombreSeleccionar );
            if( indice != -1 )
                indicesProyeccion.add(indice);
            else
                throw new ExcepcionTabla(ExcepcionTabla.TipoError.ColumnaNoExiste, nombreSeleccionar);
        }
    }
    
    /**
     * @return Devuelve el esquema de la relacion
     */
    @Override
    public Esquema obtenerEsquema() {
        TipoDato[] esquemaCompleto = relacionContenida.obtenerEsquema().obtenerTipos();
        
        ArrayList<TipoDato> esquemaActual = new ArrayList<>();
        for (Integer indice : indicesProyeccion) {
            esquemaActual.add(esquemaCompleto[indice]);
        }
        
        return new Esquema( esquemaActual.toArray( new TipoDato[0] ) );
    }

    /**
     * Devuelve la cantidad de filas en la relación.
     * @return Cantidad de filas.
     */
    @Override
    public int obtenerCantidadFilas() {
        return relacionContenida.obtenerCantidadFilas();
    }

    /**
     * Obtiene el nombre calificado de una columna en particular.
     * @param indiceColumna Indice de la columna a verificar.
     * @return String con el nombre calificado.
     * @throws ExcepcionTabla 
     */
    @Override
    public String obtenerNombreCalificado(int indiceColumna) throws ExcepcionTabla {
        return relacionContenida.obtenerNombreCalificado( indicesProyeccion.get(indiceColumna) );
    }

    /**
     * @return Devuelve un iterador para la relacion.
     */
    @Override
    public Iterator<Fila> iterator() {
        return new Iterador();
    }
    
    
    /**
     * Clase para iterar a través de la relación.
     */
    public class Iterador implements Iterator<Fila>{
        private Iterator<Fila> iteradorRelacion;

        /**
         * Constructor de la clase.
         */
        public Iterador() {
            this.iteradorRelacion = relacionContenida.iterator();
        }
        
        @Override
        public boolean hasNext() {
            return iteradorRelacion.hasNext();
        }

        @Override
        public Fila next() {
            Fila filaCompleta = iteradorRelacion.next();
            Dato[] datosFila = filaCompleta.obtenerDatos();
            
            // Proyecta la fila
            Dato[] datosProyectados = new Dato[indicesProyeccion.size()];
            for (int i = 0; i < indicesProyeccion.size(); i++) {
                datosProyectados[i] = datosFila[indicesProyeccion.get(i)];
            }
            
//            ArrayList<Dato> datosProyectados = new ArrayList<>();
//            for (Integer indice : indicesProyeccion) {
//                datosProyectados.add( datosFila[indice] );
//            }
            
            return new Fila( datosProyectados );
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("No se puede eliminar datos de una relación.");
        }
        
    }
}
