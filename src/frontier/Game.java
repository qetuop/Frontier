/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import tiled.core.MapObject;
import tiled.core.Tile;
import tiled.core.TileSet;

/**
 *
 * @author brian
 */
public class Game {
    public GameBoard gameBoard; // Tiled objects
    
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
    }
    
    public void createGameBoard(String mapName) {
        gameBoard = new GameBoard("/home/brian/NetBeansProjects/JAVA/GameTest/resources/"+mapName);
        cursor = new Cursor(this); // TODO: should move out, but need GameBoard created first
    }
    
    public Humanoid createHumanoid(int startX, int startY) {
        Humanoid humanoid = null;
        
        MapObject mapObject = gameBoard.getObject("spawn");
        if ( mapObject != null ) {
            startX = (int) (mapObject.getX()/mapObject.getWidth()); // --> Tile coord ex: (2,3)
            startY = (int) (mapObject.getY()/mapObject.getHeight());
            
            //double startXd = Math.max(0, mapObject.getX() - mapObject.getWidth()); // --> actual X,Y coord to draw at ex: (2*32, 3*32)
            //double startYd = Math.max(0, mapObject.getY() - mapObject.getHeight());
            
            //System.out.println(startX + "," +startY + " / " + startXd + "," +  startYd);
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
        Resource resource = null;
        
        MapObject mapObject = gameBoard.getObject(type);
        if ( mapObject != null ) {
            startX = (int) (mapObject.getX()/mapObject.getWidth()); // --> Tile coord ex: (2,3)
            startY = (int) (mapObject.getY()/mapObject.getHeight());
        }
        TileSet tileSet = gameBoard.getMap().getTileSets().get(0);
        Tile tile = tileSet.getTile(id);
        resource = new Resource(tile,startX,startY);
        
        resources.add(resource);
        
        // Move these into Game class
        gameBoard.resourceList.add(resource);
        gameBoard.spriteList.add(resource);
        
        return resource;
    }
    
    public void update() {
        
    }
    
    public void render(GraphicsContext gc) {
        
    }
}
