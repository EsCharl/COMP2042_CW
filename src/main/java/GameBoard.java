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
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *  this is a class that handles the gaming part of the program. which includes the pause
 */
public class GameBoard extends JComponent implements KeyListener,MouseListener,MouseMotionListener {

    private static final String CONTINUE_TEXT = "Continue";
    private static final String RESTART_TEXT = "Restart";
    private static final String EXIT_TEXT = "Exit";
    private static final String PAUSE_TEXT = "Pause Menu";

    private static final int TEXT_SIZE = 30;
    private static final Color MENU_COLOR = new Color(0,255,0);


    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;

    private static final Color BG_COLOR = Color.WHITE;

    private Timer gameTimer;

    private Wall wall;

    private String message;

    private boolean showPauseMenu;

    private Font menuFont;

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;
    private int strLen;

    private DebugConsole debugConsole;

    //for the timer
    private long timer;
    private long totalTime;
    private long pauseTime;

    //for the level saving
    private String levelPathName;

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

        strLen = 0;
        setShowPauseMenu(false);

        setTimer(0);
        setTotalTime(0);
        setPauseTime(0);

        setMenuFont(new Font("Monospaced",Font.PLAIN,TEXT_SIZE));

        this.initialize();
        setMessage("");

        setWall(Wall.singletonWall(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2,new Point(300,430)));

        setDebugConsole(DebugConsole.singletonDebugConsole(owner,wall,this));
        //initialize the first level
        getWall().nextLevel();
        setLevelFilePathName("/scores/Level"+getWall().getWallLevel()+".txt");

        setGameTimer(new Timer(10,e ->{
            getWall().move();
            getWall().findImpacts();
            setMessage(String.format("Bricks: %d Balls %d",getWall().getBrickCount(),getWall().getBallCount()));
            if(getWall().isBallLost()){
                if(getWall().ballEnd()){
                    getWall().wallReset();
                    setMessage("Game over");
                    restartTimer();
                }
                getWall().positionsReset();
                getGameTimer().stop();
                pauseTimer();
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

                //for save file saving and high score pop up
                try {
                    ArrayList<String> sorted = getHighScore();
                    updateSaveFile(sorted);
                    highScorePanel(sorted);
                } catch (IOException | BadLocationException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
                restartTimer();
            }
            setLevelFilePathName("/scores/Level"+getWall().getWallLevel()+".txt");

            repaint();
        }));

    }

    /**
     * this method sets the variables and prepares the game window based on awt. (game window size, track inputs, etc.)
     */
    private void initialize(){
        this.setPreferredSize(new Dimension(DEF_WIDTH,DEF_HEIGHT));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
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

        drawBall(getWall().ball,g2d);

        for(Brick b : getWall().bricks)
            if(!b.isBroken())
                drawBrick(b,g2d);

        drawPlayer(getWall().player,g2d);

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

        g2d.setColor(ball.getInnerColor());
        g2d.fill(s);

        g2d.setColor(ball.getBorderColor());
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
        obscureGameBoard(g2d);
        drawPauseMenu(g2d);
    }

