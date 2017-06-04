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
        gameBoard = new GameBoard("/home/brian/NetBeansProjects/JAVA/Frontier/resources/"+mapName);
        cursor = new Cursor(this); // TODO: should move out, but need GameBoard created first
        
        ////////////////////
        Map gameMap = gameBoard.getMap();
        Rectangle bounds = gameMap.getBounds();

        Canvas canvas = new Canvas(bounds.width * gameMap.getTileWidth(),
                                   bounds.height * gameMap.getTileHeightMax());
        gc = canvas.getGraphicsContext2D();
        
        HBox hbox = new HBox();
        hbox.getChildren().add(canvas);
        
        infoWindow = new InfoWindow();
        infoWindow.gameMap = gameMap;
        hbox.getChildren().add(infoWindow.vBox);
        
        mainPane.getChildren().add(hbox);
                
        astar = new AStar(gameMap);
        spriteMap = new SpriteMap(gameMap);
        spriteMap.print();
    }
    
    public Humanoid createHumanoid(int startX, int startY) {
        Humanoid humanoid = null;
        
        /*
        MapObject mapObject = gameBoard.getObject("spawn");
        if ( mapObject != null ) {
            startX = (int) (mapObject.getX()/mapObject.getWidth()); // --> Tile coord ex: (2,3)
            startY = (int) (mapObject.getY()/mapObject.getHeight());
            
            //double startXd = Math.max(0, mapObject.getX() - mapObject.getWidth()); // --> actual X,Y coord to draw at ex: (2*32, 3*32)
            //double startYd = Math.max(0, mapObject.getY() - mapObject.getHeight());
            
            //System.out.println(startX + "," +startY + " / " + startXd + "," +  startYd);
        }
        */
        Resource resource = spriteMap.getResource("spawn");
        if ( resource != null ) {
            startX = resource.positionX;
            startY  = resource.positionY;
        }
        
        TileSet tileSet = gameBoard.getMap().getTileSets().get(0);
        Tile tile = tileSet.getTile(132);
        humanoid = new Humanoid(tile,startX,startY);
        
        humanoids.add(humanoid);
        
        // Move these into Game class
        gameBoard.humanoidList.add(humanoid);
        gameBoard.spriteList.add(humanoid);
        
        return humanoid;
    }
    
    public Resource createResource(int startX, int startY, String type, int id) {
        //Resource resource = null;
        
        /*
        MapObject mapObject = gameBoard.getObject(type);
        if ( mapObject != null ) {
            startX = (int) (mapObject.getX()/mapObject.getWidth()); // --> Tile coord ex: (2,3)
            startY = (int) (mapObject.getY()/mapObject.getHeight());
        }
        */
        Resource resource = spriteMap.getResource(type);
//        if ( resource != null ) {
//            startX = r.positionX;
//            startY  = r.positionY;
//        }
        
        TileSet tileSet = gameBoard.getMap().getTileSets().get(0);
        Tile tile = tileSet.getTile(id);
        //resource = new Resource(tile,startX,startY);
        
        resources.add(resource);
        
        // Move these into Game class
        gameBoard.resourceList.add(resource);
        gameBoard.spriteList.add(resource);
        
        return resource;
    }
    
    public void update() {
        for (Humanoid humanoid : humanoids) {
            humanoid.moveChar();
        }
    }
    
    public void render() {
        //gameBoard.drawGameBoard(gc);
        gameBoard.render(spriteMap,gc);
        cursor.render(gc);
    }

    void TMP_HACK() {
        //////////// PLAYER //////////
        Humanoid player = createHumanoid(0,0);
        astar.spriteList.add(player);
        
        //////////// TREE //////////
        Resource resourceTree = createResource(0, 0, "tree", 1164);
        astar.spriteList.add(resourceTree);
               
        //////////// CHEST //////////
        Resource chest = createResource(0, 0, "storage", 2925);
        astar.spriteList.add(chest);
                
        LinkedList<AStar.Node> path = astar.findPath( player.positionX, player.positionY, 
                                                resourceTree.positionX, resourceTree.positionY);
        player.path = path;
    }
}
