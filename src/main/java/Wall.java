/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * this class is used to generate the level and maintain some game condition.
 */
public class Wall {

    private static final int LEVELS_COUNT = 6;

    private static final int CLAY = 1;
    private static final int STEEL = 2;
    private static final int CEMENT = 3;
    private static final int REINFORCED_STEEL = 4;

    private Random rnd = new Random();
    private Rectangle area;

    Brick[] bricks;
    Ball ball;
    Player player;

    private Brick[][] levels;
    private int level;

    private Point startPoint;
    private int brickCount;
    private int ballCount;
    private boolean ballLost;

    private static Wall uniqueWall;

    public static Wall singletonWall(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point ballPos){
        if(getUniqueWall() == null){
            setUniqueWall(new Wall(drawArea, brickCount, lineCount, brickDimensionRatio, ballPos));
        }
        return getUniqueWall();
    }

    /**
     * this constructor is used to generate an object which is the wall used for the levels.
     *
     * @param drawArea this is the area of the game will be held.
     * @param brickCount this is the amount of brick for the wall.
     * @param lineCount this is for how many lines (rows) of bricks are in the level.
     * @param brickDimensionRatio this is for the ratio for the brick dimension.
     * @param ballPos this is the ball position.
     */
    private Wall(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point ballPos){

        this.startPoint = new Point(ballPos);

        levels = makeLevels(drawArea,brickCount,lineCount,brickDimensionRatio);
        level = 0;

        ballCount = 3;
        ballLost = false;

        makeBall(ballPos);

        setRandomBallSpeed();

        player = Player.singletonPlayer((Point) ballPos.clone(),150,10, drawArea);

        area = drawArea;
    }

    /**
     * this method is used to set the random speed on both x-axis and y-axis for the ball.
     */
    private void setRandomBallSpeed(){
        int speedX,speedY;

        do{
            speedX = rnd.nextInt(5) - 2;
        }while(speedX == 0);

        do{
            speedY = -rnd.nextInt(3);
        }while(speedY == 0);

        ball.setSpeed(speedX,speedY);
    }

