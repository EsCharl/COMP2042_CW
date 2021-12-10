package FX.Model;

import javafx.geometry.BoundingBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameDataTest {

    GameData gameData = GameData.singletonGame();

    @Test
    void testGetBrickLevels() {
        assertTrue(gameData.getBrickLevels().length == 7);
    }

    @Test
    void testWallReset(){
        gameData.getBrickLevels()[0][1].setCurrentStrength(0);
        boolean isBroken = gameData.getBrickLevels()[0][1].isBroken();
        gameData.wallReset();
        assertEquals(isBroken, gameData.getBrickLevels()[0][1].isBroken());
    }

    @Test
    void testAutomation() {
        gameData.setBotMode(true);
        gameData.getBall().setBounds(new BoundingBox(1,1,20,20));
        gameData.automation();
        assertTrue(gameData.getPlayer().getMoveAmount() == -5);
    }

    @Test
    void testImpactWall() {
        gameData.getBall().setBounds(new BoundingBox(0,0,10,10));
        gameData.impactWall(gameData.getBall());
        assertTrue(gameData.getBrickLevels()[0][0].isBroken());
    }
}