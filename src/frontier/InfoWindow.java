/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author brian
 */
public class InfoWindow extends VBox {
    
    public InfoWindow() {
        HBox typeHbox = new HBox();
        Label typeLabel = new Label("Type:");
        Label typeValue = new Label("Type Value");
        typeHbox.getChildren().addAll(typeLabel, typeValue);
        this.getChildren().add(typeHbox);
    }
}
