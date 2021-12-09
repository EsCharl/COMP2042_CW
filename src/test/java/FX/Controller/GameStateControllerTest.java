package FX.Controller;

import FX.Model.Entities.Brick.Brick;
import FX.Model.Entities.Brick.CementBrick;
import FX.Model.Entities.Brick.ClayBrick;
import FX.Model.Entities.Brick.Crackable;
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
    void testAutomation() {
        gameData.setBotMode(true);
        gameData.getBall().setBounds(new BoundingBox(1,1,20,20));
        gameStateController.automation();
        assertTrue(gameData.getPlayer().getMoveAmount() == -5);
    }

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
    void testBallLeftCollision() {
        int ballSpeed = gameData.getBall().getSpeedX();
        int tries = 0;
        do{
            gameStateController.gameData.getBall().ballLeftCollision();
            tries++;
        }while (gameData.getBall().getSpeedX() == -ballSpeed && tries < 200);
        assertNotEquals(gameData.getBall().getSpeedX(), -ballSpeed);
    }

    @Test
    void testBallRightCollision() {
        int ballSpeed = gameData.getBall().getSpeedX();
        int tries = 0;
        do{
            gameStateController.gameData.getBall().ballRightCollision();
            tries++;
        }while (gameData.getBall().getSpeedX() == -ballSpeed && tries < 200);
        assertNotEquals(gameData.getBall().getSpeedX(), -ballSpeed);
    }

    @Test
    void testBallTopCollision() {
        int ballSpeed = gameData.getBall().getSpeedY();
        int tries = 0;
        do{
            gameStateController.gameData.getBall().ballTopCollision();
            tries++;
        }while (gameData.getBall().getSpeedY() == -ballSpeed && tries < 200);
        assertNotEquals(gameData.getBall().getSpeedY(), -ballSpeed);
    }

    @Test
    void testBallBottomCollision() {
        int ballSpeed = gameData.getBall().getSpeedY();
        int tries = 0;
        do{
            gameStateController.gameData.getBall().ballBottomCollision();
            tries++;
        }while (gameData.getBall().getSpeedY() == -ballSpeed && tries < 200);
        assertNotEquals(gameData.getBall().getSpeedY(), -ballSpeed);
    }

    @Test
    void testImpact() {
        Brick brick = new ClayBrick(new Point2D(0,0), new Dimension2D(50,20));
        gameData.getBall().setBounds(new BoundingBox(0,0,20,20));
        assertTrue(gameStateController.impact(gameData.getBall(), brick));
    }
}