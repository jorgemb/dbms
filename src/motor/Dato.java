package motor;

import excepciones.ExcepcionTabla;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import static motor.TipoDato.CHAR;
import static motor.TipoDato.DATE;
import static motor.TipoDato.FLOAT;
import static motor.TipoDato.INT;
import static motor.TipoDato.NULL;

/**
 *
 * @author Jorge
 */
public class Dato implements java.io.Serializable, Comparable<Dato>{
    private Object valor;
    private TipoDato tipo;
    
    /** Define el formato de las fechas */
    private static SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
    static{
        formatoFecha.setLenient(false);
    }
    
    /**
     * Constructo de la clase
     * @param valor Valor almacenado
     */
    public Dato(Object valor) {
        this.valor = valor;
        
        // Verifica el caso especial de double vs float
        if( valor instanceof Double )
            this.valor = ((Number)valor).floatValue();
        
        // Verifica errores
        this.tipo = obtenerTipo();
    }

    /**
     * Constructor que permite forzar un tipo de dato.
     * @param valor
     * @param tipoForzado 
     */
    Dato(Object valor, TipoDato tipoForzado) {
        this.valor = valor;
        this.tipo = tipoForzado;
    }

    /**
     * 
     * @return Valor almacenado de Dato
     */
    public Object obtenerValor() {
        return valor;
    }

    
    /**
     * @return Obtiene el tipo del dato. Si el tipo de datos es inválido
     * entonces devuelve null.
     */
    public final TipoDato obtenerTipo() throws ExcepcionTabla {
        // Verifica si ya se tiene el tipo del dato guardado
        if( tipo != null )
            return tipo;
        
        // Determina el tipo del dato
        if( valor == null )
            return TipoDato.NULL;
        
        if( valor instanceof Integer )
            return TipoDato.INT;
        else if( valor instanceof Float || valor instanceof Double )
            return TipoDato.FLOAT;
        else if( valor instanceof String ){
            try {
                String szValor = (String)valor;
                int guiones = szValor.length() - szValor.replace("-", "").length();
                if( guiones == 2 ){
                    formatoFecha.parse( (String) valor);
                    return TipoDato.DATE;
                } else {
                    return TipoDato.CHAR;
                }
            } catch (ParseException ex) {
                throw new ExcepcionTabla(ExcepcionTabla.TipoError.DatoInvalido,
                        String.format("La fecha %s no posee un formato válido (%s).", 
                        valor, ex.getMessage()) );
            }
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Dato{" + "valor=" + valor + " tipo=" + obtenerTipo().name() + '}';
    }
    
    /**
     * Devuelve la representación del dato para el usuario.
     * @return 
     */
    public String representacion(){
        switch (obtenerTipo()) {
            case INT:
                return Integer.toString((Integer)valor);
            case FLOAT:
                return Float.toString((Float)valor);
            case CHAR:
                return String.format("\'%s\'", valor);
            case DATE:
                return String.format("f\'%s\'", valor);
            case NULL:
                return "NULL";
            default:
                throw new AssertionError();
        }
    }
    
    
    /**
     * Devuelve el valor dado por defecto de un tipo.
     * @param tipo Tipo de dato.
     * @return 
     */
    public static Dato obtenerValorPorDefecto( TipoDato tipo ){
        switch (tipo) {
            case INT:
                return new Dato( new Integer(0), INT );
            case FLOAT:
                return new Dato( new Float(0.0f), FLOAT );
            case CHAR:
                return new Dato( "", CHAR );
            case DATE:
                return new Dato( "1-1-1970", DATE );  // FECHA POR DEFECTO
            case NULL:
                return new Dato( null, NULL );
            default:
                throw new AssertionError();
        }
    }
    
    /**
     * Devuelve la fecha dada para un dato.
     * @param dato
     * @return 
     */
    public static Date obtenerFechaDato( Dato dato ){
        if( dato.obtenerTipo() != TipoDato.DATE )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.DatoInvalido, "No se puede convertir el dato a fecha.");
        try {
            Date d = formatoFecha.parse((String)dato.valor);
            return d;
        } catch (ParseException ex) {
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.DatoInvalido, "No se puede convertir el dato a fecha.");
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.valor);
        return hash;
    }

