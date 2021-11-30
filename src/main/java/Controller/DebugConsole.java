package Controller;

import Model.Ball.Ball;
import Model.GameScore;
import Model.Game;
import View.DebugPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Objective of this class is to create the console
 */
public class DebugConsole extends JDialog implements WindowListener{

    private static final String TITLE = "Debug Console";

    private JFrame frameOwner;
    private DebugPanel debugPanel;
    private Game game;
    private GameScore gameScore;

    private static DebugConsole uniqueDebugConsole;

    /**
     * this method returns the one and only debug console object. singleton design pattern.
     *
     * @param frameOwner this is the JFrame object which the debug console will appear.
     * @param game this is the game object used to get and change the game variables.
     * @return this returns the one and only debug console object.
     */
    public static DebugConsole singletonDebugConsole(JFrame frameOwner, Game game){
        if(getUniqueDebugConsole() == null){
            setUniqueDebugConsole(new DebugConsole(frameOwner, game));
        }
        return getUniqueDebugConsole();
    }

    /**
     * this constructor is used for showing a console in the middle of the game window.
     *
     * @param frameOwner a graphical frame that is going to be used.
     * @param game the game level that is generated, the status of the game.
     */
    private DebugConsole(JFrame frameOwner, Game game){
        setGameScore(GameScore.singletonGameScore());

        setGame(game);
        setFrameOwner(frameOwner);
        initialize();

        setDebugPanel(DebugPanel.singletonDebugPanel(this));
        this.add(getDebugPanel(),BorderLayout.CENTER);

        this.pack();
    }

    /**
     * This method is used for creating the console in the middle of the game window.
     */
    private void initialize(){
        this.setModal(true);
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.addWindowListener(this);
        this.setFocusable(true);
    }

    /**
     * This method is used for get the size of the window and setting it in the middle of the window.
     */
    private void setLocation(){
        this.setLocation(getSetDebugConsoleXCoordinate(), getSetDebugConsoleYCoordinate());
    }

    /**
     * this method is used to get the x coordinate on where the debug console is set based on the JFrame provided.
     *
     * @return returns an X coordinate on where the debug console is to be set.
     */
    private int getSetDebugConsoleXCoordinate(){
        return ((getFrameOwner().getWidth() - this.getWidth()) / 2) + getFrameOwner().getX();
    }

    /**
     * this method is used to get the y coordinate on where the debug console is set based on the JFrame provided.
     *
     * @return returns an X coordinate on where the debug console is to be set.
     */
    private int getSetDebugConsoleYCoordinate(){
        return ((getFrameOwner().getHeight() - this.getHeight()) / 2) + getFrameOwner().getY();
    }


    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    /**
     * this is initially a WindowListener Interface class method, and it is being implemented. Its uses are for removing a window.
     *
     * @param windowEvent the action that is done on the window
     */
    @Override
    public void windowClosing(WindowEvent windowEvent) {
        repaint();
    }


    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    /**
     * this is initially a WindowListener Interface class method, and it is being implemented. It is called when the window is being active. what this method does is to set the location of the debug console and set the speed value of the ball into the debug panel.
     *
     * @param windowEvent the action that is done on the window
     */
    @Override
    public void windowActivated(WindowEvent windowEvent) {
        setLocation();
        Ball b = getGame().getBall();
        getDebugPanel().setViewValues(b.getXSpeed(),b.getYSpeed());
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }

    /**
     * this is method is used to get the JFrame object which is used to display the debug console.
     *
     * @return this returns the JFrame object.
     */
    public JFrame getFrameOwner() {
        return frameOwner;
    }

    /**
     * this method is used to set a JFrame object into a variable for future reference.
     *
     * @param frameOwner this is the JFrame used to set into a variable.
     */
    public void setFrameOwner(JFrame frameOwner) {
        this.frameOwner = frameOwner;
    }

    /**
     * this method is used to get a debug panel which contains the user inputs needed for processing.
     *
     * @return this is the debug panel object that is being returned.
     */
    public DebugPanel getDebugPanel() {
        return debugPanel;
    }

    /**
     * this method is used to set a debug panel object into a variable for future reference.
     *
     * @param debugPanel this is the debug panel object used to set into a variable.
     */
    public void setDebugPanel(DebugPanel debugPanel) {
        this.debugPanel = debugPanel;
    }

    /**
     * this method is used to get a game object which contains all the information for the game to progress.
     *
     * @return this returns a game object.
     */
    public Game getGame() {
        return game;
    }

    /**
     * this method is used to set a game object into a variable for future referencing.
     *
     * @param game this is the game object used to set into a variable
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * this method is used to get the one and only Controller.DebugConsole object
     *
     * @return uniqueDebugConsole returns a Controller.DebugConsole object.
     */
    public static DebugConsole getUniqueDebugConsole() {
        return uniqueDebugConsole;
    }

    /**
     * this method is used to get the one and only Controller.DebugConsole object
     *
     * @param uniqueDebugConsole returns a Controller.DebugConsole object.
     */
    public static void setUniqueDebugConsole(DebugConsole uniqueDebugConsole) {
        DebugConsole.uniqueDebugConsole = uniqueDebugConsole;
    }

    /**
     * this method is used to set the ball X speed value.
     *
     * @param speed this is the integer value used to set the x speed of the ball.
     */
    public void ballXSpeedValue(int speed){
        getGame().setBallXSpeed(speed);
    }

    /**
     * this method is used to set the ball y speed value.
     *
     * @param speed this is the integer value used to set the y speed of the ball.
     */
    public void ballYSpeedValue(int speed){
        getGame().setBallYSpeed(speed);
    }

    /**
     * this is used by the View.DebugPanel class to skip the level which will restart the timer and generate the next level.
     */
    public void skipLevelTriggered(){
        getGameScore().restartTimer();
        getGame().positionsReset();
        getGame().wallReset();
        getGame().nextLevel();
    }

    /**
     * this method is called to reset the ball count based on a value fixed by the Game class (a constant value).
     */
    public void resetBallCountTriggered(){
        getGame().resetBallCount();
    }

    /**
     * this method is used to get a game score object which is used to handle the scoring mechanism of the game.
     *
     * @return this is the game score object which is being returned.
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
}
