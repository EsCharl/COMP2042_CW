import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 * this class is used for the cement brick used for the walls.
 */
public class CementBrick extends Brick {


    private static final String NAME = "Cement Brick";
    private static final Color DEF_INNER = new Color(147, 147, 147);
    private static final Color DEF_BORDER = new Color(217, 199, 175);
    private static final int CEMENT_STRENGTH = 2;

    private Crack crack;
    private Shape brickFace;


    /**
     * this constructor is used to create a cement brick object.
     *
     * @param point
     * @param size
     */
    public CementBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CEMENT_STRENGTH);
        crack = new Crack(DEF_CRACK_DEPTH,DEF_STEPS);
        brickFace = super.brickFace;
    }

//    @Override
//    protected Shape makeBrickFace(Point pos, Dimension size) {
//        return new Rectangle(pos,size);
//    }

    /**
     * this method is used to determine whether the brick should be broken or draw a crack on the brick.
     *
     * @param point the point where the ball comes in contact to
     * @param dir the direction where the ball comes in contact with the object.
     * @return returns a boolean value negative if the brick is broken, true if it is not.
     */
    @Override
    public boolean setImpact(Point2D point, int dir) {
        if(super.isBroken())
            return false;
        super.impact();
        if(!super.isBroken()){
            crack.makeCrack(point,dir);
            updateBrick();
            return false;
        }
        return true;
    }

    /**
     * this method is used to get the graphic of the brick.
     *
     * @return the graphic of the brick
     */
    @Override
    public Shape getBrick() {
        return brickFace;
    }

    /**
     * this method is used to update the status of the brick on the screen.
     */
    private void updateBrick(){
        if(!super.isBroken()){
            GeneralPath gp = crack.getCrackPath();
            gp.append(super.brickFace,false);
            brickFace = gp;
        }
    }

    /**
     *this method is used to repair the brick.
     */
    @Override
    public void repair(){
        super.repair();
        crack.reset();
        brickFace = super.brickFace;
    }
}