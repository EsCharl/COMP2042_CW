import java.awt.*;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * This class is an abstract class which is going to be used for implementation. (CementBrick, ClayBrick, SteelBrick, ReinforcedSteelBrick)
 */
abstract public class Brick  {

    public static final int DEF_CRACK_DEPTH = 1;
    public static final int DEF_STEPS = 35;

    public static final int UP_IMPACT = 100;
    public static final int DOWN_IMPACT = 200;
    public static final int LEFT_IMPACT = 300;
    public static final int RIGHT_IMPACT = 400;

    /**
     * This class is used for the bricks on the level to display the crack if they are not destroyed.
     */


    private static Random rnd;

    private String name;
    Shape brickFace;

    private Color border;
    private Color inner;

    private int fullStrength;
    private int strength;

    private boolean broken;


    /**
     * this method is used to create a brick object.
     *
     * @param pos the position of the brick
     * @param size the size of the break
     * @param border the color of the brick border
     * @param inner the inside color of brick
     * @param strength the strength of the brick. (how many hits can it take before it break)
     */
    public Brick(Point pos,Dimension size,Color border,Color inner,int strength){
        setRnd(new Random());
        setBroken(false);
        setBrickFace(makeBrickFace(pos,size));
        setBorderColor(border);
        setInnerColor(inner);
        setFullStrength(strength);
        setStrength(strength);
    }

    /**
     * this method is used to set the shape of the brick.
     *
     * @param pos the position where the shape of the brick is formulated.
     * @param size the size of the brick shape.
     * @return the shape of the brick and on that position.
     */
    protected Shape makeBrickFace(Point pos,Dimension size){
        return new Rectangle(pos,size);
    }

    /**
     * this method is used to determine if the brick is broken.
     *
     * @param point the point where the ball comes in contact to
     * @param dir the direction where the ball comes in contact with the object.
     * @return returns a boolean value negative if the brick is broken, true if it is not.
     */
    public boolean setImpact(Point2D point , int dir){
        if(isBroken())
            return false;
        impact();
        return  isBroken();
    }

    /**
     * this method is used to get the shape of the brick.
     *
     * @return it returns the shape of the brick.
     */
    public Shape getBrick(){
        return brickFace;
    }


    /**
     * this method is used to get the border color of the brick.
     *
     * @return returns the Color of the border of the brick.
     */
    public Color getBorderColor(){
        return  border;
    }

    /**
     * this method is used to get the internal color of he brick.
     *
     * @return returns the Color of the internal of the brick.
     */
    public Color getInnerColor(){
        return inner;
    }

    /**
     * this method is used to get the direction of impact where the ball comes in contact with the brick,
     *
     * @param b the ball object.
     * @return an Integer where the impact direction is decided.
     */
    public final int findImpact(Ball b){
        if(isBroken())
            return 0;
        int out  = 0;
        if(getBrickFace().contains(b.getRight()))
            out = LEFT_IMPACT;
        else if(getBrickFace().contains(b.getLeft()))
            out = RIGHT_IMPACT;
        else if(getBrickFace().contains(b.getUp()))
            out = DOWN_IMPACT;
        else if(getBrickFace().contains(b.getDown()))
            out = UP_IMPACT;
        return out;
    }

    /**
     * this method is used to return the brick if it is broken or not.
     *
     * @return returns a boolean value if the brick is broken or not.
     */
    public final boolean isBroken(){
        return broken;
    }

    /**
     * this method is used to repair the brick to full strength.
     */
    public void repair() {
        setBroken(false);
        setStrength(getFullStrength());
    }

    /**
     * this method is used to decrement the strength of the brick and update the broken variable if it is broken or not.
     */
    public void impact(){
        setStrength(getStrength()-1);
        setBroken(getStrength() == 0);
    }

    public static Random getRnd() {
        return rnd;
    }

    public static void setRnd(Random rnd) {
        Brick.rnd = rnd;
    }

    public Shape getBrickFace() {
        return brickFace;
    }

    public void setBrickFace(Shape brickFace) {
        this.brickFace = brickFace;
    }

    public void setBorderColor(Color border) {
        this.border = border;
    }

    public void setInnerColor(Color inner) {
        this.inner = inner;
    }

    public int getFullStrength() {
        return fullStrength;
    }

    public void setFullStrength(int fullStrength) {
        this.fullStrength = fullStrength;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }
}





