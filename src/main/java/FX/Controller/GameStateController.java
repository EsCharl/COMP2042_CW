package FX.Controller;

import FX.Model.Entities.Ball.Ball;
import FX.Model.Entities.Ball.RubberBall;
import FX.Model.Game;
import FX.Model.GameScore;
import FX.Model.Entities.Player;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameStateController implements Initializable {

    private Game game;
    private GameScore gameScore;
    private Player player;
    private Ball ball;

    private GraphicsContext graphicsContext;
    private final Color stringColor = Color.BLUE;
    private AnimationTimer animationTimer;
    @FXML private Canvas gameBoard;
    @FXML private AnchorPane anchorPane;
    Image backgroundImage;
    Scene scene;

    boolean toggle = true;

    public GameStateController() {

        game = Game.singletonGame(30,3,6/2,new Point2D(300,430));
        gameScore = GameScore.singletonGameScore();
        player = Player.singletonPlayer(new Point2D(225,430), game.getPlayArea());
        ball = new RubberBall(new Point2D(300,425));

        startGame();
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){

        graphicsContext = gameBoard.getGraphicsContext2D();

        System.out.println(anchorPane);
        System.out.println(gameBoard);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                graphicsContext.clearRect(0,0,600,450);

                graphicsContext.setLineWidth(2);

                graphicsContext.setFill(stringColor);
                graphicsContext.fillText(String.format("Bricks: %d Balls %d", game.getBrickCount(), game.getBallCount()), 250, 225);

                for (int i = 0; i < game.getBrickCount(); i++){
                    graphicsContext.setFill(game.getBricks()[i].getInnerColor());
                    graphicsContext.fillRect(game.getBricks()[i].getPositionX(),game.getBricks()[i].getPositionY(),game.getBricks()[i].getWidth(),game.getBricks()[i].getHeight());

                    graphicsContext.setStroke(game.getBricks()[i].getBorderColor());
                    graphicsContext.strokeRect(game.getBricks()[i].getPositionX()-1,game.getBricks()[i].getPositionY()-1,game.getBricks()[i].getWidth()+2,game.getBricks()[i].getHeight()+2);
                }

                drawBall(ball);
                drawPlayer(player);

                scene = anchorPane.getScene();

                scene.setOnKeyPressed(keyEvent ->{
                    if(keyEvent.getCode().equals(KeyCode.A)){
                        player.moveLeft();
                    }else if(keyEvent.getCode().equals(KeyCode.D)){
                        player.moveRight();
                    }else if(keyEvent.getCode().equals(KeyCode.SPACE)){
                        togglePauseContinueGame();
                    }else if(keyEvent.getCode().equals(KeyCode.F1) && keyEvent.isAltDown() && keyEvent.isShiftDown()){
                        showDebugConsole();
                    }else{
                        player.stop();
                    }
                });
            }
        };

        animationTimer.start();

        anchorPane.requestFocus();
    }

    private void startGame(){
        game.nextLevel();
        gameScore.setLevelFilePathName("/scores/Level"+ game.getCurrentLevel()+".txt");
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
