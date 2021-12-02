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

package Controller;

import Model.Ball.Ball;
import Model.Ball.RubberBall;
import Model.Brick.Brick;
import Model.Brick.Crack;
import Model.GameScore;
import Model.Game;
import Model.Player;
import View.DebugConsole;
import View.GameBoardView;
import View.GameScoreDisplay;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

/**
 *  this is a class that handles the gaming part of the program. which includes the pause feature
 */
public class GameBoardController {

    private final int UP_IMPACT = 100;
    private final int DOWN_IMPACT = 200;
    private final int LEFT_IMPACT = 300;
    private final int RIGHT_IMPACT = 400;

    private final static int DEF_WIDTH = 600;
    private final static int DEF_HEIGHT = 450;

    private boolean showPauseMenu;
    private boolean canGetTime;
    private boolean botMode;

    private Timer gameTimer;

    public GameBoardView gameBoardView;
    private GameScore gameScore;
    private Game game;
    private DebugConsole debugConsole;
    private Player player;
    private Ball ball;
    private Random rnd;

    private static GameBoardController uniqueGameBoardController;
    private GameScoreDisplay gameScoreDisplay;

    /**
     * this method is used to create and return the one and only game board controller object. (singleton)
     *
     */
    public static GameBoardController singletonGameBoardController(){
        if(getUniqueGameBoardController() == null){
            setUniqueGameBoardController(new GameBoardController());
        }
        return getUniqueGameBoardController();
    }

    /**
     * this constructor is used to create a game board object.
     *
     */
    private GameBoardController(){
        setRnd(new Random());

        setCanGetTime(false);

        setBotMode(false);

        Rectangle playArea = new Rectangle(0,0, getDEF_WIDTH(), getDEF_HEIGHT());

        setGame(Game.singletonGame(playArea,30,3,6/2,new Point(300,430)));

        setDebugConsole(new DebugConsole(getGame(), this));

        setGameScore(GameScore.singletonGameScore());
        setShowPauseMenu(false);

        setGameBoardView(GameBoardView.singletonGameBoardView(this, getGame()));
        getGameBoardView().setMessage("");

        setGameScoreDisplay(new GameScoreDisplay());

        setPlayer(Player.singletonPlayer(new Point(300,430), playArea));

        makeBall(new Point(300,430));

        //initialize the first level
        startGame();
    }

    /**
     * this method is used to start the game.
     */
    private void startGame() {
        getGame().nextLevel();
        getGameScore().setLevelFilePathName("/scores/Level"+ getGame().getCurrentLevel()+".txt");

        setGameTimer(new Timer(10, e ->{
            entitiesMovements();
            findImpacts();
            getGameBoardView().setMessage(String.format("Bricks: %d Balls %d", getGame().getBrickCount(), getGame().getBallCount()));
            if(getGame().isBallLost()){
                if(getGame().isGameOver()){
                    getGame().wallReset();
                    getGameBoardView().setMessage("Game over");
                    getGameTimer().stop();
                    getGameScore().restartTimer();
                }
                positionsReset();
                getGameTimer().stop();
                getGameScore().pauseTimer();
            }
            else if(getGame().isLevelComplete()){
                if(getGame().hasLevel()){
                    getGameBoardView().setMessage("Go to Next Level");
                    getGameTimer().stop();
                    restartGameStatus();
                }
                else{
                    getGameBoardView().setMessage("ALL WALLS DESTROYED");
                    getGameTimer().stop();
                }

                getGameScore().pauseTimer();

                //for save file saving and high score pop up
                saveScoreLevel();

                getGameScore().restartTimer();
            }
            getGameScore().setLevelFilePathName("/scores/Level"+ getGame().getCurrentLevel()+".txt");

            automation();

            getGameBoardView().updateGameBoardView();
        }));
    }

    /**
     * this method is used to let the bot control the paddle instead of the player playing it.
     */
    public void automation(){
        if(isBotMode()){
            if(getBall().getCenterPosition().getX() > getPlayer().getPlayerCenterPosition().getX())
                playerMoveRight();
            else
                playerMoveLeft();
        }
    }

    /**
     * this method toggles between bot mode or player mode.
     */
    public void toggleAI(){
        setBotMode(!isBotMode());
    }

    /**
     * this method is used to get the ball object to check the position of the ball, collision, and set the speed.
     *
     * @return this returns a ball object
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * this method is used to create a ball object (rubber ball).
     *
     * @param ballPos this is the position (in the format of Point2D) of the ball that is going to be generated.
     */
    private void makeBall(Point2D ballPos){
        setBall(new RubberBall(ballPos));
    }

