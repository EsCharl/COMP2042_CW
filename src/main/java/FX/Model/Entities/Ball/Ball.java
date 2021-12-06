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
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * this is an abstract class which is used for the rubber ball implementation.
 */
abstract public class Ball extends Entities implements Movable {

    private final int MAX_BALL_SPEED = 5;

    private Point2D startTopLeftPosition;

    private Random rnd;

    private Color borderBallColor;
    private Color innerBallColor;

    private int xSpeed;
    private int ySpeed;
    private int radius;

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
        setStartTopLeftPosition(centerPosition);

        setRadius(radius);

        setRnd(new Random());
        setRandomBallSpeed();

        setBorderBallColor(border);
        setInnerBallColor(inner);
    }

    /**
     * this method is used to move the ball (changing the variables to record where the ball is).
     */
    public void move(){
//        setStartTopLeftPosition(getStartTopLeftPosition().add(getXSpeed(),getYSpeed()));
//        setUp(getUp().add(getXSpeed(),getYSpeed()));
//        setDown(getDown().add(getXSpeed(),getYSpeed()));
//        setLeft(getLeft().add(getXSpeed(),getYSpeed()));
//        setRight(getRight().add(getXSpeed(),getYSpeed()));
        setBounds(new BoundingBox(getBounds().getMinX() + getXSpeed(), getBounds().getMinY() + getYSpeed(),2*getRadius(),2*getRadius()));
    }

    /**
     * this method is used to set the speed of the ball on both axis.
     *
     * @param x the speed on the x-axis.
     * @param y the speed on the y-axis.
     */
    public void setSpeed(int x,int y){
        setXSpeed(x);
        setYSpeed(y);
    }

    /**
     * this method is used to set the random speed on both x-axis and y-axis for the ball.
     */
    public void setRandomBallSpeed(){
        int speedX,speedY;

        do {
            speedX = getRnd().nextBoolean() ? getRnd().nextInt(MAX_BALL_SPEED) : -getRnd().nextInt(MAX_BALL_SPEED);
        } while (speedX == 0);

        do{
            speedY = -getRnd().nextInt(MAX_BALL_SPEED);
        }while(speedY == 0);

        setSpeed(speedX,speedY);
    }

    /**
     * this method is used to get a random value set for the ball.
     *
     * @return this returns a random object for the random value for the ball speed.
     */
    public Random getRnd() {
        return rnd;
    }

    /**
     * this method is used to set a random object used to set into a variable for future reference.
     *
     * @param rnd this is a random object used to set into a variable.
     */
    public void setRnd(Random rnd) {
        this.rnd = rnd;
    }

    /**
     * this method is used to get the point of drawing where it is the top left of the ball.
     *
     * @return this returns the drawing position of the ball which is the top left of the ball.
     */
    public Point2D getStartTopLeftPosition() {
        return startTopLeftPosition;
    }

    /**
     * this is used to set the speed of the ball on the x-axis.
     *
     * @param s speed on the x-axis.
     */
    public void setXSpeed(int s){
        this.xSpeed = s;
    }

    /**
     * this is used to set the speed of the ball on the y-axis.
     *
     * @param s speed on the y-axis.
     */
    public void setYSpeed(int s){
        this.ySpeed = s;
    }

    /**
     * this is to get the color of the border of the ball.
     *
     * @return it returns a color that is the color of the border of the ball.
     */
    public Color getBorderBallColor(){
        return this.borderBallColor;
    }

    /**
     * this method is used to get the inner color of the ball.
     *
     * @return it returns a color that is the color of the in side of the ball.
     */
    public Color getInnerBallColor(){
        return this.innerBallColor;
    }

    /**
     * this method is used to get the speed of the ball on the x-axis.
     *
     * @return an integer of the speed of the ball on the x-axis.
     */
    public int getXSpeed(){
        return this.xSpeed;
    }

    /**
     * this methods is used to get the speed of the ball on the y-axis.
     *
     * @return an integer of the speed of the ball on the y-axis.
     */
    public int getYSpeed(){
        return this.ySpeed;
    }

    /**
     * this method is used to set the border color of the ball.
     *
     * @param border this is the color used to set the border of the ball.
     */
    public void setBorderBallColor(Color border) {
        this.borderBallColor = border;
    }

    /**
     * this method is used to set the inner color of the ball.
     *
     * @param inner this is the color used to set the inside of the ball.
     */
    public void setInnerBallColor(Color inner) {
        this.innerBallColor = inner;
    }

    /**
     * thie method is used to set the center position of the ball into a variable.
     *
     * @param startTopLeftPosition this is the Point2D value used to set into the variable.
     */
    public void setStartTopLeftPosition(Point2D startTopLeftPosition) {
        this.startTopLeftPosition = startTopLeftPosition;
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
}
