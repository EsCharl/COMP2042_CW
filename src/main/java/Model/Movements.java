package Model;

import Model.Brick.Brick;
import Model.Brick.Crack;

import java.util.Random;

/**
 * this class is used to manage the movements of the entities.
 */
public class Movements {
    private final int UP_IMPACT = 100;
    private final int DOWN_IMPACT = 200;
    private final int LEFT_IMPACT = 300;
    private final int RIGHT_IMPACT = 400;

    private final int MAX_BALL_SPEED = 5;

    private static Movements uniqueMovements;

    private Game game;
    private Random rnd;

    /**
     * this method is used to create one and only one movement class. singleton design pattern.
     *
     * @param game this is the wall object used to create the movements object.
     * @return this returns the one and only movement object.
     */
    public static Movements singletonMovements(Game game){
        if(getUniqueMovements() == null){
            setUniqueMovements(new Movements(game));
        }
        return getUniqueMovements();
    }

    /**
     * this constructor is used to create a movements object.
     *
     * @param game this is the game object used to create the movements as it is needed for data manipulation.
     */
    private Movements(Game game){
        setGame(game);
        setRnd(new Random());
    }

    /**
     * this method is used to get the one and only singleton movement object after the singletonMovements method is called.
     *
     * @return this returns the one and only movements object.
     */
    private static Movements getUniqueMovements() {
        return uniqueMovements;
    }

    /**
     * this method is used to set movements object into a variable which will be used for future reference.
     *
     * @param uniqueMovements this is the movements object used to set the variable.
     */
    private static void setUniqueMovements(Movements uniqueMovements) {
        Movements.uniqueMovements = uniqueMovements;
    }

    /**
     * This method is used for the movement of the player (paddle) and the ball.
     */
    public void entitiesMovements(){
        getGame().getPlayer().move();
        getGame().getBall().move();
    }

    /**
     * this method is used to get the wall object. which is used to get the information of the wall.
     *
     * @return returns a wall object.
     */
    public Game getGame() {
        return game;
    }

    /**
     * this method is used to set the wall object in a variable for future use.
     *
     * @param game this is the wall object used to set the variable for future use.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * this is to check if the ball comes in contact with any side of the brick.
     *
     * @return returns a boolean value if or if it doesn't touch any entity.
     */
    boolean impactWall(){
        for(Brick b : getGame().getBricks()){
            if(b.findImpact(getGame().getBall()) == getUP_IMPACT()){
                getGame().getBall().reverseY();
                return b.setImpact(getGame().getBall().getDown(),Crack.getUP());
            }
            else if (b.findImpact(getGame().getBall()) == getDOWN_IMPACT()){
                getGame().getBall().reverseY();
                return b.setImpact(getGame().getBall().getUp(),Crack.getDOWN());
            }
            else if(b.findImpact(getGame().getBall()) == getLEFT_IMPACT()){
                getGame().getBall().reverseX();
                return b.setImpact(getGame().getBall().getRight(),Crack.getRIGHT());
            }
            else if(b.findImpact(getGame().getBall()) == getRIGHT_IMPACT()){
                getGame().getBall().reverseX();
                return b.setImpact(getGame().getBall().getLeft(), Crack.getLEFT());
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
        return ((getGame().getBall().getLeft().getX() < getGame().getBorderArea().getX()) ||(getGame().getBall().getRight().getX() > (getGame().getBorderArea().getX() + getGame().getBorderArea().getWidth())));
    }

    /**
     * this method is used to check if there is an impact for the ball with any entity, the sides of the screen. which will cause a reaction to the game.
     */
    public void findImpacts(){
        if(getGame().getPlayer().impact(getGame().getBall())){
            getGame().getBall().reverseY();
            if(getRnd().nextBoolean() && getGame().getBall().getYSpeed() > -4){
                getGame().getBall().setYSpeed(getGame().getBall().getYSpeed()-1);
            }
            else if(getRnd().nextBoolean() && getGame().getBall().getYSpeed() < -1){
                getGame().getBall().setYSpeed(getGame().getBall().getYSpeed()+1);
            }
        }

        else if(impactWall()){
            getGame().setBrickCount(getGame().getBrickCount()-1);
        }

        if(impactSideBorder()) {
            getGame().getBall().reverseX();
            if(getRnd().nextBoolean() && (getGame().getBall().getXSpeed() > -4 && getGame().getBall().getXSpeed() < 4) ){
                if(getGame().getBall().getXSpeed() < 0)
                    getGame().getBall().setXSpeed(getGame().getBall().getXSpeed()-1);
                else
                    getGame().getBall().setXSpeed(getGame().getBall().getXSpeed()+1);
            }else if(getRnd().nextBoolean()){
                if(getGame().getBall().getXSpeed() < -1)
                    getGame().getBall().setXSpeed(getGame().getBall().getXSpeed()+1);
                else if(getGame().getBall().getXSpeed() > 1)
                    getGame().getBall().setXSpeed(getGame().getBall().getXSpeed()-1);
            }
            System.out.println(getGame().getBall().getXSpeed()+ "," + getGame().getBall().getYSpeed());
        }

        if(getGame().getBall().getUp().getY() < getGame().getBorderArea().getY()){
            getGame().getBall().reverseY();

            if(getRnd().nextBoolean() && getGame().getBall().getYSpeed() < 4)
                getGame().getBall().setYSpeed(getGame().getBall().getYSpeed()+1);
            else if(getRnd().nextBoolean() && getGame().getBall().getYSpeed() > 1)
                getGame().getBall().setYSpeed(getGame().getBall().getYSpeed()-1);
        }
        else if(getGame().getBall().getCenterPosition().getY() > getGame().getBorderArea().getY() + getGame().getBorderArea().getHeight()){
            getGame().setBallCount(getGame().getBallCount() - 1);
            getGame().setBallLost(true);
        }
    }

    /**
     * this method is used to set the random speed on both x-axis and y-axis for the ball.
     */
    void setRandomBallSpeed(){
        int speedX,speedY;

        // changes here, makes the maximum speed it can go on x-axis in between -max speed and max speed.
        do {
            speedX = getRnd().nextBoolean() ? getRnd().nextInt(getMAX_BALL_SPEED()) : -getRnd().nextInt(getMAX_BALL_SPEED());
        } while (speedX == 0);

        do{
            speedY = -getRnd().nextInt(getMAX_BALL_SPEED());
        }while(speedY == 0);

        getGame().getBall().setSpeed(speedX,speedY);
    }

    public Random getRnd() {
        return rnd;
    }

    public void setRnd(Random rnd) {
        this.rnd = rnd;
    }

    public int getUP_IMPACT() {
        return UP_IMPACT;
    }

    public int getDOWN_IMPACT() {
        return DOWN_IMPACT;
    }

    public int getLEFT_IMPACT() {
        return LEFT_IMPACT;
    }

    public int getRIGHT_IMPACT() {
        return RIGHT_IMPACT;
    }

    public int getMAX_BALL_SPEED() {
        return MAX_BALL_SPEED;
    }
}
