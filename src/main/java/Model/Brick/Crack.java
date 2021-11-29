package Model.Brick;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * This class is used for the bricks on the level to display the crack if they are not destroyed.
 */
public class Crack{

    public static final int LEFT = 10;
    public static final int RIGHT = 20;
    public static final int UP = 30;
    public static final int DOWN = 40;

    private int VERTICAL = 100;
    private int HORIZONTAL = 200;

    private GeneralPath crackPath;

    private int crackDepth;
    private int steps;

    private static Random rnd = new Random();

    /**
     * this is the constructor used to create the crack object.
     *
     * @param crackDepth this is the depth of crack which is going to be initialized.
     * @param steps this is the step variable which is going to be initialized.
     */
    public Crack(int crackDepth, int steps){

        crackPath = new GeneralPath();
        setCrackDepth(crackDepth);
        setSteps(steps);
    }

    /**
     * this method is used to get the Model.Brick.Crack Path properties.
     *
     * @return a GeneralPath that is the path
     */
    public GeneralPath getCrackPath(){

        return crackPath;
    }

    /**
     * this method is used to reset the crack path.
     */
    public void reset(){
        crackPath.reset();
    }

    /**
     * This method is used to calculate and determine where to draw the crack.
     *
     * @param point the point where the ball comes in contact with.
     * @param direction the direction where the ball touch the brick.
     */
    protected void makeCrack(Point2D point, int direction, Brick brick){

        Point oppositeSideOfCollisionCornerPoint1 = new Point();
        Point oppositeSideOfCollisionCornerPoint2 = new Point();

        if(direction == getLEFT()) {
            oppositeSideOfCollisionCornerPoint1.setLocation(getBounds(brick).x + getBounds(brick).width, getBounds(brick).y);
            oppositeSideOfCollisionCornerPoint2.setLocation(getBounds(brick).x + getBounds(brick).width, getBounds(brick).y + getBounds(brick).height);
            drawCrack(makeImpactPoint(point), getEndRandomPointVertical(oppositeSideOfCollisionCornerPoint1, oppositeSideOfCollisionCornerPoint2));
        }else if(direction == getRIGHT()){
            oppositeSideOfCollisionCornerPoint1.setLocation(getBounds(brick).getLocation());
            oppositeSideOfCollisionCornerPoint2.setLocation(getBounds(brick).x, getBounds(brick).y + getBounds(brick).height);
            drawCrack(makeImpactPoint(point),getEndRandomPointVertical(oppositeSideOfCollisionCornerPoint1,oppositeSideOfCollisionCornerPoint2));
        }else if(direction == getUP()){
            oppositeSideOfCollisionCornerPoint1.setLocation(getBounds(brick).x, getBounds(brick).y + getBounds(brick).height);
            oppositeSideOfCollisionCornerPoint2.setLocation(getBounds(brick).x + getBounds(brick).width, getBounds(brick).y + getBounds(brick).height);
            drawCrack(makeImpactPoint(point),getEndRandomPointHorizontal(oppositeSideOfCollisionCornerPoint1,oppositeSideOfCollisionCornerPoint2));
        }else if(direction == getDOWN()){
            oppositeSideOfCollisionCornerPoint1.setLocation(getBounds(brick).getLocation());
            oppositeSideOfCollisionCornerPoint2.setLocation(getBounds(brick).x + getBounds(brick).width, getBounds(brick).y);
            drawCrack(makeImpactPoint(point),getEndRandomPointHorizontal(oppositeSideOfCollisionCornerPoint1,oppositeSideOfCollisionCornerPoint2));
        }
    }

    /**
     * this method is used to create a point on the impact position.
     *
     * @param point the point of impact caused by the ball colliding with the brick.
     * @return it returns a Point object which is based on the collision point.
     */
    private Point makeImpactPoint(Point2D point) {
        return new Point((int) point.getX(),(int) point.getY());
    }

