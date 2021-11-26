/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *  this is a class that handles the gaming part of the program. which includes the pause feature
 */
public class GameBoardController extends JComponent implements KeyListener,MouseListener,MouseMotionListener {

    private static final int TEXT_SIZE = 30;

    private Timer gameTimer;

    private Wall wall;

    private String message;

    private boolean showPauseMenu;
    private boolean canGetTime;

    private Font menuFont;

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;
    private int stringDisplayLength;

    private DebugConsole debugConsole;
    private GameBoardView gameBoardView = new GameBoardView();
    private GameBoardModel gameBoardModel = new GameBoardModel();

    private GameScore gameScore = GameScore.singletonGameScore();

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
        super();

        setStringDisplayLength(0);
        setShowPauseMenu(false);
        setCanGetTime(false);

        gameScore.setStartTime(0);
        gameScore.setTotalTime(0);
        gameScore.setPauseTime(0);

        setMenuFont(new Font("Monospaced",Font.PLAIN,TEXT_SIZE));

        this.gameBoardView.initialize(this);
        setMessage("");

        setWall(Wall.singletonWall(new Rectangle(0,0, GameBoardView.DEF_WIDTH, GameBoardView.DEF_HEIGHT),30,3,6/2,new Point(300,430)));

        setDebugConsole(DebugConsole.singletonDebugConsole(owner,wall,this));
        //initialize the first level
        gameBoardModel.startGame(this);

    }

    public void updateGameView() {
        repaint();
    }

    public void updateGameText(String s) {
        setMessage(s);
        getGameTimer().stop();
    }

    public void restartGameCondition() {
        getWall().positionsReset();
        getWall().wallReset();
        getWall().nextLevel();
    }

    /**
     * This method is used for apply the objects, namely the pause menu, the bricks, the ball, and the paddle which is controlled by the user on the screen.
     *
     * @param g this is an object where it will be drawn upon.
     */
    public void paint(Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        gameBoardView.clear(g2d, this);

        g2d.setColor(Color.BLUE);
        g2d.drawString(getMessage(),250,225);

        gameBoardView.drawBall(getWall().getBall(),g2d);

        for(Brick b : getWall().getBricks())
            if(!b.isBroken())
                gameBoardView.drawBrick(b,g2d);

        gameBoardView.drawPlayer(getWall().getPlayer(),g2d);

        if(isShowPauseMenu())
            gameBoardView.drawMenu(g2d, this);

        Toolkit.getDefaultToolkit().sync();
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
                getWall().getPlayer().moveLeft();
                break;
            case KeyEvent.VK_D:
                getWall().getPlayer().movRight();
                break;
            case KeyEvent.VK_ESCAPE:
                setShowPauseMenu(!isShowPauseMenu());
                updateGameView();
                if (getGameTimer().isRunning()){
                    gameScore.pauseTimer();
                    setCanGetTime(false);
                    getGameTimer().stop();
                }
                break;
            case KeyEvent.VK_SPACE:
                if(!isShowPauseMenu())
                    if(getGameTimer().isRunning()){
                        gameScore.pauseTimer();
                        setCanGetTime(false);
                        getGameTimer().stop();
                    }else{
                        gameScore.startTimer();
                        getGameTimer().start();
                        setCanGetTime(true);
                    }
                break;
            case KeyEvent.VK_F1:
                if(keyEvent.isAltDown() && keyEvent.isShiftDown()){
                    setShowPauseMenu(true);
                    if(isCanGetTime()){
                        gameScore.pauseTimer();
                        setCanGetTime(false);
                    }
                    getGameTimer().stop();
                    getDebugConsole().setVisible(true);
                }
            default:
                getWall().getPlayer().stop();
        }
    }

    /**
     * this is initially a KeyListener Interface class method, and it is being implemented. It is called when a key is released from the keyboard. this is used to stop the paddle from moving.
     *
     * @param keyEvent this records the input from the keyboard.
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        getWall().getPlayer().stop();
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
        if(getContinueButtonRect().contains(mouseGetPointEvent(mouseEvent))){
            setShowPauseMenu(false);
            updateGameView();
        }
        else if(getRestartButtonRect().contains(mouseGetPointEvent(mouseEvent))){
            setMessage("Restarting Game...");
            getWall().positionsReset();
            getWall().wallReset();
            setShowPauseMenu(false);
            updateGameView();

            gameScore.restartTimer();
        }
        else if(getExitButtonRect().contains(mouseGetPointEvent(mouseEvent))){
            System.exit(0);
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
     * this is initially a MouseMotionListener Interface class method, and it is being implemented. It is called when a mouse moved onto a component but no buttons are pushed. In this case it will change the cursor when the mouse is on the button.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if(getExitButtonRect() != null && isShowPauseMenu()) {
            if (checkIfMouseMovedToButton(mouseGetPointEvent(mouseEvent)))
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else
                this.setCursor(Cursor.getDefaultCursor());
        }
        else{
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    private Point mouseGetPointEvent(MouseEvent mouseEvent){
        return mouseEvent.getPoint();
    }

    /**
     * this method is used to check if the mouse is on the button.
     *
     * @param p this takes the event generated by the mouse.
     * @return returns true if the mouse is on the button.
     */
    private boolean checkIfMouseMovedToButton(Point p){
        return (getExitButtonRect().contains(p) || getRestartButtonRect().contains(p) || getContinueButtonRect().contains(p));
    }

    /**
     * this method is used when the user is not focused on the game. i.e. when the user clicked on other components or outside the game window.
     */
    public void onLostFocus(){
        getGameTimer().stop();
        setMessage("Focus Lost");
        updateGameView();

        if(isCanGetTime()){
            gameScore.pauseTimer();
            setCanGetTime(false);
        }
    }

    /**
     * this is used by the DebugPanel class to skip the level which will restart the timer and generate the next level.
     */
    void skipLevel(){
        gameScore.restartTimer();
        getWall().nextLevel();
        getWall().wallReset();
        getWall().positionsReset();
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
     * this is to get the font of the menu font
     *
     * @return returns the font used by the menu font.
     */
    public Font getMenuFont() {
        return menuFont;
    }

    /**
     * this method is used to change the menu font.
     *
     * @param menuFont this is the font used to change the menu font to.
     */
    public void setMenuFont(Font menuFont) {
        this.menuFont = menuFont;
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

    /**
     * this method is used to get the rectangle continue button.
     *
     * @return it returns the rectangle object which is the continue button.
     */
    public Rectangle getContinueButtonRect() {
        return continueButtonRect;
    }

    /**
     * this method is used to set the rectangle continue button.
     *
     * @param continueButtonRect it takes in a rectangle object which is set to be the continue button.
     */
    public void setContinueButtonRect(Rectangle continueButtonRect) {
        this.continueButtonRect = continueButtonRect;
    }

    /**
     * this method is used to return the rectangle object which is the exit button.
     *
     * @return this returns a rectangle object which is the exit button.
     */
    public Rectangle getExitButtonRect() {
        return exitButtonRect;
    }

    /**
     * this method is used to set the rectangle exit button object.
     *
     * @param exitButtonRect this is the rectangle object which is to be the exit button.
     */
    public void setExitButtonRect(Rectangle exitButtonRect) {
        this.exitButtonRect = exitButtonRect;
    }

    /**
     * this is the method used to get the rectangle restart object.
     *
     * @return it returns a rectangle object which is the restart button.
     */
    public Rectangle getRestartButtonRect() {
        return restartButtonRect;
    }

    /**
     * this method is used to set a rectangle object to be the restart button.
     *
     * @param restartButtonRect this is the rectangle object that is used to be the restart button.
     */
    public void setRestartButtonRect(Rectangle restartButtonRect) {
        this.restartButtonRect = restartButtonRect;
    }

    /**
     * this is the method used to get the value from the stringDisplayLength variable.
     *
     * @return it returns the value from stringDisplayLength.
     */
    public int getStringDisplayLength() {
        return stringDisplayLength;
    }

    /**
     * this method is used to set the display length of the string into a variable.
     *
     * @param stringDisplayLength this is the length (Integer) used to set the stringDisplayLength variable.
     */
    public void setStringDisplayLength(int stringDisplayLength) {
        this.stringDisplayLength = stringDisplayLength;
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
}
