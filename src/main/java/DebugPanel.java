import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Objective of this class is to deal with the contents in the console
 */

public class DebugPanel extends JPanel {

    private static final Color DEF_BKG = Color.WHITE;


    private JButton skipLevel;
    private JButton resetBalls;

    private JSlider ballXSpeed;
    private JSlider ballYSpeed;

    private JLabel xText;
    private JLabel yText;

    private Wall wall;
    private GameBoardController board;

    private static DebugPanel uniqueDebugPanel;

    public static DebugPanel singletonDebugPanel(Wall wall, GameBoardController board){
        if(uniqueDebugPanel == null){
            uniqueDebugPanel = new DebugPanel(wall, board);
        }
        return uniqueDebugPanel;
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

        initialize();

        functions();


    }

    private void functions() {
        skipLevel = makeButton("Skip Level",e -> board.gameBoardModel.skipLevel());
        resetBalls = makeButton("Reset Balls",e -> wall.resetBallCount());

        ballXSpeed = makeSlider(-4,4,e -> wall.setBallXSpeed(ballXSpeed.getValue()));
        ballYSpeed = makeSlider(-4,4,e -> wall.setBallYSpeed(ballYSpeed.getValue()));

        this.add(skipLevel);
        this.add(resetBalls);

        this.add(ballXSpeed);
        this.add(ballYSpeed);

        // newly added
        ballXSpeed.setPaintTicks(true);
        ballXSpeed.setPaintTrack(true);
        ballXSpeed.setPaintLabels(true);

        ballYSpeed.setPaintTicks(true);
        ballYSpeed.setPaintTrack(true);
        ballYSpeed.setPaintLabels(true);

        xText = new JLabel("Ball x-axis speed", SwingConstants.CENTER);
        yText = new JLabel("Ball y-axis speed", SwingConstants.CENTER);

        this.add(xText);
        this.add(yText);
    }

    /**
     * this method creates a square which will specify the number of inputs allowed.
     */
    private void initialize(){
        this.setBackground(DEF_BKG);
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

}
