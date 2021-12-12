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
import FX.Model.Entities.Paddle;
import FX.Model.Entities.Player;
import FX.Model.Levels.LevelFactory;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * this class is used to generate the level and maintain some game condition.
 */
public class Game {
    private final double BRICK_DIMENSION_RATIO = 6/2;
    private final int MAKE_LEVEL_BRICK_COUNT = 30;
    private final int MAKE_LEVEL_LINE_COUNT = 3;

    private final int LEVELS_AMOUNT = 7;

    private Brick[] bricks;
    private Brick[][] brickLevels;

    private int brickCount;
    private boolean ballLost;

    private static Game uniqueGame;
    private ArrayList<BallClone> cloneBall;
    private Paddle paddle;
    private Ball mainBall;
    private Player player;

    private Rectangle playArea;

    private boolean showPauseMenu;

    /**
     * this method is used to create a game object based on the Singleton design pattern.
     */
    public static Game singletonGame(double gameAreaWidth, double gameAreaHeight){
        if(getUniqueGame() == null){
            setUniqueGame(new Game(gameAreaWidth, gameAreaHeight));
        }
        return getUniqueGame();
    }

    /**
     * this constructor is used to generate an object which is the wall used for the levels.
     */
    private Game(double gameAreaWidth, double gameAreaHeight){
        setCloneBall(new ArrayList<>());
        setPlayer(Player.singletonPlayer());

        setShowPauseMenu(false);

        setPlayArea(new Rectangle(0,0, gameAreaWidth, gameAreaHeight));

        setBallLost(false);

        setBrickLevels(makeLevels(getPlayArea(), MAKE_LEVEL_BRICK_COUNT,MAKE_LEVEL_LINE_COUNT,BRICK_DIMENSION_RATIO));

        nextLevel();

        setPaddle(Paddle.singletonPaddle(getPlayArea()));
        setMainBall(new RubberBall());
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
        Brick[][] tmp = new Brick[LEVELS_AMOUNT][];
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
        getPlayer().resetBallCount();
    }

    /**
     * this method is used to progress to the next level.
     */
    public void nextLevel(){
        setBricks(getBrickLevels()[getPlayer().getCurrentLevel()]);
        getPlayer().setCurrentLevel(getPlayer().getCurrentLevel()+1);
        setBrickCount(getBricks().length);
    }

    /**
     * this method is used to let the bot control the paddle instead of the player playing it.
     */
    public void automation(){
        if(getPlayer().isBotMode()){
            if(getMainBall().getBounds().getMinX() > getPaddle().getBounds().getMinX() + getPaddle().getBounds().getWidth()/2){
                getPaddle().setMoveAmount(getPaddle().getDEF_MOVE_AMOUNT());
            }else{
                getPaddle().setMoveAmount(-getPaddle().getDEF_MOVE_AMOUNT());
            }
        }
    }

    /**
     * this method is used to reset the game position and the clone balls.
     */
    public void restartStatus() {
        getMainBall().resetPosition();
        getPaddle().resetPosition();
        getMainBall().setRandomBallSpeed();
        getPlayer().resetBallCount();
        getCloneBall().clear();
    }

    /**
     * this method is used to get the paddle object which is used to move, set and update the paddle object.
     *
     * @return this returns a paddle object.
     */
    public Paddle getPaddle() {
        return paddle;
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
    private static Game getUniqueGame() {
        return uniqueGame;
    }

    /**
     * this method is used to set the one and only wall object and enable it to be reused in the future. Singleton design.
     *
     * @param uniqueGame this is the variable used to set the wall object into a variable.
     */
    private static void setUniqueGame(Game uniqueGame) {
        Game.uniqueGame = uniqueGame;
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
     * this method is used to set the brick amount for a level.
     *
     * @param brickCount this is the value of bricks being used for the level.
     */
    public void setBrickCount(int brickCount) {
        this.brickCount = brickCount;
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
     * this method is used to see if the game is in pause menu.
     *
     * @return returns true if it is.
     */
    public boolean isShowPauseMenu() {
        return showPauseMenu;
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
     * this method is used to set the paddle object into a variable for future reference. which includes moving the paddle paddle.
     *
     * @param paddle this is the paddle object used to set to a variable.
     */
    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
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
     * this method is used to get the array list which contains the ball clones.
     *
     * @return this is the arraylist of ball clones.
     */
    public ArrayList<BallClone> getCloneBall(){
        return cloneBall;
    }

    /**
     * this method is used to add a ball clone into an arraylist.
     *
     * @param ball this is the ballClone object used to be added into an array list.
     */
    public void addCloneBall(BallClone ball){
        this.cloneBall.add(ball);
    }

    /**
     * this method is used to get the clone ball array which stores all the ball clones.
     *
     * @param cloneBall this is the clone ball arraylist used to be stored into a variable.
     */
    public void setCloneBall(ArrayList<BallClone> cloneBall) {
        this.cloneBall = cloneBall;
    }

    /**
     * this method is used to get the player object which contains all the information about the player current level progress.
     *
     * @return this returns a player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * this method is used to set a player object into a variable.
     *
     * @param player this is the player object used to set into a variable for future reference.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
