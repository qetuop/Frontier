/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;
import javafx.application.Application;
import static javafx.application.Platform.exit;
import javafx.stage.Stage;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import tiled.core.Map;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.TileSet;

/**
 *
 * @author brian
 */
public class AStar extends Application {

    
    
    public class Node {
        int x;
        int y;
        
        int type; 
                
        int f;
        int g;
        int h;
        
        Node parent;
        
        Node(){
            f = 0; // MAX value?
            g = 0;
            h = 0;
            parent = null;            
        }

        @Override
        public String toString() {
            return "Node{" +  "x=" + x + ", y=" + y + ", f=" + f + ", g=" + g + ", h=" + h + ", type=" + type + ", parent=" + parent + '}';
        }        
    } // class Node
    
    int DIAG_COST = 14;
    int ORTH_COST = 10;
    
    Set openSet         = new HashSet();
    Set closedSet       = new HashSet();
    Set blockedTypes    = new HashSet(); // tile set type NOT map type (1 off)
    
    Node startNode = null;
    Node endNode     = null;
    
    LinkedList<Node> finalPath = new LinkedList();
    
    public ArrayList<Sprite> spriteList;
    
    
    Map map;
    
    public AStar(Map map) {
        spriteList   = new ArrayList<>();
        
        this.map = map;
        TileSet tileSet = map.getTileSets().get(0);
        for (Tile tile : tileSet) {
            Properties prop = tile.getProperties();
            if (Boolean.parseBoolean((String) prop.get("blocked"))) {
                blockedTypes.add(tile.getId());
            }
        }                
    }
    
    public void updateBlocked() {
        for ( Sprite sprite : spriteList ){
            Properties prop = sprite.tile.getProperties();
            if (Boolean.parseBoolean((String) prop.get("blocked"))) {
                blockedTypes.add(sprite.tile.getId());
            }
        }
    }
    
    public LinkedList<Node> findPath(int startX, int startY, int endX, int endY){
        updateBlocked();
        
        // for look at only base layer
        TileLayer tileLayer = (TileLayer) map.getLayer(0);
        
        // TODO: can probably just create a mapping Tile:Node so i don't
        // have to re-create the entire map
        // or...? use the Properties field for a tile?  parent = ?
        // likely will have to read the map into my own structure
        int numCols = tileLayer.getWidth();
        int numRows = tileLayer.getHeight();
        
        Node[][] nodeArr = new Node[numRows][numCols]; // row major order
        System.out.println("numRows="+ numRows +", numCols=" + numCols);
        for (int col = 0; col < numCols; col++) {
            for (int row = 0; row < numCols; row++) {
               
                Tile tile = tileLayer.getTileAt(col, row);

                if (tile == null) {
                    continue;
                }
                
                nodeArr[row][col] = new Node();
                nodeArr[row][col].x = col;
                nodeArr[row][col].y = row;         
                nodeArr[row][col].type = tile.getId();  // tile set type NOT map type (1 off)
            } // width
        } // height
        
        //
        

        startNode = nodeArr[startY][startX];
        endNode = nodeArr[endY][endX];
        
        openSet.add(startNode);
        
        System.out.println("START NODE: " + startNode);
        System.out.println("Dest  NODE: " + endNode);             
        
        Node current = null;
        // while the openSet is not empty AND the end not reached
        while ( !openSet.isEmpty() && !closedSet.contains(endNode) ) {
            current = lowestF(openSet);
            System.out.println("lowest="+current);
            if ( current == endNode ){
                createPath(current);
                break;
            }

            openSet.remove(current);
            closedSet.add(current);
            
            // get valid surrounding nodes - not blocked, in closedSet, ...
            HashSet<Node> surround = getSurrounding(nodeArr, numCols, numRows, current.x, current.y);
            System.out.println("surrounding: " + surround.size());
            //surround.forEach(System.out::println);
            //surround.forEach(s -> calcCost(s));
            for ( Node node : surround ) { 
                node.parent = current;
                calcG(node);
                calcH(node);
                calcF(node);
                System.out.println(node);
                openSet.add(node);
            }
            
        } // while not done
        
        
        
        System.out.println("final path:");
        finalPath.forEach(System.out::println);
        
        return finalPath;

    } // findPath
    
    Node lowestF(Set<Node> nodes) {
        Node lowestNode = null;
        int lowestCost = Integer.MAX_VALUE;
        
        for ( Node node : nodes ) {
            if ( node.f < lowestCost ) {
                lowestNode = node;
                lowestCost = node.f;
            }
        }
        
        return lowestNode;
    }
    
    private int calcF(Node node) {       
        node.f = node.g + node.h;
        
        return node.f;
    }
    
    private int calcG(Node node) {
        
        // take the G cost of its parent, and then add 10 or 14 depending 
        // on whether it is diagonal or orthogonal (non-diagonal) from that parent square.
        if ( isOrth(node.parent, node) ) {
            node.g = node.parent.g + ORTH_COST;
        }
        else {
            node.g = node.parent.g + DIAG_COST;
        }
        
        return node.g;
    }
    
