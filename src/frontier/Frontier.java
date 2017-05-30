/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import frontier.AStar.Node;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tiled.core.Map;
import tiled.core.MapObject;
import tiled.core.Tile;
import tiled.core.TileSet;

/**
 *
 * @author brian
 */
public class Frontier extends Application {
            
    //private final GameBoard gameBoard;
    private final Game game = new Game();
    
    public Frontier() {
        
        String mapName = "astar_8x6_32_dung.tmx";
        game.createGameBoard(mapName);
       
        //this.gameBoard = new GameBoard("/home/brian/NetBeansProjects/JAVA/GameTest/resources/"+mapName);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Frontier");

        Group root = new Group();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);

        ////////////////////
        Map gameMap = game.gameBoard.getMap();
        Rectangle bounds = gameMap.getBounds();

        Canvas canvas = new Canvas(bounds.width * gameMap.getTileWidth(),
                                   bounds.height * gameMap.getTileHeightMax());
        
        HBox hbox = new HBox();
        hbox.getChildren().add(canvas);
        
        //root.getChildren().add(canvas);
        
        
        
        
        InfoWindow infoWindow = new InfoWindow();
        hbox.getChildren().add(infoWindow);
        root.getChildren().add(hbox);
        

        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        AStar astar = new AStar(gameMap);
        
        
        TileSet tileSet = gameMap.getTileSets().get(0);
        
        //////////// PLAYER //////////
        //int startX = 0;
        //int startY = 0;
        //MapObject mapObject = game.gameBoard.getObject("spawn");

        Humanoid player = game.createHumanoid(0,0);
        astar.spriteList.add(player);
        
        //////////// TREE //////////
        Resource resourceTree = game.createResource(0, 0, "resource", 1164);
        astar.spriteList.add(resourceTree);
               
        //////////// CHEST //////////
        Resource chest = game.createResource(0, 0, "storage", 2925);
        astar.spriteList.add(chest);
                
        LinkedList<Node> path = astar.findPath( player.positionX, player.positionY, 
                                                resourceTree.positionX, resourceTree.positionY);
        player.path = path;
        
        // Render
        
        
        theScene.setOnKeyPressed((KeyEvent e) -> {
            String code = e.getCode().toString();
            System.out.println("code: " + code);
            
            // only add once... prevent duplicates
            //if ( !input.contains(code) )
            //    input.add( code );
        });

      
        // highlight - mouse move
        theScene.setOnMouseMoved((MouseEvent event) -> {                        
            game.cursor.mouseMoved(event);
        });
        
        // Select mouse up
        theScene.setOnMouseClicked((MouseEvent event) -> {   
            game.cursor.mouseClicked(event);
        });
        
        
        // MAIN LOOP
        final long startNanoTime = System.nanoTime(); 
        new AnimationTimer()
        {
            long lastUpdate = startNanoTime ;
            
            public void handle(long currentNanoTime)
            {
                //double t = (currentNanoTime - startNanoTime) / 1_000_000_000.0;                 

                // Do stuff in here every X seconds (1_000_000_000 = 1sec)
                if (currentNanoTime - lastUpdate >= 1_000_000_000) {
                   
                    player.moveChar();    
                    lastUpdate = currentNanoTime;
                }    
                
                game.gameBoard.drawGameBoard(gc); 
                
                game.cursor.render(gc);
            }
        }.start();
        
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
