/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tiled.core.Map;
import tiled.core.Tile;
import tiled.core.TileLayer;

/**
 *
 * @author brian
 */
public class InfoWindow {
    HBox mainHBox;
    
    VBox infoVBox;
    VBox cmdVBox;
    
            
    
    Sprite sprite;
    Map gameMap;
    int col;
    int row;
    Canvas canvas;
    
    SpriteMap spriteMap;
    
    public InfoWindow(SpriteMap sm) {
        spriteMap = sm;
        
        mainHBox = new HBox();
        mainHBox.setStyle("-fx-border-color: blue");
        
        infoVBox = new VBox();
        infoVBox.setStyle("-fx-border-color: red");
        infoVBox.setPrefHeight(100);
        //infoVBox.setPrefWidth(100);
               
        cmdVBox = new VBox();
        cmdVBox.setStyle("-fx-border-color: green");
        cmdVBox.setPrefWidth(100);
        
        mainHBox.getChildren().addAll(infoVBox, cmdVBox);
        
    }
    
    private VBox createInfoBox(Sprite sprite) {
        VBox infoBox = new VBox();
        infoBox.setPrefWidth(100);

        Label infoLabel = new Label("Terrain");
        //infoBox.getChildren().add(infoLabel);

        Tile tile = sprite.tile;
        if (tile != null) {
            Label typeValue = new Label();
            TextField typeTF = new TextField();
            typeTF.setEditable(false);
            
            Properties prop = tile.getProperties();

            try {
                typeValue.setText((String) prop.get("name"));
                typeTF.setText((String) prop.get("name"));
            } catch (Exception ex) {
                Logger.getLogger(InfoWindow.class.getName()).log(Level.INFO, null, ex);
            }

            canvas = new Canvas(32, 32); //TODO: dont hardcode, grab first sprite? create in update()           
            GraphicsContext gc = canvas.getGraphicsContext2D();
            try {
                gc.drawImage(Utils.createImage(tile.getImage()), 0, 0);
            } catch (IOException ex) {
                Logger.getLogger(InfoWindow.class.getName()).log(Level.SEVERE, null, ex);
            }

            HBox typeHbox = new HBox();
            typeHbox.getChildren().addAll(typeTF, canvas);
            typeHbox.setStyle("-fx-border-color: black");

            //infoVBox.getChildren().add(typeHbox);

            // COORDS             
            String coordString = "(" + sprite.positionX + "," + sprite.positionY + ")";
            Label coords = new Label(coordString);

            infoVBox.getChildren().addAll(infoLabel, typeHbox, coords);
        }
        
        
        
        return infoBox;
    }

    void update(int col, int row) {
        ArrayList<Sprite> sprites = spriteMap.getCoord(row, col);
        if ( sprites == null ) {
            return;
        }
        infoVBox.getChildren().clear();
        //System.out.println("sprites " + sprites.size());
        for ( Sprite sprite : sprites ) {
            VBox tmp = createInfoBox(sprite);
            infoVBox.getChildren().add(tmp);
        }
    }
}
