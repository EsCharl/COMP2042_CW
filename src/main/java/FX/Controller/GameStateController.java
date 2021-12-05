package FX.Controller;

import FX.Model.Entities.Ball.Ball;
import FX.Model.Entities.Ball.RubberBall;
import FX.Model.Entities.Brick.Brick;
import FX.Model.Game;
import FX.Model.GameScore;
import FX.Model.Entities.Player;

import FX.Model.Levels.LevelFactory;
import FX.Model.Levels.WallLevelTemplates;
import FX.View.GameScoreDisplay;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameStateController implements Initializable {

    private Game game;
    private GameScore gameScore;
    private GraphicsContext graphicsContext;
    private GameScoreDisplay gameScoreDisplay;
    private final Color stringColor = Color.BLUE;
    private AnimationTimer animationTimer;
    private Scene scene;

    @FXML private Canvas gameBoard;
    @FXML private AnchorPane anchorPane;


    boolean toggle = true;

    public GameStateController() {

        game = Game.singletonGame();
        gameScore = GameScore.singletonGameScore();
        gameScoreDisplay = new GameScoreDisplay();

        game.setBrickLevels(makeLevels(game.getPlayArea(),30,3,6/2));

        game.nextLevel();
        gameScore.setLevelFilePathName("/scores/Level"+ game.getCurrentLevel()+".txt");
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){

        graphicsContext = gameBoard.getGraphicsContext2D();

        System.out.println(anchorPane);
        System.out.println(gameBoard);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                graphicsContext.drawImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/GameImage.png"))),0,0);

                graphicsContext.setLineWidth(2);

                graphicsContext.setFill(stringColor);
                graphicsContext.fillText(String.format("Bricks: %d Balls %d", game.getBrickCount(), game.getBallCount()), 250, 225);

                for (int i = 0; i < game.getBrickCount(); i++){
                    graphicsContext.setFill(game.getBricks()[i].getInnerColor());
                    graphicsContext.fillRect(game.getBricks()[i].getPositionX(),game.getBricks()[i].getPositionY(),game.getBricks()[i].getWidth(),game.getBricks()[i].getHeight());

                    graphicsContext.setStroke(game.getBricks()[i].getBorderColor());
                    graphicsContext.strokeRect(game.getBricks()[i].getPositionX()-1,game.getBricks()[i].getPositionY()-1,game.getBricks()[i].getWidth()+2,game.getBricks()[i].getHeight()+2);
                }

                drawBall(game.getBall());
                drawPlayer(game.getPlayer());

                scene = anchorPane.getScene();

                scene.setOnKeyPressed(keyEvent ->{
                    if(keyEvent.getCode().equals(KeyCode.ESCAPE)){
                        pauseButtonClicked();
                    }
                    else if(keyEvent.getCode().equals(KeyCode.A)){
                        game.getPlayer().moveLeft();
                    }else if(keyEvent.getCode().equals(KeyCode.D)){
                        game.getPlayer().moveRight();
                    }else if(keyEvent.getCode().equals(KeyCode.SPACE)){
                        togglePauseContinueGame();
                    }else if(keyEvent.getCode().equals(KeyCode.F1) && keyEvent.isAltDown() && keyEvent.isShiftDown()){
                        showDebugConsole();
                    }else{
                        game.getPlayer().stop();
                    }
                });

                if(game.isLevelComplete()){
                    try {
                        gameScore.setLastLevelCompletionRecord(gameScore.getHighScore());
                        gameScore.updateSaveFile(gameScore.getLastLevelCompletionRecord());
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                    gameScoreDisplay.generateLevelCompleteWindow(gameScore.getLastLevelCompletionRecord(), gameScore.getTimerString());
                }
            }
        };

        animationTimer.start();

        anchorPane.requestFocus();
    }

    private void pauseButtonClicked(){

    }

    /**
     * this is used to generate the levels to be placed in a brick array.
     *
     * @param drawArea this is the area where the bricks will be drawn.
     * @param brickCount this is the amount bricks that will be generated in the level.
     * @param lineCount this is the total amount of rows of bricks that is allowed.
     * @param brickDimensionRatio this is the ratio for the bricks.
     * @return the levels that are generated in the form of 2 dimension brick array.
     */
    private Brick[][] makeLevels(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio){
        Brick[][] tmp = new Brick[game.getLEVELS_AMOUNT()][];
        LevelFactory levelFactory = new LevelFactory();
        tmp[0] = levelFactory.getLevel("CHAINLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, WallLevelTemplates.CLAY, WallLevelTemplates.CLAY);
        tmp[1] = levelFactory.getLevel("CHAINLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, WallLevelTemplates.CLAY, WallLevelTemplates.CEMENT);
        tmp[2] = levelFactory.getLevel("CHAINLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, WallLevelTemplates.CLAY, WallLevelTemplates.STEEL);
        tmp[3] = levelFactory.getLevel("CHAINLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, WallLevelTemplates.STEEL, WallLevelTemplates.CEMENT);
        tmp[4] = levelFactory.getLevel("TWOLINESLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, WallLevelTemplates.REINFORCED_STEEL, WallLevelTemplates.STEEL);
        tmp[5] = levelFactory.getLevel("RANDOMLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, 0, 0);
        return tmp;
    }

    private void drawBall(Ball ball){
        graphicsContext.setFill(ball.getInnerBallColor());
        graphicsContext.fillOval(ball.getxCoordinate(), ball.getyCoordinate(), ball.getRadius(), ball.getRadius());

        graphicsContext.setStroke(ball.getBorderBallColor());
        graphicsContext.strokeOval(ball.getxCoordinate()-1, ball.getyCoordinate()-1, ball.getRadius()+2, ball.getRadius()+2);
    }

    private void drawPlayer(Player player) {
        graphicsContext.setFill(player.getInnerColor());
        graphicsContext.fillRect(player.getPositionX(),player.getPositionY(),player.getWidth(),player.getHeight());

        graphicsContext.setStroke(player.getBorderColor());
        graphicsContext.strokeRect(player.getPositionX()-1,player.getPositionY()-1,player.getWidth()+2,player.getHeight()+2);
    }

    public void togglePauseContinueGame(){
        if(toggle){
            animationTimer.stop();
            toggle = false;
        }
        else{
            animationTimer.start();
            toggle = true;
        }
    }

    public void showDebugConsole(){
        Stage debugConsole = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/FX/DebugConsole.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        debugConsole.setScene(new Scene(root));
        debugConsole.setTitle("Debug Console");
        debugConsole.initModality(Modality.WINDOW_MODAL);
        debugConsole.showAndWait();
    }

    public void showPauseMenu(){

    }
}
