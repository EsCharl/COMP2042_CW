import java.awt.*;

/**
 * this class is used to create one of the levels.
 */
public class ChainWallLevel extends FullWallRowsLevels implements WallLevelTemplates{

    /**
     * this method is one of the template used for the wall (level). this creates a level which looks like a chessboard.
     *
     * @return it returns the bricks for the wall (level) in the form of a brick array.
     */
    public Brick[] level(Rectangle drawArea, int brickCount, int lineCount, double brickSizeRatio, int typeA, int typeB){
        brickCount -= brickCount % lineCount;

        int centerLeft = getBrickOnLine(brickCount,lineCount) / 2 - 1;
        int centerRight = getBrickOnLine(brickCount,lineCount) / 2 + 1;

        brickCount += lineCount / 2;

        Brick[] brickArray  = createBrickArray(brickCount);

        Dimension brickSize = new Dimension((int) getDrawBrickLength(drawArea, lineCount, brickCount),(int) getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio));
        Point p = new Point();

        int i;
        for(i = 0; i < brickArray.length; i++){
            int line = i / getBrickOnLine(brickCount,lineCount);
            if(line == lineCount)
                break;
            int posX = i % getBrickOnLine(brickCount,lineCount);
            double x = posX * getDrawBrickLength(drawArea, lineCount, brickCount);
            x = (line % 2 == 0) ? x : (x - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2));
            double y = (line) * getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio);
            p.setLocation(x,y);

            boolean b = ((line % 2 == 0 && i % 2 == 0) || (line % 2 != 0 && posX > centerLeft && posX <= centerRight));
            brickArray[i] = b ? makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
        }

        for(double y = getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio);i < brickArray.length;i++, y += 2*getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio)){
            double x = (getBrickOnLine(brickCount,lineCount) * getDrawBrickLength(drawArea, lineCount, brickCount)) - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2);
            p.setLocation(x,y);
            brickArray[i] = makeBrick(p,brickSize,typeA);
        }
        return brickArray;
    }
}
