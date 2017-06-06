/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier.command;

import frontier.Node;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author brian
 */
public class CmdHarvest extends Command {
    Set<Node> nodes;
    
    public CmdHarvest() {
        name = "Harvest";
        nodes = new HashSet<>();
    }
    
    public void setNodes(Set<Node> nodesIn ) {
        this.nodes = nodesIn;
    }
    
   public Set<Node> getNodes() {
       return nodes;
   }

            
            
} // CmdHarvest
