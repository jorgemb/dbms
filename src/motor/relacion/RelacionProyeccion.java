package motor.relacion;

import exceptions.TableException;
import java.util.ArrayList;
import java.util.Iterator;
import motor.Data;
import motor.DataType;

/**
 * Representa una proyección (SELECT) de la relación contenida.
 * @author Jorge
 */
public class RelacionProyeccion extends Relation{
    /** Relacion contenida */
    private Relation relacionContenida;
    private ArrayList<Integer> indicesProyeccion;

    /**
     * Construye la relación de proyeccion a partir de los nombres calificados
     * dados.
     * @param nombresProyeccion Nombres calificados a tomar en cuenta.
     */
    public RelacionProyeccion( Relation relacionContenida, String ... nombresProyeccion ) throws TableException {
        this.relacionContenida = relacionContenida;
        this.indicesProyeccion = new ArrayList<>();
        
        // Obtiene los nombres calificados de todas las columnas
        ArrayList<String> nombresCalificados = new ArrayList<>();
        for (int i = 0; i < relacionContenida.getSchema().getSize(); i++) {
            nombresCalificados.add( relacionContenida.getQualifiedName(i) );
        }
        
        // Obtiene los indices de cada uno de los nombres.
        for (String nombreSeleccionar : nombresProyeccion) {
            int indice = nombresCalificados.indexOf( nombreSeleccionar );
            if( indice != -1 )
                indicesProyeccion.add(indice);
            else
                throw new TableException(TableException.ErrorType.ColumnDoesNotExist, nombreSeleccionar);
        }
    }
    
    /**
     * @return Devuelve el esquema de la relacion
     */
    @Override
    public Schema getSchema() {
        DataType[] esquemaCompleto = relacionContenida.getSchema().getTypes();
        
        ArrayList<DataType> esquemaActual = new ArrayList<>();
        for (Integer indice : indicesProyeccion) {
            esquemaActual.add(esquemaCompleto[indice]);
        }
        
        return new Schema( esquemaActual.toArray(new DataType[0] ) );
    }

    /**
     * Devuelve la cantidad de filas en la relación.
     * @return Cantidad de filas.
     */
    @Override
    public int getRowNumber() {
        return relacionContenida.getRowNumber();
    }

    /**
     * Obtiene el nombre calificado de una columna en particular.
     * @param indiceColumna Indice de la columna a verificar.
     * @return String con el nombre calificado.
     * @throws TableException 
     */
    @Override
    public String getQualifiedName(int indiceColumna) throws TableException {
        return relacionContenida.getQualifiedName( indicesProyeccion.get(indiceColumna) );
    }

    /**
     * @return Devuelve un iterador para la relacion.
     */
    @Override
    public Iterator<Row> iterator() {
        return new Iterador();
    }
    
    
    /**
     * Clase para iterar a través de la relación.
     */
    public class Iterador implements Iterator<Row>{
        private Iterator<Row> iteradorRelacion;

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
        public Row next() {
            Row filaCompleta = iteradorRelacion.next();
            Data[] datosFila = filaCompleta.getData();
            
            // Proyecta la fila
            Data[] datosProyectados = new Data[indicesProyeccion.size()];
            for (int i = 0; i < indicesProyeccion.size(); i++) {
                datosProyectados[i] = datosFila[indicesProyeccion.get(i)];
            }
            
//            ArrayList<Dato> datosProyectados = new ArrayList<>();
//            for (Integer indice : indicesProyeccion) {
//                datosProyectados.add( datosFila[indice] );
//            }
            
            return new Row( datosProyectados );
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("No se puede eliminar datos de una relación.");
        }
        
    }
}
