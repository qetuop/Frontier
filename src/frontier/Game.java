/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import tiled.core.Map;
import tiled.core.MapObject;
import tiled.core.Tile;
import tiled.core.TileSet;

/**
 *
 * @author brian
 */
public class Game {
    public GameBoard gameBoard; // Tiled objects
    public SpriteMap spriteMap;
    public HBox mainPane;
    GraphicsContext gc;
    AStar astar;
    InfoWindow infoWindow;
    Canvas canvas;
    
    // internal board object?
    
    ///////////////
    // Sprite Lists
    
    // Item List
    ArrayList<Resource> resources;
    
    // Humanoid List
    ArrayList<Humanoid> humanoids;
    
    // Beast List
    ///////////////
    
    // Cursor
    Cursor cursor;
    
    public Game() {
        resources = new ArrayList<>();
        humanoids = new ArrayList<>();
        
        mainPane = new HBox();
    }
    
    public void createGameBoard(String mapName) {
        gameBoard = new GameBoard("/home/brian/NetBeansProjects/JAVA/Frontier/resources/"+mapName, this);
        cursor = new Cursor(this); // TODO: should move out?  gameboard?
        
        ////////////////////
        Map gameMap = gameBoard.getMap();
        Rectangle bounds = gameMap.getBounds();

        canvas = new Canvas(bounds.width * gameMap.getTileWidth(),
                                   bounds.height * gameMap.getTileHeightMax());
        gc = canvas.getGraphicsContext2D();
        
        HBox hbox = new HBox();
        hbox.getChildren().add(canvas);
        
               
        
        spriteMap = new SpriteMap(gameMap, humanoids, resources);
        //spriteMap.print();
        
        astar = new AStar(gameMap, spriteMap);
        
        infoWindow = new InfoWindow(spriteMap);
        infoWindow.gameMap = gameMap;
        hbox.getChildren().add(infoWindow.mainHBox);
        
        mainPane.getChildren().add(hbox);         
        mainPane.setPrefWidth(600);
    }
    
    public Humanoid createHumanoid(int startX, int startY) {
        Humanoid humanoid = null;
        
        Resource resource = getResource("spawn");
        if ( resource != null ) {
            startX = resource.positionX;
            startY  = resource.positionY;
        }
        
        TileSet tileSet = gameBoard.getMap().getTileSets().get(0);
        Tile tile = tileSet.getTile(132);
        humanoid = new Humanoid(tile,startX,startY);
        spriteMap.map.get(startY).get(startX).add(humanoid);
        
        // need for the update function
        humanoids.add(humanoid);
        

        //gameBoard.humanoidList.add(humanoid);
        //gameBoard.spriteList.add(humanoid);
        
        return humanoid;
    }
    
    public Resource createResource(int startX, int startY, String name, int id) {
        Resource resource = getResource(name);
        
        TileSet tileSet = gameBoard.getMap().getTileSets().get(0);
        Tile tile = tileSet.getTile(id);
        
        //resources.add(resource);
        
        // Move these into Game class
        //gameBoard.resourceList.add(resource);
        //gameBoard.spriteList.add(resource);
        
        return resource;
    }
    
    public void update() {
        for (Humanoid humanoid : humanoids) {
            humanoid.moveChar();
        }
    }
    
    public void render() {        
        gameBoard.render(spriteMap,gc);
        cursor.render(gc);
    }

    void TMP_HACK() {
        //////////// PLAYER //////////
        Humanoid player = createHumanoid(0,0);
        //astar.spriteList.add(player);
        
        //////////// TREE //////////
        Resource resourceTree = createResource(0, 0, "tree", 1164);
        //astar.spriteList.add(resourceTree);
               
        //////////// CHEST //////////
        Resource chest = createResource(0, 0, "storage", 2925);
        //astar.spriteList.add(chest);
                
        LinkedList<Node> path = astar.findPath( player.positionX, player.positionY, 
                                                resourceTree.positionX, resourceTree.positionY);
        player.path = path;
    }
    
    public Resource getResource(String name){
        Resource resource = null;
        for ( Resource r: resources ){
            if ( r.getProp("name").compareTo(name) == 0 ) {
                resource = r;
                break;
            }
        }
        return resource;
    }
}
