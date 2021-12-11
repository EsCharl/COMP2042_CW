package FX.Model.Entities;

import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaddleTest {

    Rectangle filler1 = new Rectangle(0,0,500,500);
    Paddle paddle = Paddle.singletonPaddle(filler1);

    @Test
    void testMove() {
        paddle.setMoveAmount(-20);
        paddle.move();
        System.out.println();
        assertEquals(paddle.getBounds().getMinX() , paddle.getFixedInitialPosition().getX()-20);
    }

    @Test
    void testNotMove(){
        paddle.setMoveAmount(-300);
        paddle.move();
        assertEquals(paddle.getBounds().getMinX() , paddle.getBounds().getMinX());
    }

    @Test
    void testResetPosition(){
        paddle.setMoveAmount(5);
        paddle.move();
        paddle.resetPosition();
        assertEquals(paddle.getBounds().getMinX() , paddle.getFixedInitialPosition().getX());
        assertEquals(paddle.getBounds().getMinY() , paddle.getFixedInitialPosition().getY());
    }
}