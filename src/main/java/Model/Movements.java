package Model;

import Model.Brick.Brick;
import Model.Brick.Crack;

/**
 * this class is used to manage the movements of the entities.
 */
public class Movements {
    private final int UP_IMPACT = 100;
    private final int DOWN_IMPACT = 200;
    private final int LEFT_IMPACT = 300;
    private final int RIGHT_IMPACT = 400;

    private static Movements uniqueMovements;

    private Wall wall;

    /**
     * this method is used to create one and only one movement class. singleton design pattern.
     *
     * @param wall this is the wall object used to create the movements object.
     * @return this returns the one and only movement object.
     */
    public static Movements singletonMovements(Wall wall){
        if(getUniqueMovements() == null){
            setUniqueMovements(new Movements(wall));
        }
        return getUniqueMovements();
    }

    private Movements(Wall wall){
        setWall(wall);
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
        getWall().getPlayer().move();
        getWall().getBall().move();
    }

    /**
     * this method is used to get the wall object. which is used to get the information of the wall.
     *
     * @return returns a wall object.
     */
    public Wall getWall() {
        return wall;
    }

    /**
     * this method is used to set the wall object in a variable for future use.
     *
     * @param wall this is the wall object used to set the variable for future use.
     */
    public void setWall(Wall wall) {
        this.wall = wall;
    }

    /**
     * this is to check if the ball comes in contact with any side of the brick.
     *
     * @return returns a boolean value if or if it doesn't touch any entity.
     */
    boolean impactWall(){
        for(Brick b : getWall().getBricks()){
            switch(b.findImpact(getWall().getBall())) {
                //Vertical Impact
                case UP_IMPACT:
                    getWall().getBall().reverseY();
                    return b.setImpact(getWall().getBall().getDown(),Crack.UP);
                case DOWN_IMPACT:
                    getWall().getBall().reverseY();
                    return b.setImpact(getWall().getBall().getUp(),Crack.DOWN);

                //Horizontal Impact
                case LEFT_IMPACT:
                    getWall().getBall().reverseX();
                    return b.setImpact(getWall().getBall().getRight(),Crack.RIGHT);
                case RIGHT_IMPACT:
                    getWall().getBall().reverseX();
                    return b.setImpact(getWall().getBall().getLeft(), Crack.LEFT);
            }
        }
        return false;
    }

    /**
     * this method is used to check if the ball have come in contact with the vertical sides of the game window.
     *
     * @return this returns a boolean value if it touches or doesn't touch the side of the game window
     */
    boolean impactSideBorder(){
        return ((getWall().getBall().getPosition().getX() < getWall().getBorderArea().getX()) ||(getWall().getBall().getPosition().getX() > (getWall().getBorderArea().getX() + getWall().getBorderArea().getWidth())));
    }

    /**
     * this method is used to check if there is an impact for the ball with any entity, the sides of the screen. which will cause a reaction to the game.
     */
    public void findImpacts(){
        if(getWall().getPlayer().impact(getWall().getBall())){
            getWall().getBall().reverseY();
        }
        else if(impactWall()){
            getWall().setBrickCount(getWall().getBrickCount()-1);
        }

        if(impactSideBorder()) {
            getWall().getBall().reverseX();
        }

        if(getWall().getBall().getPosition().getY() < getWall().getBorderArea().getY()){
            getWall().getBall().reverseY();
        }
        else if(getWall().getBall().getPosition().getY() > getWall().getBorderArea().getY() + getWall().getBorderArea().getHeight()){
            getWall().setBallCount(getWall().getBallCount() - 1);
            getWall().setBallLost(true);
        }
    }
}
