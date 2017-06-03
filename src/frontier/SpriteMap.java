/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.MapObject;
import tiled.core.ObjectGroup;
import tiled.core.Tile;
import tiled.core.TileLayer;

/**
 *
 * @author brian
 */
public class SpriteMap {
    ArrayList<ArrayList<ArrayList<Sprite>>> map; // [col][row][Sprite list]
    
    public SpriteMap(Map gameMap) {
        
        map = new ArrayList<>();
        
        
        for ( MapLayer layer : gameMap ) {
            
            // TILE LAYER
            if (layer instanceof TileLayer) {
                TileLayer tileLayer = (TileLayer) layer;

                for (int row = 0; row < tileLayer.getHeight(); row++) {
                    if (map.size() <= row || map.get(row) == null) {
                        map.add(new ArrayList<>());
                    }

                    for (int col = 0; col < tileLayer.getWidth(); col++) {
                        if (map.get(row).size() <= col || map.get(row).get(col) == null) {
                            map.get(row).add(new ArrayList<>());
                        }
                        
                        
                        Tile tile = tileLayer.getTileAt(col, row);

                        if (tile == null) {
                            continue;
                        }
                        
                        map.get(row).get(col).add(new Sprite(tile, col, row));


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

                    
                } // for each object
                
            } // ObjectGroup
            
        } // for each MapLayer
    } // ctor
    
    public void print() {
        for ( int row = 0; row < map.size(); row++ ){
            for ( int col = 0; col < map.get(row).size(); col++ ){
                for ( int spriteNum = 0; spriteNum < map.get(row).get(col).size(); spriteNum++ ){
                    Sprite sprite = map.get(row).get(col).get(spriteNum);
                    System.out.println("("+row+","+col+"): " + sprite.getProp("name"));
                }
            }
        }
    }
} // class SpriteMap
