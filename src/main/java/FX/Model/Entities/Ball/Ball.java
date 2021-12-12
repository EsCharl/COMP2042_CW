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

package FX.Model.Entities.Ball;

import FX.Model.Entities.Brick.Crackable;
import FX.Model.Entities.Entities;
import FX.Model.Entities.Movable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * this is an abstract class which is used for the rubber ball implementation.
 */
abstract public class Ball extends Entities implements Movable {

    private final int MAX_BALL_SPEED = 4;
    private final int MIN_BALL_SPEED = 2;

    private int speedX;
    private int speedY;
    private int radius;

    private Random rnd;

    /**
     * this is the constructor used to create a ball object.
     *
     * @param centerPosition this is the position where the ball is to be formed on the top left of the ball.
     * @param radius this is the diameter of the ball based on x-axis
     * @param inner this is the Color for the inside of the ball.
     * @param border this is the Color for the border of the ball.
     */
    public Ball(Point2D centerPosition,int radius,Color inner,Color border){
        super(centerPosition, border,inner, 2*radius, 2*radius);

        setRnd(new Random());
        setRadius(radius);
        setRandomBallSpeed();
    }

    /**
     * this method is used to set the random speed on both x-axis and y-axis for the ball.
     */
    public void setRandomBallSpeed(){
        do {
            setSpeedX(getRnd().nextBoolean() ? getRnd().nextInt(getMAX_BALL_SPEED()) : -getRnd().nextInt(getMAX_BALL_SPEED()));
        } while (getSpeedX() == 0);
        do{
            setSpeedY(-getRnd().nextInt(getMAX_BALL_SPEED()));
        }while(getSpeedY() == 0);
    }

    /**
     * this method is used to move the ball (changing the variables to record where the ball is).
     */
    public void move(){
        setBounds(new BoundingBox(getBounds().getMinX() + getSpeedX(), getBounds().getMinY() + getSpeedY(),2*getRadius(),2*getRadius()));
    }


    /**
     * this method is used to get the random object which is used to randomly generate the speed for the ball.
     *
     * @return this returns a random object.
     */
    public Random getRnd() {
        return rnd;
    }

    /**
     * this method is used to set the random object into a variable for future reference.
     *
     * @param rnd this is the random object used to set into a variable.
     */
    public void setRnd(Random rnd) {
        this.rnd = rnd;
    }

    /**
     * this is used to set the speed of the ball on the x-axis.
     *
     * @param s speed on the x-axis.
     */
    public void setSpeedX(int s){
        this.speedX = s;
    }

    /**
     * this is used to set the speed of the ball on the y-axis.
     *
     * @param s speed on the y-axis.
     */
    public void setSpeedY(int s){
        this.speedY = s;
    }

    /**
     * this method is used to get the speed of the ball on the x-axis.
     *
     * @return an integer of the speed of the ball on the x-axis.
     */
    public int getSpeedX(){
        return this.speedX;
    }

    /**
     * this methods is used to get the speed of the ball on the y-axis.
     *
     * @return an integer of the speed of the ball on the y-axis.
     */
    public int getSpeedY(){
        return this.speedY;
    }

    /**
     * this method is used to get the radius of the ball used for drawing the border of the ball.
     *
     * @return this returns the radius of the ball.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * this method is used to set the radius of the ball.
     *
     * @param radius this is the integer used to set the radius of the ball.
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * this method is used to get the maximum speed of the ball in either direction (x-axis and y-axis).
     *
     * @return this returns the maximum allowed speed of the ball in either direction.
     */
    public int getMAX_BALL_SPEED() {
        return MAX_BALL_SPEED;
    }

    /**
     * this method is used to get the minimum speed for the ball in either direction (x-axis and y-axis).
     *
     * @return this returns the minimum allowed speed for the ball in either direction.
     */
    public int getMIN_BALL_SPEED() {
        return MIN_BALL_SPEED;
    }

    /**
     * this method is used to change the direction of the ball to another direction on the x-axis with an increment, decrement or maintain of speed if it is being collided on the left side of the ball.
     */
    public void ballLeftCollision() {
        setSpeedX(-getSpeedX());
        if(getRnd().nextBoolean() && getSpeedX() > getMIN_BALL_SPEED())
            setSpeedX(getSpeedX()-1);
        else if(getRnd().nextBoolean() && getSpeedX() < getMAX_BALL_SPEED())
            setSpeedX(getSpeedX()+1);
    }

