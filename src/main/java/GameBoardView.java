import java.awt.*;

public class GameBoardView {
    static final int DEF_WIDTH = 600;
    static final int DEF_HEIGHT = 450;

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
}
