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

import FX.Model.Entities.Entities;
import FX.Model.Entities.Movable;
import FX.Model.GameData;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * this is an abstract class which is used for the rubber ball implementation.
 */
abstract public class Ball extends Entities implements Movable {

    private final int MAX_BALL_SPEED = 4;
    private final int MIN_BALL_SPEED = 1;

    private int sppedX;
    private int speedY;
    private int radius;

    private Random rnd = new Random();

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

        setRadius(radius);
    }

    /**
     * this method is used to move the ball (changing the variables to record where the ball is).
     */
    public void move(){
        setBounds(new BoundingBox(getBounds().getMinX() + getSpeedX(), getBounds().getMinY() + getSpeedY(),2*getRadius(),2*getRadius()));
    }

    /**
     * this is used to set the speed of the ball on the x-axis.
     *
     * @param s speed on the x-axis.
     */
    public void setSpeedX(int s){
        this.sppedX = s;
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
        return this.sppedX;
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
        if(rnd.nextBoolean() && getSpeedX() < getMAX_BALL_SPEED()){
            setSpeedX(getSpeedX()+1);
        }else if(rnd.nextBoolean() && getSpeedX() > getMIN_BALL_SPEED()){
            setSpeedX(getSpeedX()-1);
        }
    }

    /**
     * this method is used to change the direction of the ball to another direction on the x-axis with an increment, decrement or maintain of speed if it is being collided on the right side of the ball.
     */
    public void ballRightCollision() {
        setSpeedX(-getSpeedX());
        if(rnd.nextBoolean() && (getSpeedX() > -getMAX_BALL_SPEED())){
            setSpeedX(getSpeedX()-1);
        }else if(rnd.nextBoolean() && getSpeedX() < -getMIN_BALL_SPEED()){
            setSpeedX(getSpeedX()+1);
        }
    }

    /**
     * this method is used to change the direction of the ball to another direction on the y-axis with an increment, decrement or maintain of speed if it is being collided on the top side of the ball.
     */
    public void ballTopCollision() {
        setSpeedY(-getSpeedY());
        if(rnd.nextBoolean() && getSpeedY() < getMAX_BALL_SPEED())
            setSpeedY(getSpeedY()+1);
        else if(rnd.nextBoolean() && getSpeedY() > getMIN_BALL_SPEED())
            setSpeedY(getSpeedY()-1);
    }

    /**
     * this method is used to change the direction of the ball to another direction on the y-axis with an increment, decrement or maintain of speed if it is being collided on the bottom side of the ball.
     */
    public void ballBottomCollision() {
        setSpeedY(-getSpeedY());
        if (rnd.nextBoolean() && getSpeedY() > -getMAX_BALL_SPEED()) {
            setSpeedY(getSpeedY() - 1);
        }else if (rnd.nextBoolean() && getSpeedY() < -getMIN_BALL_SPEED()) {
            setSpeedY(getSpeedY() + 1);
        }
    }
}
