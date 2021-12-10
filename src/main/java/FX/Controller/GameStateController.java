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
import FX.Model.Entities.Ball.BallClones;
import FX.Model.Entities.Brick.Brick;
import FX.Model.Entities.Brick.Crackable;
import FX.Model.GameData;
import FX.Model.GameScore;
import FX.Model.Entities.Player;

import FX.View.GameScoreDisplay;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
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
import java.util.ArrayList;

/**
 * this class is used as the controller for the game state controller where it is in control of the game play.
 */
public class GameStateController {

    private GameData gameData;
    private GameScore gameScore;
    private GameScoreDisplay gameScoreDisplay;

    private GraphicsContext graphicsContext;
    private AnimationTimer animationTimer;
    private Scene scene;

    private ArrayList<KeyCode> userInput;

    @FXML private Canvas gameBoard;
    @FXML private AnchorPane anchorPane;
    @FXML private Text gameText;

    /**
     * this is the constructor for the class which is used to set the variables and objects which is going to be used in this class.
     */
    public GameStateController() {
        userInput = new ArrayList<>();

        setGameData(GameData.singletonGame());
        setGameScore(GameScore.singletonGameScore());
        setGameScoreDisplay(new GameScoreDisplay());

        getGameData().setShowPauseMenu(false);

        getGameScore().setLevelFileName("Level"+getGameData().getCurrentLevel()+".txt");
    }

