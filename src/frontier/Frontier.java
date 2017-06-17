/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Frontier");

        Group root = new Group();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);
        
        root.getChildren().add(game.mainPane);        
                
        game.TMP_HACK();

        theScene.setOnKeyPressed((KeyEvent e) -> {
            String code = e.getCode().toString();
            System.out.println("code: " + code);
        });
      
        // TODO: move to Cursor, pass Scene into Game to Cursor
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
                    // update everything
                    game.update();
                    lastUpdate = currentNanoTime;
                }    
                
                // draw everything
                game.render();                                
            } // 60Hz
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
