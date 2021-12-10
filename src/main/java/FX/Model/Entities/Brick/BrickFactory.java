package FX.Model.Entities.Brick;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

/**
 * this class is used to return the brick object used on the wall levels.
 */
public class BrickFactory {

    public static final int CLAY = 1;
    public static final int STEEL = 2;
    public static final int CEMENT = 3;
    public static final int REINFORCED_STEEL = 4;
    public static final int[] brickTypes = new int[]{CLAY,STEEL,CEMENT,REINFORCED_STEEL};

    /**
     * this method is used to select and create the brick object needed for the level.
     *
     * @param point this is used get the position where the brick is supposed to be.
     * @param size this is for the size of the brick
     * @param type this is the type of brick to be used.
     * @return this returns the brick that is created.
     */
    public Brick makeBrick(Point2D point, Dimension2D size, int type){
        return switch (type) {
            case (CLAY) -> new ClayBrick(point, size);
            case (STEEL) -> new SteelBrick(point, size);
            case (CEMENT) -> new CementBrick(point, size);
            case (REINFORCED_STEEL) -> new ReinforcedSteelBrick(point, size);
            default -> throw new IllegalArgumentException(String.format("Unknown Type:%d\n", type));
        };
    }
}
