package FX.Model;

import javafx.geometry.BoundingBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game = Game.singletonGame(500,600);

    @Test
    void testWallReset(){
        game.getBrickLevels()[0][1].setCurrentStrength(0);
        boolean isBroken = game.getBrickLevels()[0][1].isBroken();
        game.wallReset();
        assertEquals(isBroken, game.getBrickLevels()[0][1].isBroken());
    }

    @Test
    void testAutomation() {
        game.getPlayer().setBotMode(true);
        game.getMainBall().setBounds(new BoundingBox(1,1,20,20));
        game.automation();
        assertTrue(game.getPaddle().getMoveAmount() == -5);
    }
}