package FX.Model.Entities.Ball;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * this class is an addition which creates a clone ball to increase the game enjoyability.
 */
public class BallClone extends Ball {
    private static final int DEF_RADIUS = 5;

    private static final Color DEF_INNER_COLOR = Color.rgb(10, 100, 150,1);
    private static final Color DEF_BORDER_COLOR = DEF_INNER_COLOR.darker().darker();

    /**
     * this constructor is used to create a ball clone which is going to be used in the game.
     *
     * @param center this is the starting position where the ball clone will start.
     */
    public BallClone(Point2D center){
        super(center,DEF_RADIUS,DEF_INNER_COLOR,DEF_BORDER_COLOR);
    }
}
