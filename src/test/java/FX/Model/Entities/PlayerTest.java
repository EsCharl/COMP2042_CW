package FX.Model.Entities;

import FX.Model.GameData;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    GameData gameData = GameData.singletonGame();
    Rectangle filler1 = new Rectangle(0,0,50,50);
    Point2D filler2 = new Point2D(10,0);
    Player player = Player.singletonPlayer(filler2,filler1);

    @Test
    void testMove() {
        player.setMoveAmount(-20);
        player.move();
        assertEquals(player.getBounds().getMinX() , gameData.getPlayerTopLeftXStartPoint()-20);
    }

    @Test
    void testResetPosition(){
        player.setMoveAmount(5);
        player.move();
        player.resetPosition();
        assertEquals(player.getBounds().getMinX() , gameData.getPlayerTopLeftXStartPoint());
    }
}