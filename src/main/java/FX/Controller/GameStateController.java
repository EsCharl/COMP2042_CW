package FX.Controller;

import FX.Model.Entities.Ball.Ball;
import FX.Model.Entities.Brick.Brick;
import FX.Model.Entities.Brick.Crack;
import FX.Model.Entities.Entities;
import FX.Model.Game;
import FX.Model.GameScore;
import FX.Model.Entities.Player;

import FX.View.GameScoreDisplay;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class GameStateController implements Initializable {

    private Game game;
    private GameScore gameScore;
    private GraphicsContext graphicsContext;
    private GameScoreDisplay gameScoreDisplay;
    private final Color stringColor = Color.BLUE;
    private AnimationTimer animationTimer;
    private Scene scene;
    private Random rnd;
    private ArrayList userInput;

    private Image backgroundImage;

    @FXML private Canvas gameBoard;
    @FXML private AnchorPane anchorPane;
    @FXML private Text gameText;


    boolean toggle = true;

    public GameStateController() {

        setRnd(new Random());
        userInput = new ArrayList();

        game = Game.singletonGame();
        gameScore = GameScore.singletonGameScore();
        gameScoreDisplay = new GameScoreDisplay();

        game.nextLevel();
        gameScore.setLevelFilePathName("/scores/Level"+ game.getCurrentLevel()+".txt");
        backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/GameImage.png")));
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){

        graphicsContext = gameBoard.getGraphicsContext2D();

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                graphicsContext.drawImage(backgroundImage,0,0);

                graphicsContext.setLineWidth(2);

                gameText.setText(String.format("Bricks: %d Balls %d", game.getBrickCount(), game.getBallCount()));

                for (int i = 0; i < game.getBrickCount(); i++){
                    graphicsContext.setFill(game.getBrickLevels()[game.getCurrentLevel()-1][i].getInnerColor());
                    graphicsContext.fillRect(game.getBrickLevels()[game.getCurrentLevel()-1][i].getBounds().getMinX(),game.getBrickLevels()[game.getCurrentLevel()-1][i].getBounds().getMinY(),game.getBrickLevels()[game.getCurrentLevel()-1][i].getWidth(),game.getBrickLevels()[game.getCurrentLevel()-1][i].getHeight());

                    graphicsContext.setStroke(game.getBrickLevels()[game.getCurrentLevel()-1][i].getBorderColor());
                    graphicsContext.strokeRect(game.getBrickLevels()[game.getCurrentLevel()-1][i].getBounds().getMinX()-1,game.getBrickLevels()[game.getCurrentLevel()-1][i].getBounds().getMinY()-1,game.getBrickLevels()[game.getCurrentLevel()-1][i].getWidth()+2,game.getBrickLevels()[game.getCurrentLevel()-1][i].getHeight()+2);
                }

                drawBall(game.getBall());
                drawPlayer(game.getPlayer());

                if(game.isBallLost()){
                    game.setBallLost(false);
                    toggle = false;
                    stop();
                }

                scene = anchorPane.getScene();

                game.getPlayer().stop();

                scene.setOnKeyPressed(keyEvent ->{
                    if(!userInput.contains(keyEvent.getCode()))
                        userInput.add(keyEvent.getCode());
                });

                scene.setOnKeyReleased(keyEvent -> {
                    movementKeyHandler();

                    System.out.println(userInput);

                    nonMovementKeyHandler();
                    while(userInput.contains(keyEvent.getCode()))
                        userInput.remove(0);
                });

                if(game.isGameOver()){
                    stop();
                    toggle = false;
                }

                game.getBall().move();
                game.getPlayer().move();

                automation();

                findImpacts();

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

    private void movementKeyHandler(){
        if(userInput.contains(KeyCode.A) && userInput.contains(KeyCode.D)){
            game.getPlayer().stop();
        }else if(userInput.contains(KeyCode.A)){
            game.getPlayer().moveLeft();
        }
        else if(userInput.contains(KeyCode.D)){

            game.getPlayer().moveRight();
        }
    }

    private void nonMovementKeyHandler(){
        if(userInput.contains(KeyCode.ESCAPE)){
            pauseButtonClicked();
        }else if(userInput.contains(KeyCode.SPACE)){
            togglePauseContinueGame();
        }else if(userInput.contains(KeyCode.F1) && userInput.contains(KeyCode.SHIFT) && userInput.contains(KeyCode.F1)){
            animationTimer.stop();
            toggle = false;
            showDebugConsole();
        }
    }

    /**
     * this method is used to let the bot control the paddle instead of the player playing it.
     */
    public void automation(){
        if(game.isBotMode()){
            if(game.getBall().getBounds().getMinX() > game.getPlayer().getPlayerCenterPosition().getX())
                game.getPlayer().moveRight();
            else
                game.getPlayer().moveLeft();
        }
    }

    /**
     * this method is used to check if there is an impact for the ball with any entity or the sides of the screen. which will cause a reaction to the ball and the brick.
     */
    public void findImpacts(){
        if(impact(game.getBall(),game.getPlayer())){
            game.getBall().setYSpeed(-game.getBall().getYSpeed());
            if(getRnd().nextBoolean() && game.getBall().getYSpeed() > -4){
                game.getBall().setYSpeed(game.getBall().getYSpeed()-1);
            }
            else if(getRnd().nextBoolean() && game.getBall().getYSpeed() < -1){
                game.getBall().setYSpeed(game.getBall().getYSpeed()+1);
            }
        }

        else if(impactWall()){
            game.setBrickCount(game.getBrickCount()-1);
        }

        if((game.getBall().getBounds().getMinX() < scene.getX()) ||(game.getBall().getBounds().getMaxX() > (scene.getX() + scene.getWidth()))) {
            game.getBall().setXSpeed(-game.getBall().getXSpeed());
            if(getRnd().nextBoolean() && (game.getBall().getXSpeed() > -4 && game.getBall().getXSpeed() < 4) ){
                if(game.getBall().getXSpeed() < 0)
                    game.getBall().setXSpeed(game.getBall().getXSpeed()-1);
                else
                    game.getBall().setXSpeed(game.getBall().getXSpeed()+1);
            }else if(getRnd().nextBoolean()){
                if(game.getBall().getXSpeed() < -1)
                    game.getBall().setXSpeed(game.getBall().getXSpeed()+1);
                else if(game.getBall().getXSpeed() > 1)
                    game.getBall().setXSpeed(game.getBall().getXSpeed()-1);
            }
        }

        if(game.getBall().getBounds().getMinY() < scene.getY()){
            game.getBall().setYSpeed(-game.getBall().getYSpeed());

            if(getRnd().nextBoolean() && game.getBall().getYSpeed() < 4)
                game.getBall().setYSpeed(game.getBall().getYSpeed()+1);
            else if(getRnd().nextBoolean() && game.getBall().getYSpeed() > 1)
                game.getBall().setYSpeed(game.getBall().getYSpeed()-1);
        }
        else if(game.getBall().getBounds().getMinY() > scene.getY() + scene.getHeight()){
            game.setBallCount(game.getBallCount() - 1);
            game.getBall().resetPosition();
            game.getBall().setRandomBallSpeed();
            game.setBallLost(true);
        }
    }

    private boolean impact(Ball ball, Entities object){
        if(ball.getBounds().equals(object.getBounds())){
            return true;
        }
        return false;
    }

    /**
     * this is to check if the ball comes in contact with any side of the brick.
     *
     * @return returns a boolean value if or if it doesn't touch any entity.
     */
    boolean impactWall(){
        for(Brick b : game.getBricks()){
            if(b.findImpact(game.getBall()) == game.getUP_IMPACT()){
                game.getBall().setYSpeed(-game.getBall().getYSpeed());
                return b.setImpact(new Point2D(game.getBall().getBounds().getMaxX()-game.getBall().getBounds().getHeight(),game.getBall().getBounds().getMaxY()), Crack.getUP());
            }
            else if (b.findImpact(game.getBall()) == game.getDOWN_IMPACT()){
                game.getBall().setYSpeed(-game.getBall().getYSpeed());
                return b.setImpact(new Point2D(game.getBall().getBounds().getMinX()+game.getBall().getBounds().getWidth(),game.getBall().getBounds().getMinY()),Crack.getDOWN());
            }
            else if(b.findImpact(game.getBall()) == game.getLEFT_IMPACT()){
                game.getBall().setXSpeed(-game.getBall().getXSpeed());
                return b.setImpact(new Point2D(game.getBall().getBounds().getMaxX(),game.getBall().getBounds().getMaxY()-game.getBall().getBounds().getHeight()),Crack.getRIGHT());
            }
            else if(b.findImpact(game.getBall()) == game.getRIGHT_IMPACT()){
                game.getBall().setXSpeed(-game.getBall().getXSpeed());
                return b.setImpact(new Point2D(game.getBall().getBounds().getMinX(),game.getBall().getBounds().getMaxY()-game.getBall().getBounds().getHeight()), Crack.getLEFT());
            }
        }
        return false;
    }

    private void pauseButtonClicked(){

    }

    private void drawBall(Ball ball){
        graphicsContext.setFill(ball.getInnerBallColor());
        graphicsContext.fillOval(ball.getBounds().getMinX(), ball.getBounds().getMinY(), ball.getRadius(), ball.getRadius());

        graphicsContext.setStroke(ball.getBorderBallColor());
        graphicsContext.strokeOval(ball.getBounds().getMinX()-1, ball.getBounds().getMinY()-1, ball.getRadius()+2, ball.getRadius()+2);
    }

    private void drawPlayer(Player player) {
        graphicsContext.setFill(player.getInnerColor());
        graphicsContext.fillRect(player.getBounds().getMinX(),player.getBounds().getMinY(),player.getWidth(),player.getHeight());

        graphicsContext.setStroke(player.getBorderColor());
        graphicsContext.strokeRect(player.getBounds().getMinX()-1,player.getBounds().getMinY()-1,player.getWidth()+2,player.getHeight()+2);
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

    public Random getRnd() {
        return rnd;
    }

    public void setRnd(Random rnd) {
        this.rnd = rnd;
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
        debugConsole.initOwner(anchorPane.getScene().getWindow());
        debugConsole.initModality(Modality.WINDOW_MODAL);
        debugConsole.showAndWait();
    }

    public void showPauseMenu(){

    }
}
