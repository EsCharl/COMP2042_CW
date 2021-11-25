import java.awt.*;
import java.util.Random;

public class WallLevelTemplates {

    Random rnd = new Random();

    WallModel wallModel = WallModel.singletonWallModel();

    private static WallLevelTemplates uniqueWallLevelTemplates;

    /**
     * this method is for a design pattern specifically singleton.
     *
     * @return returns a WallLevelTemplate object.
     */
    public static WallLevelTemplates singletonWallLevelTemplates(){
        if(getUniqueWallLevelTemplates() == null){
            setUniqueWallLevelTemplates(new WallLevelTemplates());
        }
        return getUniqueWallLevelTemplates();
    }

    /**
     * this method is used to create a brick array which is used to store the bricks for the level templates
     *
     * @param brickCount the amount of bricks that is going to be set for the array.
     * @return it returns a new empty brick array.
     */
    private Brick[] createBrickArray(int brickCount){
        return new Brick[brickCount];
    }

    private WallLevelTemplates(){}

    /**
     * this method is one of the template used for the wall (level).
     *
     * @param drawArea this is the area which the bricks could be placed
     * @param brickCount this is the amount of bricks which will be in for the level.
     * @param lineCount this is the number of rows of bricks for the level.
     * @param brickSizeRatio this is the size ratio of the brick.
     * @param typeA this is one of the type of brick used for this level.
     * @param typeB this is one of the type of brick used for this level.
     * @return it returns the bricks for the wall (level) in the form of a brick array.
     */
    public Brick[] makeChessboardLevel(Rectangle drawArea, int brickCount, int lineCount, double brickSizeRatio, int typeA, int typeB){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCount -= brickCount % lineCount;

        int centerLeft = getBrickOnLine(brickCount,lineCount) / 2 - 1;
        int centerRight = getBrickOnLine(brickCount,lineCount) / 2 + 1;

        brickCount += lineCount / 2;

        Brick[] tmp  = createBrickArray(brickCount);

        Dimension brickSize = new Dimension((int) getDrawBrickLength(drawArea, lineCount, brickCount),(int) getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio));
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / getBrickOnLine(brickCount,lineCount);
            if(line == lineCount)
                break;
            int posX = i % getBrickOnLine(brickCount,lineCount);
            double x = posX * getDrawBrickLength(drawArea, lineCount, brickCount);
            x = (line % 2 == 0) ? x : (x - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2));
            double y = (line) * getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio);
            p.setLocation(x,y);

            boolean b = ((line % 2 == 0 && i % 2 == 0) || (line % 2 != 0 && posX > centerLeft && posX <= centerRight));
            tmp[i] = b ? makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
        }

        for(double y = getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio);i < tmp.length;i++, y += 2*getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio)){
            double x = (getBrickOnLine(brickCount,lineCount) * getDrawBrickLength(drawArea, lineCount, brickCount)) - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2);
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,typeA);
        }
        return tmp;
    }

    /**
     * this method is one of the template used for the wall (level).
     *
     * @param drawArea this is the area which the bricks could be placed
     * @param brickCount this is the amount of bricks which will be in for the level.
     * @param lineCount this is the number of rows of bricks for the level.
     * @param brickSizeRatio this is the size ratio of the brick.
     * @param typeA this is one of the type of brick used for this level.
     * @param typeB this is one of the type of brick used for this level.
     * @return it returns the bricks for the wall (level) in the form of a brick array.
     */
    public Brick[] makeSonicLevel(Rectangle drawArea, int brickCount, int lineCount, double brickSizeRatio, int typeA, int typeB){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCount -= brickCount % lineCount;

        int centerLeft = getBrickOnLine(brickCount,lineCount) / 2 - 1;
        int centerRight = getBrickOnLine(brickCount,lineCount) / 2 + 1;

        brickCount += lineCount / 2;

        Brick[] tmp  = createBrickArray(brickCount);

        Dimension brickSize = new Dimension((int) getDrawBrickLength(drawArea, lineCount, brickCount),(int) getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio));
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / getBrickOnLine(brickCount,lineCount);
            if(line == lineCount)
                break;
            int posX = i % getBrickOnLine(brickCount,lineCount);
            double x = posX * getDrawBrickLength(drawArea, lineCount, brickCount);
            x = (line % 2 == 0) ? x : (x - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2));
            double y = (line) * getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio);
            p.setLocation(x,y);

            boolean b = ((i % 2 == 0) || (posX > centerLeft && posX <= centerRight));
            tmp[i] = b ? makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
        }

        for(double y = getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio);i < tmp.length;i++, y += 2*getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio)){
            double x = (getBrickOnLine(brickCount,lineCount) * getDrawBrickLength(drawArea, lineCount, brickCount)) - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2);
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,typeA);
        }

        return tmp;
    }

    /**
     * this method is one of the template used for the wall (level).
     *
     * @param drawArea this is the area which the bricks could be placed
     * @param brickCount this is the amount of bricks which will be in for the level.
     * @param lineCount this is the number of rows of bricks for the level.
     * @param brickSizeRatio this is the size ratio of the brick.
     * @return it returns the bricks for the wall (level) in the form of a brick array.
     */
    public Brick[] makeRandomLevel(Rectangle drawArea, int brickCount, int lineCount, double brickSizeRatio){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCount -= brickCount % lineCount;

        brickCount += lineCount / 2;

        Brick[] tmp  = createBrickArray(brickCount);

        Dimension brickSize = new Dimension((int) getDrawBrickLength(drawArea, lineCount,brickCount),(int) getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio));
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / getBrickOnLine(brickCount,lineCount);
            if(line == lineCount)
                break;
            double x = (i % getBrickOnLine(brickCount,lineCount)) * getDrawBrickLength(drawArea, lineCount, brickCount);
            x = (line % 2 == 0) ? x : (x - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2));
            double y = (line) * getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio);
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize, rnd.nextInt(4)+1);
        }

        for(double y = getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio); i < tmp.length; i++, y += 2* getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio)){
            double x = (getBrickOnLine(brickCount,lineCount) * getDrawBrickLength(drawArea, lineCount, brickCount)) - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2);
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize, rnd.nextInt(4)+1);
        }
        return tmp;
    }

    /**
     * this method is used to select and create the brick object needed for the level.
     *
     * @param point this is used get the position where the brick is supposed to be.
     * @param size this is for the size of the brick
     * @param type this is the type of brick to be used.
     * @return this returns the brick that is created.
     */
    private Brick makeBrick(Point point, Dimension size, int type){
        if (wallModel.getClayIntegerConstant() == type)
            return new ClayBrick(point,size);
        else if(wallModel.getSteelIntegerConstant() == type)
            return new SteelBrick(point,size);
        else if(wallModel.getCementIntegerConstant() == type)
            return new CementBrick(point, size);
        else if(wallModel.getReinforcedSteelIntegerConstant() == type)
            return new ReinforcedSteelBrick(point, size);
        else
            throw  new IllegalArgumentException(String.format("Unknown Type:%d\n",type));
    }

    /**
     * this method is used to get the length of each brick that is going to be shown on the level.
     *
     * @param drawArea this is the draw area for the window.
     * @param lineCount this is the total amount of lines for the specific level.
     * @param brickCount this is the amount of bricks for the level.
     * @return it returns a double value for the length of the brick.
     */
    private double getDrawBrickLength(Rectangle drawArea, int lineCount, int brickCount){
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
    private double getDrawBrickHeight(Rectangle drawArea, int brickCount, int lineCount, double brickSizeRatio){
        return getDrawBrickLength(drawArea, lineCount, brickCount) / brickSizeRatio;
    }

    /**
     * this method is used to get the total amount of brick on a line. (for full horizontal lines)
     *
     * @param brickCount total amount of bricks.
     * @param lineCount total amount of lines.
     * @return returns an amount of bricks for a single line.
     */
    private int getBrickOnLine(int brickCount, int lineCount){
        return brickCount/lineCount;
    }

    /**
     * this method is used to get the one and only WallLevelTemplates object (Singleton).
     *
     * @return it returns a WallLevelTemplates object.
     */
    public static WallLevelTemplates getUniqueWallLevelTemplates() {
        return uniqueWallLevelTemplates;
    }

    public static void setUniqueWallLevelTemplates(WallLevelTemplates uniqueWallLevelTemplates) {
        WallLevelTemplates.uniqueWallLevelTemplates = uniqueWallLevelTemplates;
    }
}
