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
package test;

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

    /**
     * this method is used for showing a console in the middle of the game window.
     *
     * @param owner a graphical frame that is going to be used.
     * @param wall the game level that is generated, the status of the game.
     * @param gameBoard the status of the game board.
     */
    public DebugConsole(JFrame owner,Wall wall,GameBoard gameBoard){

        this.wall = wall;
        this.owner = owner;
        this.gameBoard = gameBoard;
        initialize();

        debugPanel = new DebugPanel(wall,gameBoard);
        this.add(debugPanel,BorderLayout.CENTER);


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
        int x = ((owner.getWidth() - this.getWidth()) / 2) + owner.getX();
        int y = ((owner.getHeight() - this.getHeight()) / 2) + owner.getY();
        this.setLocation(x,y);
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
        gameBoard.repaint();
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
        Ball b = wall.ball;
        debugPanel.setValues(b.getSpeedX(),b.getSpeedY());
    }

    /**
     * this is initially a WindowListener Interface class method, and it is being implemented. Its uses are to make a window to an inactive state.
     *
     * @param windowEvent the action that is done on the window
     */
    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }
}
