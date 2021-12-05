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

package FX.Model.Entities.Brick;

import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.Random;

/**
 * This class is used for the bricks on the level to display the crack if they are not destroyed.
 */
public class Crack{

    public static final int LEFT = 10;
    public static final int RIGHT = 20;
    public static final int UP = 30;
    public static final int DOWN = 40;

    private final int VERTICAL = 100;
    private final int HORIZONTAL = 200;

    private Path crackPath;

    private int crackDepth;
    private int steps;

    private final static Random rnd = new Random();

    /**
     * this is the constructor used to create the crack object.
     *
     * @param crackDepth this is the depth of crack which is going to be initialized.
     * @param steps this is the step variable which is going to be initialized.
     */
    public Crack(int crackDepth, int steps){

        crackPath = new Path();
        setCrackDepth(crackDepth);
        setSteps(steps);
    }

    /**
     * this method is used to get the Model.Brick.Crack Path properties.
     *
     * @return a GeneralPath that is the path
     */
    public Path getCrackPath(){

        return crackPath;
    }

    /**
     * This method is used to calculate and determine where to draw the crack.
     *
     * @param point the point where the ball comes in contact with.
     * @param direction the direction where the ball touch the brick.
     */
    protected void makeCrack(Point2D point, int direction, Brick brick){

        Point2D oppositeSideOfCollisionCornerPoint1;
        Point2D oppositeSideOfCollisionCornerPoint2;

        if(direction == getLEFT()) {
            oppositeSideOfCollisionCornerPoint1 = new Point2D(brick.getBrick().getX() + brick.getBrick().getWidth(), brick.getBrick().getY());
            oppositeSideOfCollisionCornerPoint2 = new Point2D(brick.getBrick().getX() + brick.getBrick().getWidth(), brick.getBrick().getY() + brick.getBrick().getHeight());
            drawCrack(makeImpactPoint(point), getEndRandomPointVertical(oppositeSideOfCollisionCornerPoint1, oppositeSideOfCollisionCornerPoint2));
        }else if(direction == getRIGHT()){
            oppositeSideOfCollisionCornerPoint1 = new Point2D(brick.getBrick().getX(),brick.getBrick().getY());
            oppositeSideOfCollisionCornerPoint2 = new Point2D(brick.getBrick().getX(), brick.getBrick().getY() + brick.getBrick().getHeight());
            drawCrack(makeImpactPoint(point),getEndRandomPointVertical(oppositeSideOfCollisionCornerPoint1,oppositeSideOfCollisionCornerPoint2));
        }else if(direction == getUP()){

            oppositeSideOfCollisionCornerPoint1 = new Point2D(brick.getBrick().getX(), brick.getBrick().getY() + brick.getBrick().getHeight());
            oppositeSideOfCollisionCornerPoint2 = new Point2D(brick.getBrick().getX() + brick.getBrick().getWidth(), brick.getBrick().getY() + brick.getBrick().getHeight());
            drawCrack(makeImpactPoint(point),getEndRandomPointHorizontal(oppositeSideOfCollisionCornerPoint1,oppositeSideOfCollisionCornerPoint2));
        }else if(direction == getDOWN()){
            oppositeSideOfCollisionCornerPoint1 = new Point2D(brick.getBrick().getX(),brick.getBrick().getY());
            oppositeSideOfCollisionCornerPoint2 = new Point2D(brick.getBrick().getX() + brick.getBrick().getWidth(), brick.getBrick().getY());
            drawCrack(makeImpactPoint(point),getEndRandomPointHorizontal(oppositeSideOfCollisionCornerPoint1,oppositeSideOfCollisionCornerPoint2));
        }
    }

    /**
     * this method is used to create a point on the impact position.
     *
     * @param point the point of impact caused by the ball colliding with the brick.
     * @return it returns a Point object which is based on the collision point.
     */
    private Point2D makeImpactPoint(Point2D point) {
        return new Point2D((int) point.getX(),(int) point.getY());
    }

    /**
     * this method is used to get a random point from a start point to the end point based on the direction it was given (horizontal).
     *
     * @param oppositeOfCollisionCornerPoint1 one of the point (position) corner of the opposite to the collision
     * @param oppositeOfCollisionCornerPoint2 the other point (position) corner of the opposite to the collision
     * @return it returns a random position between Point1 and Point2
     */
    private Point2D getEndRandomPointHorizontal(Point2D oppositeOfCollisionCornerPoint1, Point2D oppositeOfCollisionCornerPoint2){
        return makeRandomPointBetween(oppositeOfCollisionCornerPoint1,oppositeOfCollisionCornerPoint2,getHORIZONTAL());
    }

    /**
     * this method is used to get a random point from a start point to the end point based on the direction it was given (vertical).
     *
     * @param oppositeOfCollisionCornerPoint1 one of the point (position) corner of the opposite to the collision
     * @param oppositeOfCollisionCornerPoint2 the other point (position) corner of the opposite to the collision
     * @return it returns a random position between Point1 and Point2
     */
    private Point2D getEndRandomPointVertical(Point2D oppositeOfCollisionCornerPoint1, Point2D oppositeOfCollisionCornerPoint2){
        return makeRandomPointBetween(oppositeOfCollisionCornerPoint1,oppositeOfCollisionCornerPoint2,getVERTICAL());
    }

