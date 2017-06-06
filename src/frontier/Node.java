/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

/**
 *
 * @author brian
 */
public class Node {

    int x;
    int y;

    int type;

    int f;
    int g;
    int h;

    Node parent;

    public Node() {
        f = 0; // MAX value?
        g = 0;
        h = 0;
        parent = null;
    }

    @Override
    public String toString() {
        return "Node{" + "x=" + x + ", y=" + y + ", f=" + f + ", g=" + g + ", h=" + h + ", type=" + type + ", parent=" + parent + '}';
    }
} // class Node
