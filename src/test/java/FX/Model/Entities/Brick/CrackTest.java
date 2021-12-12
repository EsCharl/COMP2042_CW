package FX.Model.Entities.Brick;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrackTest {

    Crack crack = Crack.singletonCrack();
    Point2D point2D = new Point2D(0,0);
    Dimension2D dimension2D = new Dimension2D(50,20);
    CementBrick cementbrick1 = new CementBrick(point2D,dimension2D);
    CementBrick cementbrick2 = new CementBrick(point2D,dimension2D);

    @Test
    void testMakeCrack() {
        crack.makeCrack(new Point2D(0,0), new Point2D(50,20), cementbrick1);
        crack.makeCrack(new Point2D(0,0), new Point2D(50,20), cementbrick2);
        assertTrue(cementbrick1.getCrackPath() != null);
        assertTrue(cementbrick2.getCrackPath() != null);
        assertFalse(cementbrick1.equals(cementbrick2));
    }

    @Test
    void testMakeRandomPointBetween() {
        Point2D point1;
        Point2D point2;
        int tries = 0;
        do{
            point1 = crack.makeRandomPointBetween(new Point2D(0,0), new Point2D(50,20), crack.getVERTICAL());
            point2 = crack.makeRandomPointBetween(new Point2D(0,0), new Point2D(50,20), crack.getVERTICAL());
        }while (tries < 10 && point1.getX() == point2.getX() && point2.getY() == point1.getY());
        assertFalse(point1.equals(point2));
    }


}