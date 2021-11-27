import javax.swing.*;

/**
 *  this is a class that handles the gaming part of the program. which includes the pause feature
 */
public class GameBoardController  {

    private boolean showPauseMenu;

    GameBoardView gameBoardView;
    GameBoardModel gameBoardModel;

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

        setShowPauseMenu(false);

        gameBoardModel = GameBoardModel.singletonGameBoardModel(this);

        gameBoardView = GameBoardView.singletonGameBoardView(this, owner);

        //initialize the first level
        gameBoardModel.startGame();
    }

    /**
     * this method is used when the user is not focused on the game. i.e. when the user clicked on other components or outside the game window.
     */
    public void onLostFocus(){
        gameBoardModel.getGameTimer().stop();
        gameBoardModel.setMessage("Focus Lost");
        gameBoardView.updateGameBoardView();

        if(gameBoardModel.isCanGetTime()){
            gameBoardModel.pauseGame();
        }
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
}
