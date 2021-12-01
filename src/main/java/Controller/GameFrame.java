package Controller;

import View.HomeMenu;
import View.InfoScreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *  this class handles the frames that are shown in the game and the logic that comes with it.
 */
public class GameFrame extends JFrame implements WindowFocusListener {

    private static final String DEF_TITLE = "Brick Destroy";
    private final int HOME_MENU_WIDTH = 450;
    private final int HOME_MENU_HEIGHT = 300;

    private GameBoardController gameBoardController;
    private HomeMenu homeMenu;
    private InfoScreen infoScreen;

    private boolean gaming;

    private static GameFrame uniqueGameFrame;

    /**
     * this method is used to create a game frame object based on singleton design.
     *
     * @return this returns a one and only gameframe object.
     */
    public static GameFrame singletonGameFrame(){
        if(getUniqueGameFrame() == null){
            setUniqueGameFrame(new GameFrame());
        }
        return getUniqueGameFrame();
    }
    /**
     * this constructor is used to create a game frame object.
     */
    private GameFrame(){
        super();

        setGaming(false);

        this.setLayout(new BorderLayout());

        setGameBoardController(GameBoardController.singletonGameBoardController(this));
        setHomeMenu(HomeMenu.singletonHomeMenu(this,new Dimension(getHOME_MENU_WIDTH(), getHOME_MENU_HEIGHT())));
        setInfoScreen(new InfoScreen());

        this.add(getHomeMenu(),BorderLayout.CENTER);

        this.setUndecorated(true);

        this.setResizable(false);
    }

    /**
     * this method is used to show the game window.
     */
    public void initialize(){
        this.setTitle(DEF_TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.autoLocate();
        this.setVisible(true);
    }

    /**
     * this method is used to dispose the home menu and create the game board on the screen.
     */
    public void enableGameBoard(){
        this.dispose();
        this.remove(getHomeMenu());
        this.add(getGameBoardController().getGameBoardView(),BorderLayout.CENTER);
        this.setUndecorated(false);
        initialize();
        /*to avoid problems with graphics focus controller is added here*/
        this.addWindowFocusListener(this);
    }

    public void displayInfoClicked(){
        try {
            getInfoScreen().DisplayInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *this method is used to set the location of the game frame in the middle of the screen.
     */
    private void autoLocate(){
        this.setLocation(getGameFrameXCoordinate(getDisplayScreenSize()),getGameFrameYCoordinate(getDisplayScreenSize()));
    }

    /**
     * this method is used to get the screen size of the user device display.
     *
     * @return returns the dimension of the user screen size.
     */
    private Dimension getDisplayScreenSize(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * this method is used to get the middle of the x coordinate based on the size provided.
     *
     * @param size this is the size dimension used to get the middle of the size.
     * @return this returns an integer which is the middle of the size provided based on x coordinate.
     */
    private int getGameFrameXCoordinate(Dimension size){
        return (size.width - this.getWidth()) / 2;
    }

    /**
     * this method is used to get the middle of the y coordinate based on the size provided.
     *
     * @param size this is the size dimension used to get the middle of the size.
     * @return this returns an integegr which is the middle of the size provided based on y coordinate.
     */
    private int getGameFrameYCoordinate(Dimension size){
        return (size.height - this.getHeight()) / 2;
    }

    /**
     * this is used to gain the focus of the game window and set the gaming to be true, this will also ensure all keyboard events are being registered to the game. and set the gaming variable to true.
     *
     * @param windowEvent this returns the status of the window.
     */
    @Override
    public void windowGainedFocus(WindowEvent windowEvent) {
        /*
            the first time the frame loses focus is because
            it has been disposed to install the GameBoard,
            so went it regains the focus it's ready to play.
            of course calling a method such as 'onLostFocus'
            is useful only if the GameBoard as been displayed
            at least once
         */
        setGaming(true);
    }

    /**
     * this method is used to ensure that the focus is not on the game when the gaming is set to false, this will also ensure the keyboard events will not be registered to the game.
     *
     * @param windowEvent this returns the status of the window.
     */
    @Override
    public void windowLostFocus(WindowEvent windowEvent) {
        if(isGaming())
            gameBoardController.lostFocusTriggered();
    }

    /**
     * this method is used to get the gameboard object.
     *
     * @return it returns a game board object.
     */
    public GameBoardController getGameBoardController() {
        return gameBoardController;
    }

    /**
     * this method is used to set a game board controller object into a variable for future references.
     *
     * @param gameBoardController this is the game board controller object used to set into a variable for future references.
     */
    public void setGameBoardController(GameBoardController gameBoardController) {
        this.gameBoardController = gameBoardController;
    }

    /**
     * this method is used to get the home menu object which is used to display the home menu/ main menu window.
     *
     * @return this is the home menu object which is returned.
     */
    public HomeMenu getHomeMenu() {
        return homeMenu;
    }

    /**
     * this method is used to set the home menu object into a variable for future reference.
     *
     * @param homeMenu is the home menu object used to set into a variable for future use.
     */
    public void setHomeMenu(HomeMenu homeMenu) {
        this.homeMenu = homeMenu;
    }

    /**
     * this method is used to set if the user is gaming or not (focused on the game).
     *
     * @return this is the boolean value to see if the user is focused on the game or not.
     */
    public boolean isGaming() {
        return gaming;
    }

    /**
     * this method is used to set if the user is playing (focused on the game or not).
     *
     * @param gaming this is the boolean value used to set if the user is gaming or not.
     */
    public void setGaming(boolean gaming) {
        this.gaming = gaming;
    }

    /**
     * this method is used to get the one and only game frame object. based on singleton design pattern.
     *
     * @return this is the one and only game frame object that is returned.
     */
    public static GameFrame getUniqueGameFrame() {
        return uniqueGameFrame;
    }

    /**
     * this method is used to set a game frame object into a variable for future reference.
     *
     * @param uniqueGameFrame this is the game frame object used to set it into a variable for future reference.
     */
    public static void setUniqueGameFrame(GameFrame uniqueGameFrame) {
        GameFrame.uniqueGameFrame = uniqueGameFrame;
    }

    /**
     * this method is used to get the home menu window width (a constant).
     *
     * @return this is an integer value which is the home menu window width.
     */
    public int getHOME_MENU_WIDTH() {
        return HOME_MENU_WIDTH;
    }

    /**
     * this method is used to get the home menu window height (a constant).
     *
     * @return this is an integer value which is the home menu height.
     */
    public int getHOME_MENU_HEIGHT() {
        return HOME_MENU_HEIGHT;
    }

    /**
     * this method is used to get the info screen object which is used to display on how to play the game.
     *
     * @return this returns a info screen object used to show the user how to play the game.
     */
    public InfoScreen getInfoScreen() {
        return infoScreen;
    }

    /**
     * this method is used to set an info screen object into a variable for future referencing.
     *
     * @param infoScreen this is the info screen object used to set into a variable.
     */
    public void setInfoScreen(InfoScreen infoScreen) {
        this.infoScreen = infoScreen;
    }
}
