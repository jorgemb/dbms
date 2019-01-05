
package JSON;

/**
 *
 * @author Eddy
 */
public class Tabla {
    
    private boolean Error;
    private String Tipo;
    private Esquema Esquema;
    private Object[][] Datos;

    /**
     * @return el error
     */
    public boolean esError() {
        return Error;
    }

    /**
     * @return el tipo
     */
    public String obtenerTipo() {
        return Tipo;
    }

    /**
     * @return los datos
     */
    public Object[][] obtenerDatos() {
        return Datos;
    }

    /**
     * @return el Esquema
     */
    public Esquema obtenerEsquema() {
        return Esquema;
    }
    
    public class Esquema {
        
        private String[] Nombres;
        private String[] Tipos;

        /**
         * @return los Nombres
         */
        public String[] ObtenerNombres() {
            return Nombres;
        }

        /**
         * @return los Tipos
         */
        public String[] ObtenerTipos() {
            return Tipos;
        }
        
        
        
    }

}

