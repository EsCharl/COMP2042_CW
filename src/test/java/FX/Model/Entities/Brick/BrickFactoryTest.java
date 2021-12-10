package FX.Model.Entities.Brick;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrickFactoryTest {
    BrickFactory brickFactory = new BrickFactory();

    @Test
    void testMakeBrick() {
        assertTrue(brickFactory.makeBrick(new Point2D(0,0), new Dimension2D(50,20), BrickFactory.CLAY).getClass() == ClayBrick.class);
        assertTrue(brickFactory.makeBrick(new Point2D(0,0), new Dimension2D(50,20), BrickFactory.CEMENT).getClass() == CementBrick.class);
        assertTrue(brickFactory.makeBrick(new Point2D(0,0), new Dimension2D(50,20), BrickFactory.STEEL).getClass() == SteelBrick.class);
        assertTrue(brickFactory.makeBrick(new Point2D(0,0), new Dimension2D(50,20), BrickFactory.REINFORCED_STEEL).getClass() == ReinforcedSteelBrick.class);
        assertThrows(IllegalArgumentException.class, () -> assertTrue(brickFactory.makeBrick(new Point2D(0,0), new Dimension2D(50,20), 0).getClass() == ReinforcedSteelBrick.class));
    }
}