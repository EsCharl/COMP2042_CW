import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 *  this is a class that handles the gaming part of the program. which includes the pause feature
 */
public class GameBoardController {

    final static int DEF_WIDTH = 600;
    final static int DEF_HEIGHT = 450;

    private boolean showPauseMenu;
    private boolean canGetTime;

    private Timer gameTimer;

    private String message;

    GameBoardView gameBoardView;
    GameScore gameScore;
    private Wall wall;
    private DebugConsole debugConsole;

    private static GameBoardController uniqueGameBoardController;

    /**
     * this method is used to create and return the one and only game board controller object. (singleton)
     *
     * @param owner this takes in the JFrame for the game board view object.
     */
    public static GameBoardController singletonGameBoardController(JFrame owner){
        if(getUniqueGameBoardController() == null){
            setUniqueGameBoard(new GameBoardController(owner));
        }
        return getUniqueGameBoardController();
    }

    /**
     * this constructor is used to create a game board object.
     *
     * @param owner this takes in the JFrame for the game board view object.
     */
    private GameBoardController(JFrame owner){

        setMessage("");
        setCanGetTime(false);

        setWall(Wall.singletonWall(new Rectangle(0,0, DEF_WIDTH, DEF_HEIGHT),30,3,6/2,new Point(300,430)));

        setDebugConsole(DebugConsole.singletonDebugConsole(owner, getWall()));

        gameScore = GameScore.singletonGameScore();
        setShowPauseMenu(false);

        gameBoardView = GameBoardView.singletonGameBoardView(this);

        //initialize the first level
        startGame();
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
    private static GameBoardController getUniqueGameBoardController() {
        return uniqueGameBoardController;
    }

    /**
     * this method is used to set the set a GameBoard object into uniqueGameBoard variable.
     *
     * @param uniqueGameBoardController this is the GameBoard object used to set the uniqueGameBoard variable.
     */
    private static void setUniqueGameBoard(GameBoardController uniqueGameBoardController) {
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
        getWall().getPlayer().stop();
    }

    /**
     * this method is used to save the game score.
     */
    void saveScoreLevel() {
        try {
            ArrayList<String> sorted = gameScore.getHighScore();
            gameScore.updateSaveFile(sorted);
            gameScore.highScorePanel(sorted);
        } catch (IOException | BadLocationException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method is used to start the game.
     */
    void startGame() {
        getWall().nextLevel();
        gameScore.setLevelFilePathName("/scores/Level"+ getWall().getCurrentLevel()+".txt");

        setGameTimer(new Timer(10, e ->{
            getWall().getMovements().entitiesMovements();
            getWall().getMovements().findImpacts();
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
                saveScoreLevel();

                gameScore.restartTimer();
            }
            gameScore.setLevelFilePathName("/scores/Level"+ getWall().getCurrentLevel()+".txt");

            gameBoardViewUpdate();
        }));
    }

    public void gameBoardViewUpdate() {
        gameBoardView.updateGameBoardView();
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

    public void lostFocusTriggered(){
        getGameTimer().stop();

        setMessage("Focus Lost");
        gameBoardViewUpdate();

        if(isCanGetTime()){
            pauseGame();
        }
    }

    public void restartLevelTriggered(){
        setMessage("Restarting Game...");
        gameScore.restartTimer();
        getWall().positionsReset();
        getWall().wallReset();
        setShowPauseMenu(false);
        gameBoardViewUpdate();
    }

    public void moveLeftButtonTriggered(){
        playerMoveLeft();
    }

    public void moveRightButtonTriggered(){
        playerMoveRight();
    }

    public void pauseButtonTriggered(){
        pauseMenuButtonClicked();
    }

    /**
     * this method is used to create the
     */
    public void debugConsoleButtonClicked() {
        setShowPauseMenu(true);
        if(isCanGetTime()){
            pauseGame();
        }
        displayDebugConsole();
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

    /**
     * this method is used to restart the game status.
     */
    public void restartGameStatus() {
        getWall().positionsReset();
        getWall().wallReset();
        getWall().nextLevel();
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
        gameBoardViewUpdate();
        if (getGameTimer().isRunning()){
            stopGame();
        }
    }

    /**
     * this method is used to move the player to the left.
     */
    public void playerMoveLeft() {
        getWall().getPlayer().moveLeft();
    }

    /**
     * this method is used to move the player to the right.
     */
    public void playerMoveRight() {
        getWall().getPlayer().movRight();
    }

    /**
     * this method is used to pause the game that is using the space button.
     */
    public void pauseGame() {
        gameScore.pauseTimer();
        setCanGetTime(false);
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
}
