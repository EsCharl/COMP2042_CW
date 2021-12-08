package FX.Model;

import FX.Model.Entities.Brick.Brick;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game = Game.singletonGame();

    @Test
    void testGetBrickLevels() {
        assertTrue(game.getBrickLevels().length == 7);
    }

    @Test
    void testSetRandomBallSpeed() {
        game.getBall();
        game.getBall().setSpeedX(0);
        game.getBall().setSpeedY(0);
        game.setRandomBallSpeed(game.getBall());
        assertAll(  () -> assertNotEquals(game.getBall().getSpeedX(), 0),
                    () -> assertNotEquals(game.getBall().getSpeedY(), 0)
        );
    }

    @Test
    void testWallReset(){
        game.getBrickLevels()[0][1].setCurrentStrength(0);
        boolean isBroken = game.getBrickLevels()[0][1].isBroken();
        game.wallReset();
        assertEquals(isBroken, game.getBrickLevels()[0][1].isBroken());
    }
}