    /**
     * this method is used to change the direction of the ball to another direction on the x-axis with an increment, decrement or maintain of speed if it is being collided on the right side of the ball.
     */
    public void ballRightCollision() {
        setSpeedX(-getSpeedX());
        if(getRnd().nextBoolean() && getSpeedX() < -getMIN_BALL_SPEED())
            setSpeedX(getSpeedX()+1);
        else if(getRnd().nextBoolean() && (getSpeedX() > -getMAX_BALL_SPEED()))
                setSpeedX(getSpeedX()-1);
    }

    /**
     * this method is used to change the direction of the ball to another direction on the y-axis with an increment, decrement or maintain of speed if it is being collided on the top side of the ball.
     */
    public void ballTopCollision() {
        setSpeedY(-getSpeedY());
        if(getRnd().nextBoolean() && getSpeedY() > getMIN_BALL_SPEED())
            setSpeedY(getSpeedY()-1);
        else if(getRnd().nextBoolean() && getSpeedY() < getMAX_BALL_SPEED())
            setSpeedY(getSpeedY()+1);
    }

    /**
     * this method is used to change the direction of the ball to another direction on the y-axis with an increment, decrement or maintain of speed if it is being collided on the bottom side of the ball.
     */
    public void ballBottomCollision() {
        setSpeedY(-getSpeedY());
        if (getRnd().nextBoolean() && getSpeedY() < -getMIN_BALL_SPEED())
            setSpeedY(getSpeedY() + 1);
        else if (getRnd().nextBoolean() && getSpeedY() > -getMAX_BALL_SPEED())
            setSpeedY(getSpeedY() - 1);
    }

    /**
     * this method is used to check if there is a collision between the game Bounds and the ball.
     *
     * @param gameWindow this is the Bounds of the game.
     * @return this returns a true if it collides with any of the side of the game bounds
     */
    public boolean gameWindowCollision(Bounds gameWindow) {
        boolean top = getBounds().getMinY() <= gameWindow.getMinY();
        boolean right = getBounds().getMaxX() >= gameWindow.getMaxX();
        boolean left = getBounds().getMinX() <= gameWindow.getMinX();
        boolean bottom = getBounds().getMaxY() >= gameWindow.getMaxY();
        boolean collide = top || right || left || bottom;

        if(top){
            if(getBounds().getMinY() < gameWindow.getMinY()) {
                ballTopCollision();
            }
        }
        if(right){
            if (getSpeedX() > 0) {
                ballRightCollision();
            }
        }else if(left){
            if (getSpeedX() < 0) {
                ballLeftCollision();
            }
        }
        return collide;
    }

    /**
     * this method change the ball direction and also returns true if the ball comes in contact with any side of the entity.
     *
     * @return returns a boolean value if it collides with a brick which is not broken after the collision.
     */
    public boolean impactEntity(Entities entity){
            if(entity.getBounds().contains(getBounds().getMinX()+ getBounds().getWidth()/2, getBounds().getMaxY())){
                ballBottomCollision();
                if(entity.getClass() == BallClone.class)
                    ((BallClone) entity).ballTopCollision();
                return entity.setImpact(new Point2D(getBounds().getMinX() + getBounds().getWidth()/2, getBounds().getMaxY()), Crackable.UP);
            }
            else if (entity.getBounds().contains(getBounds().getMinX()+ getBounds().getWidth()/2, getBounds().getMinY())){
                ballTopCollision();
                if(entity.getClass() == BallClone.class)
                    ((BallClone) entity).ballBottomCollision();
                return entity.setImpact(new Point2D(getBounds().getMinX() + getBounds().getWidth() / 2, getBounds().getMinY()), Crackable.DOWN);
            }
            else if(entity.getBounds().contains(getBounds().getMaxX(), getBounds().getMinY()+ getBounds().getHeight()/2)){
                ballLeftCollision();
                if(entity.getClass() == BallClone.class)
                    ((BallClone) entity).ballRightCollision();
                return entity.setImpact(new Point2D(getBounds().getMaxX(), getBounds().getMinY() + getBounds().getHeight() / 2), Crackable.RIGHT);
            }
            else if(entity.getBounds().contains(getBounds().getMinX(), getBounds().getMinY()+ getBounds().getHeight()/2)){
                ballRightCollision();
                if(entity.getClass() == BallClone.class)
                    ((BallClone) entity).ballLeftCollision();
                return entity.setImpact(new Point2D(getBounds().getMinX(), getBounds().getMinY() + getBounds().getHeight() / 2), Crackable.LEFT);
            }
        return false;
    }
}