    /**
     * this method is used to set the ball object for future reference.
     *
     * @param ball this is the ball object used to set into a variable.
     */
    public void setBall(Ball ball){
        this.ball = ball;
    }

    /**
     * this is to check if the ball comes in contact with any side of the brick.
     *
     * @return returns a boolean value if or if it doesn't touch any entity.
     */
    boolean impactWall(){
        for(Brick b : getGame().getBricks()){
            if(b.findImpact(getBall()) == getUP_IMPACT()){
                getBall().reverseY();
                return b.setImpact(getBall().getDown(), Crack.getUP());
            }
            else if (b.findImpact(getBall()) == getDOWN_IMPACT()){
                getBall().reverseY();
                return b.setImpact(getBall().getUp(),Crack.getDOWN());
            }
            else if(b.findImpact(getBall()) == getLEFT_IMPACT()){
                getBall().reverseX();
                return b.setImpact(getBall().getRight(),Crack.getRIGHT());
            }
            else if(b.findImpact(getBall()) == getRIGHT_IMPACT()){
                getBall().reverseX();
                return b.setImpact(getBall().getLeft(), Crack.getLEFT());
            }
        }
        return false;
    }

    /**
     * this method is used to check if there is an impact for the ball with any entity, the sides of the screen. which will cause a reaction to the game.
     */
    public void findImpacts(){
        if(getPlayer().impact(getBall())){
            getBall().reverseY();
            if(getRnd().nextBoolean() && getBall().getYSpeed() > -4){
                setBallYSpeedValue(getBall().getYSpeed()-1);
            }
            else if(getRnd().nextBoolean() && getBall().getYSpeed() < -1){
                setBallYSpeedValue(getBall().getYSpeed()+1);
            }
        }

        else if(impactWall()){
            getGame().setBrickCount(getGame().getBrickCount()-1);
        }

        if(((getBall().getLeft().getX() < getGame().getBorderArea().getX()) ||(getBall().getRight().getX() > (getGame().getBorderArea().getX() + getGame().getBorderArea().getWidth())))) {
            getBall().reverseX();
            if(getRnd().nextBoolean() && (getBall().getXSpeed() > -4 && getBall().getXSpeed() < 4) ){
                if(getBall().getXSpeed() < 0)
                    setBallXSpeedValue(getBall().getXSpeed()-1);
                else
                    setBallXSpeedValue(getBall().getXSpeed()+1);
            }else if(getRnd().nextBoolean()){
                if(getBall().getXSpeed() < -1)
                    setBallXSpeedValue(getBall().getXSpeed()+1);
                else if(getBall().getXSpeed() > 1)
                    setBallXSpeedValue(getBall().getXSpeed()-1);
            }
        }

        if(getBall().getUp().getY() < getGame().getBorderArea().getY()){
            getBall().reverseY();

            if(getRnd().nextBoolean() && getBall().getYSpeed() < 4)
                setBallYSpeedValue(getBall().getYSpeed()+1);
            else if(getRnd().nextBoolean() && getBall().getYSpeed() > 1)
                setBallYSpeedValue(getBall().getYSpeed()-1);
        }
        else if(getBall().getUp().getY() > getGame().getBorderArea().getY() + getGame().getBorderArea().getHeight()){
            getGame().setBallCount(getGame().getBallCount() - 1);
            getGame().setBallLost(true);
        }
    }

    /**
     * This method is used for the movement of the player (paddle) and the ball.
     */
    public void entitiesMovements(){
        getPlayer().move();
        getBall().move();
    }

    /**
     * this method is used to get the player object for the game.
     *
     * @return this returns the player object.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * this method is used to set the player object.
     *
     * @param player this is the player object used to set into an object.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * this method is used to see if the game is in pause menu.
     *
     * @return returns true if it is.
     */
    public boolean isShowPauseMenu() {
        return showPauseMenu;
    }

    /**
     * this method is used to set the show pause menu variable, which is used to record if the game is in pause.
     *
     * @param showPauseMenu this is used to change the status of the variable.
     */
    public void setShowPauseMenu(boolean showPauseMenu) {
        this.showPauseMenu = showPauseMenu;
    }

    /**
     * this is the method used to get the GameBoard Object.
     *
     * @return returns the GameBoard Object.
     */
    public static GameBoardController getUniqueGameBoardController() {
        return uniqueGameBoardController;
    }