    // Manhattan method
    private int calcH(Node node) {
        int value = 0;
        
        int dist = Math.abs(node.x - endNode.x) + Math.abs(node.y - endNode.y);
        value = dist*ORTH_COST;
        
        node.h = value;
        
        return value;
    }
    
    private boolean isOrth(Node start, Node end){
        boolean rv = false;
        double[] a = {start.x, start.y};
        double[] b = {end.x, end.y};
        
        EuclideanDistance ed = new EuclideanDistance();
        double dist = ed.compute(a,b);
        
        if ( Double.compare(dist, 1.0) == 0 ) {
            rv = true;
        }
        
        return rv;
    }

    
    // (x-1, y-1)    (x, y-1)    (x+1, y-1)
    // (x-1, y)      (x, y)      (x+1, y)
    // (x-1, y+1)    (x, y+1)    (x+1, y+1)
    HashSet getSurrounding(Node[][] nodeArr, int width, int height, int x, int y){
        HashSet<Node> out = new HashSet();
        
        if ( ((x-1) >= 0    && (y-1) >= 0) && isValid(nodeArr[y-1][x-1]) ) out.add(nodeArr[y-1][x-1]);
        if ( (                 (y-1) >= 0) && isValid(nodeArr[y-1][x]) ) out.add(nodeArr[y-1][x]);
        if ( ((x+1) < width && (y-1) >= 0) && isValid(nodeArr[y-1][x+1]) ) out.add(nodeArr[y-1][x+1]);
        
        if ( ((x-1) >= 0         ) && isValid(nodeArr[y][x-1]) ) out.add(nodeArr[y][x-1]);
        if ( ((x+1) < width      ) && isValid(nodeArr[y][x+1]) ) out.add(nodeArr[y][x+1]);

        if ( ((x-1) >= 0    && (y+1) < height) && isValid(nodeArr[y+1][x-1]) ) out.add(nodeArr[y+1][x-1]);
        if ( (                 (y+1) < height) && isValid(nodeArr[y+1][x]) ) out.add(nodeArr[y+1][x]);
        if ( ((x+1) < width && (y+1) < height) && isValid(nodeArr[y+1][x+1]) ) out.add(nodeArr[y+1][x+1]);
        
        
        return out;
    }
    
    boolean isValid(Node node){
        boolean valid = true;
        
//        Predicate<Node> isClosed = x -> (closedSet.contains(x)); // has this node been process/rejected alreayd
//        Predicate<Node> isBockedType = x -> (blockedTypes.contains(x.type)); // is this background tile a blocked type ex: stone
//        Predicate<Sprite> isBlocked = x -> (spriteList.contains(x) && x.isBlocked()); // is there a sprite in this node and is it a blocked type ex: tree
//    
//        System.out.println("x,y="+node.x + "," + node.y);
//        System.out.println("closed=" +isClosed.test(node));
//        System.out.println("blockedType=" + isBockedType.test(node));
//        //System.out.println(isClosed.test(node));
//        
////        boolean valid = ( !closedSet.contains(node) && 
////                          !blockedTypes.contains(node.type) );
////        
//        long blocked = spriteList.stream()
//                .filter(sprite -> {
//            return ((sprite.positionX==node.x && sprite.positionY==node.y) && sprite.isBlocked());
//        }).count();
//        System.out.println("blocked="+blocked);

        valid = (!closedSet.contains(node)
                && !blockedTypes.contains(node.type));
//
//        if (closedSet.contains(node)) {
//            valid = false;
//        }
//        
//        if ( blockedTypes.contains(node)) {
//            valid = false;
//        }
//        
//        for ( Sprite sprite : spriteList ){
//            if ( (sprite.positionX==node.x && sprite.positionY==node.y) && 
//                  sprite.isBlocked() )
//                valid = false;                
//        }
        
        return valid;
    }
    
    void createPath(Node node){

        // don't add start node
        while ( node != null && node != startNode ){
            finalPath.addFirst(node); 
            node = node.parent;
        }
        
        // remove final node if blocked
        Node lastNode = finalPath.getLast();
        for ( Sprite sprite : spriteList ){
            if ( (sprite.positionX==lastNode.x && sprite.positionY==lastNode.y) && 
                  sprite.isBlocked() ) {
                finalPath.removeLast();
                break;
            }
        }        
    }
    
    @Override
    public void start(Stage theStage) {
       //String mapName = "astar_2x1.tmx";
       //String mapName = "astar_3x2.tmx";
       String mapName = "astar_8x6.tmx";
       
       final GameBoard gameBoard = new GameBoard("/home/brian/NetBeansProjects/JAVA/GameTest/resources/"+mapName);
       this.map = gameBoard.getMap();
       TileLayer tileLayer = (TileLayer) map.getLayer(1);

       // System.out.println("sarttile " + startTile.);
       findPath(2,2,6,2);
       exit();
    }
    
    public static void main(String[] args) {
        
        launch(args);
    }
}