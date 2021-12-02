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

package Model;

import Model.Ball.Ball;

import java.awt.*;

/**
 * this class is used for the user to move the player.
 */
public class Player {


    private final Color BORDER_COLOR = Color.GREEN.darker().darker();
    private final Color INNER_COLOR = Color.GREEN;
    private final int PLAYER_WIDTH = 150;
    private final int PLAYER_HEIGHT = 10;

    private final int DEF_MOVE_AMOUNT = 5;

    private Rectangle playerFace;
    private Point playerCenterPosition;
    private int moveAmount;
    private int lowestXCoordinate;
    private int largestXCoordinate;

    private static Player uniquePlayer;

    public static Player singletonPlayer(Point ballPoint,Rectangle container){
        if(getUniquePlayer() == null){
            setUniquePlayer(new Player(ballPoint, container));
        }
        return getUniquePlayer();
    }

    /**
     * this method is used to create a player object.
     *
     * @param playerCenterPosition this is the position of the ball.
     * @param playArea this is the information of the play area which will be used to calculate where the player will be created.
     */
    private Player(Point playerCenterPosition, Rectangle playArea) {
        setPlayerCenterPosition(playerCenterPosition);
        setMoveAmount(0);
        setPlayerFace(makeRectangle(PLAYER_WIDTH, PLAYER_HEIGHT));
        setLowestXCoordinate(playArea.x + (PLAYER_WIDTH / 2));
        setLargestXCoordinate(getLowestXCoordinate() + playArea.width - PLAYER_WIDTH);
    }

    /**
     * this is used to create a rectangle shape which the user will use.
     *
     * @param width this is the width of the paddle.
     * @param height this is the height of the paddle.
     * @return it will return a rectangle shape.
     */
    private Rectangle makeRectangle(int width,int height){
        return  new Rectangle(createRectanglePoint(width),new Dimension(width,height));
    }

    /**
     * this method is used to create a point for the paddle.
     *
     * @param width this is the width of the paddle.
     * @return it returns a point where the paddle is to be created.
     */
    private Point createRectanglePoint(int width){
        return new Point((int)(getPlayerCenterPosition().getX() - (width / 2)),(int) getPlayerCenterPosition().getY());
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
        getPlayerCenterPosition().setLocation(moveToX(), getPlayerCenterPosition().getY());
        getPlayerFace().setLocation(getPlayerCenterPosition().x - (int)getPlayerFace().getWidth()/2, getPlayerCenterPosition().y);
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
    public void movRight(){
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
        return  playerFace;
    }

    /**
     * this method is used to set the paddle (player) and the ball point (center) to the starting position.
     *
     * @param p this is the Point position where the ball is to be set.
     */
    public void resetPosition(Point p){
        getPlayerCenterPosition().setLocation(p);
        getPlayerFace().setLocation(getPlayerCenterPosition().x - (int)getPlayerFace().getWidth()/2, getPlayerCenterPosition().y);
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
     * this method is used to get the player border Color which is used to draw on the screen (Gameboard view).
     *
     * @return this returns the border color of the player.
     */
    public Color getBORDER_COLOR() {
        return BORDER_COLOR;
    }

    /**
     * this method is used to get the player inner Color which is used to draw on the screen (Gameboard view).
     *
     * @return this returns the inner color of the player.
     */
    public Color getINNER_COLOR() {
        return INNER_COLOR;
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
    public Point getPlayerCenterPosition() {
        return playerCenterPosition;
    }

    /**
     * this method is used to set the center position of the player.
     *
     * @param playerCenterPosition this is the Point datatype used to set the location of the paddle.
     */
    public void setPlayerCenterPosition(Point playerCenterPosition) {
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
