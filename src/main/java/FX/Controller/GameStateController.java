/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2021  Leong Chang Yung
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package FX.Controller;

import FX.GameStart;
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
import javafx.scene.media.AudioClip;
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
import java.util.ResourceBundle;

public class GameStateController implements Initializable {

    private Game game;
    private GameScore gameScore;
    private GraphicsContext graphicsContext;
    private GameScoreDisplay gameScoreDisplay;
    private AnimationTimer animationTimer;
    private Scene scene;
    private ArrayList<KeyCode> userInput;
    private AudioClip clayBrickCollisionSound1;
    private AudioClip clayBrickCollisionSound2;
    private AudioClip gameWindowCollisionSound1;
    private AudioClip gameWindowCollisionSound2;
    private AudioClip ballPlayerCollisionSound1;
    private AudioClip ballPlayerCollisionSound2;
    private AudioClip steelBrickCollisionSound1;
    private AudioClip steelBrickCollisionSound2;
    private AudioClip cementBrickCollisionSound;
    private AudioClip cementBrickDestroyedSound;

    private AudioClip victorySound;
    private AudioClip lostSound;

    private Image backgroundImage;

    @FXML private Canvas gameBoard;
    @FXML private AnchorPane anchorPane;
    @FXML private Text gameText;

    boolean toggle = true;

    public GameStateController() {
        userInput = new ArrayList<>();

        game = Game.singletonGame();
        gameScore = GameScore.singletonGameScore();
        gameScoreDisplay = new GameScoreDisplay();

        game.setShowPauseMenu(false);

        gameScore.setLevelFilePathName("/scores/Level"+ game.getCurrentLevel()+".txt");
        backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/GameImage.png")));

        clayBrickCollisionSound1 = new AudioClip(getClass().getResource("/SoundEffects/ClayImpactSound1.mp3").toExternalForm());
        clayBrickCollisionSound2 = new AudioClip(getClass().getResource("/SoundEffects/ClayImpactSound2.mp3").toExternalForm());
        steelBrickCollisionSound1 = new AudioClip(getClass().getResource("/SoundEffects/SteelImpactSound1.mp3").toExternalForm());
        steelBrickCollisionSound2 = new AudioClip(getClass().getResource("/SoundEffects/SteelImpactSound2.mp3").toExternalForm());
        victorySound = new AudioClip(getClass().getResource("/SoundEffects/VictorySound.wav").toExternalForm());
        lostSound = new AudioClip(getClass().getResource("/SoundEffects/DefeatSound.wav").toExternalForm());
        gameWindowCollisionSound1 = new AudioClip(getClass().getResource("/SoundEffects/WindowImpact1.mp3").toExternalForm());
        gameWindowCollisionSound2 = new AudioClip(getClass().getResource("/SoundEffects/WindowImpact2.mp3").toExternalForm());
        ballPlayerCollisionSound1 = new AudioClip(getClass().getResource("/SoundEffects/BallPlayerImpactSound1.mp3").toExternalForm());
        ballPlayerCollisionSound2 = new AudioClip(getClass().getResource("/SoundEffects/BallPlayerImpactSound2.mp3").toExternalForm());
        cementBrickCollisionSound = new AudioClip(getClass().getResource("/SoundEffects/CementImpactSound.wav").toExternalForm());
        cementBrickDestroyedSound = new AudioClip(getClass().getResource("/SoundEffects/CementBreak.mp3").toExternalForm());
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
                        GameStart.audio.pause();
                        lostSound.play();
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

                keyReleased(userInput);

                automation();

                game.getBall().move();
                game.getPlayer().move();

                findImpacts();

                if(game.isLevelComplete()){
                    gameScore.pauseTimer();
                    gameScore.setCanGetTime(false);
                    gameScoreHandler();
                    GameStart.audio.pause();
                    victorySound.play();
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
                    game.getBall().resetPosition();
                    game.getPlayer().resetPosition();
                    game.getBall().setRandomBallSpeed();
                }
            }

            /**
             * this method is used to add user inputs into an array.
             */
            private void keyPressed() {
                scene.setOnKeyPressed(keyEvent ->{
                    if(!userInput.contains(keyEvent.getCode()))
                        userInput.add(keyEvent.getCode());
                });
            }

