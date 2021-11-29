package Model.Levels;

import Model.Brick.Brick;

import java.awt.*;

/**
 * this interface is used to create multiple wall levels.
 */
public interface WallLevelTemplates {

    /**
     * this abstraction method is used to be implemented in the child classes. for the wall levels.
     *
     * @param drawArea this is the area which the bricks could be placed
     * @param brickCount this is the amount of bricks which will be in for the level.
     * @param lineCount this is the number of rows of bricks for the level.
     * @param brickSizeRatio this is the size ratio of the brick.
     * @param typeA this is one of the type of brick used for this level.
     * @param typeB this is one of the type of brick used for this level.
     * @return it returns the bricks for the wall (level) in the form of a brick array.
     */
    Brick[] level(Rectangle drawArea, int brickCount, int lineCount, double brickSizeRatio, int typeA, int typeB);

}
