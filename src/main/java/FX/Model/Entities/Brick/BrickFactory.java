package FX.Model.Entities.Brick;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

/**
 * this class is used to return the brick object used on the wall levels.
 */
public class BrickFactory {

    /**
     * this is the indicator on which brick is getting referenced. (Clay brick)
     */
    public static final int CLAY = 1;
    /**
     * this is the indicator on which brick is getting referenced. (Steel brick)
     */
    public static final int STEEL = 2;
    /**
     * this is the indicator on which brick is getting referenced. (Cement brick)
     */
    public static final int CEMENT = 3;
    /**
     * this is the indicator on which brick is getting referenced. (Reinforced steel brick)
     */
    public static final int REINFORCED_STEEL = 4;
    /**
     * this is returns an array of bricks which is used to be ease of expandability when a new brick is added.
     */
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
