/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import tiled.core.Tile;
import tiled.core.TileSet;

/**
 *
 * @author brian
 */
public class Cursor {
        private Game game;
    
        int highlightX;
        int highlightY;
        
        int selectX;
        int selectY;
        
        boolean isSelected = false;
        
        Tile highLightTile;
        Tile selectTile;

    Cursor(Game gameIn) {
        game = gameIn;
        
        TileSet tileSet = game.gameBoard.getMap().getTileSets().get(0);
        
        highLightTile = tileSet.getTile(8);
        selectTile = tileSet.getTile(67);
    }
        
        public void mouseMoved(MouseEvent event) {
            double x = event.getX();
            double y = event.getY();
            
            if ( x > game.canvas.getWidth() || y > game.canvas.getHeight() ) {
                return;
            }
            
            int col = game.gameBoard.transToCol(x);
            int row = game.gameBoard.transToRow(y);
            //System.out.println(col + "," + row);
            highlightX = col;
            highlightY = row;   
            
            //game.infoWindow.col = col;
            //game.infoWindow.row = row;
            
            game.infoWindow.tileHighlight(col,row);           
        }
        
        public void mouseClicked(MouseEvent event) {
            double x = event.getX();
            double y = event.getY();
            
            if ( x > game.canvas.getWidth() || y > game.canvas.getHeight() ) {
                return;
            }
            
            int col = game.gameBoard.transToCol(x);
            int row = game.gameBoard.transToRow(y);
            //System.out.println(col + "," + row);
            selectX = col;
            selectY = row;
            
            MouseButton button = event.getButton();
            if ( event.getButton() == MouseButton.PRIMARY ) {
                isSelected = true;
                game.infoWindow.tileSelect(col,row);
            }
            else if ( event.getButton() == MouseButton.SECONDARY ) {
                isSelected = false;
            }
            else {
                return;
            } 
        }
        
        public void render(GraphicsContext gc) {
            try {
                    Tile tile = highLightTile;
                    Image image = Utils.createImage(tile.getImage());
                    gc.drawImage(image, highlightX * tile.getWidth(), highlightY * tile.getHeight());
                    
                    if ( isSelected == true ) {
                        tile = selectTile;
                        image = Utils.createImage(tile.getImage());
                        gc.drawImage(image, selectX * tile.getWidth(), selectY * tile.getHeight());
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Cursor.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
}