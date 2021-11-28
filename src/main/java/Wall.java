import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * this class is used to generate the level and maintain some game condition.
 */
public class Wall {

    public static final int BALL_COUNT = 3;
    private final int LEVELS_COUNT = 6;
    private final int MAX_BALL_SPEED = 5;

    private Random rnd;
    private Rectangle borderArea;

    private Brick[] bricks;
    private Ball ball;
    private Player player;

    private Movements movements;

    private Brick[][] brickLevels;
    private int currentLevel;

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

        setMovements(Movements.singletonMovements(this));

        setRnd(new Random());
        setStartPoint(new Point(ballPos));

        setBrickLevels(makeLevels(drawArea,brickCount,lineCount,brickDimensionRatio));

        setCurrentLevel(0);

        setBallCount(BALL_COUNT);
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

        // changes here, makes the maximum speed it can go on x-axis in between -max speed and max speed.
        do {
            speedX = getRnd().nextBoolean() ? getRnd().nextInt(MAX_BALL_SPEED) : -getRnd().nextInt(MAX_BALL_SPEED);
        } while (speedX == 0);


        do{
            speedY = -getRnd().nextInt(MAX_BALL_SPEED);
        }while(speedY == 0);

        getBall().setSpeed(speedX,speedY);
    }

    /**
     * this method is used to create a ball object (rubber ball).
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
        LevelFactory levelFactory = new LevelFactory();
        tmp[0] = levelFactory.getLevel("CHAINLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, WallLevelAbstractions.CLAY, WallLevelAbstractions.CLAY);
        tmp[1] = levelFactory.getLevel("CHAINLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, WallLevelAbstractions.CLAY, WallLevelAbstractions.CEMENT);
        tmp[2] = levelFactory.getLevel("CHAINLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, WallLevelAbstractions.CLAY, WallLevelAbstractions.STEEL);
        tmp[3] = levelFactory.getLevel("CHAINLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, WallLevelAbstractions.STEEL, WallLevelAbstractions.CEMENT);
        tmp[4] = levelFactory.getLevel("TWOLINESLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, WallLevelAbstractions.REINFORCED_STEEL, WallLevelAbstractions.STEEL);
        tmp[5] = levelFactory.getLevel("RANDOMLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio,WallLevelAbstractions.REINFORCED_STEEL, WallLevelAbstractions.STEEL);
        return tmp;
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
        setBallCount(BALL_COUNT);
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
        setBricks(getBrickLevels()[currentLevel++]);
        setBrickCount(getBricks().length);
    }

    /**
     * this method is used to check if there is any more levels available for the player to play.
     *
     * @return returns a boolean value if there is or isn't any more levels for the player to play.
     */
    public boolean hasLevel(){
        return getCurrentLevel() < getBrickLevels().length;
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
        setBallCount(BALL_COUNT);
    }

    /**
     * this method is used to get the one and only one wall object based on singleton design after the singletonWall method is called.
     *
     * @return this returns the one and only wall object.
     */
    private static Wall getUniqueWall() {
        return uniqueWall;
    }

    /**
     * this method is used to set the one and only wall object and enable it to be reused in the future. Singleton design.
     *
     * @param uniqueWall this is the variable used to set the wall object into a variable.
     */
    private static void setUniqueWall(Wall uniqueWall) {
        Wall.uniqueWall = uniqueWall;
    }

    /**
     * this method is used to get the generated brick levels for the game.
     *
     * @return this returns a Brick 2 dimension array which the first array is the level and second array is the brick.
     */
    public Brick[][] getBrickLevels() {
        return brickLevels;
    }

    /**
     * this is the method used to set the brick levels for the game.
     *
     * @param brickLevels this is the brick levels used to be recorded for the game levels.
     */
    public void setBrickLevels(Brick[][] brickLevels) {
        this.brickLevels = brickLevels;
    }

    /**
     * this is the method used to get the current level variable.
     *
     * @return this returns the currentLevel variable
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * this method is used to set the current level variable.
     *
     * @param level this is the integer used to set the level.
     */
    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    /**
     * this method is used get the start point for both ball and the player (paddle).
     *
     * @return this returns A point which is the start location.
     */
    public Point getStartPoint() {
        return startPoint;
    }

    /**
     * this method is used to set the starting point for both the ball and player (paddle).
     *
     * @param startPoint this is the point position used to set the position.
     */
    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    /**
     * this method is used to set the brick amount for a level.
     *
     * @param brickCount this is the value of bricks being used for the level.
     */
    public void setBrickCount(int brickCount) {
        this.brickCount = brickCount;
    }

    /**
     * this is used to set the ball counter which is the amount of tries the player have before losing,
     *
     * @param ballCount this is the amount of balls (tries) for the level.
     */
    public void setBallCount(int ballCount) {
        this.ballCount = ballCount;
    }

    /**
     * this variable is used to set if the ball is lost. (gone under the player (paddle)).
     *
     * @param ballLost this is the boolean value used to set if the ball is lost or not.
     */
    public void setBallLost(boolean ballLost) {
        this.ballLost = ballLost;
    }

    /**
     * this method is used to get a random number generator object. (used for the setting of the random ball speed).
     *
     * @return returns a random number generator object.
     */
    public Random getRnd() {
        return rnd;
    }

    /**
     * this is used to set the random number generator object into a variable for future uses.
     *
     * @param rnd this is the random object used to be setted for future use.
     */
    public void setRnd(Random rnd) {
        this.rnd = rnd;
    }

    /**
     * this method is used to get the border area of the game.
     *
     * @return this returns a rectangle which is the border area in a rectangular shape.
     */
    public Rectangle getBorderArea() {
        return borderArea;
    }

    /**
     * this returns a rectangle border area of the game.
     *
     * @param area this is the rectangle used to set the game area.
     */
    public void setBorderArea(Rectangle area) {
        this.borderArea = area;
    }

    /**
     * this is the array of bricks used for the level.
     *
     * @return this returns a brick array.
     */
    public Brick[] getBricks() {
        return bricks;
    }

    /**
     * this method is used to set the bricks for the level.
     *
     * @param bricks this is the brick array used to set into a variable.
     */
    public void setBricks(Brick[] bricks) {
        this.bricks = bricks;
    }

    /**
     * this method is used to get the ball object to check the position of the ball, collision, and set the speed.
     *
     * @return this returns a ball object
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * this method is used to get the player object for the game.
     *
     * @return this returns the player object.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * this method is used to set the player object.
     *
     * @param player this is the player object used to set into an object.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * this method is used to get the movements object which handles all the entity movements.
     *
     * @return it returns a movements object.
     */
    public Movements getMovements() {
        return movements;
    }

    /**
     * this method is used to set the movement object into a variable for easy referencing.
     *
     * @param movements this is the movements object used to be called down the line.
     */
    public void setMovements(Movements movements) {
        this.movements = movements;
    }
}
