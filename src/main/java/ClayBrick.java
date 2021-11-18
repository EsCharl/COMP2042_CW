import java.awt.*;
import java.awt.Point;

/**
 * this class is for the clay brick for the wall.
 */
public class ClayBrick extends Brick {

    private static final String NAME = "Clay Brick";
    private static final Color DEF_INNER = new Color(178, 34, 34).darker();
    private static final Color DEF_BORDER = Color.GRAY;
    private static final int CLAY_STRENGTH = 1;


    /**
     * this constructor is used to create a clay brick object.
     *
     * @param point the position where the brick is going to be formed.
     * @param size the size of the brick.
     */
    public ClayBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CLAY_STRENGTH);
    }

}
