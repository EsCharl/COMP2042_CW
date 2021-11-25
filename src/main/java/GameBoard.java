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
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 *  this is a class that handles the gaming part of the program. which includes the pause feature
 */
public class GameBoard extends JComponent implements KeyListener,MouseListener,MouseMotionListener {

    private static final String CONTINUE_TEXT = "Continue";
    private static final String RESTART_TEXT = "Restart";
    private static final String EXIT_TEXT = "Exit";
    private static final String PAUSE_TEXT = "Pause Menu";

    private static final int TEXT_SIZE = 30;
    private static final Color MENU_COLOR = new Color(0,255,0);


    private static final Color BG_COLOR = Color.WHITE;

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
    private GameBoardController gameBoardController;
    private GameBoardView gameBoardView = new GameBoardView();

    private GameScore gameScore = GameScore.singletonGameScore();

    private static GameBoard uniqueGameBoard;

    public static GameBoard singletonGameBoard(JFrame owner){
        if(getUniqueGameBoard() == null){
            setUniqueGameBoard(new GameBoard(owner));
        }
        return getUniqueGameBoard();
    }

    /**
     * this constructor is used to create a game board object.
     *
     * @param owner this takes in the JFrame for the game board object.
     */
    private GameBoard(JFrame owner){
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
        getWall().nextLevel();
        gameScore.setLevelFilePathName("/scores/Level"+getWall().getWallLevel()+".txt");

        setGameTimer(new Timer(10,e ->{
            getWall().move();
            getWall().findImpacts();
            setMessage(String.format("Bricks: %d Balls %d",getWall().getBrickCount(),getWall().getBallCount()));
            if(getWall().isBallLost()){
                if(getWall().ballEnd()){
                    getWall().wallReset();
                    setMessage("Game over");
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
                    getWall().positionsReset();
                    getWall().wallReset();
                    getWall().nextLevel();
                }
                else{
                    setMessage("ALL WALLS DESTROYED");
                    getGameTimer().stop();
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
            gameScore.setLevelFilePathName("/scores/Level"+getWall().getWallLevel()+".txt");

            repaint();
        }));

    }

    /**
     * This method is used for apply the objects, namely the pause menu, the bricks, the ball, and the paddle which is controlled by the user on the screen.
     *
     * @param g this is an object where it will be drawn upon.
     */
    public void paint(Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        clear(g2d);

        g2d.setColor(Color.BLUE);
        g2d.drawString(getMessage(),250,225);

        drawBall(getWall().getBall(),g2d);

        for(Brick b : getWall().getBricks())
            if(!b.isBroken())
                drawBrick(b,g2d);

        drawPlayer(getWall().getPlayer(),g2d);

        if(isShowPauseMenu())
            drawMenu(g2d);

        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * This is for clearing the screen by setting the whole window to be set into the background colour.
     *
     * @param g2d this is the object that is being passed into for clearing.
     */
    private void clear(Graphics2D g2d){
        Color tmp = g2d.getColor();
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0,0,getWidth(),getHeight());
        g2d.setColor(tmp);
    }

    /**
     * This method is used for drawing the bricks for the level.
     *
     * @param brick this is the information of the brick that is going to be used for the drawing of the brick.
     * @param g2d this takes in the object that is being used for the brick generation for the level.
     */
    private void drawBrick(Brick brick,Graphics2D g2d){
        Color tmp = g2d.getColor();

        g2d.setColor(brick.getInnerColor());
        g2d.fill(brick.getBrick());

        g2d.setColor(brick.getBorderColor());
        g2d.draw(brick.getBrick());


        g2d.setColor(tmp);
    }

    /**
     * this method is used for drawing the ball used for the game.
     *
     * @param ball the object of the ball that is going to be drawn
     * @param g2d the information that is being used to draw the ball.
     */
    private void drawBall(Ball ball,Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = ball.getBallFace();

        g2d.setColor(ball.getInnerBallColor());
        g2d.fill(s);

        g2d.setColor(ball.getBorderBallColor());
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    /**
     * This is used to draw the paddle used by the user for the game.
     *
     * @param p the contains the information needed about the paddle to be drawn.
     * @param g2d this is the object where the paddle is being drawn.
     */
    private void drawPlayer(Player p,Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = p.getPlayerFace();
        g2d.setColor(Player.INNER_COLOR);
        g2d.fill(s);

        g2d.setColor(Player.BORDER_COLOR);
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    /**
     * This method is used to draw the pause menu (refer to drawPauseMenu method) and to obscure the game board (refer to the obscureGameBoard method).
     *
     * @param g2d this is the object used to draw the menu.
     */
    private void drawMenu(Graphics2D g2d){
        gameBoardView.obscureGameBoard(g2d);
        drawPauseMenu(g2d);
    }

    /**
     * This method is used to draw the pause menu and the buttons of the pause menu.
     *
     * @param g2d this is the object to draw the pause menu.
     */
    private void drawPauseMenu(Graphics2D g2d){
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();


        g2d.setFont(getMenuFont());
        g2d.setColor(MENU_COLOR);

        if(getStringDisplayLength() == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            setStringDisplayLength(getMenuFont().getStringBounds(PAUSE_TEXT,frc).getBounds().width);
        }

        // get the position of top center.
        int x = (this.getWidth() - getStringDisplayLength()) / 2;
        int y = this.getHeight() / 10;

        g2d.drawString(PAUSE_TEXT,x,y);

        x = this.getWidth() / 8;
        y = this.getHeight() / 4;


        if(getContinueButtonRect() == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            setContinueButtonRect(getMenuFont().getStringBounds(CONTINUE_TEXT,frc).getBounds());
            getContinueButtonRect().setLocation(x,y-getContinueButtonRect().height);
        }

        g2d.drawString(CONTINUE_TEXT,x,y);

        y *= 2;

        if(getRestartButtonRect() == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            setRestartButtonRect(getMenuFont().getStringBounds(RESTART_TEXT,frc).getBounds());
            getRestartButtonRect().setLocation(x,y-getRestartButtonRect().height);
        }

        g2d.drawString(RESTART_TEXT,x,y);

        y *= 3.0/2;

        if(getExitButtonRect() == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            setExitButtonRect(getMenuFont().getStringBounds(EXIT_TEXT,frc).getBounds());
            getExitButtonRect().setLocation(x,y-getExitButtonRect().height);
        }

        g2d.drawString(EXIT_TEXT,x,y);

        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);
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
                repaint();
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
            repaint();
        }
        else if(getRestartButtonRect().contains(mouseGetPointEvent(mouseEvent))){
            setMessage("Restarting Game...");
            getWall().positionsReset();
            getWall().wallReset();
            setShowPauseMenu(false);
            repaint();

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
        repaint();

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
    public static GameBoard getUniqueGameBoard() {
        return uniqueGameBoard;
    }

    /**
     * this method is used to set the set a GameBoard object into uniqueGameBoard variable.
     *
     * @param uniqueGameBoard this is the GameBoard object used to set the uniqueGameBoard variable.
     */
    public static void setUniqueGameBoard(GameBoard uniqueGameBoard) {
        GameBoard.uniqueGameBoard = uniqueGameBoard;
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
