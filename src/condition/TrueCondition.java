
package condition;

import java.util.HashMap;
import motor.Data;

/**
 *
 * @author Eddy
 */
public class TrueCondition extends Condition {
    
    /**
     * Constructor
     * @param node 
     */    
    public TrueCondition(Node node){
        super(node);
    }
    
    /**
     * @return Default string with no values
     */
    @Override
    public String[] getUsedColumns(){
        return new String[0];
    }
   
    /**
     * @param data
     * @return True by default
     */
    @Override
    public boolean evaluate( HashMap<String, Data> data ){
        return true;
    }
    
}
