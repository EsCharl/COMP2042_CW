import javax.swing.*;

/**
 * this class is used to handle the game board logic.
 */
public class GameBoardModel {

    private int wallWidth;
    private int wallLength;

    private String message;

    private boolean canGetTime;

    private Timer gameTimer;

    GameBoardController gameBoardController;

    private static GameBoardModel uniqueGameBoardModel;

    public static GameBoardModel singletonGameBoardModel(GameBoardController gameBoardController){
        if(getUniqueGameBoardModel() == null){
            setUniqueGameBoardModel(new GameBoardModel(gameBoardController));
        }
        return getUniqueGameBoardModel();
    }

    /**
     * this constructor is used to create a game board object that handles all the logic.
     *
     * @param gameBoardController this is the controller of the game board which takes in all the user inputs.
     */
    private GameBoardModel(GameBoardController gameBoardController){

        this.gameBoardController = gameBoardController;

        setWallLength(GameBoardView.DEF_HEIGHT);
        setWallWidth(GameBoardView.DEF_WIDTH);


        setMessage("");
        setCanGetTime(false);
        setCanGetTime(false);
    }

    /**
     * this method is used to pause the game that is using the space button.
     */
    public void pauseGame() {
        gameBoardController.gameScore.pauseTimer();
        setCanGetTime(false);
    }

    /**
     * this method is used to resume the game.
     */
    public void resumeGame() {
        gameBoardController.gameScore.startTimer();
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
        gameBoardController.getWall().getPlayer().movRight();
    }

    /**
     * this method is used to move the player to the left.
     */
    public void playerMoveLeft() {
        gameBoardController.getWall().getPlayer().moveLeft();
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
        gameBoardController.gameScore.restartTimer();
        restartGameStatus();
    }

    /**
     * this method is used to restart the level.
     */
    public void restartLevel() {
        setMessage("Restarting Game...");
        gameBoardController.getWall().positionsReset();
        gameBoardController.getWall().wallReset();
        gameBoardController.setShowPauseMenu(false);
        gameBoardController.gameBoardViewUpdate();
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
     * this method is used to set the game into a pause state.
     */
    public void pauseMenuButtonClicked() {
        gameBoardController.setShowPauseMenu(!gameBoardController.isShowPauseMenu());
        gameBoardController.gameBoardViewUpdate();
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
        gameBoardController.getWall().positionsReset();
        gameBoardController.getWall().wallReset();
        gameBoardController.getWall().nextLevel();
    }

    /**
     * this method is to get the wall width.
     *
     * @return returns an integer which is the wall width.
     */
    public int getWallWidth() {
        return wallWidth;
    }

    /**
     * this method is used to set the game window width.
     *
     * @param wallWidth this is the integer used to set the width of the wall.
     */
    public void setWallWidth(int wallWidth) {
        this.wallWidth = wallWidth;
    }

    /**
     * this method is used to get the wall length.
     *
     * @return returns an integer which is the wall length.
     */
    public int getWallLength() {
        return wallLength;
    }

    /**
     * this method is used to set the wall length.
     *
     * @param wallLength this is the integer used to set the wall length.
     */
    public void setWallLength(int wallLength) {
        this.wallLength = wallLength;
    }

    private static GameBoardModel getUniqueGameBoardModel() {
        return uniqueGameBoardModel;
    }

    private static void setUniqueGameBoardModel(GameBoardModel uniqueGameBoardModel) {
        GameBoardModel.uniqueGameBoardModel = uniqueGameBoardModel;
    }

    /**
     * this method is used when the user is not focused on the game. i.e. when the user clicked on other components or outside the game window.
     */
    public void onLostFocus(){
        getGameTimer().stop();
        setMessage("Focus Lost");
        gameBoardController.gameBoardViewUpdate();

        if(isCanGetTime()){
            pauseGame();
        }
    }
}
