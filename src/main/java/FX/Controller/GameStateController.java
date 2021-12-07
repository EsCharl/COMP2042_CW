package FX.Controller;

import FX.Model.Entities.Ball.Ball;
import FX.Model.Entities.Brick.Brick;
import FX.Model.Entities.Brick.Crack;
import FX.Model.Entities.Brick.Crackable;
import FX.Model.Entities.Entities;
import FX.Model.Game;
import FX.Model.GameScore;
import FX.Model.Entities.Player;

import FX.View.GameScoreDisplay;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
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
    private AnimationTimer animationTimer;
    private Scene scene;
    private Random rnd;
    private ArrayList<KeyCode> userInput;

    private Image backgroundImage;

    @FXML private Canvas gameBoard;
    @FXML private AnchorPane anchorPane;
    @FXML private Text gameText;

    boolean toggle = true;

    public GameStateController() {

        setRnd(new Random());
        userInput = new ArrayList<>();

        game = Game.singletonGame();
        gameScore = GameScore.singletonGameScore();
        gameScoreDisplay = new GameScoreDisplay();

        game.setShowPauseMenu(false);

        gameScore.setLevelFilePathName("/scores/Level"+ game.getCurrentLevel()+".txt");
        backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/GameImage.png")));
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){

        graphicsContext = gameBoard.getGraphicsContext2D();

        gameScore.startTimer();
        gameScore.setCanGetTime(true);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                graphicsContext.drawImage(backgroundImage,0,0);

                graphicsContext.setLineWidth(2);

                gameText.setText(String.format("Bricks: %d Balls %d", game.getBrickCount(), game.getBallCount()));

                drawBricks();

                drawBall(game.getBall());
                drawPlayer(game.getPlayer());

                if(game.isBallLost()){
                    gameScore.pauseTimer();
                    gameScore.setCanGetTime(false);
                    game.setBallLost(false);
                    toggle = false;
                    if(game.isGameOver()){
                        game.resetBallCount();
                        gameText.setText("Game Over. Time spent in this level: " + gameScore.getTimerString());
                        gameScore.restartTimer();
                    }
                    stop();
                }

                scene = anchorPane.getScene();

                Stage stage = (Stage) gameBoard.getScene().getWindow();

                windowLostFocus(stage);

                keyPressed();

                movementKeyHandler(userInput);

                keyReleased();

                automation();

                game.getBall().move();
                game.getPlayer().move();

                findImpacts();

                if(game.isLevelComplete()){
                    gameScore.pauseTimer();
                    gameScore.setCanGetTime(false);
                    gameScoreHandler();
                    if(game.hasLevel()){
                        gameText.setText("Go to Next Level");
                        animationTimer.stop();
                        game.nextLevel();
                        gameScore.restartTimer();
                        gameScore.setLevelFilePathName("/scores/Level"+ game.getCurrentLevel()+".txt");
                    }
                    else{
                        gameText.setText("ALL WALLS DESTROYED");
                        animationTimer.stop();
                    }
                    restartGameStatus();
                }
            }

            /**
             * this method is used when the user pressed on a key on the keyboard.
             */
            private void keyPressed() {
                scene.setOnKeyPressed(keyEvent ->{
                    if(!userInput.contains(keyEvent.getCode()))
                        userInput.add(keyEvent.getCode());
                });
            }

            /**
             * this method is used when the user released a key on the keyboard.
             */
            private void keyReleased() {
                scene.setOnKeyReleased(keyEvent -> {
                    nonMovementKeyHandler(userInput);
                    while(userInput.contains(keyEvent.getCode()))
                        userInput.remove(0);
                });
            }

            /**
             * this method is used when the user clicked out or the window lost focus
             *
             * @param stage this is the stage (window) that is being listened
             */
            private void windowLostFocus(Stage stage) {
                stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                        if(gameScore.isCanGetTime())
                            gameScore.pauseTimer();
                        gameScore.setCanGetTime(false);
                        animationTimer.stop();
                        toggle = false;
                    }
                });
            }

            /**
             * this method is used to draw the Bricks.
             */
            private void drawBricks() {
                for (int i = 0; i < game.getBrickLevels()[game.getCurrentLevel()-1].length; i++){
                    if(!game.getBrickLevels()[game.getCurrentLevel()-1][i].isBroken()){
                        graphicsContext.setFill(game.getBrickLevels()[game.getCurrentLevel()-1][i].getInnerColor());
                        graphicsContext.fillRect(game.getBrickLevels()[game.getCurrentLevel()-1][i].getBounds().getMinX(),game.getBrickLevels()[game.getCurrentLevel()-1][i].getBounds().getMinY(),game.getBrickLevels()[game.getCurrentLevel()-1][i].getWidth(),game.getBrickLevels()[game.getCurrentLevel()-1][i].getHeight());

                        graphicsContext.setStroke(game.getBrickLevels()[game.getCurrentLevel()-1][i].getBorderColor());
                        graphicsContext.strokeRect(game.getBrickLevels()[game.getCurrentLevel()-1][i].getBounds().getMinX()-1,game.getBrickLevels()[game.getCurrentLevel()-1][i].getBounds().getMinY()-1,game.getBrickLevels()[game.getCurrentLevel()-1][i].getWidth()+2,game.getBrickLevels()[game.getCurrentLevel()-1][i].getHeight()+2);

                        showCrack();
                    }
                }
            }

            /**
             * this method is used to draw the ball object on the screen.
             *
             * @param ball this is the ball object used to draw on the window.
             */
            private void drawBall(Ball ball){
                graphicsContext.setFill(ball.getInnerBallColor());
                graphicsContext.fillOval(ball.getBounds().getMinX(), ball.getBounds().getMinY(), ball.getRadius(), ball.getRadius());

                graphicsContext.setStroke(ball.getBorderBallColor());
                graphicsContext.strokeOval(ball.getBounds().getMinX()-1, ball.getBounds().getMinY()-1, ball.getRadius()+2, ball.getRadius()+2);
            }

            /**
             * this method is used to draw the player object to the screen.
             *
             * @param player this is the player object which contains the information about the player position and size needed to draw the object.
             */
            private void drawPlayer(Player player) {
                graphicsContext.setFill(player.getInnerColor());
                graphicsContext.fillRect(player.getBounds().getMinX(),player.getBounds().getMinY(),player.getWidth(),player.getHeight());

                graphicsContext.setStroke(player.getBorderColor());
                graphicsContext.strokeRect(player.getBounds().getMinX()-1,player.getBounds().getMinY()-1,player.getWidth()+2,player.getHeight()+2);
            }
        };

        animationTimer.start();

        anchorPane.requestFocus();
    }

    /**
     * this method is used to display and save the game score.
     */
    private void gameScoreHandler() {
        try {
            gameScore.setLastLevelCompletionRecord(gameScore.getHighScore());
            gameScore.updateSaveFile(gameScore.getLastLevelCompletionRecord());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        gameScoreDisplay.generateLevelCompleteWindow(gameScore.getLastLevelCompletionRecord(), gameScore.getTimerString());
    }

    private void restartGameStatus() {
        game.getBall().resetPosition();
        game.getPlayer().resetPosition();
        game.getBall().setRandomBallSpeed();
    }

    private void movementKeyHandler(ArrayList<KeyCode> userInput){
        if(userInput.contains(KeyCode.A) && userInput.contains(KeyCode.D) || userInput.isEmpty()){
            game.getPlayer().setMoveAmount(0);
        }else if(userInput.contains(KeyCode.A)){
            game.getPlayer().setMoveAmount(-game.getPlayer().getDEF_MOVE_AMOUNT());
        }
        else if(userInput.contains(KeyCode.D)){
            game.getPlayer().setMoveAmount(game.getPlayer().getDEF_MOVE_AMOUNT());
        }
    }

    private boolean canGetTime = true;

    /**
     * this method is used to deal with non movement features, like pause menu, show debug console, pause and resume of the game.
     */
    private void nonMovementKeyHandler(ArrayList<KeyCode> userInput){
        if(userInput.contains(KeyCode.ESCAPE)){
            if(gameScore.isCanGetTime()){
                gameScore.pauseTimer();
                gameScore.setCanGetTime(false);
            }
            animationTimer.stop();
            showPauseMenu();
        }else if(userInput.contains(KeyCode.SPACE)){
            togglePauseContinueGame();
        }else if(userInput.contains(KeyCode.F1) && userInput.contains(KeyCode.SHIFT) && userInput.contains(KeyCode.ALT)){
            if(gameScore.isCanGetTime()){
                gameScore.pauseTimer();
                gameScore.setCanGetTime(false);
            }
            animationTimer.stop();
            toggle = false;
            showDebugConsole();
        }else if(userInput.contains(KeyCode.H)){
            game.setBotMode(!game.isBotMode());
        }
    }

    /**
     * this method is used to let the bot control the paddle instead of the player playing it.
     */
    public void automation(){
        if(game.isBotMode()){
            if(game.getBall().getBounds().getMinX() > game.getPlayer().getBounds().getMinX() + game.getPlayer().getBounds().getWidth()/2){
                game.getPlayer().setMoveAmount(game.getPlayer().getDEF_MOVE_AMOUNT());
            }
            else{
                game.getPlayer().setMoveAmount(-game.getPlayer().getDEF_MOVE_AMOUNT());
            }
        }
    }

    /**
     * this method is used to draw the cracks on the brick.
     */
    private void showCrack(){
        for (int i = 0; i < game.getBrickLevels()[game.getCurrentLevel()-1].length; i++) {
            if (game.getBrickLevels()[game.getCurrentLevel() - 1][i] instanceof Crackable) {
                if (((Crackable) game.getBrickLevels()[game.getCurrentLevel() - 1][i]).getCrackPath() != null) {
                    Path path = ((Crackable) game.getBrickLevels()[game.getCurrentLevel() - 1][i]).getCrackPath();
                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.beginPath();
                    graphicsContext.moveTo(((MoveTo) path.getElements().get(0)).getX(), ((MoveTo) path.getElements().get(0)).getY());
                    for (int x = 1; x < path.getElements().size(); x++) {
                        graphicsContext.lineTo(((LineTo) path.getElements().get(x)).getX(), ((LineTo) path.getElements().get(x)).getY());
                    }
                    graphicsContext.closePath();
                }
            }
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

        if((game.getBall().getBounds().getMinX() < gameBoard.getBoundsInLocal().getMinX())){
            if (game.getBall().getXSpeed() < 0){
                game.getBall().setXSpeed(-game.getBall().getXSpeed());
                if(getRnd().nextBoolean() && game.getBall().getXSpeed() < 4){
                    game.getBall().setXSpeed(game.getBall().getXSpeed()+1);
                }else if(getRnd().nextBoolean() && game.getBall().getXSpeed() > 1){
                    game.getBall().setXSpeed(game.getBall().getXSpeed()-1);
                }
            }
        }else if(game.getBall().getBounds().getMaxX() > gameBoard.getBoundsInLocal().getMaxX()){
            if (game.getBall().getXSpeed() > 0){
                game.getBall().setXSpeed(-game.getBall().getXSpeed());
                if(getRnd().nextBoolean() && (game.getBall().getXSpeed() > -4)){
                    game.getBall().setXSpeed(game.getBall().getXSpeed()-1);
                }else if(getRnd().nextBoolean() && game.getBall().getXSpeed() < -1){
                    game.getBall().setXSpeed(game.getBall().getXSpeed()+1);
                }
            }
        }

        if(game.getBall().getBounds().getMinY() < gameBoard.getBoundsInLocal().getMinY()){
            game.getBall().setYSpeed(-game.getBall().getYSpeed());

            if(getRnd().nextBoolean() && game.getBall().getYSpeed() < 4)
                game.getBall().setYSpeed(game.getBall().getYSpeed()+1);
            else if(getRnd().nextBoolean() && game.getBall().getYSpeed() > 1)
                game.getBall().setYSpeed(game.getBall().getYSpeed()-1);
        }
        else if(game.getBall().getBounds().getMaxY() > gameBoard.getBoundsInLocal().getMaxY()){
            game.setBallCount(game.getBallCount() - 1);
            game.getPlayer().resetPosition();
            game.getBall().resetPosition();
            game.getBall().setRandomBallSpeed();
            game.setBallLost(true);
        }
    }

    /**
     * this method is used to check if there is a collision occurring between the ball and another object entity.
     *
     * @param ball this is the ball used to check if there is a collision.
     * @param object this is the object used to see if it is going to get collided.
     * @return this returns a boolean value if collision occur
     */
    private boolean impact(Ball ball, Entities object){
        return ball.getBounds().intersects(object.getBounds());
    }

    /**
     * this is to check if the ball comes in contact with any side of the brick.
     *
     * @return returns a boolean value if or if it doesn't touch any entity.
     */
    private boolean impactWall(){
        for(Brick b : game.getBricks()){
            if(!b.isBroken()){
                if(b.getBounds().contains(game.getBall().getBounds().getMinX()+game.getBall().getBounds().getWidth()/2, game.getBall().getBounds().getMaxY())){
                    game.getBall().setYSpeed(-game.getBall().getYSpeed());
                    return b.setImpact(new Point2D(game.getBall().getBounds().getMaxX()-game.getBall().getBounds().getHeight(),game.getBall().getBounds().getMaxY()), Crack.getUP());
                }
                else if (b.getBounds().contains(game.getBall().getBounds().getMinX()+game.getBall().getBounds().getWidth()/2,game.getBall().getBounds().getMinY())){
                    game.getBall().setYSpeed(-game.getBall().getYSpeed());
                    return b.setImpact(new Point2D(game.getBall().getBounds().getMinX()+game.getBall().getBounds().getWidth(),game.getBall().getBounds().getMinY()), Crack.getDOWN());
                }
                else if(b.getBounds().contains(game.getBall().getBounds().getMaxX(),game.getBall().getBounds().getMinY()+game.getBall().getBounds().getHeight()/2)){
                    game.getBall().setXSpeed(-game.getBall().getXSpeed());
                    return b.setImpact(new Point2D(game.getBall().getBounds().getMaxX(),game.getBall().getBounds().getMaxY()-game.getBall().getBounds().getHeight()), Crack.getRIGHT());
                }
                else if(b.getBounds().contains(game.getBall().getBounds().getMinX(),game.getBall().getBounds().getMinY()+game.getBall().getBounds().getHeight()/2)){
                    game.getBall().setXSpeed(-game.getBall().getXSpeed());
                    return b.setImpact(new Point2D(game.getBall().getBounds().getMinX(),game.getBall().getBounds().getMaxY()-game.getBall().getBounds().getHeight()), Crack.getLEFT());
                }
            }
        }
        return false;
    }

    /**
     * this method is used to toggle between pausing the game and proceeding th game
     */
    public void togglePauseContinueGame(){
        if(toggle){
            animationTimer.stop();
            gameScore.pauseTimer();
            gameScore.setCanGetTime(true);
            toggle = false;
        }
        else{
            animationTimer.start();
            gameScore.startTimer();
            gameScore.setCanGetTime(true);
            toggle = true;
        }
    }

    /**
     * this method is used to get the random object used to have some randomness in the game.
     * @return
     */
    public Random getRnd() {
        return rnd;
    }

    /**
     * this method is used to set a random object used for randomness of the game.
     *
     * @param rnd this is the random object used to set into the variable.
     */
    public void setRnd(Random rnd) {
        this.rnd = rnd;
    }

    /**
     * this method is used to show a debug console.
     */
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

    /**
     * this method is used to show the pause menu on the screen.
     */
    public void showPauseMenu(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/FX/PauseMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!game.isShowPauseMenu()){
            anchorPane.getChildren().add(root);
            game.setShowPauseMenu(true);
        }
    }
}
