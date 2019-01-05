
package condition;

import java.util.HashMap;
import java.util.Set;
import motor.Data;

/**
 * Node interface
 * @author eddycastro
 */
public interface Node {
    /**
     * Evaluates the node based on the data map
     * @param mapaDatos Mapa de datos.
     * @return 
     */
    public Object evaluate(HashMap<String, Data> mapaDatos);

    /**
     * Adds the used names in the nodes from the current branch.
     * @param names Place where the new names are added.
     */
    public void addUsedNames( Set<String> names );
    
    
    /**
     * Changes the table name in the qualified names of the columns.
     * @param previousName Previous name
     * @param newName New name
     */
    public void changeTableName( String previousName, String newName );
}
