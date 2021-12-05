package FX.Controller;

import FX.Model.Entities.Ball.Ball;
import FX.Model.Entities.Ball.RubberBall;
import FX.Model.Game;
import FX.Model.GameScore;
import FX.Model.Entities.Player;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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

    public GameStateController() {

        game = Game.singletonGame(30,3,6/2,new Point2D(300,430));
        gameScore = GameScore.singletonGameScore();
        player = Player.singletonPlayer(new Point2D(225,430), game.getPlayArea());
        ball = new RubberBall(new Point2D(300,400));

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
//                graphicsContext.setStroke(Color.WHITE);
//                graphicsContext.fillRect(0,0,600,450);

                graphicsContext.setStroke(stringColor);
                graphicsContext.fillText(String.format("Bricks: %d Balls %d", game.getBrickCount(), game.getBallCount()), 250, 225);

                for (int i = 0; i < game.getBrickCount(); i++){
                    graphicsContext.setStroke(game.getBricks()[i].getBorderColor());
                    graphicsContext.fillRect(game.getBricks()[i].getPositionX(),game.getBricks()[i].getPositionY(),game.getBricks()[i].getWidth(),game.getBricks()[i].getHeight());

                    graphicsContext.setStroke(game.getBricks()[i].getInnerColor());
                    graphicsContext.fillRect(game.getBricks()[i].getPositionX(),game.getBricks()[i].getPositionY(),game.getBricks()[i].getWidth(),game.getBricks()[i].getHeight());
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
        graphicsContext.setStroke(ball.getBorderBallColor());
        graphicsContext.fillOval(ball.getCenterPosition().getX(), ball.getCenterPosition().getY(), ball.getRadius(), ball.getRadius());

        graphicsContext.setStroke(ball.getInnerBallColor());
        graphicsContext.strokeOval(ball.getCenterPosition().getX(), ball.getCenterPosition().getY(), ball.getRadius(), ball.getRadius());
    }

    private void drawPlayer(Player player) {
        graphicsContext.setStroke(player.getBorderColor());
        graphicsContext.fillRect(player.getPositionX(),player.getPositionY(),player.getWidth(),player.getHeight());

        graphicsContext.setStroke(player.getInnerColor());
        graphicsContext.fillRect(player.getPositionX(),player.getPositionY(),player.getWidth(),player.getHeight());
    }

    public void togglePauseContinueGame(){
        System.out.println("hello");
        if(toggle){
            animationTimer.stop();
            toggle = false;
        }
        else{
            animationTimer.start();
            toggle = true;
        }
    }

    boolean toggle = true;
    public void showDebugConsole(){

    }

    public void showPauseMenu(){

    }
}
