import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;

/**
 * this class handles the rendering for the game board.
 */
public class GameBoardView extends JComponent implements KeyListener, MouseListener, MouseMotionListener {
    final static int DEF_WIDTH = 600;
    final static int DEF_HEIGHT = 450;

    private final int TEXT_SIZE = 30;

    private final String CONTINUE_TEXT = "Continue";
    private final String RESTART_TEXT = "Restart";
    private final String EXIT_TEXT = "Exit";
    private final String PAUSE_TEXT = "Pause Menu";

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;

    private GameBoardController gameBoardController;

    private DebugConsole debugConsole;

    private Font menuFont;

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color MENU_COLOR = new Color(0,255,0);

    private int stringDisplayLength;

    private static GameBoardView uniqueGameBoardView;

    public static GameBoardView singletonGameBoardView(GameBoardController gameBoardController, JFrame owner){
        if(getUniqueGameBoardView() == null){
            setUniqueGameBoardView(new GameBoardView(gameBoardController, owner));
        }
        return getUniqueGameBoardView();
    }

    public GameBoardView(GameBoardController gameBoardController, JFrame owner){
        super();

        setStringDisplayLength(0);

        setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        setGameBoardController(gameBoardController);

        setDebugConsole(DebugConsole.singletonDebugConsole(owner,getGameBoardController().gameBoardModel.getWall(),getGameBoardController()));

        setMenuFont(new Font("Monospaced",Font.PLAIN,TEXT_SIZE));
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
     */
    void clear(Graphics2D g2d){
        Color tmp = g2d.getColor();
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0,0, getWidth(), getHeight());
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
     */
    void drawPauseMenu(Graphics2D g2d){
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();


        g2d.setFont(getMenuFont());
        g2d.setColor(MENU_COLOR);

        if(getStringDisplayLength() == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            setStringDisplayLength(getMenuFont().getStringBounds(PAUSE_TEXT,frc).getBounds().width);
        }

        // get the position of top center.
        int x = (getWidth() - getStringDisplayLength()) / 2;
        int y = getHeight() / 10;

        g2d.drawString(PAUSE_TEXT,x,y);

        x = getWidth() / 8;
        y = getHeight() / 4;


        if(getContinueButtonRect() == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            setContinueButtonRect(getMenuFont().getStringBounds(CONTINUE_TEXT,frc).getBounds());
            getContinueButtonRect().setLocation(x,y- getContinueButtonRect().height);
        }

        g2d.drawString(CONTINUE_TEXT,x,y);

        y *= 2;

        if(getRestartButtonRect() == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            setRestartButtonRect(getMenuFont().getStringBounds(RESTART_TEXT,frc).getBounds());
            getRestartButtonRect().setLocation(x,y- getRestartButtonRect().height);
        }

        g2d.drawString(RESTART_TEXT,x,y);

        y *= 3.0/2;

        if(getExitButtonRect() == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            setExitButtonRect(getMenuFont().getStringBounds(EXIT_TEXT,frc).getBounds());
            getExitButtonRect().setLocation(x,y- getExitButtonRect().height);
        }

        g2d.drawString(EXIT_TEXT,x,y);

        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);
    }

    /**
     * This method is used to draw the pause menu (refer to drawPauseMenu method) and to obscure the game board (refer to the obscureGameBoard method).
     *
     * @param g2d this is the object used to draw the menu.
     */
    void drawMenu(Graphics2D g2d){
        obscureGameBoard(g2d);
        drawPauseMenu(g2d);
    }

