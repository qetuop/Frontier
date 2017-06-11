/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import tiled.core.Tile;

/**
 *
 * @author brian
 */
public class Node {

    int x;
    int y;

    int type;
    
    boolean blocked;
    
    Tile tile;

    int f;
    int g;
    int h;

    Node parent;

    public Node() {
        f = 0; // MAX value?
        g = 0;
        h = 0;
        parent = null;
        blocked = false;
        tile = new Tile();
    }

    @Override
    public String toString() {
        return "Node{" + "x=" + x + ", y=" + y + 
               ", f=" + f + ", g=" + g + ", h=" + h + 
                ", type=" + type + ", blocked=" + blocked +
                ", parent=" + parent + '}';
    }
} // class Node
