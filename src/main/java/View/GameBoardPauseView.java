/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2021  Leong Chang Yung
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

package View;

import Controller.GameBoardController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;

public class GameBoardPauseView extends JComponent implements MouseListener, MouseMotionListener {

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;

    private final Color PAUSE_MENU_STRING_COLOR = new Color(0,255,0);

    private final String CONTINUE_TEXT = "Continue";
    private final String RESTART_TEXT = "Restart";
    private final String EXIT_TEXT = "Exit";
    private final String PAUSE_TEXT = "Pause Menu";

    private int stringDisplayLength;
    private final int PAUSE_MENU_TEXT_SIZE = 30;

    private Font menuFont;

    public GameBoardPauseView(){

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        setStringDisplayLength(0);
        setMenuFont(new Font("Monospaced",Font.PLAIN, getPAUSE_MENU_TEXT_SIZE()));
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
        g2d.fillRect(0,0, GameBoardController.getDEF_WIDTH(), GameBoardController.getDEF_HEIGHT());

        g2d.setComposite(tmp);
        g2d.setColor(tmpColor);
    }

    /**
     * This method is used to draw the pause menu and the buttons of the pause menu.
     *
     * @param g2d this is the object to draw the pause menu.
     */
    void drawPauseMenuContents(Graphics2D g2d){
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();


        g2d.setFont(getMenuFont());
        g2d.setColor(getPAUSE_MENU_STRING_COLOR());

        if(getStringDisplayLength() == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            setStringDisplayLength(getMenuFont().getStringBounds(getPAUSE_TEXT(),frc).getBounds().width);
        }

        // get the position of top center.
        int x = (getWidth() - getStringDisplayLength()) / 2;
        int y = getHeight() / 10;

        g2d.drawString(getPAUSE_TEXT(),x,y);

        x = getWidth() / 8;
        y = getHeight() / 4;


        if(getContinueButtonRect() == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            setContinueButtonRect(getMenuFont().getStringBounds(getCONTINUE_TEXT(),frc).getBounds());
            getContinueButtonRect().setLocation(x,y- getContinueButtonRect().height);
        }

        g2d.drawString(getCONTINUE_TEXT(),x,y);

        y *= 2;

        if(getRestartButtonRect() == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            setRestartButtonRect(getMenuFont().getStringBounds(getRESTART_TEXT(),frc).getBounds());
            getRestartButtonRect().setLocation(x,y- getRestartButtonRect().height);
        }

        g2d.drawString(getRESTART_TEXT(),x,y);

        y *= 3.0/2;

        if(getExitButtonRect() == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            setExitButtonRect(getMenuFont().getStringBounds(getEXIT_TEXT(),frc).getBounds());
            getExitButtonRect().setLocation(x,y- getExitButtonRect().height);
        }

        g2d.drawString(getEXIT_TEXT(),x,y);

        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);
    }

    /**
     * This method is used to draw the pause menu (refer to drawPauseMenu method) and to obscure the game board (refer to the obscureGameBoard method).
     *
     * @param g2d this is the object used to draw the menu.
     */
    void drawPauseMenu(Graphics2D g2d){
        obscureGameBoard(g2d);
        drawPauseMenuContents(g2d);
    }

    /**
     * this method is used to get the pause menu string color which is used to display the color of the string during the pause menu state.
     *
     * @return this is the color used to set the string text on the pause menu.
     */
    public Color getPAUSE_MENU_STRING_COLOR() {
        return PAUSE_MENU_STRING_COLOR;
    }

    /**
     * this method is used to get the text size which is a constant value. This is used to set the string text sizes.
     *
     * @return a text size in integer which is used to set the string size.
     */
    public int getPAUSE_MENU_TEXT_SIZE() {
        return PAUSE_MENU_TEXT_SIZE;
    }

    /**
     * this method is used to get the continue text which is used to display as an option for the user to select in order to continue the game.
     *
     * @return this string returned is the string used to display on the pause menu screen.
     */
    public String getCONTINUE_TEXT() {
        return CONTINUE_TEXT;
    }

    /**
     * this method is used to get the restart text which is used to display as an option for the user to select in order to restart the level.
     *
     * @return this string returned is the string used to display on the pause menu screen.
     */
    public String getRESTART_TEXT() {
        return RESTART_TEXT;
    }

    /**
     * this method is used to get the exit text which is used to display as an option for the user to select in order to exit the game.
     *
     * @return this string returned is the string used to display on the pause menu screen.
     */
    public String getEXIT_TEXT() {
        return EXIT_TEXT;
    }

    /**
     * this method is used to get the pause text which is used to display on the pause menu to indicate that the user is in the pause menu state.
     *
     * @return this is the pause text used to display on the pause menu to indicate to the user he is in the pause menu state.
     */
    public String getPAUSE_TEXT() {
        return PAUSE_TEXT;
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

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse button is clicked. this is used to select the options that are shown during the pause menu is shown.
     *
     * @param mouseEvent this records the input from the mouse
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(!gameBoardController.isShowPauseMenu())
            return;
        if(getContinueButtonRect().contains(mouseEvent.getPoint())){
            gameBoardController.setShowPauseMenu(false);
            updateGameBoardView();
        }
        else if(getRestartButtonRect().contains(mouseEvent.getPoint())){
            gameBoardController.restartLevelTriggered();
        }
        else if(getExitButtonRect().contains(mouseEvent.getPoint())){
            System.exit(0);
        }
    }

    /**
     * this method is used to change the look of the cursor.
     *
     * @param Cursor the cursor state change to.
     */
    public void setCursorLook(Cursor Cursor) {
        setCursor(Cursor);
    }

    /**
     * this is initially a MouseMotionListener Interface class method, and it is being implemented. It is called when a mouse moved onto a component but no buttons are pushed. In this case it will change the cursor when the mouse is on the button.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if(getExitButtonRect() != null && gameBoardController.isShowPauseMenu()) {
            if (checkIfMouseMovedToButton(mouseEvent.getPoint()))
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
     * this method is used to check if the mouse is on the button.
     *
     * @param p this takes the event generated by the mouse.
     * @return returns true if the mouse is on the button.
     */
    public boolean checkIfMouseMovedToButton(Point p){
        return (getExitButtonRect().contains(p) || getRestartButtonRect().contains(p) || getContinueButtonRect().contains(p));
    }
}