    /**
     * Devuelve true si dos datos son del mismo tipo
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dato other = (Dato) obj;
        
        // Verificar null
        if( this.valor == null && other.valor == null )
            return true;
        
        if (!Objects.equals(this.valor, other.valor)) {
            return false;
        }
        return true;
    }

    /**
     * Permite comparar dos datos.
     * @param t
     * @return 
     */
    @Override
    public int compareTo(Dato t) {
        TipoDato tipoIzq = this.obtenerTipo(), tipoDer = t.obtenerTipo();
        
        // Verifica el caso null
        if( tipoIzq == NULL || tipoDer == NULL ){
            if( tipoIzq == tipoDer ) return 0;
            else if( tipoIzq == NULL ) return -1;
            else return 1;
        }
        
        if( tipoIzq != tipoDer )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.DatoInvalido, 
                    String.format("No se pueden comparar datos con tipo %s y %s.", tipoIzq.name(), tipoDer.name()));
        
        switch (tipoIzq) {
            case INT:
                return Integer.compare((Integer)this.valor, (Integer)t.valor);
            case FLOAT:
                return Float.compare((Float)this.valor, (Float)t.valor);
            case CHAR:
                return ((String)this.valor).compareTo((String)t.valor);
            case DATE:{
                try {
                    Date izq = formatoFecha.parse((String)this.valor);
                    Date der = formatoFecha.parse((String)t.valor);
                    return izq.compareTo(der);
                } catch (ParseException ex) {
                    throw new ExcepcionTabla(ExcepcionTabla.TipoError.DatoInvalido, "No se puede parsear la fecha: " + ex.getMessage());
                }  
            }
            default:
                throw new AssertionError();
        }
    }
    
    /**
     * Devuelve un dato nuevo con el valor del actual convertido al nuevo
     * tipo.
     * @param tipoConvertir Tipo al cual se desea convertir.
     * @return Nuevo Dato
     * @throws ExcepcionTabla Si la conversión no es posible.
     */
    public Dato convertirA( TipoDato tipoConvertir ) throws ExcepcionTabla{
        // Verifica si tienen el mismo tipo
        if( this.obtenerTipo() == tipoConvertir )
            return new Dato( this.valor );
        
        // Verifica si el destino es NULL
        if( tipoConvertir == NULL )
            return new Dato(null);
        
        // Verifica si el actual es NULL
        if( this.obtenerTipo() == NULL )
            throw new ExcepcionTabla(ExcepcionTabla.TipoError.DatoInvalido, 
                    "No se puede convertir el dato de NULL a " + tipoConvertir.name());
        
        // Verifica si la conversion es a char
        if( tipoConvertir == CHAR )
            return new Dato( this.valor.toString(), CHAR );
        
        // Conversión de CHAR a otro
        try {
            if (this.obtenerTipo() == CHAR) {
                switch (tipoConvertir) {
                    case INT:
                        return new Dato(Integer.parseInt((String) this.valor));
                    case FLOAT:
                        return new Dato(Float.parseFloat((String) this.valor));
                    default:
                        throw new ExcepcionTabla(ExcepcionTabla.TipoError.DatoInvalido, 
                                "No se puede convertur el dato de CHAR a " + tipoConvertir.name() );
                }
            }
        } catch (ExcepcionTabla | NumberFormatException ex) {
            throw new ExcepcionTabla( ExcepcionTabla.TipoError.DatoInvalido, 
                    String.format("No se puede convertir el dato de CHAR a %s. (%s)", tipoConvertir.name(), ex.getMessage()));
        }
        
        // Convertir de INT a otro
        if( this.obtenerTipo() == INT && tipoConvertir == FLOAT ){
            return new Dato ( ((Number)this.valor).floatValue(), FLOAT );
        }
        
        // Convertir FLOAT a otro
        if( this.obtenerTipo() == FLOAT && tipoConvertir == INT ){
            return new Dato( ((Number)this.valor).intValue(), INT );
        }
        
        // CONVERSIÓN NO SOPORTADA
        throw new ExcepcionTabla(ExcepcionTabla.TipoError.DatoInvalido,
                String.format("No se puede convertir el dato de %s a %s.", 
                this.obtenerTipo().name(), tipoConvertir.name()));
    }
}
