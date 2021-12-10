package FX.Model;

import javafx.geometry.BoundingBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameDataTest {

    GameData gameData = GameData.singletonGame();

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
        gameData.getMainBall().setBounds(new BoundingBox(1,1,20,20));
        gameData.automation();
        assertTrue(gameData.getPlayer().getMoveAmount() == -5);
    }

    @Test
    void testImpactWall() {
        gameData.getMainBall().setBounds(new BoundingBox(0,0,10,10));
        gameData.impactWall(gameData.getMainBall());
        assertTrue(gameData.getBrickLevels()[0][0].isBroken());
    }

    @Test
    void testCloneBallRandomGenerator() {
        int initialSize = gameData.getCloneBall().size();
        for (int i =0; i < 10; i++)
            gameData.cloneBallRandomGenerator();
        assertNotEquals(initialSize, gameData.getCloneBall().size());
    }
}