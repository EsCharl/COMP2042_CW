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

package Controller;

import Model.Ball.Ball;
import Model.GameScore;
import Model.Game;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Objective of this class is to create the console
 */
public class DebugConsole extends JDialog implements WindowListener{
    private final Color BACKGROUND_COLOR = Color.WHITE;

    private final String RESET_BALLS_TEXT = "Reset Balls";
    private final String SKIP_LEVEL_TEXT = "Skip Level";
    private final String BALL_X_AXIS_SPEED_TEXT = "Ball x-axis speed";
    private final String BALL_Y_AXIS_SPEED_TEXT = "Ball y-axis speed";

    private final int MAX_POSITIVE_SPEED_X = 4;
    private final int MAX_NEGATIVE_SPEED_X = -4;
    private final int MAX_POSITIVE_SPEED_Y = 4;
    private final int MAX_NEGATIVE_SPEED_Y = -4;

    private JButton skipLevelButton;
    private JButton resetBallsButton;

    private JSlider ballXSpeedSlider;
    private JSlider ballYSpeedSlider;

    private static final String TITLE = "Debug Console";

    private JPanel jPanel;
    private Game game;
    private GameScore gameScore;

    private GameBoardController gameBoardController;

    /**
     * this constructor is used for showing a console in the middle of the game window.
     *
     * @param game the game level that is generated, the status of the game.
     */
    public DebugConsole(Game game, GameBoardController gameBoardController){
        setGameBoardController(gameBoardController);

        setjPanel(new JPanel());

        setDebugPanelLook(getjPanel());

        debugFunctions(getjPanel());

        addTextSpeedCategory(getjPanel());

        setGameScore(GameScore.singletonGameScore());

        setGame(game);
        initialize();

        this.add(getjPanel(),BorderLayout.CENTER);

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
        this.setLocation(getSetDebugConsoleXCoordinate(), getSetDebugConsoleYCoordinate());
    }

