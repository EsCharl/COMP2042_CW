import javax.swing.*;

/**
 * this class is used to handle the game board logic.
 */
public class GameBoardModel {

    private String message;

    private boolean canGetTime;


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
        gameBoardController.getGameTimer().start();
        setCanGetTime(true);
    }

    /**
     * this method is used to stop the game
     */
    public void stopGame() {
        pauseGame();
        gameBoardController.getGameTimer().stop();
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
     * this method is used to set the game into a pause state.
     */
    public void pauseMenuButtonClicked() {
        gameBoardController.setShowPauseMenu(!gameBoardController.isShowPauseMenu());
        gameBoardController.gameBoardViewUpdate();
        if (gameBoardController.getGameTimer().isRunning()){
            stopGame();
        }
    }

    /**
     * this method is used to toggle between start and pause of the game.
     */
    public void startPauseGameButtonClicked() {
        if(!gameBoardController.isShowPauseMenu())
            if(gameBoardController.getGameTimer().isRunning()){
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

    private static GameBoardModel getUniqueGameBoardModel() {
        return uniqueGameBoardModel;
    }

    private static void setUniqueGameBoardModel(GameBoardModel uniqueGameBoardModel) {
        GameBoardModel.uniqueGameBoardModel = uniqueGameBoardModel;
    }
}
