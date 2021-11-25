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

    private final int LEVELS_COUNT = 6;
    private final int UP_IMPACT = 100;
    private final int DOWN_IMPACT = 200;
    private final int LEFT_IMPACT = 300;
    private final int RIGHT_IMPACT = 400;

    private Random rnd = new Random();
    private Rectangle borderArea;

    private Brick[] bricks;
    private Ball ball;
    private Player player;
    private WallLevelTemplates wallLevelTemplates = WallLevelTemplates.singletonWallLevelTemplates();

    private WallModel wallModel = WallModel.singletonWallModel();

    private Brick[][] levels;
    private int level;

    private Point startPoint;
    private int brickCount;
    private int ballCount;
    private boolean ballLost;

    private static Wall uniqueWall;

    /**
     * this method is used to create a Wall object based on the Singleton design pattern.
     *
     * @param drawArea this is the area of the game will be held.
     * @param brickCount this is the amount of brick for the wall.
     * @param lineCount this is for how many lines (rows) of bricks are in the level.
     * @param brickDimensionRatio this is for the ratio for the brick dimension.
     * @param ballPos this is the ball position.
     */
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

        setStartPoint(new Point(ballPos));

        setLevels(makeLevels(drawArea,brickCount,lineCount,brickDimensionRatio));
        setLevel(0);

        setBallCount(3);
        setBallLost(false);

        makeBall(ballPos);

        setRandomBallSpeed();

        setPlayer(Player.singletonPlayer((Point) ballPos.clone(),150,10, drawArea));

        setBorderArea(drawArea);
    }

    /**
     * this method is used to set the random speed on both x-axis and y-axis for the ball.
     */
    private void setRandomBallSpeed(){
        int speedX,speedY;

        do{
            speedX = getRnd().nextInt(5) - 2;
        }while(speedX == 0);

        do{
            speedY = -getRnd().nextInt(3);
        }while(speedY == 0);

        getBall().setSpeed(speedX,speedY);
    }

    /**
     * this method is used to create a ball object.
     *
     * @param ballPos this is the position (in the format of Point2D) of the ball that is going to be generated.
     */
    private void makeBall(Point2D ballPos){
        ball = RubberBall.singletonRubberBall(ballPos);
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
        tmp[0] = getWallLevelTemplates().makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio, getWallModel().getClayIntegerConstant(), getWallModel().getClayIntegerConstant());
        tmp[1] = getWallLevelTemplates().makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio, getWallModel().getClayIntegerConstant(), getWallModel().getCementIntegerConstant());
        tmp[2] = getWallLevelTemplates().makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio, getWallModel().getClayIntegerConstant(), getWallModel().getSteelIntegerConstant());
        tmp[3] = getWallLevelTemplates().makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio, getWallModel().getSteelIntegerConstant(), getWallModel().getCementIntegerConstant());
        tmp[4] = getWallLevelTemplates().makeSonicLevel(drawArea,brickCount,lineCount,brickDimensionRatio, getWallModel().getReinforcedSteelIntegerConstant(), getWallModel().getSteelIntegerConstant());
        tmp[5] = getWallLevelTemplates().makeRandomLevel(drawArea,brickCount,lineCount,brickDimensionRatio);
        return tmp;
    }

    /**
     * This method is used for the movement of the player (paddle) and the ball.
     */
    public void move(){
        getPlayer().move();
        getBall().move();
    }

    /**
     * this method is used to check if there is an impact for the ball with any entity, the sides of the screen. which will cause a reaction to the game.
     */
    public void findImpacts(){
        if(getPlayer().impact(getBall())){
            getBall().reverseY();
        }
        else if(impactWall()){
            /*for efficiency reverse is done into method impactWall
            * because for every brick program checks for horizontal and vertical impacts
            */
            setBrickCount(getBrickCount()-1);
        }

        if(impactSideBorder()) {
            getBall().reverseX();
        }

        if(getBall().getPosition().getY() < getBorderArea().getY()){
            getBall().reverseY();
        }
        else if(getBall().getPosition().getY() > getBorderArea().getY() + getBorderArea().getHeight()){
            setBallCount(getBallCount() - 1);
            setBallLost(true);
        }
    }

    /**
     * this is to check if the ball comes in contact with any side of the brick.
     *
     * @return returns a boolean value if or if it doesn't touch any entity.
     */
    private boolean impactWall(){
        for(Brick b : getBricks()){
            switch(b.findImpact(getBall())) {
                //Vertical Impact
                case UP_IMPACT:
                    getBall().reverseY();
                    return b.setImpact(getBall().getDown(),Crack.UP);
                case DOWN_IMPACT:
                    getBall().reverseY();
                    return b.setImpact(getBall().getUp(),Crack.DOWN);

                //Horizontal Impact
                case LEFT_IMPACT:
                    getBall().reverseX();
                    return b.setImpact(getBall().getRight(),Crack.RIGHT);
                case RIGHT_IMPACT:
                    getBall().reverseX();
                    return b.setImpact(getBall().getLeft(),Crack.LEFT);
            }
        }
        return false;
    }

    /**
     * this method is used to check if the ball have come in contact with the vertical sides of the game window.
     *
     * @return this returns a boolean value if it touches or doesn't touch the side of the game window
     */
    private boolean impactSideBorder(){
        return ((getBall().getPosition().getX() < getBorderArea().getX()) ||(getBall().getPosition().getX() > (getBorderArea().getX() + getBorderArea().getWidth())));
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
        getPlayer().resetPosition(getStartPoint());
        getBall().moveTo(getStartPoint());

        setRandomBallSpeed();

        setBallLost(false);
    }

    /**
     * this is used to reset the wall (bricks) and the ball count (tries).
     */
    public void wallReset(){
        for(Brick b : getBricks())
            b.repair();
        setBrickCount(getBricks().length);
        setBallCount(3);
    }

    /**
     * this method checks if there is or isn't any more tries for the player.
     *
     * @return returns a boolean value if there is or isn't any more tries allowed for the player.
     */
    public boolean ballEnd(){
        return getBallCount() == 0;
    }

    /**
     * this method checks if the level is completed.
     *
     * @return returns a boolean value if the level is completed or isn't completed.
     */
    public boolean isDone(){
        return getBrickCount() == 0;
    }

    /**
     * this method is used to progress to the next level.
     */
    public void nextLevel(){
        setBricks(getLevels()[level++]);
        setBrickCount(getBricks().length);
    }

    /**
     * this method is used to check if there is any more levels available for the player to play.
     *
     * @return returns a boolean value if there is or isn't any more levels for the player to play.
     */
    public boolean hasLevel(){
        return getLevel() < getLevels().length;
    }

    /**
     * this method is used to set the ball speed in x-axis.
     *
     * @param s this is the parameter (in integer) used to set the ball speed in x-axis.
     */
    public void setBallXSpeed(int s){
        getBall().setXSpeed(s);
    }

    /**
     * this method os used to set the ball speed in the y-axis.
     *
     * @param s this is the parameter (in integer) used to set the ball speed in y-axis.
     */
    public void setBallYSpeed(int s){
        getBall().setYSpeed(s);
    }

    /**
     * this method is used to reset the ball count (tries count) to 3.
     */
    public void resetBallCount(){
        setBallCount(3);
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

    public Brick[][] getLevels() {
        return levels;
    }

    public void setLevels(Brick[][] levels) {
        this.levels = levels;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public void setBrickCount(int brickCount) {
        this.brickCount = brickCount;
    }

    public void setBallCount(int ballCount) {
        this.ballCount = ballCount;
    }

    public void setBallLost(boolean ballLost) {
        this.ballLost = ballLost;
    }

    public Random getRnd() {
        return rnd;
    }

    public void setRnd(Random rnd) {
        this.rnd = rnd;
    }

    public Rectangle getBorderArea() {
        return borderArea;
    }

    public void setBorderArea(Rectangle area) {
        this.borderArea = area;
    }

    public Brick[] getBricks() {
        return bricks;
    }

    public void setBricks(Brick[] bricks) {
        this.bricks = bricks;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public WallLevelTemplates getWallLevelTemplates() {
        return wallLevelTemplates;
    }

    public WallModel getWallModel() {
        return wallModel;
    }
}
