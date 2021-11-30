package Controller;

import Model.GameScore;
import Model.Game;
import View.GameBoardView;
import View.GameScoreDisplay;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 *  this is a class that handles the gaming part of the program. which includes the pause feature
 */
public class GameBoardController implements KeyListener, MouseListener, MouseMotionListener {

    private final static int DEF_WIDTH = 600;
    private final static int DEF_HEIGHT = 450;

    private boolean showPauseMenu;
    private boolean canGetTime;

    private Timer gameTimer;

    private GameBoardView gameBoardView;
    private GameScore gameScore;
    private Game game;
    private DebugConsole debugConsole;

    private static GameBoardController uniqueGameBoardController;
    private GameScoreDisplay gameScoreDisplay;

    /**
     * this method is used to create and return the one and only game board controller object. (singleton)
     *
     * @param owner this takes in the JFrame for the game board view object.
     */
    public static GameBoardController singletonGameBoardController(JFrame owner){
        if(getUniqueGameBoardController() == null){
            setUniqueGameBoardController(new GameBoardController(owner));
        }
        return getUniqueGameBoardController();
    }

    /**
     * this constructor is used to create a game board object.
     *
     * @param owner this takes in the JFrame for the game board view object.
     */
    private GameBoardController(JFrame owner){

        setCanGetTime(false);

        setGame(Game.singletonWall(new Rectangle(0,0, getDEF_WIDTH(), getDEF_HEIGHT()),30,3,6/2,new Point(300,430)));

        setDebugConsole(DebugConsole.singletonDebugConsole(owner, getGame()));

        setGameScore(GameScore.singletonGameScore());
        setShowPauseMenu(false);

        setGameBoardView(GameBoardView.singletonGameBoardView(this, getGame()));
        getGameBoardView().setMessage("");

        gameBoardView.addKeyListener(this);
        gameBoardView.addMouseListener(this);
        gameBoardView.addMouseMotionListener(this);

        setGameScoreDisplay(new GameScoreDisplay());

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
        getGame().getPlayer().stop();
    }