    /**
     * this method is used to change the look of the cursor.
     *
     * @param Cursor the cursor state change to.
     */
    void setCursorLook(Cursor Cursor) {
        setCursor(Cursor);
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
     * this is the method used to get the rectangle restart object.
     *
     * @return it returns a rectangle object which is the restart button.
     */
    public Rectangle getRestartButtonRect() {
        return restartButtonRect;
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
     * this method is used to return the rectangle object which is the exit button.
     *
     * @return this returns a rectangle object which is the exit button.
     */
    public Rectangle getExitButtonRect() {
        return exitButtonRect;
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
     * this method is used to get the rectangle continue button.
     *
     * @return it returns the rectangle object which is the continue button.
     */
    public Rectangle getContinueButtonRect() {
        return continueButtonRect;
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
     * this is to get the font of the menu font
     *
     * @return returns the font used by the menu font.
     */
    public Font getMenuFont() {
        return menuFont;
    }

    /**
     * this method is used to update the game board view to update the screen.
     */
    public void updateGameBoardView() {
        repaint();
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
        g2d.drawString(getGameBoardController().gameBoardModel.getMessage(),250,225);

        drawBall(getGameBoardController().gameBoardModel.getWall().getBall(),g2d);

        for(Brick b : getGameBoardController().gameBoardModel.getWall().getBricks())
            if(!b.isBroken())
                drawBrick(b,g2d);

        drawPlayer(getGameBoardController().gameBoardModel.getWall().getPlayer(),g2d);

        if(getGameBoardController().isShowPauseMenu())
            drawMenu(g2d);

        Toolkit.getDefaultToolkit().sync();
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
     * this is the method used to get the value from the stringDisplayLength variable.
     *
     * @return it returns the value from stringDisplayLength.
     */
    public int getStringDisplayLength() {
        return stringDisplayLength;
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
                getGameBoardController().gameBoardModel.playerMoveLeft();
                break;
            case KeyEvent.VK_D:
                getGameBoardController().gameBoardModel.playerMoveRight();
                break;
            case KeyEvent.VK_ESCAPE:
                getGameBoardController().gameBoardModel.pauseMenuButtonClicked();
                break;
            case KeyEvent.VK_SPACE:
                getGameBoardController().gameBoardModel.startPauseGameButtonClicked();
                break;
            case KeyEvent.VK_F1:
                if(keyEvent.isAltDown() && keyEvent.isShiftDown()){
                    getGameBoardController().gameBoardModel.debugConsoleButtonClicked();
                }
            default:
                getGameBoardController().gameBoardModel.playerStopMoving();
        }
    }

    /**
     * this is initially a KeyListener Interface class method, and it is being implemented. It is called when a key is released from the keyboard. this is used to stop the paddle from moving.
     *
     * @param keyEvent this records the input from the keyboard.
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        gameBoardController.gameBoardModel.playerStopMoving();
    }

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse button is clicked. this is used to select the options that are shown during the pause menu is shown.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(!gameBoardController.isShowPauseMenu())
            return;
        if(getContinueButtonRect().contains(mouseGetPointEvent(mouseEvent))){
            gameBoardController.setShowPauseMenu(false);
            updateGameBoardView();
        }
        else if(getRestartButtonRect().contains(mouseGetPointEvent(mouseEvent))){
            gameBoardController.gameBoardModel.restartLevel();
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


    Point mouseGetPointEvent(MouseEvent mouseEvent){
        return mouseEvent.getPoint();
    }

    /**
     * this method is used to check if the mouse is on the button.
     *
     * @param p this takes the event generated by the mouse.
     * @return returns true if the mouse is on the button.
     */
    boolean checkIfMouseMovedToButton(Point p){
        return (getExitButtonRect().contains(p) || getRestartButtonRect().contains(p) || getContinueButtonRect().contains(p));
    }

    /**
     * this is initially a MouseMotionListener Interface class method, and it is being implemented. It is called when a mouse moved onto a component but no buttons are pushed. In this case it will change the cursor when the mouse is on the button.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if(getExitButtonRect() != null && gameBoardController.isShowPauseMenu()) {
            if (checkIfMouseMovedToButton(mouseGetPointEvent(mouseEvent)))
                setCursorLook(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else {
                setCursorLook(Cursor.getDefaultCursor());
            }
        }
        else{
            setCursorLook(Cursor.getDefaultCursor());
        }
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
     * this method is used
     * @return
     */
    public GameBoardController getGameBoardController() {
        return gameBoardController;
    }

    public void setGameBoardController(GameBoardController gameBoardController) {
        this.gameBoardController = gameBoardController;
    }

    private static GameBoardView getUniqueGameBoardView() {
        return uniqueGameBoardView;
    }

    private static void setUniqueGameBoardView(GameBoardView uniqueGameBoardView) {
        GameBoardView.uniqueGameBoardView = uniqueGameBoardView;
    }
}
