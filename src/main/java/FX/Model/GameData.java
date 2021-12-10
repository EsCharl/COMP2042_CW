/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2021  Leong Chang Yung
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

package FX.Model;

import FX.Model.Entities.Ball.Ball;
import FX.Model.Entities.Ball.BallClone;
import FX.Model.Entities.Ball.RubberBall;
import FX.Model.Entities.Brick.Brick;
import FX.Model.Entities.Brick.BrickFactory;
import FX.Model.Entities.Brick.Crackable;
import FX.Model.Entities.Player;
import FX.Model.Levels.LevelFactory;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * this class is used to generate the level and maintain some game condition.
 */
public class GameData {
    private SoundEffects soundEffects;

    private final double brickDimensionRatio = 6/2;
    private final int makeLevelBrickCount = 30;
    private final int makeLevelLineCount = 3;

    private final int MAX_BALL_COUNT = 3;
    private final int LEVELS_AMOUNT = 7;

    private final int GAME_WINDOW_WIDTH = 600;
    private final int GAME_WINDOW_HEIGHT = 450;

    private final int playerTopLeftXStartPoint = 225;
    private final int playerTopLeftYStartPoint = 430;

    private final int ballTopLeftXStartPoint = 300;
    private final int ballTopLeftYStartPoint = 410;

    private Brick[] bricks;

    private Brick[][] brickLevels;
    private int currentLevel;
    private ArrayList<BallClone> cloneBall;
    private final int maxCloneBall = 3;

    private int brickCount;
    private int ballCount;
    private boolean ballLost;

    private static GameData uniqueGameData;
    private Player player;
    private Ball mainBall;

    private Random rnd;
    private final double cloneBallGenerationProbability = 0.3;

    private Rectangle playArea;

    private boolean showPauseMenu;
    private boolean botMode;
    private boolean pauseMode;

    /**
     * this method is used to create a Model.Wall object based on the Singleton design pattern.
     */
    public static GameData singletonGame(){
        if(getUniqueGame() == null){
            setUniqueGame(new GameData());
        }
        return getUniqueGame();
    }

