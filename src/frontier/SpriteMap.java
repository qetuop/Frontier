/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    //ArrayList<Resource> resources; // TODO: need better wy to store, map?
    
    public SpriteMap(Map gameMap,ArrayList<Humanoid> humanoids, ArrayList<Resource>  resources) {
        //resources = new ArrayList<>();
        
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
                        Properties prop = tile.getProperties();
                        String type = null;
                        try {
                           type = (String) prop.get("type");
                           //System.out.println("type="+type);
                        } catch (Exception ex) {
                            Logger.getLogger(Sprite.class.getName()).log(Level.INFO, null, ex);
                        }
                        
                        if ( type == null ) {
                            map.get(row).get(col).add(new Sprite(tile, col, row));
                        }
                        else if ( type.compareTo("resource") == 0 ) {
                            Resource resource = new Resource(tile, col, row);
                            map.get(row).get(col).add(resource);
                            resources.add(resource);
                        }
                        else if ( type.compareTo("humanoid") == 0 ) {
                            Humanoid sp = new Humanoid(tile, col, row);
                            map.get(row).get(col).add(sp);
                            //resources.add(resource);
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
                    
                     Properties prop = tile.getProperties();

                        try {
                           String type = (String) prop.get("type");
                            //System.out.println("object type="+type);
                        } catch (Exception ex) {
                            Logger.getLogger(Sprite.class.getName()).log(Level.INFO, null, ex);
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
                    System.out.println("("+row+","+col+")[" + spriteNum + "]: " + sprite.getProp("name"));
                }
            }
        }
    }
    
    public ArrayList<Sprite> getCoord( int row, int col ) {
        ArrayList<Sprite> rv = new ArrayList<Sprite>();
        
        if ( row >=0 && row < map.size() &&
             col >=0 && col < map.get(row).size() ) {
            rv = map.get(row).get(col);
        }
        
        return rv;
    }
    
//    public Resource getResource(String name){
//        Resource resource = null;
//        for ( Resource r: resources ){
//            if ( r.getProp("name").compareTo(name) == 0 ) {
//                resource = r;
//                break;
//            }
//        }
//        return resource;
//    }
    
} // class SpriteMap
