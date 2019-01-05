
package condition;

import java.util.HashMap;
import java.util.Set;
import motor.Data;

/**
 * Represents a node with a literal.
 * @author eddycastro
 */
public class LiteralNode implements Node, java.io.Serializable{

    private Object value;
    
    private LiteralType type;
    public enum LiteralType{
        INT,
        FLOAT,
        STRING,
        NULL,
    }

    /**
     * Constructor
     * @param value 
     * @param type
     */
    public LiteralNode(Object value, LiteralType type) {
        this.value = value;
        this.type = type;
    }
    
    public LiteralType getType(){
        return this.type;
    }
    
    @Override
    public Object evaluate(HashMap<String, Data> dataMap) {
        return value;
    }

    @Override
    public void addUsedNames(Set<String> names) {
    }

    /**
     * @return String representation
     */
    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public void changeTableName(String previousName, String newName) {
        return;
    }
    
    
}
