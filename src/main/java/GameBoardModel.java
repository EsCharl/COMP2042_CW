import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * this class is used to handle the game board logic.
 */
public class GameBoardModel {

    private String message;

    private boolean canGetTime;

    private Timer gameTimer;

    private Wall wall;

    GameScore gameScore;
    GameBoardController gameBoardController;


    /**
     * this constructor is used to create a game board object that handles all the logic.
     *
     * @param gameBoardController this is the controller of the game board which takes in all the user inputs.
     */
    public GameBoardModel(GameBoardController gameBoardController){
        gameScore = GameScore.singletonGameScore();

        gameScore.setStartTime(0);
        gameScore.setTotalTime(0);
        gameScore.setPauseTime(0);

        this.gameBoardController = gameBoardController;

        setWall(Wall.singletonWall(new Rectangle(0,0, GameBoardController.DEF_WIDTH, GameBoardController.DEF_HEIGHT),30,3,6/2,new Point(300,430)));

        setMessage("");
        setCanGetTime(false);
        setCanGetTime(false);
    }

    /**
     * this method is used to start the game.
     */
    void startGame() {
        getWall().nextLevel();
        gameScore.setLevelFilePathName("/scores/Level"+ getWall().getCurrentLevel()+".txt");

        setGameTimer(new Timer(10, e ->{
            getWall().movements.entitiesMovements();
            getWall().movements.findImpacts();
            setMessage(String.format("Bricks: %d Balls %d", getWall().getBrickCount(), getWall().getBallCount()));
            if(getWall().isBallLost()){
                if(getWall().ballEnd()){
                    getWall().wallReset();
                    setMessage("Game over");
                    getGameTimer().stop();
                    gameScore.restartTimer();
                }
                getWall().positionsReset();
                getGameTimer().stop();
                gameScore.pauseTimer();
            }
            else if(getWall().isDone()){
                if(getWall().hasLevel()){
                    setMessage("Go to Next Level");
                    getGameTimer().stop();
                    restartGameStatus();
                }
                else{
                    setMessage("ALL WALLS DESTROYED");
                    getGameTimer().stop();
                }

                gameScore.pauseTimer();

                //for save file saving and high score pop up
                saveLevelScore();

                gameScore.restartTimer();
            }
            gameScore.setLevelFilePathName("/scores/Level"+ getWall().getCurrentLevel()+".txt");

            gameBoardController.gameBoardView.updateGameBoardView();
        }));
    }

    /**
     * this method is used to save the game score.
     */
    private void saveLevelScore() {
        try {
            ArrayList<String> sorted = gameScore.getHighScore();
            gameScore.updateSaveFile(sorted);
            gameScore.highScorePanel(sorted);
        } catch (IOException | BadLocationException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method is used to pause the game that is using the space button.
     */
    public void pauseGame() {
        gameScore.pauseTimer();
        setCanGetTime(false);
    }

    /**
     * this method is used to set the debug console visible on the window and let the game .
     */
    public void displayDebugConsole() {
        getGameTimer().stop();
        gameBoardController.gameBoardView.getDebugConsole().setVisible(true);
    }

    /**
     * this method is used to stop the player movement.
     */
    public void playerStopMoving() {
        getWall().getPlayer().stop();
    }

    /**
     * this method is used to resume the game.
     */
    public void resumeGame() {
        gameScore.startTimer();
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
     * this method is used to move the player to the right.
     */
    public void playerMoveRight() {
        getWall().getPlayer().movRight();
    }

    /**
     * this method is used to move the player to the left.
     */
    public void playerMoveLeft() {
        getWall().getPlayer().moveLeft();
    }

    /**
     * this method is used to get the value in the message variable.
     *
     * @return returns the value in the message variable.
     */
    public String getMessage() {
        return message;
    }

    /**
     * this method is used to change the message variable.
     *
     * @param message the String used to change the message variable.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * this is used by the DebugPanel class to skip the level which will restart the timer and generate the next level.
     *
     */
    void skipLevel(){
        gameScore.restartTimer();
        restartGameStatus();
    }

    /**
     * this method is used to restart the level.
     */
    public void restartLevel() {
        setMessage("Restarting Game...");
        getWall().positionsReset();
        getWall().wallReset();
        gameBoardController.setShowPauseMenu(false);
        gameBoardController.gameBoardView.updateGameBoardView();

        gameScore.restartTimer();
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
     * this method is used to get the gameTimer variable.
     *
     * @return returns a Timer datatype of gameTimer variable.
     */
    public Timer getGameTimer() {
        return gameTimer;
    }

    /**
     * this method is used used to create the
     */
    public void debugConsoleButtonClicked() {
        gameBoardController.setShowPauseMenu(true);
        if(isCanGetTime()){
            pauseGame();
        }
        displayDebugConsole();
    }

    /**
     * this method is used to set the game into a pause state.
     */
    public void pauseMenuButtonClicked() {
        gameBoardController.setShowPauseMenu(!gameBoardController.isShowPauseMenu());
        gameBoardController.gameBoardView.updateGameBoardView();
        if (getGameTimer().isRunning()){
            stopGame();
        }
    }

    /**
     * this method is used to toggle between start and pause of the game.
     */
    public void startPauseGameButtonClicked() {
        if(!gameBoardController.isShowPauseMenu())
            if(getGameTimer().isRunning()){
                stopGame();
            }else{
                resumeGame();
            }
    }

    /**
     * this method is used to set the variable if it can get the time to set to a scoring variable.
     *
     * @param canGetTime this is the variable used to set if it can take the time for the scoring.
     */
    public void setCanGetTime(boolean canGetTime) {
        this.canGetTime = canGetTime;
    }

    /**
     * this method is used to check if it can get the time to set it to a scoring variable.
     *
     * @return it returns true based on the variable value.
     */
    public boolean isCanGetTime() {
        return canGetTime;
    }

    public void restartGameStatus() {
        getWall().positionsReset();
        getWall().wallReset();
        getWall().nextLevel();
    }

    /**
     * this method is used to get the wall variable.
     *
     * @return returns a Wall datatype of wall variable.
     */
    public Wall getWall() {
        return wall;
    }

    /**
     * this method is used to set the wall variable
     *
     * @param wall this is the Wall datatype which will be used to set the wall variable.
     */
    public void setWall(Wall wall) {
        this.wall = wall;
    }


}
