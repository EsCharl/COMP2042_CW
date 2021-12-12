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

import FX.BrickDestroyer;
import FX.Model.Entities.Ball.Ball;
import FX.Model.Entities.Ball.BallClone;
import FX.Model.Entities.Brick.Brick;
import FX.Model.Entities.Brick.Crackable;
import FX.Model.Entities.Entities;
import FX.Model.Entities.Paddle;
import FX.Model.Game;
import FX.Model.GameScore;

import FX.Model.SoundEffects;
import FX.View.GameScoreDisplay;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.ArrayList;
import java.util.Random;

/**
 * this class is used as the controller for the game state controller where it is in control of the game play.
 */
public class GameController {

    private Game game;
    private GameScore gameScore;
    private GameScoreDisplay gameScoreDisplay;
    private SoundEffects soundEffects;

    private GraphicsContext graphicsContext;
    private AnimationTimer animationTimer;
    private Scene scene;

    private ArrayList<KeyCode> userInput;
    private Random rnd;

    @FXML public Canvas gameBoard;
    @FXML private AnchorPane anchorPane;
    @FXML private Text gameText;

    /**
     * this is the constructor for the class which is used to set the variables and objects which is going to be used in this class.
     */
    public GameController() {
        userInput = new ArrayList<>();
        setSoundEffects(new SoundEffects());

        setGameScore(GameScore.singletonGameScore());
        setGameScoreDisplay(new GameScoreDisplay());
        rnd = new Random();
    }

    /**
     * this method is used to initialize and have the logic on how the game should work. (game logic).
     */
    @FXML
    private void initialize(){
        setGame(Game.singletonGame(gameBoard.getWidth(),gameBoard.getHeight()));

        getGame().setShowPauseMenu(false);

        getGameScore().setLevelFileName("Level"+ getGame().getPlayer().getCurrentLevel()+".txt");

        graphicsContext = gameBoard.getGraphicsContext2D();

        getGameScore().startTimer();
        getGameScore().setCanGetTime(true);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                graphicsContext.clearRect(0,0,gameBoard.getWidth(),gameBoard.getHeight());

                graphicsContext.setLineWidth(2);

                gameText.setText(String.format("Bricks: %d Balls %d", getGame().getBrickCount(), getGame().getPlayer().getBallCount()));

                drawBricks();

                drawBall(getGame().getMainBall());
                drawPlayer(getGame().getPaddle());
                drawCloneBall(getGame().getCloneBall());

                if(getGame().isBallLost()){
                    gameScore.recordGameTimer();
                    getGame().setBallLost(false);
                    getGame().getPlayer().setPauseMode(false);
                    if(getGame().getPlayer().getBallCount() == 0){
                        BrickDestroyer.audio.pause();
                        getSoundEffects().getLostSound().play();
                        getGame().getPlayer().resetBallCount();
                        gameText.setText("Game Over. Time spent in this level: " + getGameScore().getTimerString());
                        getGameScore().restartTimer();
                    }
                    getGame().getCloneBall().clear();
                    stop();
                }

                scene = anchorPane.getScene();

                Stage stage = (Stage) gameBoard.getScene().getWindow();

                windowLostFocus(stage);

                keyPressed();

                movementKeyHandler(userInput);

                keyReleased(userInput);

                getGame().automation();

                getGame().getPaddle().move();

                if(!getGame().getCloneBall().isEmpty()) {
                    for (int i = 0; i < getGame().getCloneBall().size(); i++) {
                        getGame().getCloneBall().get(i).impactEntity(getGame().getMainBall());
                        if(ballActions(getGame().getCloneBall().get(i))) {
                            if (!(getEntityMaxY(getGame().getCloneBall().get(i)) >= gameBoard.getBoundsInLocal().getMaxY())) {
                                getSoundEffects().ballCollisionRandomSound(getSoundEffects().getGameWindowCollisionSound1(), getSoundEffects().getGameWindowCollisionSound2());
                            } else {
                                getGame().getCloneBall().remove(i);
                            }
                        }
                    }
                }

                if(ballActions(getGame().getMainBall()))
                    if(!(getEntityMaxY(getGame().getMainBall()) >= gameBoard.getBoundsInLocal().getMaxY())){
                        getSoundEffects().ballCollisionRandomSound(getSoundEffects().getGameWindowCollisionSound1(), getSoundEffects().getGameWindowCollisionSound2());
                    }else{
                        getGame().getPlayer().setBallCount(getGame().getPlayer().getBallCount() - 1);
                        getGame().getMainBall().resetPosition();
                        getGame().getPaddle().resetPosition();
                        getGame().getMainBall().setRandomBallSpeed();
                        getGame().setBallLost(true);
                    }

                if(getGame().getBrickCount() == 0){
                    gameScore.recordGameTimer();
                    gameScoreHandler();
                    BrickDestroyer.audio.pause();
                    getSoundEffects().getVictorySound().play();
                    if(getGame().getPlayer().getCurrentLevel() < getGame().getBrickLevels().length){
                        gameText.setText("Go to Next Level");
                        animationTimer.stop();
                    }
                    else{
                        gameText.setText("ALL WALLS DESTROYED! Restart?");
                        animationTimer.stop();
                        getGame().getPlayer().setCurrentLevel(0);
                    }
                    getGame().nextLevel();
                    getGame().restartStatus();
                    getGameScore().setLevelFileName("Level"+ getGame().getPlayer().getCurrentLevel()+".txt");
                    getGameScore().restartTimer();
                }
            }

