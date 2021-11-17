import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

public class ReinforcedSteelBrick extends Brick{
    private static final String NAME = "Reinforced Steel Brick";
    private static final Color DEF_INNER = Color.GRAY;
    private static final Color DEF_BORDER = new Color(203, 203, 201);
    private static final int REINFORCED_STEEL_STRENGTH = 2;
    private static final double STEEL_PROBABILITY = 0.5;

    private Crack crack;
    private Random rnd = new Random();
    private Shape brickFace;
    /**
     * this method is used to create a reinforced steel brick object.
     *
     * @param point this is the point where the steel brick is created.
     * @param size  this is for the size of the brick
     */
    public ReinforcedSteelBrick(Point point, Dimension size) {
        super(NAME,point,size,DEF_BORDER,DEF_INNER,REINFORCED_STEEL_STRENGTH);
        crack = new Crack(DEF_CRACK_DEPTH,DEF_STEPS);
        brickFace = super.brickFace;
    }

    /**
     * this method is used to determine if the brick can be broken based on a probability.
     */
    @Override
    public void impact(){
        if(rnd.nextDouble() < STEEL_PROBABILITY){
            super.impact();
        }
    }

    /**
     * this method is used to update the status of the brick on the screen.
     */
    private void updateBrick(){
        if(!super.isBroken()){
            GeneralPath gp = crack.draw();
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
}
