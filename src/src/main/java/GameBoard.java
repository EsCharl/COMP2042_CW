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
import java.util.ArrayList;
import java.util.Scanner;

/**
 *  this is a class that handles the gaming part of the program. which includes the pause
 */

public class GameBoard extends JComponent implements KeyListener,MouseListener,MouseMotionListener {

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
    private long timer = 0;
    private long totalTime = 0;
    private long pauseTime = 0;

    //for the level saving
    private String Levelname;

    /**
     * this method prepares the game
     *
     * @param owner this takes in a frame
     */
    public GameBoard(JFrame owner){
        super();

        strLen = 0;
        showPauseMenu = false;



        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE);


        this.initialize();
        message = "";
        wall = new Wall(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2,new Point(300,430));

        debugConsole = new DebugConsole(owner,wall,this);
        //initialize the first level
        wall.nextLevel();

        gameTimer = new Timer(10,e ->{
            wall.move();
            wall.findImpacts();
            message = String.format("Bricks: %d Balls %d",wall.getBrickCount(),wall.getBallCount());
            if(wall.isBallLost()){
                if(wall.ballEnd()){
                    wall.wallReset();
                    message = "Game over";
                    restartTimer();
                }
                wall.ballReset();
                gameTimer.stop();
                pauseTimer();
            }
            else if(wall.isDone()){
                if(wall.hasLevel()){
                    message = "Go to Next Level";
                    gameTimer.stop();
                    wall.ballReset();
                    wall.wallReset();
                    wall.nextLevel();
                }
                else{
                    message = "ALL WALLS DESTROYED";
                    gameTimer.stop();
                }

                //for save file saving and highscore pop up
                try {
                    ArrayList<String> sorted = getHighScore(wall.getWallLevel()-1);
                    updateSaveFile(sorted);
                    highScorePanel(wall.getWallLevel()-1,sorted);
                } catch (IOException | BadLocationException ex) {
                    ex.printStackTrace();
                }
                restartTimer();
            }

            repaint();
        });

    }

    /**
     * this method sets the variables and prepares the game. (game window size, track inputs, etc
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
     * This method is used for drawing the objects, namely the bricks, the ball, and the paddle which is controlled by the user
     *
     * @param g this is an object where it will be drawn upon.
     */
    public void paint(Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        clear(g2d);

        g2d.setColor(Color.BLUE);
        g2d.drawString(message,250,225);

        drawBall(wall.ball,g2d);

        for(Brick b : wall.bricks)
            if(!b.isBroken())
                drawBrick(b,g2d);

        drawPlayer(wall.player,g2d);

        if(showPauseMenu)
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
     * This method is used for drawing/inputting the bricks for the level.
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
     * @param ball the information of the ball that is going to be drawn
     * @param g2d the object that is beiung used to draw the ball.
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


        g2d.setFont(menuFont);
        g2d.setColor(MENU_COLOR);

        if(strLen == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = menuFont.getStringBounds("Pause Menu",frc).getBounds().width;
        }

        int x = (this.getWidth() - strLen) / 2;
        int y = this.getHeight() / 10;

        g2d.drawString("Pause Menu",x,y);

        x = this.getWidth() / 8;
        y = this.getHeight() / 4;


        if(continueButtonRect == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            continueButtonRect = menuFont.getStringBounds("CONTINUE",frc).getBounds();
            continueButtonRect.setLocation(x,y-continueButtonRect.height);
        }

        g2d.drawString("CONTINUE",x,y);

        y *= 2;

        if(restartButtonRect == null){
            restartButtonRect = (Rectangle) continueButtonRect.clone();
            restartButtonRect.setLocation(x,y-restartButtonRect.height);
        }

        g2d.drawString("RESTART",x,y);

        y *= 3.0/2;

        if(exitButtonRect == null){
            exitButtonRect = (Rectangle) continueButtonRect.clone();
            exitButtonRect.setLocation(x,y-exitButtonRect.height);
        }

        g2d.drawString("EXIT",x,y);



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
     * this is initially a KeyListener Interface class method, and it is being implemented. It is activated when a key is pressed from the keyboard.
     *
     * @param keyEvent this records the input from the keyboard.
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch(keyEvent.getKeyCode()){
            case KeyEvent.VK_A:
                wall.player.moveLeft();
                break;
            case KeyEvent.VK_D:
                wall.player.movRight();
                break;
            case KeyEvent.VK_ESCAPE:
                showPauseMenu = !showPauseMenu;
                repaint();
                if (gameTimer.isRunning()){
                    gameTimer.stop();
                    pauseTimer();
                }
                break;
            case KeyEvent.VK_SPACE:
                if(!showPauseMenu)
                    if(gameTimer.isRunning()){
                        gameTimer.stop();
                        pauseTimer();
                        System.out.println(getTimer());
                    }else{
                        gameTimer.start();
                        startTimer();
                    }
                break;
            case KeyEvent.VK_F1:
                if(keyEvent.isAltDown() && keyEvent.isShiftDown()){
                    showPauseMenu = true;
                    pauseTimer();
                    gameTimer.stop();
                    debugConsole.setVisible(true);
                }
            default:
                wall.player.stop();
        }
    }

    /**
     * this is initially a KeyListener Interface class method, and it is being implemented. It is called when a key is released from the keyboard.
     *
     * @param keyEvent this records the input from the keyboard.
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        wall.player.stop();
    }

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse button is clicked.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(!showPauseMenu)
            return;
        if(continueButtonRect.contains(p)){
            showPauseMenu = false;
            repaint();
        }
        else if(restartButtonRect.contains(p)){
            message = "Restarting Game...";
            wall.ballReset();
            wall.wallReset();
            showPauseMenu = false;
            repaint();


            restartTimer();
        }
        else if(exitButtonRect.contains(p)){
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
        Point p = mouseEvent.getPoint();
        if(exitButtonRect != null && showPauseMenu) {
            if (exitButtonRect.contains(p) || continueButtonRect.contains(p) || restartButtonRect.contains(p))
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else
                this.setCursor(Cursor.getDefaultCursor());
        }
        else{
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * this method is used when the user is not focused on the game. i.e. when the user clicked on other components or outside the game window.
     */
    public void onLostFocus(){
        gameTimer.stop();
        message = "Focus Lost";
        repaint();
    }

    /**
     * This method creates a high score panel to show
     *
     * @param level for the pop up screen level title.
     * @param sorted takes in the arraylist of string from getHighScore method to display on the panel.
     * @throws BadLocationException just incase if the insertion of the string into the pop up is an error.
     */
    private void highScorePanel(int level, ArrayList<String> sorted) throws BadLocationException {
        JFrame frame=new JFrame("LEVEL "+ level + " HIGH SCORE");
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
     * this method is used to get the scores from the save file and the player score (in terms of time) and rank them.
     *
     * @param level this is used determine which file to take.
     * @return it returns an arraylist of string that contains the sorted name and time for the player and the records in the save file.
     * @throws FileNotFoundException just incase if the save file is missing.
     */
    private ArrayList<String> getHighScore(int level) throws IOException {

        Boolean placed = false;
        Levelname = "src/main/resources/scores/Level"+level+".txt";

        ArrayList<String> Completed = new ArrayList<String>();
        Scanner scan = null;

        try {
            scan = new Scanner(new File(Levelname));
        }catch (FileNotFoundException e) {
            File myObj = new File(Levelname);
            myObj.createNewFile();
            scan = new Scanner(new File(Levelname));
        }
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
            if (totalTime < total_millisecond && !placed){
                Completed.add(System.getProperty("user.name") + ',' + getTimer());
                placed = true;
                Completed.add(name + ',' + time);
            }else{
                Completed.add(name + ',' + time);
            }
        }
        if(!placed)
            Completed.add(System.getProperty("user.name") + ',' + getTimer());
        return Completed;
    }

    /**
     * this method is used to get the time taken for the level.
     *
     * @return A time in String format.
     */
    private String getTimer(){
        pauseTimer();
        long elapsedSeconds = totalTime / 2000;
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
     * This method is used to pause the timer when there is a pause of the game or the game lost focus.
     */
    private void pauseTimer(){
        pauseTime = System.currentTimeMillis();

        totalTime += (pauseTime - timer);
    }

    /**
     * This method is used when a new level or a restart of the level is selected to restart the total time variable which is used to complete the level.
     */
    private void restartTimer(){
        totalTime = 0;
    }

    /**
     * This method is used to save the user record in a save file.
     *
     * @param sorted this is the arraylist of string to store inside the save file.
     * @throws IOException This is incase if there is a problem writing the file.
     */
    private void updateSaveFile(ArrayList<String> sorted) throws IOException {
        File file = new File(Levelname);
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
        wall.nextLevel();
        wall.wallReset();
        wall.ballReset();
    }
}
