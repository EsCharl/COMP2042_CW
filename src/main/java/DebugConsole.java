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
import java.awt.event.WindowListener;

/**
 * Objective of this class is to create the console
 */
public class DebugConsole extends JDialog implements WindowListener{

    private static final String TITLE = "Debug Console";


    private JFrame owner;
    private DebugPanel debugPanel;
    private GameBoard gameBoard;
    private Wall wall;

    private static DebugConsole uniqueDebugConsole;

    public static DebugConsole singletonDebugConsole(JFrame owner,Wall wall,GameBoard gameBoard){
        if(getUniqueDebugConsole() == null){
            setUniqueDebugConsole(new DebugConsole(owner,wall,gameBoard));
        }
        return getUniqueDebugConsole();
    }

    /**
     * this constructor is used for showing a console in the middle of the game window.
     *
     * @param owner a graphical frame that is going to be used.
     * @param wall the game level that is generated, the status of the game.
     * @param gameBoard the status of the game board.
     */
    private DebugConsole(JFrame owner,Wall wall,GameBoard gameBoard){

        setWall(wall);
        setOwner(owner);
        setGameBoard(gameBoard);
        initialize();

        setDebugPanel(DebugPanel.singletonDebugPanel(wall,gameBoard));
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
        this.setLocation(getXDebugConsoleCoordinate(),getYDebugConsoleCoordinate());
    }

    /**
     * this method is used to get the x coordinate on where the debug console is set based on the JFrame provided.
     *
     * @return returns an X coordinate on where the debug console is to be set.
     */
    private int getXDebugConsoleCoordinate(){
        return ((getOwner().getWidth() - this.getWidth()) / 2) + getOwner().getX();
    }

    /**
     * this method is used to get the y coordinate on where the debug console is set based on the JFrame provided.
     *
     * @return returns an X coordinate on where the debug console is to be set.
     */
    private int getYDebugConsoleCoordinate(){
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
        getGameBoard().repaint();
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
        getDebugPanel().setValues(b.getSpeedX(),b.getSpeedY());
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

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }

    public static DebugConsole getUniqueDebugConsole() {
        return uniqueDebugConsole;
    }

    public static void setUniqueDebugConsole(DebugConsole uniqueDebugConsole) {
        DebugConsole.uniqueDebugConsole = uniqueDebugConsole;
    }
}
