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

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

/**
 * this is an abstract class which is used for the rubber ball implementation.
 */
abstract public class Ball {

    private final int MAX_BALL_SPEED = 5;

    private Point2D centerPosition;

    private Point2D up;
    private Point2D down;
    private Point2D left;
    private Point2D right;

    private Random rnd;

    private Color borderBallColor;
    private Color innerBallColor;

    private int xSpeed;
    private int ySpeed;
    private int radius;

    /**
     * this is the constructor used to create a ball object.
     *
     * @param centerPosition this is the position where the ball is to be formed.
     * @param radius this is the diameter of the ball based on x-axis
     * @param inner this is the Color for the inside of the ball.
     * @param border this is the Color for the border of the ball.
     */
    public Ball(Point2D centerPosition,int radius,Color inner,Color border){
        setCenterPosition(centerPosition);

        setRadius(radius);

        setRnd(new Random());
        setRandomBallSpeed();

        createDirectionalPoint2D(radius);
        setColorOfBall(inner, border);
    }

    /**
     * This method is used to set the color of the ball.
     *
     * @param inner the inner color of the ball that is used to be set for the ball inner color.
     * @param border the border color of the ball that is used to be set for the ball border color.
     */
    private void setColorOfBall(Color inner,Color border){
        setBorderBallColor(border);
        setInnerBallColor(inner);
    }

    /**
     * this method is used to create the direction pointer2D objects.
     */
    private void createDirectionalPoint2D(int radius){
        setUp(new Point2D(0,radius));
        setDown(new Point2D(0,-radius));
        setLeft(new Point2D(-radius,0));
        setRight(new Point2D(radius,0));
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
     * this method is used to create a shape of the ball.
     *
     * @param center this is the position where the ball is formed
     * @param radius this is the radius of the ball
     * @return it returns a shape of the ball on a position specified in center.
     */
    protected Circle makeBall(Point2D center, int radius) {
        return new Circle(getRubberBallLeftXCoordinate(center, radius), getRubberBallTopYCoordinate(center, radius),radius);
    }

    /**
     * this method is used to get the left side of the rubber ball x - axis.
     *
     * @param center this is the point2D of the center of the ball.
     * @param horizontalDiameter this is the Horizontal diameter of the ball.
     * @return returns a double value which is the value of the x - coordinate for the left side of the ball.
     */
    private double getRubberBallLeftXCoordinate(Point2D center, int horizontalDiameter){
        return center.getX() - (horizontalDiameter / 2);
    }

    /**
     * this method is used to get the up side of the rubber ball y-axis.
     *
     * @param center this is the point2D of the center of the Ball.
     * @param verticalDiameter this is the vertical diameter of the ball.
     * @return this returns a double value where the y - coordinate for the top side of the ball.
     */
    private double getRubberBallTopYCoordinate(Point2D center, int verticalDiameter){
        return center.getY() - (verticalDiameter / 2);
    }

    /**
     * this method is used to set the random speed on both x-axis and y-axis for the ball.
     */
    public void setRandomBallSpeed(){
        int speedX,speedY;

        // changes here, makes the maximum speed it can go on x-axis in between -max speed and max speed.
        do {
            speedX = getRnd().nextBoolean() ? getRnd().nextInt(getMAX_BALL_SPEED()) : -getRnd().nextInt(getMAX_BALL_SPEED());
        } while (speedX == 0);

        do{
            speedY = -getRnd().nextInt(getMAX_BALL_SPEED());
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
     * this method is used to get the maximum speed that a ball can go in both axis.
     *
     * @return this returns a constant which is the maximum ball speed in each axis.
     */
    public int getMAX_BALL_SPEED() {
        return MAX_BALL_SPEED;
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
     * this method is used to reverse the direction where the ball is going on the x-axis.
     */
    public void reverseX(){
        setXSpeed(-this.getXSpeed());
    }

    /**
     * this method is used to reverse the direction where the ball is going on the y-axis.
     */
    public void reverseY(){
        setYSpeed(-this.getYSpeed());
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
     * this is used to get the position of the ball.
     *
     * @return it returns a Point2D format of the position of the ball.
     */
    public Point2D getCenterPosition(){
        return this.centerPosition;
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
     * this method is used to get the ball upwards position in point 2D.
     *
     * @return this returns a point2D object for the upside of the ball.
     */
    public Point2D getUp() {
        return up;
    }

    /**
     * this method is used to get the ball downwards position in point 2D.
     *
     * @return this returns a point2D object for the downside of the ball.
     */
    public Point2D getDown() {
        return down;
    }

    /**
     * this method is used to get the ball left position in point 2D.
     *
     * @return this returns a point2D object for the left side of the ball.
     */
    public Point2D getLeft() {
        return left;
    }

    /**
     * this method is used to get the ball right position in point 2D.
     *
     * @return this returns a point2D object for the right side of the ball.
     */
    public Point2D getRight() {
        return right;
    }

    /**
     * this method is used to set the up position of the ball into a variable.
     *
     * @param up this is the Point2D value used to set into the variable.
     */
    public void setUp(Point2D up) {
        this.up = up;
    }

    /**
     * this method is used to set the down position of the ball into a variable.
     *
     * @param down this is the Point2D value used to set into the variable.
     */
    public void setDown(Point2D down) {
        this.down = down;
    }

    /**
     * this method is used to set the left position of the ball into a variable.
     *
     * @param left this is the Point2D value used to set into the variable.
     */
    public void setLeft(Point2D left) {
        this.left = left;
    }

    /**
     * this method is used to set the right position of the ball into a variable.
     *
     * @param right this is the Point2D value used to set into the variable.
     */
    public void setRight(Point2D right) {
        this.right = right;
    }

    /**
     * thie method is used to set the center position of the ball into a variable.
     *
     * @param centerPosition this is the Point2D value used to set into the variable.
     */
    public void setCenterPosition(Point2D centerPosition) {
        this.centerPosition = centerPosition;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
