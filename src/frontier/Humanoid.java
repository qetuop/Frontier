/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import java.util.LinkedList;
import tiled.core.Tile;

/**
 *
 * @author brian
 */
public class Humanoid extends Sprite {
    LinkedList<Node> path;
    
    public Humanoid(Tile tile, int startX, int startY) {
        super(tile, startX, startY);
        path = new LinkedList<>();
    }
    
    public void moveChar() {
        if ( path.isEmpty() ) {
            return;
        }
        
        Node node = path.pop();
        if ( node != null ) {
            positionX = node.x;
            positionY = node.y;
        }
    }
    
}
