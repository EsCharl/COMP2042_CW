import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class GameBoardModel {

    private String message;

    private boolean canGetTime;

    private Timer gameTimer;

    private Wall wall;

    private DebugConsole debugConsole;

    GameScore gameScore;
    GameBoardController gameBoardController;


    public GameBoardModel(GameBoardController gameBoardController, JFrame owner){
        gameScore = GameScore.singletonGameScore();

        gameScore.setStartTime(0);
        gameScore.setTotalTime(0);
        gameScore.setPauseTime(0);

        this.gameBoardController = gameBoardController;

        setWall(Wall.singletonWall(new Rectangle(0,0, GameBoardController.DEF_WIDTH, GameBoardController.DEF_HEIGHT),30,3,6/2,new Point(300,430)));

        setDebugConsole(DebugConsole.singletonDebugConsole(owner,getWall(),gameBoardController));

        setMessage("");
        setCanGetTime(false);
        setCanGetTime(false);
    }

    void startGame() {
        getWall().nextLevel();
        gameScore.setLevelFilePathName("/scores/Level"+ getWall().getWallLevel()+".txt");

        setGameTimer(new Timer(10, e ->{
            getWall().move();
            getWall().findImpacts();
            setMessage(String.format("Bricks: %d Balls %d", getWall().getBrickCount(), getWall().getBallCount()));
            if(getWall().isBallLost()){
                if(getWall().ballEnd()){
                    getWall().wallReset();
                    updateGameText("Game over");
                    gameScore.restartTimer();
                }
                getWall().positionsReset();
                getGameTimer().stop();
                gameScore.pauseTimer();
            }
            else if(getWall().isDone()){
                if(getWall().hasLevel()){
                    updateGameText("Go to Next Level");
                    restartGameStatus();
                }
                else{
                    updateGameText("ALL WALLS DESTROYED");
                }

                gameScore.pauseTimer();

                //for save file saving and high score pop up
                try {
                    ArrayList<String> sorted = gameScore.getHighScore();
                    gameScore.updateSaveFile(sorted);
                    gameScore.highScorePanel(sorted);
                } catch (IOException | BadLocationException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
                gameScore.restartTimer();
            }
            gameScore.setLevelFilePathName("/scores/Level"+ getWall().getWallLevel()+".txt");

            gameBoardController.gameBoardView.updateGameBoardView();
        }));
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
        getDebugConsole().setVisible(true);
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

    public void playerMoveRight() {
        getWall().getPlayer().movRight();
    }

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
        getWall().nextLevel();
        getWall().wallReset();
        getWall().positionsReset();
    }

    public void restartLevel() {
        setMessage("Restarting Game...");
        getWall().positionsReset();
        getWall().wallReset();
        gameBoardController.setShowPauseMenu(false);
        gameBoardController.gameBoardView.updateGameBoardView();

        gameScore.restartTimer();
    }

    public void updateGameText(String s) {
        setMessage(s);
        getGameTimer().stop();
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

    public void debugConsoleButtonClicked() {
        gameBoardController.setShowPauseMenu(true);
        if(isCanGetTime()){
            pauseGame();
        }
        displayDebugConsole();
    }

    public void pauseMenuButtonClicked() {
        gameBoardController.setShowPauseMenu(!gameBoardController.isShowPauseMenu());
        gameBoardController.gameBoardView.updateGameBoardView();
        if (getGameTimer().isRunning()){
            stopGame();
        }
    }

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

    /**
     * this method is used to get the DebugConsole object.
     *
     * @return returns the DebugConsole object.
     */
    public DebugConsole getDebugConsole() {
        return debugConsole;
    }

    /**
     * this method is used to set the DebugConsole object.
     *
     * @param debugConsole this is the DebugConsole object used to set into the variable.
     */
    public void setDebugConsole(DebugConsole debugConsole) {
        this.debugConsole = debugConsole;
    }
}