    /**
     * this method is used to save the game score.
     */
    private void saveScoreLevel() {
        try {
            ArrayList<String> sorted = getGameScore().getHighScore();
            getGameScore().updateSaveFile(sorted);
            getGameScoreDisplay().highScorePanel(sorted);
        } catch (IOException | BadLocationException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method is used to start the game.
     */
    private void startGame() {
        getGame().nextLevel();
        getGameScore().setLevelFilePathName("/scores/Level"+ getGame().getCurrentLevel()+".txt");

        setGameTimer(new Timer(10, e ->{
            getGame().getMovements().entitiesMovements();
            getGame().getMovements().findImpacts();
            getGameBoardView().setMessage(String.format("Bricks: %d Balls %d", getGame().getBrickCount(), getGame().getBallCount()));
            if(getGame().isBallLost()){
                if(getGame().ballEnd()){
                    getGame().wallReset();
                    getGameBoardView().setMessage("Game over");
                    getGameTimer().stop();
                    getGameScore().restartTimer();
                }
                getGame().positionsReset();
                getGameTimer().stop();
                getGameScore().pauseTimer();
            }
            else if(getGame().isDone()){
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

            getGameBoardView().updateGameBoardView();
        }));
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
        getGame().positionsReset();
        getGame().wallReset();
        setShowPauseMenu(false);
        getGameBoardView().updateGameBoardView();
    }

    /**
     * this method is used when the user used the move left button (A) during the gameplay to move left.
     */
    public void moveLeftButtonTriggered(){
        playerMoveLeft();
    }

    /**
     * this method is used when the user used the move right button (D) during the gameplay to move right.
     */
    public void moveRightButtonTriggered(){
        playerMoveRight();
    }

    /**
     * this method is used when the pause button (esc) is pressed during the gameplay.
     */
    public void pauseButtonTriggered(){
        pauseMenuButtonClicked();
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
     * this method is used to get the Controller.DebugConsole object.
     *
     * @return returns the Controller.DebugConsole object.
     */
    public DebugConsole getDebugConsole() {
        return debugConsole;
    }

    /**
     * this method is used to set the Controller.DebugConsole object.
     *
     * @param debugConsole this is the Controller.DebugConsole object used to set into the variable.
     */
    public void setDebugConsole(DebugConsole debugConsole) {
        this.debugConsole = debugConsole;
    }

    /**
     * this method is used to restart the game status.
     */
    public void restartGameStatus() {
        getGame().positionsReset();
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
        getGame().getPlayer().moveLeft();
    }

    /**
     * this method is used to move the player to the right.
     */
    public void playerMoveRight() {
        getGame().getPlayer().movRight();
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
     * this is initially a KeyListener Interface class method, and it is being implemented. It iS activated when a key is typed from the keyboard.
     *
     * @param keyEvent this records the input from the keyboard.
     */
    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    /**
     * this is initially a KeyListener Interface class method, and it is being implemented. It is activated when a key is pressed from the keyboard. This is used to control the user experience for the game.
     *
     * @param keyEvent this records the input from the keyboard.
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch(keyEvent.getKeyCode()){
            case KeyEvent.VK_A:
                moveLeftButtonTriggered();
                break;
            case KeyEvent.VK_D:
                moveRightButtonTriggered();
                break;
            case KeyEvent.VK_ESCAPE:
                pauseButtonTriggered();
                break;
            case KeyEvent.VK_SPACE:
                startPauseButtonTriggered();
                break;
            case KeyEvent.VK_F1:
                if(keyEvent.isAltDown() && keyEvent.isShiftDown()){
                    debugConsoleButtonClicked();
                }
            default:
                playerStopMoving();
        }
    }

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse button is pressed.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse button is released.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse enters a component.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse exits a component.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    /**
     * this is initially a MouseMotionListener Interface class method, and it is being implemented. It is called when a mouse pressed on a component and dragged.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse button is clicked. this is used to select the options that are shown during the pause menu is shown.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(!isShowPauseMenu())
            return;
        if(gameBoardView.getContinueButtonRect().contains(mouseGetPointEvent(mouseEvent))){
            setShowPauseMenu(false);
            gameBoardView.updateGameBoardView();
        }
        else if(gameBoardView.getRestartButtonRect().contains(mouseGetPointEvent(mouseEvent))){
            restartLevelTriggered();
        }
        else if(gameBoardView.getExitButtonRect().contains(mouseGetPointEvent(mouseEvent))){
            System.exit(0);
        }
    }

    /**
     * this is initially a KeyListener Interface class method, and it is being implemented. It is called when a key is released from the keyboard. this is used to stop the paddle from moving.
     *
     * @param keyEvent this records the input from the keyboard.
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        playerStopMoving();
    }

    /**
     * this method is used to check if the mouse is on the button.
     *
     * @param p this takes the event generated by the mouse.
     * @return returns true if the mouse is on the button.
     */
    public boolean checkIfMouseMovedToButton(Point p){
        return (gameBoardView.getExitButtonRect().contains(p) || gameBoardView.getRestartButtonRect().contains(p) || gameBoardView.getContinueButtonRect().contains(p));
    }

    public Point mouseGetPointEvent(MouseEvent mouseEvent){
        return mouseEvent.getPoint();
    }

    /**
     * this method is used to change the look of the cursor.
     *
     * @param Cursor the cursor state change to.
     */
    public void setCursorLook(Cursor Cursor) {
        gameBoardView.setCursor(Cursor);
    }

    /**
     * this is initially a MouseMotionListener Interface class method, and it is being implemented. It is called when a mouse moved onto a component but no buttons are pushed. In this case it will change the cursor when the mouse is on the button.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if(gameBoardView.getExitButtonRect() != null && isShowPauseMenu()) {
            if (checkIfMouseMovedToButton(mouseGetPointEvent(mouseEvent)))
                setCursorLook(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else {
                setCursorLook(Cursor.getDefaultCursor());
            }
        }
        else{
            setCursorLook(Cursor.getDefaultCursor());
        }
    }
}
