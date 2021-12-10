package FX.Model.Entities.Brick;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrackTest {

    Crack crack = new Crack();
    Point2D point2D = new Point2D(0,0);
    Dimension2D dimension2D = new Dimension2D(50,20);
    CementBrick cementbrick = new CementBrick(point2D,dimension2D);

    @Test
    void testMakeCrack() {
        crack.makeCrack(new Point2D(0,0), new Point2D(50,20), cementbrick);
        assertTrue(cementbrick.getCrackPath() != null);
    }

    @Test
    void testMakeRandomPointBetween() {
        Point2D point1;
        Point2D point2;
        int tries = 0;
        do{
            point1 = crack.makeRandomPointBetween(new Point2D(0,0), new Point2D(50,20), Crackable.VERTICAL);
            point2 = crack.makeRandomPointBetween(new Point2D(0,0), new Point2D(50,20), Crackable.VERTICAL);
            System.out.println(point1);
            System.out.println(point2);
        }while (tries < 10 && point1.getX() == point2.getX() && point2.getY() == point1.getY());
        assertNotEquals(point1,point2);
    }
}