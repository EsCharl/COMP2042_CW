package FX.Controller;

import FX.Model.Entities.Brick.Brick;
import FX.Model.Entities.Brick.CementBrick;
import FX.Model.Entities.Brick.ClayBrick;
import FX.Model.Entities.Brick.Crackable;
import FX.Model.Entities.Player;
import FX.Model.Game;
import javafx.geometry.BoundingBox;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameStateControllerTest {

    GameStateController gameStateController = new GameStateController();
    Game game = Game.singletonGame();

    @Test
    void testAutomation() {
        game.setBotMode(true);
        game.getBall().setBounds(new BoundingBox(1,1,20,20));
        gameStateController.automation();
        assertTrue(game.getPlayer().getMoveAmount() == -5);
    }

    @Test
    void testMovementKeyHandlerPlayerLeft(){
        ArrayList input = new ArrayList();
        input.add(KeyCode.A);
        gameStateController.movementKeyHandler(input);
        assertTrue(game.getPlayer().getMoveAmount() == -5);
    }

    @Test
    void testMovementKeyHandlerPlayerRight(){
        ArrayList input = new ArrayList();
        input.add(KeyCode.D);
        gameStateController.movementKeyHandler(input);
        assertTrue(game.getPlayer().getMoveAmount() == 5);
    }

    @Test
    void testMovementKeyHandlerPlayerStay(){
        ArrayList input = new ArrayList();
        input.add(KeyCode.D);
        input.add(KeyCode.A);
        gameStateController.movementKeyHandler(input);
        assertTrue(game.getPlayer().getMoveAmount() == 0);
    }

    @Test
    void testNonMovementHandlerBot(){
        ArrayList input = new ArrayList();
        Boolean beforeIsBot = game.isBotMode();
        input.add(KeyCode.H);
        gameStateController.nonMovementKeyHandler(input);
        assertTrue(game.isBotMode() != beforeIsBot);
    }

    @Test
    void testBallLeftCollision() {
        int ballSpeed = game.getBall().getSpeedX();
        int tries = 0;
        do{
            gameStateController.ballLeftCollision();
            tries++;
        }while (game.getBall().getSpeedX() == -ballSpeed && tries < 200);
        assertNotEquals(game.getBall().getSpeedX(), -ballSpeed);
    }

    @Test
    void testBallRightCollision() {
        int ballSpeed = game.getBall().getSpeedX();
        int tries = 0;
        do{
            gameStateController.ballRightCollision();
            tries++;
        }while (game.getBall().getSpeedX() == -ballSpeed && tries < 200);
        assertNotEquals(game.getBall().getSpeedX(), -ballSpeed);
    }

    @Test
    void testBallTopCollision() {
        int ballSpeed = game.getBall().getSpeedY();
        int tries = 0;
        do{
            gameStateController.ballTopCollision();
            tries++;
        }while (game.getBall().getSpeedY() == -ballSpeed && tries < 200);
        assertNotEquals(game.getBall().getSpeedY(), -ballSpeed);
    }

    @Test
    void testBallBottomCollision() {
        int ballSpeed = game.getBall().getSpeedY();
        int tries = 0;
        do{
            gameStateController.ballBottomCollision();
            tries++;
        }while (game.getBall().getSpeedY() == -ballSpeed && tries < 200);
        assertNotEquals(game.getBall().getSpeedY(), -ballSpeed);
    }

    @Test
    void testImpact() {
        Brick brick = new ClayBrick(new Point2D(0,0), new Dimension2D(50,20));
        game.getBall().setBounds(new BoundingBox(0,0,20,20));
        assertTrue(gameStateController.impact(game.getBall(), brick));
    }

    @Test
    void testSetImpact() {
        CementBrick cement = new CementBrick(new Point2D(0,0), new Dimension2D(50,20));
        gameStateController.setImpact(new Point2D(20,0), Crackable.LEFT, cement);
        assertAll(  () -> assertTrue(gameStateController.setImpact(new Point2D(20,0), Crackable.LEFT, cement)),
                    () -> assertFalse(cement.getCrackPath() == null),
                    () -> assertFalse(gameStateController.setImpact(new Point2D(20,0), Crackable.LEFT, cement))
        );
    }
}