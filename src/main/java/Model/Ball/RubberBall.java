package Model.Ball;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * this class is for the rubber ball, an extension from ball abstract class.
 */
public class RubberBall extends Ball {


    private static final int DEF_DIAMETER = 10;
    private static final Color DEF_INNER_COLOR = new Color(255, 219, 88);
    private static final Color DEF_BORDER_COLOR = DEF_INNER_COLOR.darker().darker();

    /**
     * this constructor is used to create a rubber ball.
     *
     * @param center this is the position where the ball is created.
     */
    public RubberBall(Point2D center){
        super(center,DEF_DIAMETER,DEF_DIAMETER,DEF_INNER_COLOR,DEF_BORDER_COLOR);
    }

    /**
     * this method is used to create a shape of the ball.
     *
     * @param center this is the position where the ball is formed
     * @param horizontalDiameter this is the diameter of the ball based on x-axis
     * @param verticalDiameter this is the diameter of the ball based on y-axis.
     * @return it returns a shape of the ball on a position specified in center.
     */
    @Override
    protected Shape makeBall(Point2D center, int horizontalDiameter, int verticalDiameter) {

        return new Ellipse2D.Double(getRubberBallLeftXCoordinate(center, horizontalDiameter), getRubberBallTopYCoordinate(center, verticalDiameter),horizontalDiameter,verticalDiameter);
    }

    /**
     * this method is used to get the left side of the rubber ball x - axis.
     *
     * @param center this is the point2D of the center of the ball.
     * @param horizontalDiameter this is the Horizontal diameter of the ball.
     * @return returns a double value which is the value of the x - coordinate for the left side of the ball.
     */
    private double getRubberBallLeftXCoordinate(Point2D center, int horizontalDiameter){
        return center.getX() - (horizontalDiameter / 2);
    }

    /**
     * this method is used to get the up side of the rubber ball y-axis.
     *
     * @param center this is the point2D of the center of the Ball.
     * @param verticalDiameter this is the vertical diameter of the ball.
     * @return this returns a double value where the y - coordinate for the top side of the ball.
     */
    private double getRubberBallTopYCoordinate(Point2D center, int verticalDiameter){
        return center.getY() - (verticalDiameter / 2);
    }
}
