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

import FX.Model.Entities.Ball.Ball;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * this class is used for the user to move the player.
 */
public class Player extends RectangularEntities {

    private static final Color BORDER_COLOR = Color.GREEN.darker().darker();
    private static final Color INNER_COLOR = Color.GREEN;
    private static final int PLAYER_WIDTH = 150;
    private static final int PLAYER_HEIGHT = 10;

    private final int DEF_MOVE_AMOUNT = 5;

    private Rectangle playerFace;
    private Point2D playerCenterPosition;
    private int moveAmount;
    private int lowestXCoordinate;
    private int largestXCoordinate;

    private static Player uniquePlayer;

    public static Player singletonPlayer(Point2D ballPoint, Rectangle container){
        if(getUniquePlayer() == null){
            setUniquePlayer(new Player(ballPoint, container));
        }
        return getUniquePlayer();
    }

    /**
     * this method is used to create a player object.
     *
     * @param playerTopLeftPosition this is the top left position of the player (paddle).
     * @param playArea this is the information of the play area which will be used to calculate where the player will be created.
     */
    private Player(Point2D playerTopLeftPosition, Rectangle playArea) {
        super((int)playerTopLeftPosition.getX(),(int)playerTopLeftPosition.getY(),BORDER_COLOR,INNER_COLOR,PLAYER_WIDTH, PLAYER_HEIGHT);
        setPlayerCenterPosition(playerTopLeftPosition);
        setMoveAmount(0);
        setPlayerFace(makeRectangle(PLAYER_WIDTH, PLAYER_HEIGHT));
        setLowestXCoordinate((int)playArea.getX());
        setLargestXCoordinate(getLowestXCoordinate() + (int)playArea.getWidth() - PLAYER_WIDTH);
    }

    /**
     * this is used to create a rectangle shape which the user will use.
     *
     * @param width this is the width of the paddle.
     * @param height this is the height of the paddle.
     * @return it will return a rectangle shape.
     */
    private Rectangle makeRectangle(int width,int height){
        return new Rectangle(getPlayerCenterPosition().getX(),getPlayerCenterPosition().getY(), width, height);
    }

    /**
     * this is used to check if there is an impact between the paddle and the ball.
     *
     * @param b the ball object which is used to detect if it collides with the paddle.
     * @return returns true if the ball comes in contact with the paddle, false if it doesn't
     */
    public boolean impact(Ball b){
        return getPlayerFace().contains(b.getCenterPosition()) && getPlayerFace().contains(b.getDown()) ;
    }

    /**
     * this method is used to move the paddle (player).
     */
    public void move(){
        if(moveToX() < getLowestXCoordinate() || moveToX() > getLargestXCoordinate())
            return;
        setLocation((int)(getPlayerCenterPosition().getX() - getPlayerFace().getWidth()/2), (int)getPlayerCenterPosition().getY());
    }

    public void setLocation(int x, int y){
        getPlayerFace().setX(x);
        getPlayerFace().setY(y);
    }

    /**
     * this method is used to get the supposing final position for the player in the X-axis.
     *
     * @return it returns the location where the paddle is supposed to go.
     */
    private double moveToX(){
        return getPlayerCenterPosition().getX() + getMoveAmount();
    }

    /**
     * this method is used to set the move amount to the left direction which will be used to move the paddle.
     */
    public void moveLeft(){
        setMoveAmount(-getDEF_MOVE_AMOUNT());
    }

    /**
     * this method is used to set the move amount to the right direction which will be used to mve the paddle.
     */
    public void moveRight(){
        setMoveAmount(getDEF_MOVE_AMOUNT());
    }

    /**
     * this method is used to stop the movement of the paddle by setting the move amount to 0.
     */
    public void stop(){
        setMoveAmount(0);
    }

    /**
     * this method is used to get the shape of the paddle.
     *
     * @return it returns the shape of the paddle.
     */
    public Rectangle getPlayerFace(){
        return playerFace;
    }

    /**
     * this method is used to get the one and only player object.
     *
     * @return it returns a player object after the method singletonPlayer is called.
     */
    private static Player getUniquePlayer() {
        return uniquePlayer;
    }

    /**
     * this method is used to set the player object into a variable for future reference.
     *
     * @param uniquePlayer this is the Model.Player object used to store into a variable.
     */
    private static void setUniquePlayer(Player uniquePlayer) {
        Player.uniquePlayer = uniquePlayer;
    }

    /**
     * this method is used to set the player face into a variable for setting the location of the player to update the view. (gameboard view).
     *
     * @param playerFace this is the rectangle object which is the player face to be set into a variable.
     */
    public void setPlayerFace(Rectangle playerFace) {
        this.playerFace = playerFace;
    }

    /**
     * this method is used to get the center position of the player. which is used to draw the paddle.
     *
     * @return this returns the position of the player in Point datatype.
     */
    public Point2D getPlayerCenterPosition() {
        return playerCenterPosition;
    }

    /**
     * this method is used to set the center position of the player.
     *
     * @param playerCenterPosition this is the Point datatype used to set the location of the paddle.
     */
    public void setPlayerCenterPosition(Point2D playerCenterPosition) {
        this.playerCenterPosition = playerCenterPosition;
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
}
