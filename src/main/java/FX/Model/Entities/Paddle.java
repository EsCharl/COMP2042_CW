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

package FX.Model.Entities;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * this class is used for the user to move the player.
 */
public class Paddle extends Entities implements Movable {

    private static final Color BORDER_COLOR = Color.GREEN.darker().darker();
    private static final Color INNER_COLOR = Color.GREEN;
    private static final int PLAYER_WIDTH = 150;
    private static final int PLAYER_HEIGHT = 10;

    private static final int playerTopLeftXStartPoint = 225;
    private static final int playerTopLeftYStartPoint = 430;

    private final int DEF_MOVE_AMOUNT = 5;

    private int moveAmount;
    private int lowestXCoordinate;
    private int largestXCoordinate;

    private static Paddle uniquePaddle;

    /**
     * this method is used to create and return the one and only paddle object.
     *
     * @param container this is the rectangle which is the play area of the game.
     * @return this returns the one and only paddle object
     */
    public static Paddle singletonPaddle(Rectangle container){
        if(getUniquePlayer() == null){
            setUniquePlayer(new Paddle(container));
        }
        return getUniquePlayer();
    }

    /**
     * this method is used to create a player object.
     *
     * @param playArea this is the information of the play area which will be used to calculate where the player will be created.
     */
    private Paddle(Rectangle playArea) {
        super(new Point2D(playerTopLeftXStartPoint,playerTopLeftYStartPoint),BORDER_COLOR,INNER_COLOR,PLAYER_WIDTH, PLAYER_HEIGHT);
        setMoveAmount(0);
        setLowestXCoordinate((int)playArea.getX());
        setLargestXCoordinate((int)playArea.getX() + (int)playArea.getWidth() - PLAYER_WIDTH);
    }

    /**
     * this method is used to move the paddle (player).
     */
    public void move(){
        if(getBounds().getMinX() + getMoveAmount() < getLowestXCoordinate() || getBounds().getMinX() + getMoveAmount() > getLargestXCoordinate())
            return;
        setBounds(new BoundingBox(getBounds().getMinX() + getMoveAmount(), getBounds().getMinY(), getBounds().getWidth(), getBounds().getHeight()));
    }

    /**
     * this method is used to the move amount which the player could move based on a tick from the gameTimer (thread).
     *
     * @return this is the move amount allowed by the player per tick.
     */
    public int getMoveAmount() {
        return moveAmount;
    }

    /**
     * this method is used to set the movement amount where the player could move.
     *
     * @param moveAmount this is the integer used to set the player movement speed.
     */
    public void setMoveAmount(int moveAmount) {
        this.moveAmount = moveAmount;
    }

    /**
     * this method is used to get the lowest x-axis coordinate that the player could go.
     *
     * @return this returns the lowest x coordinate that the player could go.
     */
    public int getLowestXCoordinate() {
        return lowestXCoordinate;
    }

    /**
     * this method is used to set the lowest X-axis coordinate that the player could go.
     *
     * @param min this is the integer value used to set the lowest x coordinate that the player could go.
     */
    public void setLowestXCoordinate(int min) {
        this.lowestXCoordinate = min;
    }

    /**
     * this method is used to get the largest x-axis coordinate that the player could go.
     *
     * @return this returns the largest x coordinate that the player could go.
     */
    public int getLargestXCoordinate() {
        return largestXCoordinate;
    }

    /**
     * this method is used to set the largest x-axis coordinate that the player could go.
     *
     * @param max this is the integer value used to set the largest x coordinate that the player could go.
     */
    public void setLargestXCoordinate(int max) {
        this.largestXCoordinate = max;
    }

    /**
     * this method is used to get the amount of distance (pixel) that the player could move.
     *
     * @return this returns a constant value that is used to move the player movement.
     */
    public int getDEF_MOVE_AMOUNT() {
        return DEF_MOVE_AMOUNT;
    }

    /**
     * this method is used to get the one and only player object.
     *
     * @return it returns a player object after the method singletonPlayer is called.
     */
    private static Paddle getUniquePlayer() {
        return uniquePaddle;
    }

    /**
     * this method is used to set the player object into a variable for future reference.
     *
     * @param uniquePaddle this is the Model.Player object used to store into a variable.
     */
    private static void setUniquePlayer(Paddle uniquePaddle) {
        Paddle.uniquePaddle = uniquePaddle;
    }
}
