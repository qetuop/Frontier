/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import frontier.AStar.Node;
import java.awt.Rectangle;
import java.util.LinkedList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    private final GameBoard gameBoard;
    
    public Frontier() {
        String mapName = "astar_8x6_32_dung.tmx";
       
        this.gameBoard = new GameBoard("/home/brian/NetBeansProjects/JAVA/GameTest/resources/"+mapName);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Frontier");

        Group root = new Group();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);

        ////////////////////
        Map gameMap = gameBoard.getMap();
        Rectangle bounds = gameMap.getBounds();

        Canvas canvas = new Canvas(bounds.width * gameMap.getTileWidth(),
                                   bounds.height * gameMap.getTileHeightMax());
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        AStar astar = new AStar(gameMap);
        
        
        TileSet tileSet = gameMap.getTileSets().get(0);
        
        //////////// PLAYER //////////
        int startX = 0;
        int startY = 0;
        MapObject mapObject = gameBoard.getObject("spawn");
        if ( mapObject != null ) {
            startX = (int) (mapObject.getX()/mapObject.getWidth()); // --> Tile coord ex: (2,3)
            startY = (int) (mapObject.getY()/mapObject.getHeight());
            
            //double startXd = Math.max(0, mapObject.getX() - mapObject.getWidth()); // --> actual X,Y coord to draw at ex: (2*32, 3*32)
            //double startYd = Math.max(0, mapObject.getY() - mapObject.getHeight());
            
            //System.out.println(startX + "," +startY + " / " + startXd + "," +  startYd);
        }
        
        Tile tile = tileSet.getTile(132);
        Humanoid player = new Humanoid(tile,startX,startY);
        gameBoard.humanoidList.add(player);
        gameBoard.spriteList.add(player);
        astar.spriteList.add(player);
        
        //////////// TREE //////////
        startX = 0;
        startY = 0;
        mapObject = gameBoard.getObject("resource");
        if ( mapObject != null ) {
            startX = (int) (mapObject.getX()/mapObject.getWidth());
            startY = (int) (mapObject.getY()/mapObject.getHeight());
            
            
        }

        tile = tileSet.getTile(1164);
        Resource resourceTree = new Resource(tile,startX,startY);
        gameBoard.resourceList.add(resourceTree);
        gameBoard.spriteList.add(resourceTree);
        astar.spriteList.add(resourceTree);
        
        //////////// CHEST //////////
        startX = 0;
        startY = 0;
        mapObject = gameBoard.getObject("storage");
        if ( mapObject != null ) {
            startX = (int) (mapObject.getX()/mapObject.getWidth());
            startY = (int) (mapObject.getY()/mapObject.getHeight());
        }

        tile = tileSet.getTile(2925);
        Resource resourceStorage = new Resource(tile,startX,startY);
        gameBoard.resourceList.add(resourceStorage);
        gameBoard.spriteList.add(resourceStorage);
        astar.spriteList.add(resourceStorage);
        //
        
        LinkedList<Node> path = astar.findPath( player.positionX, player.positionY, 
                                                resourceTree.positionX, resourceTree.positionY);
        player.path = path;
        
        // Render
        
        
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
                
                gameBoard.drawGameBoard(gc);                
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