    /**
     * this method is used to get a random point from a start point to the end point based on the direction it was given (horizontal).
     *
     * @param oppositeOfCollisionCornerPoint1 one of the point (position) corner of the opposite to the collision
     * @param oppositeOfCollisionCornerPoint2 the other point (position) corner of the opposite to the collision
     * @return it returns a random position between Point1 and Point2
     */
    private Point getEndRandomPointHorizontal(Point oppositeOfCollisionCornerPoint1, Point oppositeOfCollisionCornerPoint2){
        return makeRandomPointBetween(oppositeOfCollisionCornerPoint1,oppositeOfCollisionCornerPoint2,HORIZONTAL);
    }

    /**
     * this method is used to get a random point from a start point to the end point based on the direction it was given (vertical).
     *
     * @param oppositeOfCollisionCornerPoint1 one of the point (position) corner of the opposite to the collision
     * @param oppositeOfCollisionCornerPoint2 the other point (position) corner of the opposite to the collision
     * @return it returns a random position between Point1 and Point2
     */
    private Point getEndRandomPointVertical(Point oppositeOfCollisionCornerPoint1, Point oppositeOfCollisionCornerPoint2){
        return makeRandomPointBetween(oppositeOfCollisionCornerPoint1,oppositeOfCollisionCornerPoint2,VERTICAL);
    }

    /**
     * this method is used to get the brick boundaries.
     *
     * @param brick the brick object.
     * @return it returns a Rectangle object which is the boundary of the brick.
     */
    private Rectangle getBounds(Brick brick){
        return brick.getBrick().getBounds();
    }

    /**
     * this method is used to draw the crack.
     *
     * @param start this is the start point where the crack is going to start.
     * @param end this is the end point where the crack is going to end.
     */
    //method name change
    protected void drawCrack(Point start, Point end){

        GeneralPath path = new GeneralPath();

        path.moveTo(start.x,start.y);

        int bound = getCrackDepth();

        double x,y;

        for(int i = 1; i < getSteps();i++){

            x = (i * getBrickWidth(start, end)) + start.x;
            y = (i * getPartOfTheBrickHeight(start, end)) + start.y + randomInBounds(bound);

            path.lineTo(x,y);

        }

        path.lineTo(end.x,end.y);
        getCrackPath().append(path,true);
    }

    /**
     * this method is used to get the height of the brick.
     *
     * @param start the start position of the brick a point of collision on the horizontal part of the brick.
     * @param end the end position of the brick a point on the opposite side of the brick.
     * @return it returns a part of the brick based on the total crack steps.
     */
    private double getPartOfTheBrickHeight(Point start, Point end) {
        return (end.y - start.y) / (double)getSteps();
    }

    /**
     * this method is used to get the width of the brick.
     *
     * @param start the start position of the brick a point of collision on the vertical part of the brick.
     * @param end the end position of the brick a point on the opposite side of the brick.
     * @return it returns a part of the brick based on the total crack steps.
     */
    private double getBrickWidth(Point start, Point end) {
        return (end.x - start.x) / (double)getSteps();
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
    private Point makeRandomPointBetween(Point oppositeOfCollisionCornerPoint1, Point oppositeOfCollisionPoint2, int direction){

        Point out = new Point();
        int position;

        if(direction == getHORIZONTAL()) {
            position = rnd.nextInt(oppositeOfCollisionPoint2.x - oppositeOfCollisionCornerPoint1.x) + oppositeOfCollisionCornerPoint1.x;
            out.setLocation(position, oppositeOfCollisionPoint2.y);
        }else if(direction == getVERTICAL()){
            position = rnd.nextInt(oppositeOfCollisionPoint2.y - oppositeOfCollisionCornerPoint1.y) + oppositeOfCollisionCornerPoint1.y;
            out.setLocation(oppositeOfCollisionPoint2.x,position);
        }
        return out;
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

    public int getLEFT() {
        return LEFT;
    }

    public int getRIGHT() {
        return RIGHT;
    }

    public int getUP() {
        return UP;
    }

    public int getDOWN() {
        return DOWN;
    }

    public int getVERTICAL() {
        return VERTICAL;
    }

    public int getHORIZONTAL() {
        return HORIZONTAL;
    }
}

