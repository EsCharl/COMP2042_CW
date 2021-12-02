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

package Model.Ball;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

/**
 * this is an abstract class which is used for the rubber ball implementation.
 */
abstract public class Ball {

    private Shape ballFace;

    private Point2D centerPosition;

    private Point2D up;
    private Point2D down;
    private Point2D left;
    private Point2D right;

    private Color borderBallColor;
    private Color innerBallColor;

    private int xSpeed;
    private int ySpeed;

    /**
     * this is the constructor used to create a ball object.
     *
     * @param centerPosition this is the position where the ball is to be formed.
     * @param diameterA this is the diameter of the ball based on x-axis
     * @param diameterB this is the diameter of the ball based on y-axis.
     * @param inner this is the Color for the inside of the ball.
     * @param border this is the Color for the border of the ball.
     */
    public Ball(Point2D centerPosition,int diameterA,int diameterB,Color inner,Color border){
        setCenterPosition(centerPosition);

        createDirectionalPoint2D();

        setDirectionalPoints(diameterA, diameterB);

        setBallFace(makeBall(centerPosition,diameterA,diameterB));
        setColorOfBall(inner, border);
        setSpeed(0,0);
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
    private void createDirectionalPoint2D(){
        setUp(new Point2D.Double());
        setDown(new Point2D.Double());
        setLeft(new Point2D.Double());
        setRight(new Point2D.Double());
    }

    protected abstract Shape makeBall(Point2D center,int diameterA,int diameterB);

    /**
     * this method is used to move the ball.
     */
    public void move(){
        getCenterPosition().setLocation((getCenterPosition().getX() + getXSpeed()),(getCenterPosition().getY() + getYSpeed()));

        setDisplayBallFace();

        setDirectionalPoints(getRectangularShape().getWidth(),getRectangularShape().getHeight());
    }

    /**
     * this method is used to set the ball face graphically.
     */
    private void setDisplayBallFace(){
        getRectangularShape().setFrame((getCenterPosition().getX() -(getRectangularShape().getWidth() / 2)),(getCenterPosition().getY() - (getRectangularShape().getHeight() / 2)),getRectangularShape().getWidth(),getRectangularShape().getHeight());

        setBallFace(getRectangularShape());
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
     * this method is used to get the shape of the ball.
     *
     * @return it returns the shape of the ball
     */
    public Shape getBallFace(){
        return this.ballFace;
    }

    /**
     * this method is used to move the ball to the said location.
     *
     * @param p the position in Point format where the ball is going to.
     */
    public void moveTo(Point p){
        getCenterPosition().setLocation(p);

        setDisplayBallFace();
    }

    /**
     * this gets the Rectangle with the shape of the ball.
     *
     * @return returns a rectangle with the shape of the ball.
     */
    private RectangularShape getRectangularShape(){
        return (RectangularShape) getBallFace();
    }

    /**
     * this method is used to set the ball direction point location.
     *
     * @param width the width of the ball.
     * @param height the height of the ball.
     */
    private void setDirectionalPoints(double width,double height){
        getUp().setLocation(getCenterPosition().getX(), getCenterPosition().getY()-(height / 2));
        getDown().setLocation(getCenterPosition().getX(), getCenterPosition().getY()+(height / 2));

        getLeft().setLocation(getCenterPosition().getX()-(width / 2), getCenterPosition().getY());
        getRight().setLocation(getCenterPosition().getX()+(width / 2), getCenterPosition().getY());
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
     * this is used to set the shape of the ball.
     *
     * @param ballFace this is the shape used to set the ball shape.
     */
    public void setBallFace(Shape ballFace) {
        this.ballFace = ballFace;
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
}