            /**
             * this method is used to cause the ball to move and deals with any collision it will face.
             *
             * @param ball this is the ball object used to move and collide.
             * @return returns a boolean if it collides with the game sides (top, left, right) false if not.
             */
            private boolean ballActions(Ball ball) {
                ball.move();
                if (ball.impactEntity(getGame().getPaddle())) {
                    if (ball.equals(getGame().getMainBall())){
                        if(rnd.nextDouble() < BallClone.CLONE_BALL_GENERATION_PROBABILITY && getGame().getCloneBall().size() < BallClone.MAX_CLONE_BALL){
                            getGame().addCloneBall(new BallClone(new Point2D(getGame().getMainBall().getBounds().getMinX(), getGame().getMainBall().getBounds().getMinY())));
                        }
                    }
                    getSoundEffects().ballCollisionRandomSound(getSoundEffects().getBallPlayerCollisionSound1(), getSoundEffects().getBallPlayerCollisionSound2());
                }
                ballBrickCollision(ball);
                if(ball.gameWindowCollision(gameBoard.getBoundsInLocal()))
                    return true;
                return false;
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
                    gameScore.recordGameTimer();
                    gameText.setText("Focus Lost");
                    animationTimer.stop();
                    getGame().getPlayer().setPauseMode(false);
                });
            }

            /**
             * this method is used to draw the Bricks.
             */
            private void drawBricks() {
                for (int i = 0; i < getGame().getBrickLevels()[getGame().getPlayer().getCurrentLevel()-1].length; i++){
                    if(!getCurrentLevelBrick(i).isBroken()){
                        graphicsContext.setFill(getCurrentLevelBrick(i).getInnerColor());
                        graphicsContext.fillRect(getEntityMinX(getCurrentLevelBrick(i)), getEntityMinY(getCurrentLevelBrick(i)), getCurrentLevelBrick(i).getWidth(), getCurrentLevelBrick(i).getHeight());

                        graphicsContext.setStroke(getCurrentLevelBrick(i).getBorderColor());
                        graphicsContext.strokeRect(getEntityMinX(getCurrentLevelBrick(i)) -1, getEntityMinY(getCurrentLevelBrick(i)) -1, getCurrentLevelBrick(i).getWidth()+2, getCurrentLevelBrick(i).getHeight()+2);

                        showCrack(i);
                    }
                }
            }

            /**
             * this method is used to draw the ball clones.
             *
             * @param clones this is the ball clones arraylist used to draw the ball elements inside it.
             */
            private void drawCloneBall(ArrayList<BallClone> clones){
                if(!clones.isEmpty()){
                    for (int i = 0; i < getGame().getCloneBall().size(); i++){
                        graphicsContext.setFill(clones.get(i).getInnerColor());
                        graphicsContext.fillOval(getEntityMinX(clones.get(i)), getEntityMinY(clones.get(i)), clones.get(i).getRadius(),clones.get(i).getRadius());

                        graphicsContext.setStroke(clones.get(i).getBorderColor());
                        graphicsContext.strokeOval(getEntityMinX(clones.get(i)) -1, getEntityMinY(clones.get(i)) -1, clones.get(i).getRadius()+2, clones.get(i).getRadius()+2);
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
                graphicsContext.fillOval(getEntityMinX(ball), getEntityMinY(ball), ball.getRadius(), ball.getRadius());

                graphicsContext.setStroke(ball.getBorderColor());
                graphicsContext.strokeOval(getEntityMinX(ball) -1, getEntityMinY(ball) -1, ball.getRadius()+2, ball.getRadius()+2);
            }

            /**
             * this method is used to draw the player object to the screen.
             *
             * @param paddle this is the player object which contains the information about the player position and size needed to draw the object.
             */
            private void drawPlayer(Paddle paddle) {
                graphicsContext.setFill(paddle.getInnerColor());
                graphicsContext.fillRect(getEntityMinX(paddle), getEntityMinY(paddle), paddle.getWidth(), paddle.getHeight());

                graphicsContext.setStroke(paddle.getBorderColor());
                graphicsContext.strokeRect(getEntityMinX(paddle) -1, getEntityMinY(paddle) -1, paddle.getWidth()+2, paddle.getHeight()+2);
            }

            /**
             * this method is used to draw the cracks on the brick.
             */
            private void showCrack(int i){
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
        };

        animationTimer.start();

        anchorPane.requestFocus();
    }

    /**
     * this method is used to detect and collide with the brick.
     *
     * @param ball this is the ball object used for the collision with the brick.
     */
    private void ballBrickCollision(Ball ball) {
        for (int x = 0; x < getGame().getBrickLevels()[getGame().getPlayer().getCurrentLevel()-1].length; x++) {
            if (!getGame().getBrickLevels()[getGame().getPlayer().getCurrentLevel() - 1][x].isBroken()) {
                if (getGame().getBrickLevels()[getGame().getPlayer().getCurrentLevel() - 1][x].getBounds().intersects(ball.getBounds())) {
                    getSoundEffects().playBrickSoundEffect(getGame().getBrickLevels()[getGame().getPlayer().getCurrentLevel() - 1][x]);
                    if (ball.impactEntity(getGame().getBrickLevels()[getGame().getPlayer().getCurrentLevel() - 1][x]))
                        getGame().setBrickCount(getGame().getBrickCount() - 1);
                }
            }
        }
    }

    /**
     * this method is used to get the minimum y coordinate for the bounding box object.
     *
     * @param entities this is the entity used to get the minimum y coordinate.
     * @return this returns the minimum y coordinate of the entity object
     */
    private double getEntityMinY(Entities entities) {
        return entities.getBounds().getMinY();
    }

    /**
     * this method is used to get the entity minimum x coordinate of the bounding box.
     *
     * @param entities this is the entity used to get the minimum x coordinate.
     * @return this returns the minimum x coordinate of the entity object
     */
    private double getEntityMinX(Entities entities) {
        return entities.getBounds().getMinX();
    }

    /**
     * this method is used to get the entity maximum y coordinate of the bounding box.
     *
     * @param entities this is the entity used to get the maximum y coordinate.
     * @return this returns the maximum y coordinate of the entity object
     */
    private double getEntityMaxY(Entities entities) {
        return entities.getBounds().getMaxY();
    }

    /**
     * this method is used to get the brick from the brick array on the current level.
     *
     * @param i this is the index for the brick array.
     * @return this returns the brick object based on the index provided.
     */
    private Brick getCurrentLevelBrick(int i) {
        return getGame().getBrickLevels()[getGame().getPlayer().getCurrentLevel() - 1][i];
    }

    /**
     * this method is used to display and save the game score.
     */
    private void gameScoreHandler() {
        try {
            getGameScore().setLastLevelCompletionRecord(getGameScore().getHighScore());
            getGameScore().updateSaveFile(getGameScore().getLastLevelCompletionRecord());
        } catch (IOException e) {
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
            getGame().getPaddle().setMoveAmount(0);
        }else if(userInput.contains(KeyCode.A)){
            getGame().getPaddle().setMoveAmount(-getGame().getPaddle().getDEF_MOVE_AMOUNT());
        }
        else if(userInput.contains(KeyCode.D)){
            getGame().getPaddle().setMoveAmount(getGame().getPaddle().getDEF_MOVE_AMOUNT());
        }
    }

    /**
     * this method is used to deal with non movement features, like pause menu, show debug console, pause and resume of the game.
     *
     * @param userInput this is the array of user input.
     */
    public void nonMovementKeyHandler(ArrayList<KeyCode> userInput){
        if(userInput.contains(KeyCode.ESCAPE)){
            gameScore.recordGameTimer();
            animationTimer.stop();
            showPauseMenu();
        }else if(userInput.contains(KeyCode.SPACE)){
            togglePauseContinueGame();
        }else if(userInput.contains(KeyCode.F1) && userInput.contains(KeyCode.SHIFT) && userInput.contains(KeyCode.ALT)){
            gameScore.recordGameTimer();
            animationTimer.stop();
            getGame().getPlayer().setPauseMode(false);
            showDebugConsole();
        }else if(userInput.contains(KeyCode.H)){
            getGame().getPlayer().setBotMode(!getGame().getPlayer().isBotMode());
        }
    }

    /**
     * this method is used to toggle between pausing the game and proceeding th game
     */
    private void togglePauseContinueGame(){
        if(getGame().getPlayer().isPauseMode()){
            animationTimer.stop();
            gameScore.recordGameTimer();
            getGame().getPlayer().setPauseMode(false);
        }
        else{
            getSoundEffects().getVictorySound().stop();
            BrickDestroyer.audio.play();
            animationTimer.start();
            getGameScore().startTimer();
            getGameScore().setCanGetTime(true);
            getGame().getPlayer().setPauseMode(true);
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
        if(!getGame().isShowPauseMenu()){
            anchorPane.getChildren().add(root);
            getGame().setShowPauseMenu(true);
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
     * @param game this is the game data object used to set into a variable for future reference.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * this returns a game data object which is used to get the information of the game.
     *
     * @return this returns the game data object.
     */
    public Game getGame() {
        return game;
    }

    /**
     * this method is used to get the sound effects object class which deals with all the sound effects for the game.
     *
     * @return this returns the sound effects object which contains all the sound effects.
     */
    public SoundEffects getSoundEffects() {
        return soundEffects;
    }

    /**
     * this method is used to set the sound effects object into a variable for future reference.
     *
     * @param soundEffects this is the sound effects object which is to be set into a variable.
     */
    public void setSoundEffects(SoundEffects soundEffects) {
        this.soundEffects = soundEffects;
    }
}