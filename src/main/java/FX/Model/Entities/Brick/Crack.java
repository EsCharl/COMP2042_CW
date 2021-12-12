package FX.Model.Entities.Brick;

import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.Random;

/**
 * this class is used to make a crack path for the brick.
 */
public class Crack {

    private Random rnd;
    private static Crack uniqueCrack;

    private int DEF_CRACK_DEPTH = 1;
    private int DEF_STEPS = 35;

    private final int VERTICAL = 100;
    private final int HORIZONTAL = 200;

    /**
     * this method is used to create a one and only crack object and return it. based on singleton design pattern
     *
     * @return this returns a one and only crack class object.
     */
    public static Crack singletonCrack(){
        if(getUniqueCrack() == null){
            setUniqueCrack(new Crack());
        }
        return getUniqueCrack();
    }

    private Crack(){
        setRnd(new Random());
    }

    /**
     * This method is used to calculate and determine where to draw the crack to based on the direction provided.
     *
     * @param point the point where the ball comes in contact with.
     * @param direction the direction where the ball touch the brick.
     * @param brick this is the brick where it will get the boundary of the brick to set the position of the opposite edge of collision
     */
    public void prepareCrack(Point2D point, int direction, Brick brick){

        Point2D oppositeSideOfCollisionCornerPoint1;
        Point2D oppositeSideOfCollisionCornerPoint2;

        switch (direction) {
            case Crackable.LEFT -> {
                oppositeSideOfCollisionCornerPoint1 = new Point2D(brick.getBounds().getMinX() + brick.getBounds().getWidth(), brick.getBounds().getMinY());
                oppositeSideOfCollisionCornerPoint2 = new Point2D(brick.getBounds().getMinX() + brick.getBounds().getWidth(), brick.getBounds().getMinY() + brick.getBounds().getHeight());
                makeCrack(point, makeRandomPointBetween(oppositeSideOfCollisionCornerPoint1, oppositeSideOfCollisionCornerPoint2, getVERTICAL()), brick);
            }
            case Crackable.RIGHT -> {
                oppositeSideOfCollisionCornerPoint1 = new Point2D(brick.getBounds().getMinX(), brick.getBounds().getMinY());
                oppositeSideOfCollisionCornerPoint2 = new Point2D(brick.getBounds().getMinX(), brick.getBounds().getMinY() + brick.getBounds().getHeight());
                makeCrack(point, makeRandomPointBetween(oppositeSideOfCollisionCornerPoint1, oppositeSideOfCollisionCornerPoint2, getVERTICAL()), brick);
            }
            case Crackable.UP -> {
                oppositeSideOfCollisionCornerPoint1 = new Point2D(brick.getBounds().getMinX(), brick.getBounds().getMinY() + brick.getBounds().getHeight());
                oppositeSideOfCollisionCornerPoint2 = new Point2D(brick.getBounds().getMinX() + brick.getBounds().getWidth(), brick.getBounds().getMinY() + brick.getBounds().getHeight());
                makeCrack(point, makeRandomPointBetween(oppositeSideOfCollisionCornerPoint1, oppositeSideOfCollisionCornerPoint2, getHORIZONTAL()), brick);
            }
            case Crackable.DOWN -> {
                oppositeSideOfCollisionCornerPoint1 = new Point2D(brick.getBounds().getMinX(), brick.getBounds().getMinY());
                oppositeSideOfCollisionCornerPoint2 = new Point2D(brick.getBounds().getMinX() + brick.getBounds().getWidth(), brick.getBounds().getMinY());
                makeCrack(point, makeRandomPointBetween(oppositeSideOfCollisionCornerPoint1, oppositeSideOfCollisionCornerPoint2, getHORIZONTAL()), brick);
            }
        }
    }

    /**
     * this method is used to make the crack path.
     *
     * @param start this is the start point where the crack is going to start.
     * @param end this is the end point where the crack is going to end.
     * @param brick this is the brick where the crack path will be saved to.
     */
    //method name change
    public void makeCrack(Point2D start, Point2D end, Brick brick){

        Path path = new Path();

        MoveTo firstPoint = new MoveTo(start.getX(), start.getY());

        path.getElements().add(firstPoint);

        double x,y;

        for(int i = 1; i < DEF_STEPS;i++){

            x = (i * ((end.getX() - start.getX()) / (double) DEF_STEPS)) + start.getX();
            y = (i * ((end.getY() - start.getY()) / (double) DEF_STEPS)) + start.getY() + getRnd().nextInt((DEF_CRACK_DEPTH * 2) + 1) - DEF_CRACK_DEPTH;

            path.getElements().add(new LineTo(x,y));
        }

        path.getElements().add(new LineTo(end.getX(),end.getY()));

        if (brick instanceof Crackable)
            ((Crackable) brick).setCrackPath(path);
    }

    /**
     * this method is used to create a random point based on the direction provided.
     *
     * @param oppositeOfCollisionCornerPoint1 this is the position where it begins.
     * @param oppositeOfCollisionPoint2 this is the position where it ends.
     * @param direction the direction using an integer constant.
     * @return it returns a random point (coordinate) on the brick.
     */
    public Point2D makeRandomPointBetween(Point2D oppositeOfCollisionCornerPoint1, Point2D oppositeOfCollisionPoint2, int direction){

        int position;

        switch (direction){
            case HORIZONTAL:
                position = getRnd().nextInt((int)(oppositeOfCollisionPoint2.getX() - oppositeOfCollisionCornerPoint1.getX())) + (int)oppositeOfCollisionCornerPoint1.getX();
                return new Point2D(position, oppositeOfCollisionPoint2.getY());
            case VERTICAL:
                position = getRnd().nextInt((int)(oppositeOfCollisionPoint2.getY() - oppositeOfCollisionCornerPoint1.getY())) + (int)oppositeOfCollisionCornerPoint1.getY();
                return new Point2D(oppositeOfCollisionPoint2.getX(),position);
            default:
                return new Point2D(0,0);
        }
    }

    /**
     * this method is used to get the crack object used for singleton design.
     *
     * @return this returns a crack object.
     */
    private static Crack getUniqueCrack() {
        return uniqueCrack;
    }

    /**
     * this method is used to set a one and only crack object into a variable for future reference.
     *
     * @param uniqueCrack this is the one and only crack object set into a variable for future reference.
     */
    private static void setUniqueCrack(Crack uniqueCrack) {
        Crack.uniqueCrack = uniqueCrack;
    }

    /**
     * this method is used to get the crack object used to have a randomness on the crack path.
     *
     * @return this returns a random object.
     */
    public Random getRnd() {
        return rnd;
    }

    /**
     * this method is used to set a random object into a variable for future reference.
     *
     * @param rnd this is the random object used to set into a variable.
     */
    public void setRnd(Random rnd) {
        this.rnd = rnd;
    }

    /**
     * this method is used to get a constant which is an integer constant for the vertical direction.
     *
     * @return this returns a constant to indicate the direction axis.
     */
    public int getVERTICAL() {
        return VERTICAL;
    }

    /**
     * this method is used to get a constant which is an integer constant for the horizontal direction.
     *
     * @return this returns a constant to indicate the direction axis.
     */
    public int getHORIZONTAL() {
        return HORIZONTAL;
    }
}
