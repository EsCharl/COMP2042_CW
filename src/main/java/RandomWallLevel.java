import java.awt.*;
import java.util.Random;

public class RandomWallLevel extends WallLevelAbstractions implements WallLevelTemplates{

    Random rnd = new Random();

    /**
     * this method is one of the template used for the wall (level). this creates a random brick level.
     *
     * @param drawArea this is the area which the bricks could be placed
     * @param brickCount this is the amount of bricks which will be in for the level.
     * @param lineCount this is the number of rows of bricks for the level.
     * @param brickSizeRatio this is the size ratio of the brick.
     * @return it returns the bricks for the wall (level) in the form of a brick array.
     */
    public Brick[] level(Rectangle drawArea, int brickCount, int lineCount, double brickSizeRatio, int typeA, int typeB){
        brickCount -= brickCount % lineCount;

        brickCount += lineCount / 2;

        Brick[] brickArray  = createBrickArray(brickCount);

        Dimension brickSize = new Dimension((int) getDrawBrickLength(drawArea, lineCount,brickCount),(int) getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio));
        Point p = new Point();

        int i;
        for(i = 0; i < brickArray.length; i++){
            int line = i / getBrickOnLine(brickCount,lineCount);
            if(line == lineCount)
                break;
            double x = (i % getBrickOnLine(brickCount,lineCount)) * getDrawBrickLength(drawArea, lineCount, brickCount);
            x = (line % 2 == 0) ? x : (x - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2));
            double y = (line) * getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio);
            p.setLocation(x,y);
            brickArray[i] = makeBrick(p,brickSize, rnd.nextInt(4)+1);
        }

        for(double y = getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio); i < brickArray.length; i++, y += 2 * getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio)){
            double x = (getBrickOnLine(brickCount,lineCount) * getDrawBrickLength(drawArea, lineCount, brickCount)) - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2);
            p.setLocation(x,y);
            brickArray[i] = makeBrick(p,brickSize, rnd.nextInt(4)+1);
        }
        return brickArray;
    }
}