            /**
             * this method is used to remove a key from the user input array only when the user releases the key, which will check if there are any keys that are pressed which will activate the menu, atc. and finally remove the user input from an array.
             *
             * @param userInput this is the array of user input.
             */
            private void keyReleased(ArrayList<KeyCode> userInput) {
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
                stage.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
                    if (gameScore.isCanGetTime())
                        gameScore.pauseTimer();
                    gameText.setText("Focus Lost");
                    gameScore.setCanGetTime(false);
                    animationTimer.stop();
                    toggle = false;
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

    /**
     * this method is used to get the user input and act accordingly based on the inputs provided in the array.
     *
     * @param userInput this is the array of user input.
     */
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
            if (game.getBrickLevels()[game.getCurrentLevel() - 1][i] instanceof Crackable && !game.getBrickLevels()[game.getCurrentLevel() - 1][i].isBroken()) {
                if (((Crackable) game.getBrickLevels()[game.getCurrentLevel() - 1][i]).getCrackPath() != null) {
                    Path path = ((Crackable) game.getBrickLevels()[game.getCurrentLevel() - 1][i]).getCrackPath();
                    graphicsContext.setStroke(game.getBrickLevels()[game.getCurrentLevel()-1][i].getBorderColor().darker());
                    graphicsContext.beginPath();
                    graphicsContext.moveTo(((MoveTo) path.getElements().get(0)).getX(), ((MoveTo) path.getElements().get(0)).getY());
                    for (int x = 1; x < path.getElements().size(); x++) {
                        graphicsContext.lineTo(((LineTo) path.getElements().get(x)).getX(), ((LineTo) path.getElements().get(x)).getY());
                    }
                    graphicsContext.closePath();
                    graphicsContext.stroke();
                }
            }
        }
    }

    /**
     * this method is used to check if there is an impact for the ball with any entity or the sides of the screen. which will cause a reaction to the ball and the brick.
     */
    public void findImpacts(){
        if(impact(game.getBall(),game.getPlayer())){
            ballBottomCollision();
            ballCollisionRandomSound(ballPlayerCollisionSound1, ballPlayerCollisionSound2);
        }
        else if(impactWall()){
            game.setBrickCount(game.getBrickCount()-1);
        }
        if((game.getBall().getBounds().getMinX() < gameBoard.getBoundsInLocal().getMinX())){
            if (game.getBall().getXSpeed() < 0){
                ballCollisionRandomSound(gameWindowCollisionSound1, gameWindowCollisionSound2);
                ballLeftCollision();
            }
        }else if(game.getBall().getBounds().getMaxX() > gameBoard.getBoundsInLocal().getMaxX()){
            if (game.getBall().getXSpeed() > 0){
                ballCollisionRandomSound(gameWindowCollisionSound1, gameWindowCollisionSound2);
                ballRightCollision();
            }
        }

        if(game.getBall().getBounds().getMinY() < gameBoard.getBoundsInLocal().getMinY()){
            ballTopCollision();
            ballCollisionRandomSound(gameWindowCollisionSound1, gameWindowCollisionSound2);
        }
        else if(game.getBall().getBounds().getMaxY() > gameBoard.getBoundsInLocal().getMaxY()){
            game.setBallCount(game.getBallCount() - 1);
            game.getBall().resetPosition();
            game.getPlayer().resetPosition();
            game.getBall().setRandomBallSpeed();
            game.setBallLost(true);
        }
    }

    /**
     * this method is used to play a sound effect when the brick collides with a brick.
     *
     * @param b this is the brick which is being collided by the ball.
     */
    private void playBrickSoundEffect(Brick b){
        switch (b.getBrickName()){
            case "Clay Brick":
                ballCollisionRandomSound(clayBrickCollisionSound1, clayBrickCollisionSound2);
                break;
            case "Reinforced Steel Brick":
            case "Steel Brick":
                ballCollisionRandomSound(steelBrickCollisionSound1, steelBrickCollisionSound2);
                break;
            case "Cement Brick":
                if(!b.isBroken())
                    cementBrickCollisionSound.play();
                else
                    cementBrickDestroyedSound.play();
                break;
            default:
        }
    }

    /**
     * this method is used to play a random sound effect when there is a collision from the ball.
     *
     * @param soundEffect1 this is one of the sound effect which might be selected based on a random probability
     * @param soundEffect2 this is the other sound effect which might be selected based on a random probability
     */
    private void ballCollisionRandomSound(AudioClip soundEffect1, AudioClip soundEffect2) {
        if (game.getRnd().nextBoolean())
            soundEffect1.play();
        else
            soundEffect2.play();
    }

    /**
     * this method is used to change the direction of the ball to another direction on the x-axis with an increment of a decrement in speed if it is being collided on the left side of the ball.
     */
    private void ballLeftCollision() {
        game.getBall().setXSpeed(-game.getBall().getXSpeed());
        if(game.getRnd().nextBoolean() && game.getBall().getXSpeed() < 4){
            game.getBall().setXSpeed(game.getBall().getXSpeed()+1);
        }else if(game.getRnd().nextBoolean() && game.getBall().getXSpeed() > 1){
            game.getBall().setXSpeed(game.getBall().getXSpeed()-1);
        }
    }

    /**
     * this method is used to change the direction of the ball to another direction on the x-axis with an increment of a decrement in speed if it is being collided on the right side of the ball.
     */
    private void ballRightCollision() {
        game.getBall().setXSpeed(-game.getBall().getXSpeed());
        if(game.getRnd().nextBoolean() && (game.getBall().getXSpeed() > -4)){
            game.getBall().setXSpeed(game.getBall().getXSpeed()-1);
        }else if(game.getRnd().nextBoolean() && game.getBall().getXSpeed() < -1){
            game.getBall().setXSpeed(game.getBall().getXSpeed()+1);
        }
    }

    /**
     * this method is used to change the direction of the ball to another direction on the y-axis with an increment of a decrement in speed if it is being collided on the top side of the ball.
     */
    private void ballTopCollision() {
        game.getBall().setYSpeed(-game.getBall().getYSpeed());
        if(game.getRnd().nextBoolean() && game.getBall().getYSpeed() < 4)
            game.getBall().setYSpeed(game.getBall().getYSpeed()+1);
        else if(game.getRnd().nextBoolean() && game.getBall().getYSpeed() > 1)
            game.getBall().setYSpeed(game.getBall().getYSpeed()-1);
    }

    /**
     * this method is used to change the direction of the ball to another direction on the y-axis with an increment of a decrement in speed if it is being collided on the bottom side of the ball.
     */
    private void ballBottomCollision() {
        game.getBall().setYSpeed(-game.getBall().getYSpeed());
        if (game.getRnd().nextBoolean() && game.getBall().getYSpeed() > -4) {
            game.getBall().setYSpeed(game.getBall().getYSpeed() - 1);
        } else if (game.getRnd().nextBoolean() && game.getBall().getYSpeed() < -1) {
            game.getBall().setYSpeed(game.getBall().getYSpeed() + 1);
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
     * this method change the ball direction and also returns true if the ball comes in contact with any side of the brick.
     *
     * @return returns a boolean value if or if it doesn't touch any entity.
     */
    private boolean impactWall(){
        for(Brick b : game.getBricks()){
            if(!b.isBroken()){
                if(b.getBounds().contains(game.getBall().getBounds().getMinX()+game.getBall().getBounds().getWidth()/2, game.getBall().getBounds().getMaxY())){
                    ballBottomCollision();
                    playBrickSoundEffect(b);
                    return b.setImpact(new Point2D(game.getBall().getBounds().getMaxX()-game.getBall().getBounds().getHeight(),game.getBall().getBounds().getMaxY()), Crack.getUP());
                }
                else if (b.getBounds().contains(game.getBall().getBounds().getMinX()+game.getBall().getBounds().getWidth()/2,game.getBall().getBounds().getMinY())){
                    ballTopCollision();
                    playBrickSoundEffect(b);
                    return b.setImpact(new Point2D(game.getBall().getBounds().getMinX()+game.getBall().getBounds().getWidth(),game.getBall().getBounds().getMinY()), Crack.getDOWN());
                }
                else if(b.getBounds().contains(game.getBall().getBounds().getMaxX(),game.getBall().getBounds().getMinY()+game.getBall().getBounds().getHeight()/2)){
                    ballLeftCollision();
                    playBrickSoundEffect(b);
                    return b.setImpact(new Point2D(game.getBall().getBounds().getMaxX(),game.getBall().getBounds().getMaxY()-game.getBall().getBounds().getHeight()), Crack.getRIGHT());
                }
                else if(b.getBounds().contains(game.getBall().getBounds().getMinX(),game.getBall().getBounds().getMinY()+game.getBall().getBounds().getHeight()/2)){
                    ballRightCollision();
                    playBrickSoundEffect(b);
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
            victorySound.stop();
            GameStart.audio.play();
            animationTimer.start();
            gameScore.startTimer();
            gameScore.setCanGetTime(true);
            toggle = true;
        }
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
