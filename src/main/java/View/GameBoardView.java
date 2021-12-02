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
import Model.Ball.Ball;
import Model.Brick.Brick;
import Model.Player;
import Model.Game;

import javax.swing.*;
import java.awt.*;

/**
 * this class handles the rendering for the game board.
 */
public class GameBoardView extends JComponent {






    private String message;

    private GameBoardController gameBoardController;
    private Game game;


    private final Color GAMEBOARD_BACKGROUND_COLOR = Color.WHITE;
    private final Color GAMEBOARD_STRING_COLOR = Color.BLUE;


    private static GameBoardView uniqueGameBoardView;

    public static GameBoardView singletonGameBoardView(GameBoardController gameBoardController, Game game){
        if(getUniqueGameBoardView() == null){
            setUniqueGameBoardView(new GameBoardView(gameBoardController, game));
        }
        return getUniqueGameBoardView();
    }

    public GameBoardView(GameBoardController gameBoardController, Game game){
        super();


        setGame(game);

        setPreferredSize(new Dimension(GameBoardController.getDEF_WIDTH(), GameBoardController.getDEF_HEIGHT()));
        setFocusable(true);
        requestFocusInWindow();

        setGameBoardController(gameBoardController);

    }



    /**
     * This is for clearing the screen by setting the whole window to be set into the background colour.
     *
     * @param g2d this is the object that is being passed into for clearing.
     */
    void clear(Graphics2D g2d){
        Color tmp = g2d.getColor();
        g2d.setColor(getGAMEBOARD_BACKGROUND_COLOR());
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
        g2d.setColor(p.getINNER_COLOR());
        g2d.fill(s);

        g2d.setColor(p.getBORDER_COLOR());
        g2d.draw(s);

        g2d.setColor(tmp);
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

        g2d.setColor(getGAMEBOARD_STRING_COLOR());
        g2d.drawString(getMessage(),250,225);

        drawBall(getGame().getBall(),g2d);

        for(Brick b : getGame().getBricks())
            if(!b.isBroken())
                drawBrick(b,g2d);

        drawPlayer(getGame().getPlayer(),g2d);

        if(getGameBoardController().isShowPauseMenu())
            drawPauseMenu(g2d);

        Toolkit.getDefaultToolkit().sync();
    }




    /**
     * this method is used get the Game Board Controller object.
     *
     * @return this returns a game board controller.
     */
    public GameBoardController getGameBoardController() {
        return gameBoardController;
    }

    /**
     * this method is used to set the game board controller object into a variable for future reference.
     *
     * @param gameBoardController this is the game board controller used to set the variable.
     */
    public void setGameBoardController(GameBoardController gameBoardController) {
        this.gameBoardController = gameBoardController;
    }

    public static GameBoardView getUniqueGameBoardView() {
        return uniqueGameBoardView;
    }

    public static void setUniqueGameBoardView(GameBoardView uniqueGameBoardView) {
        GameBoardView.uniqueGameBoardView = uniqueGameBoardView;
    }

    /**
     * this method is used to change the message variable.
     *
     * @param message the String used to change the message variable.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * this method is used to get the value in the message variable.
     *
     * @return returns the value in the message variable.
     */
    public String getMessage() {
        return message;
    }



    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * this method is used to get the color used to set into the game board background.
     *
     * @return this is the color used to set on the background of the game board.
     */
    public Color getGAMEBOARD_BACKGROUND_COLOR() {
        return GAMEBOARD_BACKGROUND_COLOR;
    }



    /**
     * this method is used to get the string color for the gameboard to display the status of the game. namely, the ball count, brick count.
     *
     * @return this returns the color used to set the string color on the gameboard.
     */
    public Color getGAMEBOARD_STRING_COLOR() {
        return GAMEBOARD_STRING_COLOR;
    }


}
