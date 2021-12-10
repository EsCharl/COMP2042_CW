package FX.Controller;

import FX.Model.Entities.Brick.Brick;
import FX.Model.Entities.Brick.ClayBrick;
import FX.Model.GameData;
import javafx.geometry.BoundingBox;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameStateControllerTest {

    GameStateController gameStateController = new GameStateController();
    GameData gameData = GameData.singletonGame();

    @Test
    void testMovementKeyHandlerPlayerLeft(){
        ArrayList input = new ArrayList();
        input.add(KeyCode.A);
        gameStateController.movementKeyHandler(input);
        assertTrue(gameData.getPlayer().getMoveAmount() == -5);
    }

    @Test
    void testMovementKeyHandlerPlayerRight(){
        ArrayList input = new ArrayList();
        input.add(KeyCode.D);
        gameStateController.movementKeyHandler(input);
        assertTrue(gameData.getPlayer().getMoveAmount() == 5);
    }

    @Test
    void testMovementKeyHandlerPlayerStay(){
        ArrayList input = new ArrayList();
        input.add(KeyCode.D);
        input.add(KeyCode.A);
        gameStateController.movementKeyHandler(input);
        assertTrue(gameData.getPlayer().getMoveAmount() == 0);
    }

    @Test
    void testNonMovementHandlerBot(){
        ArrayList input = new ArrayList();
        Boolean beforeIsBot = gameData.isBotMode();
        input.add(KeyCode.H);
        gameStateController.nonMovementKeyHandler(input);
        assertTrue(gameData.isBotMode() != beforeIsBot);
    }

    @Test
    void testImpact() {
        Brick brick = new ClayBrick(new Point2D(0,0), new Dimension2D(50,20));
        gameData.getMainBall().setBounds(new BoundingBox(0,0,20,20));
        assertTrue(gameData.getMainBall().getBounds().intersects(brick.getBounds()));
    }
}