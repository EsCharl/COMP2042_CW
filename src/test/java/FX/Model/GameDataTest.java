package FX.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameDataTest {

    GameData gameData = GameData.singletonGame();

    @Test
    void testGetBrickLevels() {
        assertTrue(gameData.getBrickLevels().length == 7);
    }

    @Test
    void testSetRandomBallSpeed() {
        gameData.getBall();
        gameData.getBall().setSpeedX(0);
        gameData.getBall().setSpeedY(0);
        gameData.setRandomBallSpeed(gameData.getBall());
        assertAll(  () -> assertNotEquals(gameData.getBall().getSpeedX(), 0),
                    () -> assertNotEquals(gameData.getBall().getSpeedY(), 0)
        );
    }

    @Test
    void testWallReset(){
        gameData.getBrickLevels()[0][1].setCurrentStrength(0);
        boolean isBroken = gameData.getBrickLevels()[0][1].isBroken();
        gameData.wallReset();
        assertEquals(isBroken, gameData.getBrickLevels()[0][1].isBroken());
    }
}