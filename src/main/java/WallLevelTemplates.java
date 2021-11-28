import java.awt.*;
import java.util.Random;

/**
 * this class has the walls (levels) templates used for the game.
 */
public interface WallLevelTemplates {

    Brick[] level(Rectangle drawArea, int brickCount, int lineCount, double brickSizeRatio, int typeA, int typeB);

}
