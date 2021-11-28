/**
 * this class is used to deal with impacts by caused by the ball,bricks(wall), player(paddle).
 */
public class Impact {

    private final int UP_IMPACT = 100;
    private final int DOWN_IMPACT = 200;
    private final int LEFT_IMPACT = 300;
    private final int RIGHT_IMPACT = 400;

    private Movements movements;

    public Impact(Movements movements){
        this.movements = movements;
    }
    /**
     * this method is used to check if there is an impact for the ball with any entity, the sides of the screen. which will cause a reaction to the game.
     */
    public void findImpacts(){
        if(movements.getWall().getPlayer().impact(movements.getWall().getBall())){
            movements.getWall().getBall().reverseY();
        }
        else if(movements.impact.impactWall()){
            movements.getWall().setBrickCount(movements.getWall().getBrickCount()-1);
        }

        if(movements.impact.impactSideBorder()) {
            movements.getWall().getBall().reverseX();
        }

        if(movements.getWall().getBall().getPosition().getY() < movements.getWall().getBorderArea().getY()){
            movements.getWall().getBall().reverseY();
        }
        else if(movements.getWall().getBall().getPosition().getY() > movements.getWall().getBorderArea().getY() + movements.getWall().getBorderArea().getHeight()){
            movements.getWall().setBallCount(movements.getWall().getBallCount() - 1);
            movements.getWall().setBallLost(true);
        }
    }

    /**
     * this method is used to check if the ball have come in contact with the vertical sides of the game window.
     *
     * @return this returns a boolean value if it touches or doesn't touch the side of the game window
     */
    boolean impactSideBorder(){
        return ((movements.getWall().getBall().getPosition().getX() < movements.getWall().getBorderArea().getX()) ||(movements.getWall().getBall().getPosition().getX() > (movements.getWall().getBorderArea().getX() + movements.getWall().getBorderArea().getWidth())));
    }

    /**
     * this is to check if the ball comes in contact with any side of the brick.
     *
     * @return returns a boolean value if or if it doesn't touch any entity.
     */
    boolean impactWall(){
        for(Brick b : movements.getWall().getBricks()){
            switch(b.findImpact(movements.getWall().getBall())) {
                //Vertical Impact
                case UP_IMPACT:
                    movements.getWall().getBall().reverseY();
                    return b.setImpact(movements.getWall().getBall().getDown(),Crack.UP);
                case DOWN_IMPACT:
                    movements.getWall().getBall().reverseY();
                    return b.setImpact(movements.getWall().getBall().getUp(),Crack.DOWN);

                //Horizontal Impact
                case LEFT_IMPACT:
                    movements.getWall().getBall().reverseX();
                    return b.setImpact(movements.getWall().getBall().getRight(),Crack.RIGHT);
                case RIGHT_IMPACT:
                    movements.getWall().getBall().reverseX();
                    return b.setImpact(movements.getWall().getBall().getLeft(),Crack.LEFT);
            }
        }
        return false;
    }
}
