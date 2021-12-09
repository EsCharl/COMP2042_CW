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
import FX.Model.Entities.Brick.Crackable;
import FX.Model.Entities.Entities;
import FX.Model.GameData;
import FX.Model.GameScore;
import FX.Model.Entities.Player;

import FX.Model.SoundEffects;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
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
import java.util.ResourceBundle;

public class GameStateController implements Initializable {

    GameData gameData;
    private GameScore gameScore;
    private GameScoreDisplay gameScoreDisplay;
    private SoundEffects soundEffects;

    private GraphicsContext graphicsContext;
    private AnimationTimer animationTimer;
    private Scene scene;

    private ArrayList<KeyCode> userInput;

    @FXML private Canvas gameBoard;
    @FXML private AnchorPane anchorPane;
    @FXML private Text gameText;

    public GameStateController() {
        soundEffects = new SoundEffects();
        userInput = new ArrayList<>();

        gameData = GameData.singletonGame();
        gameScore = GameScore.singletonGameScore();
        gameScoreDisplay = new GameScoreDisplay();

        gameData.setShowPauseMenu(false);

        gameScore.setLevelFilePathName("/scores/Level"+ gameData.getCurrentLevel()+".txt");
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){

        graphicsContext = gameBoard.getGraphicsContext2D();

        gameScore.startTimer();
        gameScore.setCanGetTime(true);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                graphicsContext.drawImage(gameData.getBackgroundImage(),0,0);

                graphicsContext.setLineWidth(2);

                gameText.setText(String.format("Bricks: %d Balls %d", gameData.getBrickCount(), gameData.getBallCount()));

                drawBricks();

                drawBall(gameData.getBall());
                drawPlayer(gameData.getPlayer());

                if(gameData.isBallLost()){
                    gameScore.pauseTimer();
                    gameScore.setCanGetTime(false);
                    gameData.setBallLost(false);
                    gameData.setPauseMode(false);
                    if(gameData.isGameOver()){
                        GameStart.audio.pause();
                        soundEffects.getLostSound().play();
                        gameData.resetBallCount();
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

                gameData.getBall().move();
                gameData.getPlayer().move();

                findImpacts();

                if(gameData.isLevelComplete()){
                    gameScore.pauseTimer();
                    gameScore.setCanGetTime(false);
                    gameScoreHandler();
                    GameStart.audio.pause();
                    soundEffects.getVictorySound().play();
                    if(gameData.getCurrentLevel() < gameData.getBrickLevels().length){
                        gameText.setText("Go to Next Level");
                        animationTimer.stop();
                        gameData.nextLevel();
                        gameScore.restartTimer();
                        gameScore.setLevelFilePathName("/scores/Level"+ gameData.getCurrentLevel()+".txt");
                    }
                    else{
                        gameText.setText("ALL WALLS DESTROYED");
                        animationTimer.stop();
                    }
                    gameData.getBall().resetPosition();
                    gameData.getPlayer().resetPosition();
                    gameData.setRandomBallSpeed(gameData.getBall());
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
                    gameData.setPauseMode(false);
                });
            }

            /**
             * this method is used to draw the Bricks.
             */
            private void drawBricks() {
                for (int i = 0; i < gameData.getBrickLevels()[gameData.getCurrentLevel()-1].length; i++){
                    if(!gameData.getBrickLevels()[gameData.getCurrentLevel()-1][i].isBroken()){
                        graphicsContext.setFill(gameData.getBrickLevels()[gameData.getCurrentLevel()-1][i].getInnerColor());
                        graphicsContext.fillRect(gameData.getBrickLevels()[gameData.getCurrentLevel()-1][i].getBounds().getMinX(), gameData.getBrickLevels()[gameData.getCurrentLevel()-1][i].getBounds().getMinY(), gameData.getBrickLevels()[gameData.getCurrentLevel()-1][i].getWidth(), gameData.getBrickLevels()[gameData.getCurrentLevel()-1][i].getHeight());

                        graphicsContext.setStroke(gameData.getBrickLevels()[gameData.getCurrentLevel()-1][i].getBorderColor());
                        graphicsContext.strokeRect(gameData.getBrickLevels()[gameData.getCurrentLevel()-1][i].getBounds().getMinX()-1, gameData.getBrickLevels()[gameData.getCurrentLevel()-1][i].getBounds().getMinY()-1, gameData.getBrickLevels()[gameData.getCurrentLevel()-1][i].getWidth()+2, gameData.getBrickLevels()[gameData.getCurrentLevel()-1][i].getHeight()+2);

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
                graphicsContext.setFill(ball.getInnerColor());
                graphicsContext.fillOval(ball.getBounds().getMinX(), ball.getBounds().getMinY(), ball.getRadius(), ball.getRadius());

                graphicsContext.setStroke(ball.getBorderColor());
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

            /**
             * this method is used to draw the cracks on the brick.
             */
            private void showCrack(){
                for (int i = 0; i < gameData.getBrickLevels()[gameData.getCurrentLevel()-1].length; i++) {
                    if (gameData.getBrickLevels()[gameData.getCurrentLevel() - 1][i] instanceof Crackable && !gameData.getBrickLevels()[gameData.getCurrentLevel() - 1][i].isBroken()) {
                        if (((Crackable) gameData.getBrickLevels()[gameData.getCurrentLevel() - 1][i]).getCrackPath() != null) {
                            Path path = ((Crackable) gameData.getBrickLevels()[gameData.getCurrentLevel() - 1][i]).getCrackPath();
                            graphicsContext.setStroke(gameData.getBrickLevels()[gameData.getCurrentLevel()-1][i].getBorderColor().darker());
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
    public void movementKeyHandler(ArrayList<KeyCode> userInput){
        if(userInput.contains(KeyCode.A) && userInput.contains(KeyCode.D) || userInput.isEmpty()){
            gameData.getPlayer().setMoveAmount(0);
        }else if(userInput.contains(KeyCode.A)){
            gameData.getPlayer().setMoveAmount(-gameData.getPlayer().getDEF_MOVE_AMOUNT());
        }
        else if(userInput.contains(KeyCode.D)){
            gameData.getPlayer().setMoveAmount(gameData.getPlayer().getDEF_MOVE_AMOUNT());
        }
    }

    /**
     * this method is used to deal with non movement features, like pause menu, show debug console, pause and resume of the game.
     *
     * @param userInput this is the array of user input.
     */
    public void nonMovementKeyHandler(ArrayList<KeyCode> userInput){
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
            gameData.setPauseMode(false);
            showDebugConsole();
        }else if(userInput.contains(KeyCode.H)){
            gameData.setBotMode(!gameData.isBotMode());
        }
    }

    /**
     * this method is used to let the bot control the paddle instead of the player playing it.
     */
    public void automation(){
        if(gameData.isBotMode()){
            if(gameData.getBall().getBounds().getMinX() > gameData.getPlayer().getBounds().getMinX() + gameData.getPlayer().getBounds().getWidth()/2){
                gameData.getPlayer().setMoveAmount(gameData.getPlayer().getDEF_MOVE_AMOUNT());
            }else{
                gameData.getPlayer().setMoveAmount(-gameData.getPlayer().getDEF_MOVE_AMOUNT());
            }
        }
    }

    /**
     * this method is used to check if there is an impact for the ball with any entity or the sides of the screen. which will cause a reaction to the ball and the brick.
     */
    private void findImpacts(){
        if(impact(gameData.getBall(), gameData.getPlayer())){
            gameData.getBall().ballBottomCollision();
            soundEffects.ballCollisionRandomSound(soundEffects.getBallPlayerCollisionSound1(), soundEffects.getBallPlayerCollisionSound2());
        }
        else if(impactWall()){
            gameData.setBrickCount(gameData.getBrickCount()-1);
        }
        if((gameData.getBall().getBounds().getMinX() < gameBoard.getBoundsInLocal().getMinX())){
            if (gameData.getBall().getSpeedX() < 0){
                soundEffects.ballCollisionRandomSound(soundEffects.getGameWindowCollisionSound1(), soundEffects.getGameWindowCollisionSound2());
                gameData.getBall().ballLeftCollision();
            }
        }else if(gameData.getBall().getBounds().getMaxX() > gameBoard.getBoundsInLocal().getMaxX()){
            if (gameData.getBall().getSpeedX() > 0){
                soundEffects.ballCollisionRandomSound(soundEffects.getGameWindowCollisionSound1(), soundEffects.getGameWindowCollisionSound2());
                gameData.getBall().ballRightCollision();
            }
        }
        if(gameData.getBall().getBounds().getMinY() < gameBoard.getBoundsInLocal().getMinY()){
            gameData.getBall().ballTopCollision();
            soundEffects.ballCollisionRandomSound(soundEffects.getGameWindowCollisionSound1(), soundEffects.getGameWindowCollisionSound2());
        }
        else if(gameData.getBall().getBounds().getMaxY() > gameBoard.getBoundsInLocal().getMaxY()){
            gameData.setBallCount(gameData.getBallCount() - 1);
            gameData.getBall().resetPosition();
            gameData.getPlayer().resetPosition();
            gameData.setRandomBallSpeed(gameData.getBall());
            gameData.setBallLost(true);
        }
    }

    /**
     * this method is used to check if there is a collision occurring between the ball and another object entity.
     *
     * @param ball this is the ball used to check if there is a collision.
     * @param object this is the object used to see if it is going to get collided.
     * @return this returns a boolean value if collision occur
     */
    public boolean impact(Ball ball, Entities object){
        return ball.getBounds().intersects(object.getBounds());
    }

    /**
     * this method change the ball direction and also returns true if the ball comes in contact with any side of the brick.
     *
     * @return returns a boolean value if or if it doesn't touch any entity.
     */
    private boolean impactWall(){
        for(Brick b : gameData.getBricks()){
            if(!b.isBroken()){
                if(b.getBounds().contains(gameData.getBall().getBounds().getMinX()+ gameData.getBall().getBounds().getWidth()/2, gameData.getBall().getBounds().getMaxY())){
                    gameData.getBall().ballBottomCollision();
                    soundEffects.playBrickSoundEffect(b);
                    return b.setImpact(new Point2D(gameData.getBall().getBounds().getMaxX()- gameData.getBall().getBounds().getHeight(), gameData.getBall().getBounds().getMaxY()), Crackable.UP);
                }
                else if (b.getBounds().contains(gameData.getBall().getBounds().getMinX()+ gameData.getBall().getBounds().getWidth()/2, gameData.getBall().getBounds().getMinY())){
                    gameData.getBall().ballTopCollision();
                    soundEffects.playBrickSoundEffect(b);
                    return b.setImpact(new Point2D(gameData.getBall().getBounds().getMinX()+ gameData.getBall().getBounds().getWidth(), gameData.getBall().getBounds().getMinY()), Crackable.DOWN);
                }
                else if(b.getBounds().contains(gameData.getBall().getBounds().getMaxX(), gameData.getBall().getBounds().getMinY()+ gameData.getBall().getBounds().getHeight()/2)){
                    gameData.getBall().ballLeftCollision();
                    soundEffects.playBrickSoundEffect(b);
                    return b.setImpact(new Point2D(gameData.getBall().getBounds().getMaxX(), gameData.getBall().getBounds().getMaxY()- gameData.getBall().getBounds().getHeight()), Crackable.RIGHT);
                }
                else if(b.getBounds().contains(gameData.getBall().getBounds().getMinX(), gameData.getBall().getBounds().getMinY()+ gameData.getBall().getBounds().getHeight()/2)){
                    gameData.getBall().ballRightCollision();
                    soundEffects.playBrickSoundEffect(b);
                    return b.setImpact(new Point2D(gameData.getBall().getBounds().getMinX(), gameData.getBall().getBounds().getMaxY()- gameData.getBall().getBounds().getHeight()), Crackable.LEFT);
                }
            }
        }
        return false;
    }

    /**
     * this method is used to toggle between pausing the game and proceeding th game
     */
    private void togglePauseContinueGame(){
        if(gameData.isPauseMode()){
            animationTimer.stop();
            gameScore.pauseTimer();
            gameScore.setCanGetTime(true);
            gameData.setPauseMode(false);
        }
        else{
            soundEffects.getVictorySound().stop();
            GameStart.audio.play();
            animationTimer.start();
            gameScore.startTimer();
            gameScore.setCanGetTime(true);
            gameData.setPauseMode(true);
        }
    }

    /**
     * this method is used to show a debug console.
     */
    private void showDebugConsole(){
        Stage debugConsole = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/FX/DebugConsole.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        debugConsole.setScene(new Scene(root));
        debugConsole.setTitle("Debug Console");
        debugConsole.setResizable(false);
        debugConsole.initOwner(anchorPane.getScene().getWindow());
        debugConsole.initModality(Modality.WINDOW_MODAL);
        debugConsole.showAndWait();
    }

    /**
     * this method is used to show the pause menu on the screen.
     */
    private void showPauseMenu(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/FX/PauseMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!gameData.isShowPauseMenu()){
            anchorPane.getChildren().add(root);
            gameData.setShowPauseMenu(true);
        }
    }
}
