import java.awt.*;

/**
 * this class is for the steel brick which is used for the level.
 */
public class SteelBrick extends Brick {

    private static final Color DEF_INNER = new Color(203, 203, 201);
    private static final Color DEF_BORDER = Color.BLACK;
    private static final int STEEL_STRENGTH = 1;
    private static final double STEEL_PROBABILITY = 0.4;

    /**
     * this method is used to create a steel brick object.
     *
     * @param point this is the point where the steel brick is created.
     * @param size this is for the size of the brick
     */
    public SteelBrick(Point point, Dimension size){
        super(point,size,DEF_BORDER,DEF_INNER,STEEL_STRENGTH);
    }

    /**
     * this method is used to determine if the brick can be broken based on a probability.
     */
    @Override
    public void impacted(){
        if(getRnd().nextDouble() < STEEL_PROBABILITY){
            super.impacted();
        }
    }

}