    /**
     * this method is used to set the set a GameBoard object into uniqueGameBoard variable.
     *
     * @param uniqueGameBoardController this is the GameBoard object used to set the uniqueGameBoard variable.
     */
    public static void setUniqueGameBoardController(GameBoardController uniqueGameBoardController) {
        GameBoardController.uniqueGameBoardController = uniqueGameBoardController;
    }

    /**
     * this method is used to set the debug console visible on the window and let the game.
     */
    public void displayDebugConsole() {
        getGameTimer().stop();
        getDebugConsole().setVisible(true);
    }

    /**
     * this method is used to stop the player movement.
     */
    public void playerStopMoving() {
        getPlayer().stop();
    }

    /**
     * this method is used to save the game score.
     */
    private void saveScoreLevel() {
        try {
            ArrayList<String> sorted = getGameScore().getHighScore();
            getGameScore().updateSaveFile(sorted);
            getGameScoreDisplay().highScorePanel(sorted,getGameScore().getTimerString());
        } catch (IOException | BadLocationException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }



    /**
     * this is used to reset the ball and the player to the starting position and giving it a random speed for the ball.
     */
    public void positionsReset(){
        getPlayer().resetPosition(getGame().getStartPoint());
        getBall().moveTo(getGame().getStartPoint());

        getBall().setRandomBallSpeed();

        getGame().setBallLost(false);
    }

    public Random getRnd() {
        return rnd;
    }

    public void setRnd(Random rnd) {
        this.rnd = rnd;
    }

    public int getUP_IMPACT() {
        return UP_IMPACT;
    }

    public int getDOWN_IMPACT() {
        return DOWN_IMPACT;
    }

    public int getLEFT_IMPACT() {
        return LEFT_IMPACT;
    }

    public int getRIGHT_IMPACT() {
        return RIGHT_IMPACT;
    }

    /**
     * this method is used to get the wall variable.
     *
     * @return returns a Model.Wall datatype of wall variable.
     */
    public Game getGame() {
        return game;
    }

    /**
     * this method is used to set the wall variable
     *
     * @param game this is the Model.Wall datatype which will be used to set the wall variable.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * this method is used when the user tabbed out or proceeds to a new level.
     */
    public void lostFocusTriggered(){
        getGameTimer().stop();

        getGameBoardView().setMessage("Focus Lost");
        getGameBoardView().updateGameBoardView();

        if(isCanGetTime()){
            pauseGame();
        }
    }

    /**
     * this method is used when the user selects restart option in the pause menu.
     */
    public void restartLevelTriggered(){
        getGameBoardView().setMessage("Restarting Game...");
        getGameScore().restartTimer();
        positionsReset();
        getGame().wallReset();
        setShowPauseMenu(false);
        getGameBoardView().updateGameBoardView();
    }

    /**
     * this method is used to show a debug console when the button (Alt + Shift + F1) is clicked.
     */
    public void debugConsoleButtonClicked() {
        setShowPauseMenu(true);
        if(isCanGetTime()){
            pauseGame();
        }
        displayDebugConsole();
    }

    /**
     * this method is used to get the View.DebugConsole object.
     *
     * @return returns the View.DebugConsole object.
     */
    public DebugConsole getDebugConsole() {
        return debugConsole;
    }

    /**
     * this method is used to set the View.DebugConsole object.
     *
     * @param debugConsole this is the View.DebugConsole object used to set into the variable.
     */
    public void setDebugConsole(DebugConsole debugConsole) {
        this.debugConsole = debugConsole;
    }

    /**
     * this method is used to restart the game status.
     */
    public void restartGameStatus() {
        positionsReset();
        getGame().wallReset();
        getGame().nextLevel();
    }

    /**
     * this method is used to get the gameTimer variable.
     *
     * @return returns a Timer datatype of gameTimer variable.
     */
    public Timer getGameTimer() {
        return gameTimer;
    }

    /**
     * this method is used to set the gameTimer variable.
     *
     * @param gameTimer this is the Timer datatype which will be used to set the gameTimer variable.
     */
    public void setGameTimer(Timer gameTimer) {
        this.gameTimer = gameTimer;
    }

    /**
     * this method is used to toggle between start and pause of the game.
     */
    public void startPauseButtonTriggered() {
        if(!isShowPauseMenu())
            if(getGameTimer().isRunning()){
                stopGame();
            }else{
                resumeGame();
            }
    }

    /**
     * this method is used to set the game into a pause state.
     */
    public void pauseMenuButtonClicked() {
        setShowPauseMenu(!isShowPauseMenu());
        getGameBoardView().updateGameBoardView();
        if (getGameTimer().isRunning()){
            stopGame();
        }
    }

    /**
     * this method is used to move the player to the left.
     */
    public void playerMoveLeft() {
        getPlayer().moveLeft();
    }

    /**
     * this method is used to move the player to the right.
     */
    public void playerMoveRight() {
        getPlayer().movRight();
    }

    /**
     * this method is used to pause the game that is using the space button.
     */
    public void pauseGame() {
        getGameScore().pauseTimer();
        setCanGetTime(false);
    }

    /**
     * this method is used to resume the game.
     */
    public void resumeGame() {
        getGameScore().startTimer();
        getGameTimer().start();
        setCanGetTime(true);
    }

    /**
     * this method is used to stop the game
     */
    public void stopGame() {
        pauseGame();
        getGameTimer().stop();
    }

    /**
     * this method is used to check if it can get the time to set it to a scoring variable.
     *
     * @return it returns true based on the variable value.
     */
    public boolean isCanGetTime() {
        return canGetTime;
    }

    /**
     * this method is used to set the variable if it can get the time to set to a scoring variable.
     *
     * @param canGetTime this is the variable used to set if it can take the time for the scoring.
     */
    public void setCanGetTime(boolean canGetTime) {
        this.canGetTime = canGetTime;
    }

    public GameBoardView getGameBoardView() {
        return gameBoardView;
    }

    /**
     * this method is used to set a game board view into a variable for future reference.
     *
     * @param gameBoardView this returns a game board view object.
     */
    public void setGameBoardView(GameBoardView gameBoardView) {
        this.gameBoardView = gameBoardView;
    }

    /**
     * this method is used to get a game score object which is used for getting the game score and saving it.
     *
     * @return this returns a game score object.
     */
    public GameScore getGameScore() {
        return gameScore;
    }

    /**
     * this method is used to set a game score object into a variable for future reference.
     *
     * @param gameScore this is the game score object used to set into a variable.
     */
    public void setGameScore(GameScore gameScore) {
        this.gameScore = gameScore;
    }

    /**
     * this method is used to get the definite width of the game board and the wall level width generation.
     *
     * @return it returns an integer to be used for the width of the game board and wall level.
     */
    public static int getDEF_WIDTH(){
        return DEF_WIDTH;
    }

    /**
     * this method is used to get the definite height of the game board and the wall level height generation
     *
     * @return it returns an integer to be used for the height of the game board and wall level.
     */
    public static int getDEF_HEIGHT(){
        return DEF_HEIGHT;
    }

    public GameScoreDisplay getGameScoreDisplay() {
        return gameScoreDisplay;
    }

    public void setGameScoreDisplay(GameScoreDisplay gameScoreDisplay) {
        this.gameScoreDisplay = gameScoreDisplay;
    }

    /**
     * this is used by the View.DebugPanel class to skip the level which will restart the timer and show the next level.
     */
    public void skipLevelTriggered(){
        getGameScore().restartTimer();
        positionsReset();
        getGame().wallReset();
        getGame().nextLevel();
    }

    /**
     * this method is used to set the ball y speed value.
     *
     * @param speed this is the integer value used to set the y speed of the ball.
     */
    public void setBallYSpeedValue(int speed){
        getBall().setYSpeed(speed);
    }

    /**
     * this method is used to set the ball X speed value.
     *
     * @param speed this is the integer value used to set the x speed of the ball.
     */
    public void setBallXSpeedValue(int speed){
        getBall().setXSpeed(speed);
    }

    /**
     * this method is called to reset the ball count based on a value fixed by the Game class (a constant value).
     */
    public void resetBallCountTriggered(){
        getGame().resetBallCount();
    }

    /**
     * this method is used to check if the bot mode (AI) is activated for the bot to play in behalf of the player.
     *
     * @return this returns a boolean value to see if the game is enabled for the bot to play.
     */
    public boolean isBotMode() {
        return botMode;
    }

    /**
     * this method is used to change between the player mode and AI mode.
     *
     * @param botMode this is the boolean value to set if it's player mode (false) or AI mode (true).
     */
    public void setBotMode(boolean botMode) {
        this.botMode = botMode;
    }
}