    /**
     * this constructor is used to generate an object which is the wall used for the levels.
     */
    private GameData(){
        rnd = new Random();
        cloneBall = new ArrayList<>();
        setSoundEffects(new SoundEffects());

        setPauseMode(true);

        setBotMode(false);
        setShowPauseMenu(false);

        setPlayArea(new Rectangle(0,0, getGAME_WINDOW_WIDTH(), getGAME_WINDOW_HEIGHT()));

        setCurrentLevel(0);

        setBallCount(getMAX_BALL_COUNT());
        setBallLost(false);

        setBrickLevels(makeLevels(getPlayArea(), getMakeLevelBrickCount(),getMakeLevelLineCount(),getBrickDimensionRatio()));

        nextLevel();

        setPlayer(Player.singletonPlayer(new Point2D(getPlayerTopLeftXStartPoint(),getPlayerTopLeftYStartPoint()), getPlayArea()));
        setMainBall(new RubberBall(new Point2D(getBallTopLeftXStartPoint(),getBallTopLeftYStartPoint())));
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
    private Brick[][] makeLevels(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio){
        Brick[][] tmp = new Brick[getLEVELS_AMOUNT()][];
        LevelFactory levelFactory = new LevelFactory();
        tmp[0] = levelFactory.makeLevel("CHAINLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, BrickFactory.CLAY, BrickFactory.CLAY);
        tmp[1] = levelFactory.makeLevel("CHAINLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, BrickFactory.CLAY, BrickFactory.CEMENT);
        tmp[2] = levelFactory.makeLevel("CHAINLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, BrickFactory.CLAY, BrickFactory.STEEL);
        tmp[3] = levelFactory.makeLevel("CHAINLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, BrickFactory.STEEL, BrickFactory.CEMENT);
        tmp[4] = levelFactory.makeLevel("STRAIGHTLINESLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, BrickFactory.REINFORCED_STEEL, BrickFactory.STEEL);
        tmp[5] = levelFactory.makeLevel("CURLYLINESLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, BrickFactory.REINFORCED_STEEL, BrickFactory.STEEL);
        tmp[6] = levelFactory.makeLevel("RANDOMLEVEL").level(drawArea,brickCount,lineCount,brickDimensionRatio, 0, 0);
        return tmp;
    }

    /**
     * this is used to reset the wall (bricks) and the ball count (tries).
     */
    public void wallReset(){
        for(Brick b : getBricks()){
            b.setBroken(false);
            b.setCurrentStrength(b.getMaxStrength());
            if(b instanceof Crackable)
                ((Crackable) b).setCrackPath(null);
        }
        setBrickCount(getBricks().length);
        setBallCount(getMAX_BALL_COUNT());
    }

    /**
     * this method is used to get the sound effects object class which deals with all the sound effects for the game.
     *
     * @return this returns the sound effects object which contains all the sound effects.
     */
    public SoundEffects getSoundEffects() {
        return soundEffects;
    }

    /**
     * this method is used to set the sound effects object into a variable for future reference.
     *
     * @param soundEffects this is the sound effects object which is to be set into a variable.
     */
    public void setSoundEffects(SoundEffects soundEffects) {
        this.soundEffects = soundEffects;
    }

    /**
     * this method is used to progress to the next level.
     */
    public void nextLevel(){
        setBricks(getBrickLevels()[currentLevel++]);
        setBrickCount(getBricks().length);
    }

    /**
     * this method change the ball direction and also returns true if the ball comes in contact with any side of the brick.
     *
     * @return returns a boolean value if or if it doesn't touch any entity.
     */
    public boolean impactWall(Ball ball){
        BoundingBox ballBound = ball.getBounds();
        for(Brick b : getBricks()){
            if(!b.isBroken()){
                if(b.getBounds().contains(ballBound.getMinX()+ ballBound.getWidth()/2, ballBound.getMaxY())){
                    ball.ballBottomCollision();
                    getSoundEffects().playBrickSoundEffect(b);
                    return b.setImpact(new Point2D(ballBound.getMaxX()- ballBound.getHeight(), ballBound.getMaxY()), Crackable.UP);
                }
                else if (b.getBounds().contains(ballBound.getMinX()+ ballBound.getWidth()/2, ballBound.getMinY())){
                    ball.ballTopCollision();
                    getSoundEffects().playBrickSoundEffect(b);
                    return b.setImpact(new Point2D(ballBound.getMinX()+ ballBound.getWidth(), ballBound.getMinY()), Crackable.DOWN);
                }
                else if(b.getBounds().contains(ballBound.getMaxX(), ballBound.getMinY()+ ballBound.getHeight()/2)){
                    ball.ballLeftCollision();
                    getSoundEffects().playBrickSoundEffect(b);
                    return b.setImpact(new Point2D(ballBound.getMaxX(), ballBound.getMaxY()- ballBound.getHeight()), Crackable.RIGHT);
                }
                else if(b.getBounds().contains(ballBound.getMinX(), ballBound.getMinY()+ ballBound.getHeight()/2)){
                    ball.ballRightCollision();
                    getSoundEffects().playBrickSoundEffect(b);
                    return b.setImpact(new Point2D(ballBound.getMinX(), ballBound.getMaxY()- ballBound.getHeight()), Crackable.LEFT);
                }
            }
        }
        return false;
    }

    /**
     * this method is used to let the bot control the paddle instead of the player playing it.
     */
    public void automation(){
        if(isBotMode()){
            if(getMainBall().getBounds().getMinX() > getPlayer().getBounds().getMinX() + getPlayer().getBounds().getWidth()/2){
                getPlayer().setMoveAmount(getPlayer().getDEF_MOVE_AMOUNT());
            }else{
                getPlayer().setMoveAmount(-getPlayer().getDEF_MOVE_AMOUNT());
            }
        }
    }

    /**
     * this method is used to reset the ball count (tries count) to 3.
     */
    public void resetBallCount(){
        setBallCount(getMAX_BALL_COUNT());
    }

    /**
     * this returns the brick dimension ratio used to set the brick dimension on the wall level.
     *
     * @return this returns a double value of the ratio of the brick size.
     */
    public double getBrickDimensionRatio() {
        return brickDimensionRatio;
    }

    /**
     * this method is used to get the amount of bricks in for each level. (vary based on the level templates used).
     *
     * @return this returns the amount used to create the wall level.
     */
    public int getMakeLevelBrickCount() {
        return makeLevelBrickCount;
    }

    /**
     * this method is used to get the amount of lines for the wall level.
     *
     * @return this returns the line count used to create the wall level.
     */
    public int getMakeLevelLineCount() {
        return makeLevelLineCount;
    }

    /**
     * this method is used to get the player object which is used to move, set and update the player object.
     *
     * @return this returns a player object.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * this method is used to get the ball object which is used to collide with different entities in the game.
     *
     * @return this returns a ball object.
     */

    public Ball getMainBall() {
        return mainBall;
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
     * this method is used to get the one and only one wall object based on singleton design after the singletonWall method is called.
     *
     * @return this returns the one and only wall object.
     */
    private static GameData getUniqueGame() {
        return uniqueGameData;
    }

    /**
     * this method is used to set the one and only wall object and enable it to be reused in the future. Singleton design.
     *
     * @param uniqueGameData this is the variable used to set the wall object into a variable.
     */
    private static void setUniqueGame(GameData uniqueGameData) {
        GameData.uniqueGameData = uniqueGameData;
    }

    /**
     * this method is used to get the generated brick levels for the game.
     *
     * @return this returns a Model.Brick.Brick 2 dimension array which the first array is the level and second array is the brick.
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
     * this method is used to get the levels that are going to be created.
     *
     * @return this is the value of the level amount
     */
    public int getLEVELS_AMOUNT() {
        return LEVELS_AMOUNT;
    }

    /**
     * this method is used to get the maximum ball count (bries) for each level.
     *
     * @return this returns the maximum tries that is allowed for each level.
     */
    public int getMAX_BALL_COUNT() {
        return MAX_BALL_COUNT;
    }

    /**
     * this method is used to get the definite height of the game board and the wall level height generation
     *
     * @return it returns an integer to be used for the height of the game board and wall level.
     */
    public int getGAME_WINDOW_HEIGHT(){
        return GAME_WINDOW_HEIGHT;
    }

    /**
     * this method is used to get the definite width of the game board and the wall level width generation.
     *
     * @return it returns an integer to be used for the width of the game board and wall level.
     */
    public int getGAME_WINDOW_WIDTH(){
        return GAME_WINDOW_WIDTH;
    }

    /**
     * this method is used to see if the game is in pause menu.
     *
     * @return returns true if it is.
     */
    public boolean isShowPauseMenu() {
        return showPauseMenu;
    }

    /**
     * this method is used to check if the bot mode (AI) is activated for the bot to play in behalf of the player.
     *
     * @return this returns a boolean value to see if the game is enabled for the bot to play.
     */
    public boolean isBotMode() {
        return botMode;
    }

    /**
     * this method is used to set the show pause menu variable, which is used to record if the game is in pause.
     *
     * @param showPauseMenu this is used to change the status of the variable.
     */
    public void setShowPauseMenu(boolean showPauseMenu) {
        this.showPauseMenu = showPauseMenu;
    }

    /**
     * this method is used to change between the player mode and AI mode.
     *
     * @param botMode this is the boolean value to set if it's player mode (false) or AI mode (true).
     */
    public void setBotMode(boolean botMode) {
        this.botMode = botMode;
    }

    /**
     * this method is used to get the play area of the game.
     *
     * @return this returns a rectangle which is the play area.
     */
    public Rectangle getPlayArea() {
        return playArea;
    }

    /**
     * this method is used to set a rectangle which have the play area dimension be placed into a variable for future reference.
     *
     * @param playArea this is the rectangle object which is the play area of the game.
     */
    public void setPlayArea(Rectangle playArea) {
        this.playArea = playArea;
    }

    /**
     * this method is used to get the top left position of the player X-coordinate.
     *
     * @return this returns the top left position of the player X-coordinate.
     */
    public int getPlayerTopLeftXStartPoint() {
        return playerTopLeftXStartPoint;
    }

    /**
     * this method is used to get the top left position of the player Y-coordinate.
     *
     * @return this returns the top left position of the player Y-coordinate.
     */
    public int getPlayerTopLeftYStartPoint() {
        return playerTopLeftYStartPoint;
    }

    /**
     * this method is used to get the top left position of the ball X-coordinate.
     *
     * @return this returns the top left position of the ball X-coordinate.
     */
    public int getBallTopLeftXStartPoint() {
        return ballTopLeftXStartPoint;
    }

    /**
     * this method is used to get the top left position of the ball X-coordinate.
     *
     * @return this returns the top left position of the ball X-coordinate.
     */
    public int getBallTopLeftYStartPoint() {
        return ballTopLeftYStartPoint;
    }

    /**
     * this method is used to set the player object into a variable for future reference. which includes moving the player paddle.
     *
     * @param player this is the player object used to set to a variable.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * this method is used to set a ball object into a variable for future reference. which includes moving the ball and getting the position of the ball for collision.
     *
     * @param mainBall this is the ball object that is going to be set into a variable.
     */
    public void setMainBall(Ball mainBall) {
        this.mainBall = mainBall;
    }

    /**
     * this method is used to get if the game is paused.
     *
     * @return this returns a boolean variable to confirm if it is paused or not paused.
     */
    public boolean isPauseMode() {
        return pauseMode;
    }

    /**
     * this method is used to check if the game is pause or no.
     *
     * @param pauseMode this is used to set the if the game paused or no.
     */
    public void setPauseMode(boolean pauseMode) {
        this.pauseMode = pauseMode;
    }

    /**
     * this method is used to add a ball clone into an arraylist.
     *
     * @param ball this is the ballClone object used to be added into an array list.
     */
    public void addCloneBall(BallClone ball){
        cloneBall.add(ball);
    }

    /**
     * this method is used to get the array list which contains the ball clones.
     *
     * @return this is the arraylist of ball clones.
     */
    public ArrayList<BallClone> getCloneBall(){
        return cloneBall;
    }

    /**
     * this method is used to randomly generate a clone ball and add it into an array once it is being created.
     */
    public void cloneBallRandomGenerator() {
        if(rnd.nextDouble() < cloneBallGenerationProbability && getCloneBall().size() < maxCloneBall){
            addCloneBall(new BallClone(new Point2D(getMainBall().getBounds().getMinX(), getMainBall().getBounds().getMinY())));
        }
    }
}