    /**
     * This method is used by the drawMenu to darken the screen when the pause menu is prompted.
     *
     * @param g2d this is the object where the darkening of the screen when the pause menu is prompted.
     */
    private void obscureGameBoard(Graphics2D g2d){

        Composite tmp = g2d.getComposite();
        Color tmpColor = g2d.getColor();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.55f);
        g2d.setComposite(ac);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,DEF_WIDTH,DEF_HEIGHT);

        g2d.setComposite(tmp);
        g2d.setColor(tmpColor);
    }

    /**
     * This method is used to draw the pause menu.
     *
     * @param g2d this is the object to draw the pause menu.
     */
    private void drawPauseMenu(Graphics2D g2d){
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();


        g2d.setFont(getMenuFont());
        g2d.setColor(MENU_COLOR);

        if(getStrLen() == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            setStrLen(getMenuFont().getStringBounds(PAUSE_TEXT,frc).getBounds().width);
        }

        // get the position of top center.
        int x = (this.getWidth() - getStrLen()) / 2;
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
            setRestartButtonRect((Rectangle) getContinueButtonRect().clone());
            getRestartButtonRect().setLocation(x,y-getRestartButtonRect().height);
        }

        g2d.drawString(RESTART_TEXT,x,y);

        y *= 3.0/2;

        if(getExitButtonRect() == null){
            setExitButtonRect((Rectangle) getContinueButtonRect().clone());
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
                getWall().player.moveLeft();
                break;
            case KeyEvent.VK_D:
                getWall().player.movRight();
                break;
            case KeyEvent.VK_ESCAPE:
                setShowPauseMenu(!isShowPauseMenu());
                repaint();
                if (getGameTimer().isRunning()){
                    getGameTimer().stop();
                    pauseTimer();
                }
                break;
            case KeyEvent.VK_SPACE:
                if(!isShowPauseMenu())
                    if(getGameTimer().isRunning()){
                        getGameTimer().stop();
                        pauseTimer();
                    }else{
                        getGameTimer().start();
                        startTimer();
                    }
                break;
            case KeyEvent.VK_F1:
                if(keyEvent.isAltDown() && keyEvent.isShiftDown()){
                    setShowPauseMenu(true);
                    pauseTimer();
                    getGameTimer().stop();
                    getDebugConsole().setVisible(true);
                }
            default:
                getWall().player.stop();
        }
    }

    /**
     * this is initially a KeyListener Interface class method, and it is being implemented. It is called when a key is released from the keyboard. this is used to stop the paddle from moving.
     *
     * @param keyEvent this records the input from the keyboard.
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        getWall().player.stop();
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

            restartTimer();
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

        pauseTimer();
    }

    /**
     * This method creates a high score panel to show the scores after each game.
     *
     * @param sorted takes in the arraylist of string from getHighScore method to display on the panel.
     * @throws BadLocationException just incase if the insertion of the string into the pop up is an error.
     */
    private void highScorePanel(ArrayList<String> sorted) throws BadLocationException {
        JFrame frame=new JFrame("HIGH SCORE");
        frame.setLayout(new FlowLayout());
        frame.setSize(500,400);

        Container cp = frame.getContentPane();
        JTextPane pane = new JTextPane();
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(attributeSet, true);

        // Set the attributes before adding text
        pane.setCharacterAttributes(attributeSet, true);
        pane.setText(String.format("%-20s %s\n\n", "Name", "Time"));

        attributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attributeSet, Color.RED);
        StyleConstants.setBackground(attributeSet, Color.GREEN);

        Document doc = pane.getStyledDocument();
        for (int i = 0 ; i < sorted.size(); i++){
            String[] list = sorted.get(i).split(",",2);
            String name = list[0];
            String time = list[1];
            doc.insertString(doc.getLength(),String.format("%-20s %s\n",name, time), attributeSet);
        }
        JScrollPane scrollPane = new JScrollPane(pane);
        cp.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /**
     * this is used to set the levelPath file.
     *
     * @param levelPathName this is the file path which will be set into the variable.
     */
    public void setLevelFilePathName(String levelPathName){
        this.levelPathName = levelPathName;
    }

    /**
     * this method is used to get the LevelPath file in String form.
     *
     * @return returns the LevelPath file in String.
     */
    public String getLevelFilePathName(){
        return levelPathName;
    }

    /**
     * this method is used to get the scores from the save file and the player score (in terms of time) and rank them.
     *
     * @return it returns an arraylist of string that contains the sorted name and time for the player and the records in the save file.
     * @throws FileNotFoundException just in case if the save file is missing.
     */
    private ArrayList<String> getHighScore() throws IOException, URISyntaxException {

        Boolean placed = false;

        ArrayList<String> Completed = new ArrayList<String>();

        Scanner scan = new Scanner(new File(GameBoard.class.getResource(getLevelFilePathName()).toURI()));

        while (scan.hasNextLine()){
            // to split the name and time to include inside the highscore.
            String[] line = scan.nextLine().split(",",2);
            String name = line[0];
            String time = line[1];

            // for conversion of the time to seconds.
            String[] preTime = time.split(":",2);
            int minute = Integer.parseInt(preTime[0]);
            int second = Integer.parseInt(preTime[1]);

            int total_millisecond = (minute * 60 + second) * 1000;
            if (getTotalTime() < total_millisecond && !placed){
                Completed.add(System.getProperty("user.name") + ',' + getTimerString());
                placed = true;
                Completed.add(name + ',' + time);
            }else{
                Completed.add(name + ',' + time);
            }
        }
        if(!placed)
            Completed.add(System.getProperty("user.name") + ',' + getTimerString());
        return Completed;
    }

    /**
     * this method is used to get the time passed.
     *
     * @return A time in String format.
     */
    private String getTimerString(){
        pauseTimer();
        long elapsedSeconds = getTotalTime() / 2000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        if (secondsDisplay <= 9){
            return elapsedMinutes + ":0" + secondsDisplay;
        }
        return elapsedMinutes + ":" + secondsDisplay;
    }

    /**
     * This method is used to start the timer.
     */
    private void startTimer(){
        timer = System.currentTimeMillis();
    }

    /**
     * This method is used to pause the timer.
     */
    private void pauseTimer(){
        if (getTimer() != 0){
            setPauseTime(System.currentTimeMillis());

            setTotalTime(getTotalTime() + getPauseTime() - getTimer());
        }
    }

    /**
     * this method is used to set the total time variable.
     *
     * @param totalTime the total time which is used to set the total time variable.
     */
    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * this method is used to get the total time value variable.
     *
     * @return returns a long datatype of the total time variable.
     */
    public long getTotalTime(){
        return totalTime;
    }

    /**
     * This method is used when a new level or a restart of the level is selected to restart the total time variable which is used to complete the level.
     */
    private void restartTimer(){
        setTotalTime(0);
    }

    /**
     * This method is used to save the user record in a .txt save file.
     *
     * @param sorted this is the arraylist of string to store inside the save file.
     * @throws IOException This is in case if there is a problem writing the file.
     */
    private void updateSaveFile(ArrayList<String> sorted) throws IOException, URISyntaxException {
        File file = new File(GameBoard.class.getResource(getLevelFilePathName()).toURI());
        FileWriter overwrite = new FileWriter(file,false);
        for (int i = 0; i < sorted.size()-1; i++)
            overwrite.write(sorted.get(i)+"\n");
        overwrite.write(sorted.get(sorted.size()-1));
        overwrite.close();
    }

    /**
     * this is used by the DebugPanel class to skip the level which will restart the timer and generate the next level.
     */
    void skipLevel(){
        restartTimer();
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isShowPauseMenu() {
        return showPauseMenu;
    }

    public void setShowPauseMenu(boolean showPauseMenu) {
        this.showPauseMenu = showPauseMenu;
    }

    public Font getMenuFont() {
        return menuFont;
    }

    public void setMenuFont(Font menuFont) {
        this.menuFont = menuFont;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public long getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }

    public DebugConsole getDebugConsole() {
        return debugConsole;
    }

    public void setDebugConsole(DebugConsole debugConsole) {
        this.debugConsole = debugConsole;
    }

    public static GameBoard getUniqueGameBoard() {
        return uniqueGameBoard;
    }

    public static void setUniqueGameBoard(GameBoard uniqueGameBoard) {
        GameBoard.uniqueGameBoard = uniqueGameBoard;
    }

    public Rectangle getContinueButtonRect() {
        return continueButtonRect;
    }

    public void setContinueButtonRect(Rectangle continueButtonRect) {
        this.continueButtonRect = continueButtonRect;
    }

    public Rectangle getExitButtonRect() {
        return exitButtonRect;
    }

    public void setExitButtonRect(Rectangle exitButtonRect) {
        this.exitButtonRect = exitButtonRect;
    }

    public Rectangle getRestartButtonRect() {
        return restartButtonRect;
    }

    public void setRestartButtonRect(Rectangle restartButtonRect) {
        this.restartButtonRect = restartButtonRect;
    }

    public int getStrLen() {
        return strLen;
    }

    public void setStrLen(int strLen) {
        this.strLen = strLen;
    }

}
