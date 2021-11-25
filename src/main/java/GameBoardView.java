import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;

public class GameBoardView extends JComponent {
    static final int DEF_WIDTH = 600;
    static final int DEF_HEIGHT = 450;

    private static final String CONTINUE_TEXT = "Continue";
    private static final String RESTART_TEXT = "Restart";
    private static final String EXIT_TEXT = "Exit";
    private static final String PAUSE_TEXT = "Pause Menu";

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color MENU_COLOR = new Color(0,255,0);

    /**
     * this method sets the variables and prepares the game window based on awt. (game window size, track inputs, etc.)
     * @param gameBoard
     */
    public void initialize(GameBoard gameBoard){
        gameBoard.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
        gameBoard.setFocusable(true);
        gameBoard.requestFocusInWindow();
        gameBoard.addKeyListener(gameBoard);
        gameBoard.addMouseListener(gameBoard);
        gameBoard.addMouseMotionListener(gameBoard);
    }

    /**
     * This method is used by the drawMenu to darken the screen when the pause menu is prompted.
     *
     * @param g2d this is the object where the darkening of the screen when the pause menu is prompted.
     */
    public void obscureGameBoard(Graphics2D g2d){

        Composite tmp = g2d.getComposite();
        Color tmpColor = g2d.getColor();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.55f);
        g2d.setComposite(ac);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0, DEF_WIDTH, DEF_HEIGHT);

        g2d.setComposite(tmp);
        g2d.setColor(tmpColor);
    }

    /**
     * This is for clearing the screen by setting the whole window to be set into the background colour.
     *
     * @param g2d this is the object that is being passed into for clearing.
     * @param gameBoard
     */
    void clear(Graphics2D g2d, GameBoard gameBoard){
        Color tmp = g2d.getColor();
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0,0, gameBoard.getWidth(), gameBoard.getHeight());
        g2d.setColor(tmp);
    }

    /**
     * This method is used for drawing the bricks for the level.
     *  @param brick this is the information of the brick that is going to be used for the drawing of the brick.
     * @param g2d this takes in the object that is being used for the brick generation for the level.
     */
    void drawBrick(Brick brick, Graphics2D g2d){
        Color tmp = g2d.getColor();

        g2d.setColor(brick.getInnerColor());
        g2d.fill(brick.getBrick());

        g2d.setColor(brick.getBorderColor());
        g2d.draw(brick.getBrick());


        g2d.setColor(tmp);
    }

    /**
     * this method is used for drawing the ball used for the game.
     *  @param ball the object of the ball that is going to be drawn
     * @param g2d the information that is being used to draw the ball.
     */
    void drawBall(Ball ball, Graphics2D g2d){
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
     *  @param p the contains the information needed about the paddle to be drawn.
     * @param g2d this is the object where the paddle is being drawn.
     */
    void drawPlayer(Player p, Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = p.getPlayerFace();
        g2d.setColor(Player.INNER_COLOR);
        g2d.fill(s);

        g2d.setColor(Player.BORDER_COLOR);
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    /**
     * This method is used to draw the pause menu and the buttons of the pause menu.
     *
     * @param g2d this is the object to draw the pause menu.
     * @param gameBoard
     */
    void drawPauseMenu(Graphics2D g2d, GameBoard gameBoard){
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();


        g2d.setFont(gameBoard.getMenuFont());
        g2d.setColor(MENU_COLOR);

        if(gameBoard.getStringDisplayLength() == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            gameBoard.setStringDisplayLength(gameBoard.getMenuFont().getStringBounds(PAUSE_TEXT,frc).getBounds().width);
        }

        // get the position of top center.
        int x = (gameBoard.getWidth() - gameBoard.getStringDisplayLength()) / 2;
        int y = gameBoard.getHeight() / 10;

        g2d.drawString(PAUSE_TEXT,x,y);

        x = gameBoard.getWidth() / 8;
        y = gameBoard.getHeight() / 4;


        if(gameBoard.getContinueButtonRect() == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            gameBoard.setContinueButtonRect(gameBoard.getMenuFont().getStringBounds(CONTINUE_TEXT,frc).getBounds());
            gameBoard.getContinueButtonRect().setLocation(x,y- gameBoard.getContinueButtonRect().height);
        }

        g2d.drawString(CONTINUE_TEXT,x,y);

        y *= 2;

        if(gameBoard.getRestartButtonRect() == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            gameBoard.setRestartButtonRect(gameBoard.getMenuFont().getStringBounds(RESTART_TEXT,frc).getBounds());
            gameBoard.getRestartButtonRect().setLocation(x,y- gameBoard.getRestartButtonRect().height);
        }

        g2d.drawString(RESTART_TEXT,x,y);

        y *= 3.0/2;

        if(gameBoard.getExitButtonRect() == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            gameBoard.setExitButtonRect(gameBoard.getMenuFont().getStringBounds(EXIT_TEXT,frc).getBounds());
            gameBoard.getExitButtonRect().setLocation(x,y- gameBoard.getExitButtonRect().height);
        }

        g2d.drawString(EXIT_TEXT,x,y);

        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);
    }

    /**
     * This method is used to draw the pause menu (refer to drawPauseMenu method) and to obscure the game board (refer to the obscureGameBoard method).
     *
     * @param g2d this is the object used to draw the menu.
     * @param gameBoard
     */
    void drawMenu(Graphics2D g2d, GameBoard gameBoard){
        obscureGameBoard(g2d);
        drawPauseMenu(g2d, gameBoard);
    }
}
