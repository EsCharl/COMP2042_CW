import javax.swing.*;

/**
 *  this is a class that handles the gaming part of the program. which includes the pause feature
 */
public class GameBoardController  {

    final static int DEF_WIDTH = 600;
    final static int DEF_HEIGHT = 450;

    private boolean showPauseMenu;

    GameBoardView gameBoardView;
    GameBoardModel gameBoardModel;

    private static GameBoardController uniqueGameBoardController;

    public static GameBoardController singletonGameBoard(JFrame owner){
        if(getUniqueGameBoard() == null){
            setUniqueGameBoard(new GameBoardController(owner));
        }
        return getUniqueGameBoard();
    }

    /**
     * this constructor is used to create a game board object.
     *
     * @param owner this takes in the JFrame for the game board object.
     */
    private GameBoardController(JFrame owner){

        setShowPauseMenu(false);

        gameBoardModel = new GameBoardModel(this);

        gameBoardView = new GameBoardView(this, owner);

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
    public static GameBoardController getUniqueGameBoard() {
        return uniqueGameBoardController;
    }

    /**
     * this method is used to set the set a GameBoard object into uniqueGameBoard variable.
     *
     * @param uniqueGameBoardController this is the GameBoard object used to set the uniqueGameBoard variable.
     */
    public static void setUniqueGameBoard(GameBoardController uniqueGameBoardController) {
        GameBoardController.uniqueGameBoardController = uniqueGameBoardController;
    }
}
