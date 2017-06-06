/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier.command;

import frontier.Node;

/**
 *
 * @author brian
 */
public class CmdMove extends Command {
    Node node;
    
    public CmdMove() {
        name = "Move";
        node = new Node();        
    }
}
