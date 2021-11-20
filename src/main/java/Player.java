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

import java.awt.*;

/**
 * this class is used for the user to move the player.
 */
public class Player {


    public static final Color BORDER_COLOR = Color.GREEN.darker().darker();
    public static final Color INNER_COLOR = Color.GREEN;

    private static final int DEF_MOVE_AMOUNT = 5;

    private Rectangle playerFace;
    private Point ballPoint;
    private int moveAmount;
    private int min;
    private int max;

    /**
     * this method is used to create a player object.
     *
     * @param ballPoint this is the position of the ball.
     * @param width this is the width of the paddle which the user will be controlling.
     * @param height this will the height of the paddle which the user will be controlling.
     * @param container this is the information of the rectangle which will be used to be created.
     */
    public Player(Point ballPoint,int width,int height,Rectangle container) {
        this.ballPoint = ballPoint;
        moveAmount = 0;
        playerFace = makeRectangle(width, height);
        min = container.x + (width / 2);
        max = min + container.width - width;
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
        return new Point((int)(ballPoint.getX() - (width / 2)),(int)ballPoint.getY());
    }

    /**
     * this is used to check if there is an impact between the paddle and the ball.
     *
     * @param b the ball object which is used to detect if it collides with the paddle.
     * @return returns true if the ball comes in contact with the paddle, false if it doesn't
     */
    public boolean impact(Ball b){
        return playerFace.contains(b.getPosition()) && playerFace.contains(b.down) ;
    }

    /**
     * this method is used to move the paddle (player).
     */
    public void move(){
        if(moveToX() < min || moveToX() > max)
            return;
        ballPoint.setLocation(moveToX(),ballPoint.getY());
        playerFace.setLocation(ballPoint.x - (int)playerFace.getWidth()/2,ballPoint.y);
    }

    /**
     * this method is used to get the supposing final position in the X-axis.
     *
     * @return it returns the location where the paddle is supposed to go.
     */
    private double moveToX(){
        return ballPoint.getX() + moveAmount;
    }

    /**
     * this method is used to set the move amount to the left direction which will be used to move the paddle.
     */
    public void moveLeft(){
        moveAmount = -DEF_MOVE_AMOUNT;
    }

    /**
     * this method is used to set the move amount to the right direction which will be used to mve the paddle.
     */
    public void movRight(){
        moveAmount = DEF_MOVE_AMOUNT;
    }

    /**
     * this method is used to stop the movement of the paddle by setting the move amount to 0.
     */
    public void stop(){
        moveAmount = 0;
    }

    /**
     * this method is used to get the shape of the paddle.
     *
     * @return it returns the shape of the paddle.
     */
    public Shape getPlayerFace(){
        return  playerFace;
    }

    /**
     * this method is used to set the paddle (player) and the ball point (center) to the starting position.
     *
     * @param p this is the Point position where the ball is to be set.
     */
    public void resetPosition(Point p){
        ballPoint.setLocation(p);
        playerFace.setLocation(ballPoint.x - (int)playerFace.getWidth()/2,ballPoint.y);
    }
}
