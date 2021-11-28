import java.awt.*;

/**
 * this abstract class is used to call methods which are used in multiple classes.
 */
public abstract class WallLevelAbstractions {
    public static final int CLAY = 1;
    public static final int STEEL = 2;
    public static final int CEMENT = 3;
    public static final int REINFORCED_STEEL = 4;

    /**
     * this method is used to create a brick array which is used to store the bricks for the level templates
     *
     * @param brickCount the amount of bricks that is going to be set for the array.
     * @return it returns a new empty brick array.
     */
    Brick[] createBrickArray(int brickCount){
        return new Brick[brickCount];
    }

    /**
     * this method is used to select and create the brick object needed for the level.
     *
     * @param point this is used get the position where the brick is supposed to be.
     * @param size this is for the size of the brick
     * @param type this is the type of brick to be used.
     * @return this returns the brick that is created.
     */
    Brick makeBrick(Point point, Dimension size, int type){
        switch(type){
            case(CLAY):
                return new ClayBrick(point, size);
            case(STEEL):
                return new SteelBrick(point, size);
            case(CEMENT):
                return new CementBrick(point, size);
            case(REINFORCED_STEEL):
                return new ReinforcedSteelBrick(point, size);
            default:
                throw new IllegalArgumentException(String.format("Unknown Type:%d\n",type));
        }
    }

    /**
     * this method is used to get the length of each brick that is going to be shown on the level.
     *
     * @param drawArea this is the draw area for the window.
     * @param lineCount this is the total amount of lines for the specific level.
     * @param brickCount this is the amount of bricks for the level.
     * @return it returns a double value for the length of the brick.
     */
    double getDrawBrickLength(Rectangle drawArea, int lineCount, int brickCount){
        return drawArea.getWidth() / getBrickOnLine(brickCount,lineCount);
    }

    /**
     * this is the method used to get the height of each brick that is going to be shown on the level.
     *
     * @param drawArea this is the draw area which the bricks are going to be drawn.
     * @param brickCount this is the amount of bricks that is going to be on that level.
     * @param lineCount this is the amount of lines that are going to be on that level.
     * @param brickSizeRatio this is the brick size ratio used to determine the height of the brick.
     * @return it returns a double value for the height of the brick.
     */
    double getDrawBrickHeight(Rectangle drawArea, int brickCount, int lineCount, double brickSizeRatio){
        return getDrawBrickLength(drawArea, lineCount, brickCount) / brickSizeRatio;
    }

    /**
     * this method is used to get the total amount of brick on a line. (for full horizontal lines)
     *
     * @param brickCount total amount of bricks.
     * @param lineCount total amount of lines.
     * @return returns an amount of bricks for a single line.
     */
    int getBrickOnLine(int brickCount, int lineCount){
        return brickCount/lineCount;
    }
}