    /**
     * this method is used to initialize and have the info on how the game should work. (game logic).
     */
    @FXML
    private void initialize(){

        graphicsContext = gameBoard.getGraphicsContext2D();

        getGameScore().startTimer();
        getGameScore().setCanGetTime(true);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                graphicsContext.clearRect(0,0,gameBoard.getWidth(),gameBoard.getHeight());

                graphicsContext.setLineWidth(2);

                gameText.setText(String.format("Bricks: %d Balls %d", getGameData().getBrickCount(), getGameData().getBallCount()));

                drawBricks();

                drawBall(getGameData().getBall());
                drawPlayer(getGameData().getPlayer());
                drawCloneBall();

                if(getGameData().isBallLost()){
                    recordGameTimer();
                    getGameData().setBallLost(false);
                    getGameData().setPauseMode(false);
                    if(getGameData().isGameOver()){
                        GameStart.audio.pause();
                        getGameData().getSoundEffects().getLostSound().play();
                        getGameData().resetBallCount();
                        gameText.setText("Game Over. Time spent in this level: " + getGameScore().getTimerString());
                        getGameScore().restartTimer();
                    }
                    getGameData().getCloneBall().clear();
                    stop();
                }

                scene = anchorPane.getScene();

                Stage stage = (Stage) gameBoard.getScene().getWindow();

                windowLostFocus(stage);

                keyPressed();

                movementKeyHandler(userInput);

                keyReleased(userInput);

                getGameData().automation();

                getGameData().getBall().move();
                getGameData().getPlayer().move();

                if(!gameData.getCloneBall().isEmpty()) {
                    for (int i = 0; i < getGameData().getCloneBall().size(); i++) {
                        getGameData().getCloneBall().get(i).move();
                        findImpacts(getGameData().getCloneBall().get(i));
                    }
                }

                findImpacts(getGameData().getBall());

                if(getGameData().isLevelComplete()){
                    recordGameTimer();
                    gameScoreHandler();
                    GameStart.audio.pause();
                    getGameData().getSoundEffects().getVictorySound().play();
                    if(getGameData().getCurrentLevel() < getGameData().getBrickLevels().length){
                        gameText.setText("Go to Next Level");
                        animationTimer.stop();
                        getGameData().nextLevel();
                        getGameScore().restartTimer();
                        getGameScore().setLevelFileName("Level"+getGameData().getCurrentLevel()+".txt");
                    }
                    else{
                        gameText.setText("ALL WALLS DESTROYED");
                        animationTimer.stop();
                    }
                    getGameData().getBall().resetPosition();
                    getGameData().getPlayer().resetPosition();
                    getGameData().getBall().setRandomBallSpeed();
                    getGameData().resetBallCount();
                    getGameData().getCloneBall().clear();
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
                    recordGameTimer();
                    gameText.setText("Focus Lost");
                    animationTimer.stop();
                    getGameData().setPauseMode(false);
                });
            }

            /**
             * this method is used to draw the Bricks.
             */
            private void drawBricks() {
                for (int i = 0; i < getGameData().getBrickLevels()[getGameData().getCurrentLevel()-1].length; i++){
                    if(!getCurrentLevelBrick(i).isBroken()){
                        graphicsContext.setFill(getCurrentLevelBrick(i).getInnerColor());
                        graphicsContext.fillRect(getMinX(getCurrentLevelBrick(i).getBounds()), getMinY(getCurrentLevelBrick(i).getBounds()), getCurrentLevelBrick(i).getWidth(), getCurrentLevelBrick(i).getHeight());

                        graphicsContext.setStroke(getCurrentLevelBrick(i).getBorderColor());
                        graphicsContext.strokeRect(getMinX(getCurrentLevelBrick(i).getBounds()) -1, getMinY(getCurrentLevelBrick(i).getBounds()) -1, getCurrentLevelBrick(i).getWidth()+2, getCurrentLevelBrick(i).getHeight()+2);

                        showCrack();
                    }
                }
            }

            private void drawCloneBall(){
                if(!gameData.getCloneBall().isEmpty()){
                    for (int i = 0; i <getGameData().getCloneBall().size(); i++){
                        graphicsContext.setFill(gameData.getCloneBall().get(i).getInnerColor());
                        graphicsContext.fillOval(getMinX(gameData.getCloneBall().get(i).getBounds()), getMinY(gameData.getCloneBall().get(i).getBounds()), gameData.getCloneBall().get(i).getRadius(),gameData.getCloneBall().get(i).getRadius());

                        graphicsContext.setStroke(gameData.getCloneBall().get(i).getBorderColor());
                        graphicsContext.strokeOval(getMinX(gameData.getCloneBall().get(i).getBounds()) -1, getMinY(gameData.getCloneBall().get(i).getBounds()) -1, gameData.getCloneBall().get(i).getRadius()+2, gameData.getCloneBall().get(i).getRadius()+2);
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
                graphicsContext.fillOval(getMinX(ball.getBounds()), getMinY(ball.getBounds()), ball.getRadius(), ball.getRadius());

                graphicsContext.setStroke(ball.getBorderColor());
                graphicsContext.strokeOval(getMinX(ball.getBounds()) -1, getMinY(ball.getBounds()) -1, ball.getRadius()+2, ball.getRadius()+2);
            }

            /**
             * this method is used to draw the player object to the screen.
             *
             * @param player this is the player object which contains the information about the player position and size needed to draw the object.
             */
            private void drawPlayer(Player player) {
                graphicsContext.setFill(player.getInnerColor());
                graphicsContext.fillRect(getMinX(player.getBounds()), getMinY(player.getBounds()),player.getWidth(),player.getHeight());

                graphicsContext.setStroke(player.getBorderColor());
                graphicsContext.strokeRect(getMinX(player.getBounds()) -1, getMinY(player.getBounds()) -1,player.getWidth()+2,player.getHeight()+2);
            }

            /**
             * this method is used to draw the cracks on the brick.
             */
            private void showCrack(){
                for (int i = 0; i < getGameData().getBrickLevels()[getGameData().getCurrentLevel()-1].length; i++) {
                    if (getCurrentLevelBrick(i) instanceof Crackable && !getCurrentLevelBrick(i).isBroken()) {
                        if (((Crackable) getCurrentLevelBrick(i)).getCrackPath() != null) {
                            Path path = ((Crackable) getCurrentLevelBrick(i)).getCrackPath();
                            graphicsContext.setStroke(getCurrentLevelBrick(i).getBorderColor().darker());
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
     * this method is used to get the minimum y coordinate for the bounding box object.
     *
     * @param bounds this is the bounding box object.
     * @return this returns the minimum y coordinate of the bounding box object.
     */
    public double getMinY(BoundingBox bounds) {
        return bounds.getMinY();
    }

    /**
     * this method is used to get the objects minimum x coordinate of the bounding box.
     *
     * @param bounds this is the bounding box object
     * @return this returns the minimum x coordinate of the bounding box object
     */
    public double getMinX(BoundingBox bounds) {
        return bounds.getMinX();
    }

    /**
     * this method is used to get the brick from the brick array on the current level.
     *
     * @param i this is the index for the brick array.
     * @return this returns the brick object based on the index provided.
     */
    public Brick getCurrentLevelBrick(int i) {
        return getGameData().getBrickLevels()[getGameData().getCurrentLevel() - 1][i];
    }

    /**
     * this method is used to display and save the game score.
     */
    private void gameScoreHandler() {
        try {
            getGameScore().setLastLevelCompletionRecord(getGameScore().getHighScore());
            getGameScore().updateSaveFile(getGameScore().getLastLevelCompletionRecord());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        getGameScoreDisplay().generateLevelCompleteWindow(getGameScore().getLastLevelCompletionRecord(), getGameScore().getTimerString());
    }

    /**
     * this method is used to get the user input and act accordingly based on the inputs provided in the array.
     *
     * @param userInput this is the array of user input.
     */
    public void movementKeyHandler(ArrayList<KeyCode> userInput){
        if(userInput.contains(KeyCode.A) && userInput.contains(KeyCode.D) || userInput.isEmpty()){
            getGameData().getPlayer().setMoveAmount(0);
        }else if(userInput.contains(KeyCode.A)){
            getGameData().getPlayer().setMoveAmount(-getGameData().getPlayer().getDEF_MOVE_AMOUNT());
        }
        else if(userInput.contains(KeyCode.D)){
            getGameData().getPlayer().setMoveAmount(getGameData().getPlayer().getDEF_MOVE_AMOUNT());
        }
    }

    /**
     * this method is used to deal with non movement features, like pause menu, show debug console, pause and resume of the game.
     *
     * @param userInput this is the array of user input.
     */
    public void nonMovementKeyHandler(ArrayList<KeyCode> userInput){
        if(userInput.contains(KeyCode.ESCAPE)){
            recordGameTimer();
            animationTimer.stop();
            showPauseMenu();
        }else if(userInput.contains(KeyCode.SPACE)){
            togglePauseContinueGame();
        }else if(userInput.contains(KeyCode.F1) && userInput.contains(KeyCode.SHIFT) && userInput.contains(KeyCode.ALT)){
            recordGameTimer();
            animationTimer.stop();
            getGameData().setPauseMode(false);
            showDebugConsole();
        }else if(userInput.contains(KeyCode.H)){
            getGameData().setBotMode(!getGameData().isBotMode());
        }
    }

    /**
     * this method is used to record the time passed between the starting of the timer and the pausing of the game.
     */
    private void recordGameTimer() {
        if (getGameScore().isCanGetTime()) {
            getGameScore().pauseTimer();
            getGameScore().setCanGetTime(false);
        }
    }

    /**
     * this method is used to check if there is an impact for the ball with any entity or the sides of the screen. which will cause a reaction to the ball and the brick.
     */
    private void findImpacts(Ball ball){
        if(ball.getBounds().intersects(getGameData().getPlayer().getBounds())){
            if(ball.equals(getGameData().getBall()))
                getGameData().cloneBallRandomGenerator();
            ball.ballBottomCollision();
            getGameData().getSoundEffects().ballCollisionRandomSound(getGameData().getSoundEffects().getBallPlayerCollisionSound1(), getGameData().getSoundEffects().getBallPlayerCollisionSound2());
        }
        else if(getGameData().impactWall(ball)){
            getGameData().setBrickCount(getGameData().getBrickCount()-1);
        }
        if((getMinX(ball.getBounds()) < getMinX((BoundingBox) gameBoard.getBoundsInLocal()))){
            if (ball.getSpeedX() < 0){
                getGameData().getSoundEffects().ballCollisionRandomSound(getGameData().getSoundEffects().getGameWindowCollisionSound1(), getGameData().getSoundEffects().getGameWindowCollisionSound2());
                ball.ballLeftCollision();
            }
        }else if(ball.getBounds().getMaxX() > gameBoard.getBoundsInLocal().getMaxX()){
            if (ball.getSpeedX() > 0){
                getGameData().getSoundEffects().ballCollisionRandomSound(getGameData().getSoundEffects().getGameWindowCollisionSound1(), getGameData().getSoundEffects().getGameWindowCollisionSound2());
                ball.ballRightCollision();
            }
        }
        if(getMinY(ball.getBounds()) < getMinY((BoundingBox) gameBoard.getBoundsInLocal())){
            ball.ballTopCollision();
            getGameData().getSoundEffects().ballCollisionRandomSound(getGameData().getSoundEffects().getGameWindowCollisionSound1(), getGameData().getSoundEffects().getGameWindowCollisionSound2());
        }
        else if(ball.getBounds().getMaxY() > gameBoard.getBoundsInLocal().getMaxY()){
            if(ball.getClass() == BallClones.class){
                for(int i = 0; i < gameData.getCloneBall().size(); i++){
                    if(gameData.getCloneBall().get(i).equals(ball)){
                        gameData.getCloneBall().remove(i);
                    }
                }
            }else{
                getGameData().setBallCount(getGameData().getBallCount() - 1);
                ball.resetPosition();
                getGameData().getPlayer().resetPosition();
                ball.setRandomBallSpeed();
                getGameData().setBallLost(true);
            }
        }
    }

    /**
     * this method is used to toggle between pausing the game and proceeding th game
     */
    private void togglePauseContinueGame(){
        if(getGameData().isPauseMode()){
            animationTimer.stop();
            recordGameTimer();
            getGameData().setPauseMode(false);
        }
        else{
            getGameData().getSoundEffects().getVictorySound().stop();
            GameStart.audio.play();
            animationTimer.start();
            getGameScore().startTimer();
            getGameScore().setCanGetTime(true);
            getGameData().setPauseMode(true);
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
        if(!getGameData().isShowPauseMenu()){
            anchorPane.getChildren().add(root);
            getGameData().setShowPauseMenu(true);
        }
    }

    /**
     * this method is used to get the game score object which is used to record the game score (time used in the level)
     *
     * @param gameScore this is the game score object going to be set into a variable for future reference.
     */
    public void setGameScore(GameScore gameScore) {
        this.gameScore = gameScore;
    }

    /**
     * this method is used to get the game score display object which is used to display all the game score on a pop-up window.
     *
     * @return this returns a game score display object.
     */
    public GameScoreDisplay getGameScoreDisplay() {
        return gameScoreDisplay;
    }

    /**
     * this method is used to set the game score display object into a variable for future reference.
     *
     * @param gameScoreDisplay this is the game score display object used to be set into a variable.
     */
    public void setGameScoreDisplay(GameScoreDisplay gameScoreDisplay) {
        this.gameScoreDisplay = gameScoreDisplay;
    }

    /**
     * this method is used to get the game score object which is used to manage the scoring system.
     *
     * @return this returns a game score object used to be record the score (time used) for the level.
     */
    public GameScore getGameScore() {
        return gameScore;
    }

    /**
     * this method is used to get the game data information which is used to access the information for the game.
     *
     * @param gameData this is the game data object used to set into a variable for future reference.
     */
    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    /**
     * this returns a game data object which is used to get the information of the game.
     *
     * @return this returns the game data object.
     */
    public GameData getGameData() {
        return gameData;
    }
}
