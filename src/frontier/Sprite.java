/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import tiled.core.Tile;

/**
 *
 * @author brian
 */
public class Sprite {
    // TODO: make private with gett/setters
    private Image image;
    public Tile tile;
    public int positionX; // --> Tile coord ex: (2,3), do not draw at this location --> 
    public int positionY;    
    
    // public int gridX;
    // public int gridY;
    // public int coordX;
    // public int coordY;
    
    private double velocityX;
    private double velocityY;
    public double width;
    public double height;
 
    public Sprite(Tile tile, int startX, int startY) {
        this.tile = tile;
        this.positionX = startX;
        this.positionY = startY;
        this.velocityX = 1;
        this.velocityY = 1;
        this.width = tile.getWidth();
        this.height = tile.getHeight();
        try {
            this.image = Utils.createImage(tile.getImage());
        } catch (IOException ex) {
            Logger.getLogger(Sprite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public boolean isBlocked() {
        boolean rv = false;
        Properties prop = tile.getProperties();
        
        try {
            rv = Boolean.parseBoolean((String) prop.get("blocked"));
        } catch (Exception ex) {
            Logger.getLogger(Sprite.class.getName()).log(Level.INFO, null, ex);
        }
              
        return rv;
    }
    
    public String getProp(String property) {
        String rv = "";
        Properties prop = tile.getProperties();
        
        try {
            rv =  (String) prop.get(property);
        } catch (Exception ex) {
            Logger.getLogger(Sprite.class.getName()).log(Level.INFO, null, ex);
        }
        
        return rv;
    }
    
    
    
 
    public void update(double time)
    {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }
 
    public void render(GraphicsContext gc)
    {
        gc.drawImage( image, positionX, positionY );
    }
 
    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,width,height);
    }
 
    public boolean intersects(Sprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }
}
