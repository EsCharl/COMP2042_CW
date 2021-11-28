import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Objective of this class is to deal with the contents in the console
 */

public class DebugPanel extends JPanel {

    private static final Color BACKGROUND_COLOR = Color.WHITE;

    public static final String RESET_BALLS_TEXT = "Reset Balls";
    public static final String SKIP_LEVEL_TEXT = "Skip Level";
    public static final String BALL_X_AXIS_SPEED_TEXT = "Ball x-axis speed";
    public static final String BALL_Y_AXIS_SPEED_TEXT = "Ball y-axis speed";

    private final int MAX_POSITIVE_SPEED_X = 4;
    private final int MAX_NEGATIVE_SPEED_X = -4;
    private final int MAX_POSITIVE_SPEED_Y = 4;
    private final int MAX_NEGATIVE_SPEED_Y = -4;

    private JButton skipLevel;
    private JButton resetBalls;

    private JSlider ballXSpeed;
    private JSlider ballYSpeed;

    private Wall wall;
    private GameBoardController board;

    private static DebugPanel uniqueDebugPanel;

    public static DebugPanel singletonDebugPanel(Wall wall, GameBoardController board){
        if(getUniqueDebugPanel() == null){
            setUniqueDebugPanel(new DebugPanel(wall, board));
        }
        return getUniqueDebugPanel();
    }
    /**
     * This constructor contains the code needed for the console in the game.
     *
     * @param   wall A wall class that is created, and it is used for nextLevel, resetBallCount, setBallXSpeed,
     *               and setBallYSpeed method that is available in wall class.
     */
    private DebugPanel(Wall wall, GameBoardController board){
        this.wall = wall;
        this.board = board;

        setDebugPanelLook();

        debugFunctions();

        addTextSpeedCategory();
    }

    /**
     * this method is to add the speed category into the Debug Panel.
     */
    private void addTextSpeedCategory() {
        this.add(createJLabelCentered(BALL_X_AXIS_SPEED_TEXT));
        this.add(createJLabelCentered(BALL_Y_AXIS_SPEED_TEXT));
    }

    /**
     * this method is used to create a JLabel and set a text into it.
     *
     * @param setText the text used to set the string into the JLabel.
     * @return returns a new JLabel object.
     */
    private JLabel createJLabelCentered(String setText) {
        return new JLabel(setText, SwingConstants.CENTER);
    }

    private void debugFunctions() {
        skipLevel = makeButton(SKIP_LEVEL_TEXT, e -> board.gameBoardModel.skipLevel());
        resetBalls = makeButton(RESET_BALLS_TEXT, e -> wall.resetBallCount());

        ballXSpeed = makeSlider(MAX_NEGATIVE_SPEED_X, MAX_POSITIVE_SPEED_X, e -> wall.setBallXSpeed(ballXSpeed.getValue()));
        ballYSpeed = makeSlider(MAX_NEGATIVE_SPEED_Y, MAX_POSITIVE_SPEED_Y, e -> wall.setBallYSpeed(ballYSpeed.getValue()));

        this.add(skipLevel);
        this.add(resetBalls);

        this.add(ballXSpeed);
        this.add(ballYSpeed);
    }

    /**
     * this method creates a square which will specify the number of inputs allowed.
     */
    private void setDebugPanelLook(){
        this.setBackground(BACKGROUND_COLOR);
        this.setLayout(new GridLayout(3,2));
    }

    /**
     *  This method is used to create a button for the console which is used to change the level or the speed of the ball.
     *
     * @param title the title is the name listen inside the button.
     * @param e this allows that when the button is clicked it will be a true.
     * @return a button for the game console
     */
    private JButton makeButton(String title, ActionListener e){
        JButton out = new JButton(title);
        out.addActionListener(e);
        return  out;
    }

    /**
     * this method is used to create a slider for the intent of changing the value.
     *
     * @param min   the lowest possible value that it can go for the slider
     * @param max   the highest possible value that it can go for the slider
     * @param e     this detects if there is a change of value
     * @return      this returns a slider for the console
     */
    private JSlider makeSlider(int min, int max, ChangeListener e){
        JSlider out = new JSlider(min,max);
        out.setMajorTickSpacing(1);
        out.setSnapToTicks(true);
        out.setPaintTicks(true);
        out.addChangeListener(e);

        out.setPaintTicks(true);
        out.setPaintTrack(true);
        out.setPaintLabels(true);
        return out;
    }

    /**
     * This method used for changing the value of the ball speed.
     *
     * @param x this takes in the value that the ball supposed to go in the x-axis and set it for the ball
     * @param y this takes in the value that the ball supposed to go in the y-axis and set it for the ball
     */
    public void setValues(int x,int y){
        ballXSpeed.setValue(x);
        ballYSpeed.setValue(y);
    }

    /**
     * this method is used to get the one and only debug panel after the method singletonDebugPanel is called.
     *
     * @return this returns the debug panel object.
     */
    private static DebugPanel getUniqueDebugPanel() {
        return uniqueDebugPanel;
    }

    /**
     * this method is used to set a one and only debug panel into a variable for future referencing used in singletonDebugPanel method.
     *
     * @param uniqueDebugPanel this is the debug panel object used to set into a variable for future uses.
     */
    private static void setUniqueDebugPanel(DebugPanel uniqueDebugPanel) {
        DebugPanel.uniqueDebugPanel = uniqueDebugPanel;
    }
}
