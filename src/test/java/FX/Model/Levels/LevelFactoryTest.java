package FX.Model.Levels;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelFactoryTest {
    LevelFactory levelFactory = new LevelFactory();

    @Test
    void testMakeLevel() {
        assertEquals(levelFactory.makeLevel("chainlevel").getClass(), ChainWallLevel.class);
        assertEquals(levelFactory.makeLevel("curlylineslevel").getClass(), CurlyLinesWallLevel.class);
        assertEquals(levelFactory.makeLevel("randomlevel").getClass(), RandomWallLevel.class);
        assertEquals(levelFactory.makeLevel("straightlineslevel").getClass(), StraightLinesLevel.class);
        assertThrows(NullPointerException.class, () -> assertEquals(levelFactory.makeLevel("hellothere").getClass(), null));
    }
}