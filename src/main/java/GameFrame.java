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
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

/**
 *  this class handles the frames that are shown in the game and the logic that comes with it.
 */
public class GameFrame extends JFrame implements WindowFocusListener {

    private static final String DEF_TITLE = "Brick Destroy";

    private GameBoard gameBoard;
    private HomeMenu homeMenu;

    private boolean gaming;

    private static GameFrame uniqueGameFrame;

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

        setGameBoard(GameBoard.singletonGameBoard(this));
        setHomeMenu(HomeMenu.singletonHomeMenu(this,new Dimension(450,300)));

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
        this.add(getGameBoard(),BorderLayout.CENTER);
        this.setUndecorated(false);
        initialize();
        /*to avoid problems with graphics focus controller is added here*/
        this.addWindowFocusListener(this);

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
     * this method is used to get the
     *
     * @param size
     * @return
     */
    private int getGameFrameXCoordinate(Dimension size){
        return (size.width - this.getWidth()) / 2;
    }

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
            gameBoard.onLostFocus();

    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public HomeMenu getHomeMenu() {
        return homeMenu;
    }

    public void setHomeMenu(HomeMenu homeMenu) {
        this.homeMenu = homeMenu;
    }

    public boolean isGaming() {
        return gaming;
    }

    public void setGaming(boolean gaming) {
        this.gaming = gaming;
    }

    public static GameFrame getUniqueGameFrame() {
        return uniqueGameFrame;
    }

    public static void setUniqueGameFrame(GameFrame uniqueGameFrame) {
        GameFrame.uniqueGameFrame = uniqueGameFrame;
    }
}