    /**
     * this method is used to get the x coordinate on where the debug console is set based on the JFrame provided.
     *
     * @return returns an X coordinate on where the debug console is to be set.
     */
    private int getSetDebugConsoleXCoordinate(){
        return ((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2);
    }

    /**
     * this method is used to get the y coordinate on where the debug console is set based on the JFrame provided.
     *
     * @return returns an X coordinate on where the debug console is to be set.
     */
    private int getSetDebugConsoleYCoordinate(){
        return ((Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2);
    }

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
        repaint();
    }


    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    /**
     * this is initially a WindowListener Interface class method, and it is being implemented. It is called when the window is being active. what this method does is to set the location of the debug console and set the speed value of the ball into the debug panel.
     *
     * @param windowEvent the action that is done on the window
     */
    @Override
    public void windowActivated(WindowEvent windowEvent) {
        setLocation();
        Ball b = getGame().getBall();
        setViewValues(b.getXSpeed(),b.getYSpeed());
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }

    /**
     * this method is used to get a game object which contains all the information for the game to progress.
     *
     * @return this returns a game object.
     */
    public Game getGame() {
        return game;
    }

    /**
     * this method is used to set a game object into a variable for future referencing.
     *
     * @param game this is the game object used to set into a variable
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * this method is used to get a game score object which is used to handle the scoring mechanism of the game.
     *
     * @return this is the game score object which is being returned.
     */
    public GameScore getGameScore() {
        return gameScore;
    }

    /**
     * this method is used to set a game score object into a variable for future reference.
     *
     * @param gameScore this is the game score object used to set into a variable.
     */
    public void setGameScore(GameScore gameScore) {
        this.gameScore = gameScore;
    }

    /**
     * this method is to add the speed category into the Debug Panel.
     */
    private void addTextSpeedCategory(JPanel jPanel) {
        jPanel.add(createJLabelCentered(getBALL_X_AXIS_SPEED_TEXT()));
        jPanel.add(createJLabelCentered(getBALL_Y_AXIS_SPEED_TEXT()));
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

    /**
     * this method is used to create the functions of the debug panel and to display them.
     */
    private void debugFunctions(JPanel jPanel) {
        setSkipLevelButton(makeButton(getSKIP_LEVEL_TEXT(), e -> getGameBoardController().skipLevelTriggered()));
        setResetBallsButton(makeButton(getRESET_BALLS_TEXT(), e -> getGameBoardController().resetBallCountTriggered()));

        setBallXSpeedSlider(makeSlider(getMAX_NEGATIVE_SPEED_X(), getMAX_POSITIVE_SPEED_X(), e -> getGameBoardController().ballXSpeedValue(getBallXSpeedSlider().getValue())));
        setBallYSpeedSlider(makeSlider(getMAX_NEGATIVE_SPEED_Y(), getMAX_POSITIVE_SPEED_Y(), e -> getGameBoardController().ballYSpeedValue(getBallYSpeedSlider().getValue())));

        jPanel.add(getSkipLevelButton());
        jPanel.add(getResetBallsButton());

        jPanel.add(getBallXSpeedSlider());
        jPanel.add(getBallYSpeedSlider());
    }

    /**
     * this method creates a square which will specify the number of inputs allowed.
     */
    private void setDebugPanelLook(JPanel jPanel){
        jPanel.setBackground(getBACKGROUND_COLOR());
        jPanel.setLayout(new GridLayout(3,2));
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
        return out;
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
    public void setViewValues(int x, int y){
        getBallXSpeedSlider().setValue(x);
        getBallYSpeedSlider().setValue(y);
    }

    /**
     * this method is used to get the max positive speed on the x-axis for the ball.
     *
     * @return this returns a max positive speed for the ball on the x-axis;
     */
    public int getMAX_POSITIVE_SPEED_X() {
        return MAX_POSITIVE_SPEED_X;
    }

    /**
     * this method is used to get the max negative speed on the x-axis for the ball.
     *
     * @return this returns a max negative speed for the ball on the x-axis.
     */
    public int getMAX_NEGATIVE_SPEED_X() {
        return MAX_NEGATIVE_SPEED_X;
    }

    /**
     * this method is used to get the max positive speed on the y-axis for the ball.
     *
     * @return this returns a max positive speed for the ball on the y-axis;
     */
    public int getMAX_POSITIVE_SPEED_Y() {
        return MAX_POSITIVE_SPEED_Y;
    }

    /**
     * this method is used to get the max negative speed on the y-axis for the ball.
     *
     * @return this returns a max negative speed for the ball on the y-axis.
     */
    public int getMAX_NEGATIVE_SPEED_Y() {
        return MAX_NEGATIVE_SPEED_Y;
    }

    /**
     * this method is used to get a button object which is called the skip level button.
     *
     * @return it returns a skip level button.
     */
    public JButton getSkipLevelButton() {
        return skipLevelButton;
    }

    /**
     * this method is used to set a button object for the skip level button which is used to future references.
     *
     * @param skipLevelButton this is the skip level button used to set into a variable for future reference.
     */
    public void setSkipLevelButton(JButton skipLevelButton) {
        this.skipLevelButton = skipLevelButton;
    }

    /**
     * this method is used to get a button object for the reset balls button.
     *
     * @return it returns a reset ball button.
     */
    public JButton getResetBallsButton() {
        return resetBallsButton;
    }

    /**
     * this method is used to set a button object for the reset balls button.
     *
     * @param resetBallsButton this is the button object used to set a button for the reset balls button.
     */
    public void setResetBallsButton(JButton resetBallsButton) {
        this.resetBallsButton = resetBallsButton;
    }

    /**
     * this method is used to get a slider which is the x-axis speed for the ball slider.
     *
     * @return this returns a slider for the x speed of the ball.
     */
    public JSlider getBallXSpeedSlider() {
        return ballXSpeedSlider;
    }

    /**
     * this method is used to set a slider object for the x-axis speed for the ball slider.
     *
     * @param ballXSpeedSlider this is the slider used to set into a variable that is in control of the ball x speed.
     */
    public void setBallXSpeedSlider(JSlider ballXSpeedSlider) {
        this.ballXSpeedSlider = ballXSpeedSlider;
    }

    /**
     * this method is used to get a slider object for the y-axis speed for the ball slider.
     *
     * @return this returns a slider for the y speed of the ball.
     */
    public JSlider getBallYSpeedSlider() {
        return ballYSpeedSlider;
    }

    /**
     * this method is used to set a slider object for the y-axis speed for the ball slider.
     *
     * @param ballYSpeedSlider this is the slider used to set into a variable that is in control of the ball y speed.
     */
    public void setBallYSpeedSlider(JSlider ballYSpeedSlider) {
        this.ballYSpeedSlider = ballYSpeedSlider;
    }

    /**
     * this method is used to get a constant background color for the debug panel.
     *
     * @return this is the constant background color which it is being returned.
     */
    public Color getBACKGROUND_COLOR() {
        return BACKGROUND_COLOR;
    }

    /**
     * this method is used to get the reset balls text to be displayed on the reset balls button.
     *
     * @return this returns a string which is for the reset ball button.
     */
    public String getRESET_BALLS_TEXT() {
        return RESET_BALLS_TEXT;
    }

    /**
     * this method is used to get the skip level text which is used to be displayed on the skip level button.
     *
     * @return this returns a string which is for the skip level button.
     */
    public String getSKIP_LEVEL_TEXT() {
        return SKIP_LEVEL_TEXT;
    }

    /**
     * this method is used to get the ball x-axis speed text which is being displayed on the debug panel to indicate which slider is for which axis of the ball speed.
     *
     * @return this returns a string which is used to be displayed on the debug panel right under the x-axis slider.
     */
    public String getBALL_X_AXIS_SPEED_TEXT() {
        return BALL_X_AXIS_SPEED_TEXT;
    }

    /**
     * this method is used to get the ball y-axis speed text which is being displayed on the debug panel to indicate which slider is for which axis of the ball speed.
     *
     * @return this returns a string which is used to be displayed on the debug panel right under the y-axis slider.
     */
    public String getBALL_Y_AXIS_SPEED_TEXT() {
        return BALL_Y_AXIS_SPEED_TEXT;
    }

    /**
     * this method is used to get the panel which is used to add the sliders, button and label.
     *
     * @return this returns the JPanel used to add the sliders, button and label.
     */
    public JPanel getjPanel() {
        return jPanel;
    }

    /**
     * this method is used to set a JPanel object into a variable for future reference.
     *
     * @param jPanel this is the JPanel object used to set into a variable.
     */
    public void setjPanel(JPanel jPanel) {
        this.jPanel = jPanel;
    }

    /**
     * this method is used to get the game board object which is used to get the game board controller (Controller) to process the user input.
     *
     * @return this returns the game board controller used to process the user input.
     */
    public GameBoardController getGameBoardController() {
        return gameBoardController;
    }

    /**
     * this method is used to set the game board object which is going to be referenced in the future.
     *
     * @param gameBoardController this is the game board object used to set into a variable.
     */
    public void setGameBoardController(GameBoardController gameBoardController) {
        this.gameBoardController = gameBoardController;
    }
}