    /**
     * this method is one of the template used for the wall (level).
     *
     * @param drawArea this is the area which the bricks could be placed
     * @param brickCnt this is the amount of bricks which will be in for the level.
     * @param lineCnt this is the number of rows of bricks for the level.
     * @param brickSizeRatio this is the size ratio of the brick.
     * @param typeA this is one of the type of brick used for this level.
     * @param typeB this is one of the type of brick used for this level.
     * @return it returns a wall (level) in the form of a brick array.
     */
    private Brick[] makeChessboardLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int typeA, int typeB){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCnt -= brickCnt % lineCnt;

        int centerLeft = getBrickOnLine(brickCnt,lineCnt) / 2 - 1;
        int centerRight = getBrickOnLine(brickCnt,lineCnt) / 2 + 1;

        double brickLen = drawArea.getWidth() / getBrickOnLine(brickCnt,lineCnt);
        double brickHgt = brickLen / brickSizeRatio;

        brickCnt += lineCnt / 2;

        Brick[] tmp  = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt);
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / getBrickOnLine(brickCnt,lineCnt);
            if(line == lineCnt)
                break;
            int posX = i % getBrickOnLine(brickCnt,lineCnt);
            double x = posX * brickLen;
            x = (line % 2 == 0) ? x : (x - (brickLen / 2));
            double y = (line) * brickHgt;
            p.setLocation(x,y);

            boolean b = ((line % 2 == 0 && i % 2 == 0) || (line % 2 != 0 && posX > centerLeft && posX <= centerRight));
            tmp[i] = b ? makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){
            double x = (getBrickOnLine(brickCnt,lineCnt) * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,typeA);
        }
        return tmp;
    }

    /**
     * this method is one of the template used for the wall (level).
     *
     * @param drawArea this is the area which the bricks could be placed
     * @param brickCnt this is the amount of bricks which will be in for the level.
     * @param lineCnt this is the number of rows of bricks for the level.
     * @param brickSizeRatio this is the size ratio of the brick.
     * @param typeA this is one of the type of brick used for this level.
     * @param typeB this is one of the type of brick used for this level.
     * @return it returns a wall (level) in the form of a brick array.
     */
    private Brick[] makeSonicLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int typeA, int typeB){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCnt -= brickCnt % lineCnt;

        int centerLeft = getBrickOnLine(brickCnt,lineCnt) / 2 - 1;
        int centerRight = getBrickOnLine(brickCnt,lineCnt) / 2 + 1;

        double brickLen = drawArea.getWidth() / getBrickOnLine(brickCnt,lineCnt);
        double brickHgt = brickLen / brickSizeRatio;

        brickCnt += lineCnt / 2;

        Brick[] tmp  = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt);
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / getBrickOnLine(brickCnt,lineCnt);
            if(line == lineCnt)
                break;
            int posX = i % getBrickOnLine(brickCnt,lineCnt);
            double x = posX * brickLen;
            x = (line % 2 == 0) ? x : (x - (brickLen / 2));
            double y = (line) * brickHgt;
            p.setLocation(x,y);

            boolean b = ((i % 2 == 0) || (posX > centerLeft && posX <= centerRight));
            tmp[i] = b ? makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){
            double x = (getBrickOnLine(brickCnt,lineCnt) * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,typeA);
        }

        return tmp;
    }

    /**
     * this method is one of the template used for the wall (level).
     *
     * @param drawArea this is the area which the bricks could be placed
     * @param brickCnt this is the amount of bricks which will be in for the level.
     * @param lineCnt this is the number of rows of bricks for the level.
     * @param brickSizeRatio this is the size ratio of the brick.
     * @return it returns a wall (level) in the form of a brick array.
     */
    private Brick[] makeRandomLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCnt -= brickCnt % lineCnt;

        double brickLen = drawArea.getWidth() / getBrickOnLine(brickCnt,lineCnt);
        double brickHgt = brickLen / brickSizeRatio;

        brickCnt += lineCnt / 2;

        Brick[] tmp  = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt);
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / getBrickOnLine(brickCnt,lineCnt);
            if(line == lineCnt)
                break;
            double x = (i % getBrickOnLine(brickCnt,lineCnt)) * brickLen;
            x = (line % 2 == 0) ? x : (x - (brickLen / 2));
            double y = (line) * brickHgt;
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize, rnd.nextInt(4)+1);
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){
            double x = (getBrickOnLine(brickCnt,lineCnt) * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = new ClayBrick(p,brickSize);
        }
        return tmp;
    }

    /**
     * this method is used to get the total amount of brick on a line. (for full horizontal lines)
     *
     * @param brickCnt total amount of bricks.
     * @param lineCnt total amount of lines.
     * @return returns an amount of bricks for a single line.
     */
    private int getBrickOnLine(int brickCnt, int lineCnt){
        return brickCnt/lineCnt;
    }

    /**
     * this method is used to create a ball object.
     *
     * @param ballPos this is the position (in the format of Point2D) of the ball that is going to be generated.
     */
    private void makeBall(Point2D ballPos){
        ball = new RubberBall(ballPos);
    }

    /**
     * this is used to generate the levels to be placed in a brick array.
     *
     * @param drawArea this is the area where the bricks will be drawn.
     * @param brickCount this is the amount bricks that will be generated in the level.
     * @param lineCount this is the total amount of rows of bricks that is allowed.
     * @param brickDimensionRatio this is the ratio for the bricks.
     * @return the levels that are generated in the form of 2 dimension brick array.
     */
    private Brick[][] makeLevels(Rectangle drawArea,int brickCount,int lineCount,double brickDimensionRatio){
        Brick[][] tmp = new Brick[LEVELS_COUNT][];
        tmp[0] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,CLAY);
        tmp[1] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,CEMENT);
        tmp[2] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,STEEL);
        tmp[3] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,STEEL,CEMENT);
        tmp[4] = makeSonicLevel(drawArea,brickCount,lineCount,brickDimensionRatio,REINFORCED_STEEL,STEEL);
        tmp[5] = makeRandomLevel(drawArea,brickCount,lineCount,brickDimensionRatio);
        return tmp;
    }

    /**
     * This method is used for the movement of the player (paddle) and the ball.
     */
    public void move(){
        player.move();
        ball.move();
    }

    /**
     * this method is used to check if there is an impact for the ball with any entity, the sides of the screen. which will cause a reaction to the game.
     */
    public void findImpacts(){
        if(player.impact(ball)){
            ball.reverseY();
        }
        else if(impactWall()){
            /*for efficiency reverse is done into method impactWall
            * because for every brick program checks for horizontal and vertical impacts
            */
            brickCount--;
        }

        if(impactBorder()) {
            ball.reverseX();
        }

        if(ball.getPosition().getY() < area.getY()){
            ball.reverseY();
        }
        else if(ball.getPosition().getY() > area.getY() + area.getHeight()){
            ballCount--;
            ballLost = true;
        }
    }

    /**
     * this is to check if the ball comes in contact with any side of the brick.
     *
     * @return returns a boolean value if or if it doesn't touch any entity.
     */
    private boolean impactWall(){
        for(Brick b : bricks){
            switch(b.findImpact(ball)) {
                //Vertical Impact
                case Brick.UP_IMPACT:
                    ball.reverseY();
                    return b.setImpact(ball.down,Crack.UP);
                case Brick.DOWN_IMPACT:
                    ball.reverseY();
                    return b.setImpact(ball.up,Crack.DOWN);

                //Horizontal Impact
                case Brick.LEFT_IMPACT:
                    ball.reverseX();
                    return b.setImpact(ball.right,Crack.RIGHT);
                case Brick.RIGHT_IMPACT:
                    ball.reverseX();
                    return b.setImpact(ball.left,Crack.LEFT);
            }
        }
        return false;
    }

    /**
     * this method is used to check if the ball have come in contact with the vertical sides of the game window.
     *
     * @return this returns a boolean value if it touches or doesn't touch the side of the game window
     */
    private boolean impactBorder(){
        Point2D p = ball.getPosition();
        return ((p.getX() < area.getX()) ||(p.getX() > (area.getX() + area.getWidth())));
    }

    /**
     * this is used to get the total amount of bricks still in the level
     *
     * @return this returns a int value of the amount of bricks still in the level.
     */
    public int getBrickCount(){
        return brickCount;
    }

    /**
     * this is used to get the amount of balls (tries) for the level.
     *
     * @return this returns the amount of balls (tries) in integer.
     */
    public int getBallCount(){
        return ballCount;
    }

    /**
     * this is used to check if the ball is lost.
     *
     * @return this returns a boolean value if the ball is or isn't lost.
     */
    public boolean isBallLost(){
        return ballLost;
    }

    /**
     * this is used to reset the ball and the player to the starting position and giving it a random speed for the ball.
     */
    public void positionsReset(){
        player.resetPosition(startPoint);
        ball.moveTo(startPoint);

        setRandomBallSpeed();

        ballLost = false;
    }

    /**
     * this is used to reset the wall (bricks) and the ball count (tries).
     */
    public void wallReset(){
        for(Brick b : bricks)
            b.repair();
        brickCount = bricks.length;
        ballCount = 3;
    }

    /**
     * this method checks if there is or isn't any more tries for the player.
     *
     * @return returns a boolean value if there is or isn't any more tries allowed for the player.
     */
    public boolean ballEnd(){
        return ballCount == 0;
    }

    /**
     * this method checks if the level is completed.
     *
     * @return returns a boolean value if the level is completed or isn't completed.
     */
    public boolean isDone(){
        return brickCount == 0;
    }

    /**
     * this method is used to progress to the next level.
     */
    public void nextLevel(){
        bricks = levels[level++];
        this.brickCount = bricks.length;
    }

    /**
     * this method is used to check if there is any more levels available for the player to play.
     *
     * @return returns a boolean value if there is or isn't any more levels for the player to play.
     */
    public boolean hasLevel(){
        return level < levels.length;
    }

    /**
     * this method is used to set the ball speed in x-axis.
     *
     * @param s this is the parameter (in integer) used to set the ball speed in x-axis.
     */
    public void setBallXSpeed(int s){
        ball.setXSpeed(s);
    }

    /**
     * this method os used to set the ball speed in the y-axis.
     *
     * @param s this is the parameter (in integer) used to set the ball speed in y-axis.
     */
    public void setBallYSpeed(int s){
        ball.setYSpeed(s);
    }

    /**
     * this method is used to reset the ball count (tries count) to 3.
     */
    public void resetBallCount(){
        ballCount = 3;
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
        switch(type){
            case CLAY:
                return new ClayBrick(point,size);
            case STEEL:
                return new SteelBrick(point,size);
            case CEMENT:
                return new CementBrick(point, size);
            case REINFORCED_STEEL:
                return new ReinforcedSteelBrick(point, size);
            default:
                throw  new IllegalArgumentException(String.format("Unknown Type:%d\n",type));
        }
    }

    /**
     * this is used to get the wall level.
     *
     * @return this returns an integer value of the level which the player is currently in.
     */
    public int getWallLevel(){
        return level;
    }

    public static Wall getUniqueWall() {
        return uniqueWall;
    }

    public static void setUniqueWall(Wall uniqueWall) {
        Wall.uniqueWall = uniqueWall;
    }
}