    /**
     * this method is used to draw the crack.
     *
     * @param start this is the start point where the crack is going to start.
     * @param end this is the end point where the crack is going to end.
     */
    //method name change
    protected void drawCrack(Point2D start, Point2D end){

        Path path = new Path();

        MoveTo firstPoint = new MoveTo(start.getX(), start.getY());

        path.getElements().add(firstPoint);

        double x,y;

        for(int i = 1; i < getSteps();i++){

            x = (i * getBrickWidth(start, end)) + start.getY();
            y = (i * getPartOfTheBrickHeight(start, end)) + start.getY() + randomInBounds(getCrackDepth());

            path.getElements().add(new LineTo(x,y));
        }

        path.getElements().add(new LineTo(end.getX(),end.getY()));
    }

    /**
     * this method is used to get the height of the brick.
     *
     * @param start the start position of the brick a point of collision on the horizontal part of the brick.
     * @param end the end position of the brick a point on the opposite side of the brick.
     * @return it returns a part of the brick based on the total crack steps.
     */
    private double getPartOfTheBrickHeight(Point2D start, Point2D end) {
        return (end.getY() - start.getY()) / (double)getSteps();
    }

    /**
     * this method is used to get the width of the brick.
     *
     * @param start the start position of the brick a point of collision on the vertical part of the brick.
     * @param end the end position of the brick a point on the opposite side of the brick.
     * @return it returns a part of the brick based on the total crack steps.
     */
    private double getBrickWidth(Point2D start, Point2D end) {
        return (end.getX() - start.getX()) / (double)getSteps();
    }

    /**
     * this method is used to give a random value between negative bound and bound.
     *
     * @param bound the highest in both negative and positive value that it can go.
     * @return it returns a value between negative bound and bound.
     */
    private int randomInBounds(int bound){
        return rnd.nextInt((bound * 2) + 1) - bound;
    }

    /**
     * this method is used to create a random point based on the direction provided.
     *
     * @param oppositeOfCollisionCornerPoint1 this is the position where it begins.
     * @param oppositeOfCollisionPoint2 this is the position where it ends.
     * @param direction the direction using an integer constant.
     * @return it returns a random point (coordinate) on the brick.
     */
    private Point2D makeRandomPointBetween(Point2D oppositeOfCollisionCornerPoint1, Point2D oppositeOfCollisionPoint2, int direction){

        int position;

        if(direction == getHORIZONTAL()) {
            position = rnd.nextInt((int)(oppositeOfCollisionPoint2.getX() - oppositeOfCollisionCornerPoint1.getX())) + (int)oppositeOfCollisionCornerPoint1.getX();
            return new Point2D(position, oppositeOfCollisionPoint2.getY());
        }else if(direction == getVERTICAL()){
            position = rnd.nextInt((int)(oppositeOfCollisionPoint2.getY() - oppositeOfCollisionCornerPoint1.getY())) + (int)oppositeOfCollisionCornerPoint1.getY();
            return new Point2D(oppositeOfCollisionPoint2.getX(),position);
        }
        return new Point2D(0,0);
    }

    /**
     * this method is used to get the crackDepth.
     *
     * @return it returns a value which is the depth of the crack.
     */
    public int getCrackDepth() {
        return crackDepth;
    }

    /**
     * this method is used to set the depth of the crack.
     *
     * @param crackDepth this is the value used to set the depth of the crack.
     */
    public void setCrackDepth(int crackDepth) {
        this.crackDepth = crackDepth;
    }

    /**
     * this method is used to get how many steps for the crack to complete.
     *
     * @return it returns the amount of steps needed to complete the crack.
     */
    public int getSteps() {
        return steps;
    }

    /**
     * this method is used to set the total amount of steps needed for the crack to complete.
     *
     * @param steps this is the amount of steps that is needed to complete the crack.
     */
    public void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * this method is used to get a constant integer value for the left direction.
     *
     * @return this returns a constant integer value for the left direction.
     */
    public static int getLEFT() {
        return LEFT;
    }

    /**
     * this method is used to get a constant integer value for the right direction.
     *
     * @return this returns a constant integer value for the right direction.
     */
    public static int getRIGHT() {
        return RIGHT;
    }

    /**
     * this method is used to get a constant integer value for the up direction.
     *
     * @return this returns a constant integer value for the up direction.
     */
    public static int getUP() {
        return UP;
    }

    /**
     * this method is used to get a constant integer value for the down direction.
     *
     * @return this returns a constant integer value for the down direction.
     */
    public static int getDOWN() {
        return DOWN;
    }

    /**
     * this method is used to get a constant integer value for the vertical axis.
     *
     * @return this returns a constant integer value for the vertical axis.
     */
    public int getVERTICAL() {
        return VERTICAL;
    }

    /**
     * this method is used to get a constant integer value for the horizontal axis.
     *
     * @return this returns a constant integer value for the horizontal axis.
     */
    public int getHORIZONTAL() {
        return HORIZONTAL;
    }
}

