import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Objective of this class is to create the console
 */
public class DebugConsole extends JDialog implements WindowListener{

    private static final String TITLE = "Debug Console";

    private JFrame owner;
    private DebugPanel debugPanel;
    private Wall wall;
    private GameScore gameScore;

    private static DebugConsole uniqueDebugConsole;

    public static DebugConsole singletonDebugConsole(JFrame owner, Wall wall){
        if(getUniqueDebugConsole() == null){
            setUniqueDebugConsole(new DebugConsole(owner,wall));
        }
        return getUniqueDebugConsole();
    }

    /**
     * this constructor is used for showing a console in the middle of the game window.
     *
     * @param owner a graphical frame that is going to be used.
     * @param wall the game level that is generated, the status of the game.
     */
    private DebugConsole(JFrame owner, Wall wall){
        gameScore = GameScore.singletonGameScore();

        setWall(wall);
        setOwner(owner);
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
        return ((getOwner().getWidth() - this.getWidth()) / 2) + getOwner().getX();
    }

    /**
     * this method is used to get the y coordinate on where the debug console is set based on the JFrame provided.
     *
     * @return returns an X coordinate on where the debug console is to be set.
     */
    private int getSetDebugConsoleYCoordinate(){
        return ((getOwner().getHeight() - this.getHeight()) / 2) + getOwner().getY();
    }

    /**
     * this is initially a WindowListener Interface class method, and it is being implemented. Its uses create to make a window.
     *
     * @param windowEvent the action that is done on the window
     */
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

    /**
     * this is initially a WindowListener Interface class method, and it is being implemented. Its uses if to remove/dispose a window.
     *
     * @param windowEvent the action that is done on the window
     */
    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    /**
     * this is initially a WindowListener Interface class method, and it is being implemented. Its uses are for removing a window.
     *
     * @param windowEvent the action that is done on the window
     */
    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    /**
     * this is initially a WindowListener Interface class method, and it is being implemented. Its uses are for minimizing a window.
     *
     * @param windowEvent the action that is done on the window
     */
    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    /**
     * this is initially a WindowListener Interface class method, and it is being implemented. Its uses are to set the window in an active state.
     *
     * @param windowEvent the action that is done on the window
     */
    @Override
    public void windowActivated(WindowEvent windowEvent) {
        setLocation();
        Ball b = getWall().getBall();
        getDebugPanel().setViewValues(b.getSpeedX(),b.getSpeedY());
    }

    /**
     * this is initially a WindowListener Interface class method, and it is being implemented. Its uses are to make a window to an inactive state.
     *
     * @param windowEvent the action that is done on the window
     */
    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }

    @Override
    public JFrame getOwner() {
        return owner;
    }

    public void setOwner(JFrame owner) {
        this.owner = owner;
    }

    public DebugPanel getDebugPanel() {
        return debugPanel;
    }

    public void setDebugPanel(DebugPanel debugPanel) {
        this.debugPanel = debugPanel;
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }

    /**
     * this method is used to get the one and only DebugConsole object
     *
     * @return uniqueDebugConsole returns a DebugConsole object.
     */
    public static DebugConsole getUniqueDebugConsole() {
        return uniqueDebugConsole;
    }

    /**
     * this method is used to get the one and only DebugConsole object
     *
     * @param uniqueDebugConsole returns a DebugConsole object.
     */
    public static void setUniqueDebugConsole(DebugConsole uniqueDebugConsole) {
        DebugConsole.uniqueDebugConsole = uniqueDebugConsole;
    }

    public void ballXSpeedValue(int speed){
        getWall().setBallXSpeed(speed);
    }

    public void ballYSpeedValue(int speed){
        getWall().setBallYSpeed(speed);
    }

    /**
     * this is used by the DebugPanel class to skip the level which will restart the timer and generate the next level.
     *
     */
    void skipLevelTriggered(){
        gameScore.restartTimer();
        getWall().positionsReset();
        getWall().wallReset();
        getWall().nextLevel();
    }

    public void resetBallCountTriggered(){
        getWall().resetBallCount();
    }
}
