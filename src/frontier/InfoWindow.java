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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
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
    VBox vBox;
    Sprite sprite;
    Map gameMap;
    int col;
    int row;
    Canvas canvas;
    Label typeValue;
    
    public InfoWindow() {
        vBox = new VBox();
        vBox.setStyle("-fx-border-color: red");
        
        HBox typeHbox = new HBox();
        Label typeLabel = new Label("Type:");
        typeValue = new Label("Type Value");
        typeHbox.getChildren().addAll(typeLabel, typeValue);
        
        typeHbox.setStyle("-fx-border-color: black");
        canvas = new Canvas(32, 32);
        vBox.getChildren().add(typeHbox);    
        vBox.getChildren().add(canvas);
    }

    void update() {
        TileLayer tileLayer = (TileLayer) gameMap.getLayer(0);
        Tile tile = tileLayer.getTileAt(col, row);
        if (tile != null) {

            Properties prop = tile.getProperties();
        
            try {
                typeValue.setText((String) prop.get("name"));
                //System.out.println("typeValue=" + typeValue.getText());
            } catch (Exception ex) {
                Logger.getLogger(InfoWindow.class.getName()).log(Level.INFO, null, ex);
            }
            
            GraphicsContext gc = canvas.getGraphicsContext2D();
            try {
                gc.drawImage(Utils.createImage(tile.getImage()), 0, 0);
            } catch (IOException ex) {
                Logger.getLogger(InfoWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            
        }
    }
}
