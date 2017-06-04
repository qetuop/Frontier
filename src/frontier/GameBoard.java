/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.MapObject;
import tiled.core.ObjectGroup;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.io.TMXMapReader;

/**
 *
 * @author brian
 */
public class GameBoard {
    private Map gameMap;
    
    // move these to game class?
    public ArrayList<Humanoid> humanoidList;
    public ArrayList<Resource> resourceList;
    public ArrayList<Sprite> spriteList;
        
    public GameBoard(String tmx) {
        TMXMapReader tmxMapReader = new TMXMapReader();
        try {
             gameMap = tmxMapReader.readMap(tmx);

        } catch (Exception ex) {
            Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        humanoidList = new ArrayList<>();
        resourceList = new ArrayList<>();
        spriteList   = new ArrayList<>();
    }
    
    public Map getMap() {
        return gameMap;
    }
    
    public void render(SpriteMap spriteMap, GraphicsContext gc){
        ArrayList<ArrayList<ArrayList<Sprite>>> map = spriteMap.map;
        for (int row = 0; row < map.size(); row++) {
            for (int col = 0; col < map.get(row).size(); col++) {
                for (int spriteNum = 0; spriteNum < map.get(row).get(col).size(); spriteNum++) {
                    Sprite sprite = map.get(row).get(col).get(spriteNum);
                    Tile tile = sprite.tile;

                    if (tile == null) {
                        continue;
                    }

                    try {
                        Image image = Utils.createImage(tile.getImage());
                        gc.drawImage(image, col * tile.getWidth(), row * tile.getHeight());
                    } catch (IOException ex) {
                        Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        // draw humanoids
        for (Sprite sprite : spriteList) {
            try {

                Image image = Utils.createImage(sprite.tile.getImage());

                gc.drawImage(image, sprite.positionX * image.getWidth(), sprite.positionY * image.getHeight());
            } catch (IOException ex) {
                Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public void drawGameBoard(GraphicsContext gc) {
        // TODO: only draw whats on the screen
        
        for ( MapLayer layer : gameMap ) {
            
            // TILE LAYER
            if (layer instanceof TileLayer) {
                TileLayer tileLayer = (TileLayer) layer;
                for (int row = 0; row < tileLayer.getHeight(); row++) {
                    for (int col = 0; col < tileLayer.getWidth(); col++) {
                        Tile tile = tileLayer.getTileAt(col, row);

                        if (tile == null) {
                            continue;
                        }
                        
                        try {
                            Image image = Utils.createImage(tile.getImage());
                            gc.drawImage(image, col * tile.getWidth(), row * tile.getHeight());
                        } catch (IOException ex) {
                            Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } // col
                } // row
            } // TileLayer
            
            // OBJECTS
            else if ( layer instanceof ObjectGroup ) {
                ObjectGroup objectGroup = (ObjectGroup)layer;
                
                for ( MapObject mapObject : objectGroup ) {
                    Tile tile = mapObject.getTile();

                    if (tile == null) {
                        continue;
                    }

                    try {
                        Image image = Utils.createImage(tile.getImage());

                        // upper left of image
                        double startX = Math.max(0, mapObject.getX() - mapObject.getWidth());
                        double startY = Math.max(0, mapObject.getY() - mapObject.getHeight());
    
                        gc.drawImage(image, startX, startY);
                    } catch (IOException ex) {
                        Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } // for each object
                
            } // ObjectGroup
            
        } // for each MapLayer
        
        // draw humanoids
        for ( Sprite sprite : spriteList ){
            try {
                
                Image image = Utils.createImage(sprite.tile.getImage());
                
                gc.drawImage(image, sprite.positionX*image.getWidth(), sprite.positionY*image.getHeight());
            } catch (IOException ex) {
                Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    } // drawGameBoard
    
    // get an Object with a given Name
    public MapObject getObject(String objectName){
        MapObject rv = null;
        
        for ( MapLayer layer : gameMap ) {            
            // TILE LAYER
            if ( layer instanceof ObjectGroup ) {
                ObjectGroup objectGroup = (ObjectGroup)layer;                
                
                for ( MapObject mapObject : objectGroup ) {
                    if ( mapObject.getName().compareTo(objectName) == 0 ) {
                        rv = mapObject;
                        break;
                    }
                    
                } // for each mapObject
                
            } // ObjectGroup
            
        } // for each MapLayer

        return rv;
    } // getObject
    
} // class GameBoard
