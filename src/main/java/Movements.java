/**
 * this class is used to manage the movements of the entities.
 */
public class Movements {

    private static Movements uniqueMovements;

    private Wall wall;
    Impact impact;

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
        impact = new Impact(this);
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
}
