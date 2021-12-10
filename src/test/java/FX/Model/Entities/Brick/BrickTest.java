package FX.Model.Entities.Brick;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrickTest {

    Point2D point2D = new Point2D(0,0);
    Dimension2D dimension2D = new Dimension2D(50,20);
    Brick clayBrick = new ClayBrick(point2D,dimension2D);
    CementBrick cementbrick = new CementBrick(point2D,dimension2D);

    @Test
    void testSetImpact() {
        clayBrick.setImpact(new Point2D(10,0),Crackable.UP);
        cementbrick.setImpact(point2D,Crackable.DOWN);

        assertAll(  ()->assertTrue(clayBrick.isBroken()),
                    ()->assertTrue(cementbrick.getCrackPath()!=null),
                    ()->assertFalse(cementbrick.isBroken())
        );
    }
}