package FX.Model.Entities.Ball;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BallTest {

    RubberBall ball = new RubberBall();

    @Test
    void testSetRandomBallSpeed() {
        ball.setSpeedX(0);
        ball.setSpeedY(0);
        ball.setRandomBallSpeed();
        assertAll(  () -> assertNotEquals(ball.getSpeedX(), 0),
                () -> assertNotEquals(ball.getSpeedY(), 0)
        );
    }

    @Test
    void testResetPosition(){
        BoundingBox box = new BoundingBox(ball.getFixedInitialPosition().getX(),ball.getFixedInitialPosition().getY(), ball.getWidth(), ball.getHeight());
        ball.setSpeedX(5);
        ball.move();
        ball.resetPosition();
        assertAll(  ()->  assertEquals(ball.getBounds().getMinX(), box.getMinX()),
                    ()->  assertEquals(ball.getBounds().getMinY(), box.getMinY())
                );
    }

    @Test
    void testMove(){
        BoundingBox box = new BoundingBox(0,0, ball.getWidth(), ball.getHeight());
        ball.setSpeedX(5);
        ball.setSpeedY(5);
        ball.move();
        assertAll(  ()->  assertNotEquals(ball.getBounds().getMinX(), box.getMinX()),
                    ()->  assertNotEquals(ball.getBounds().getMinY(), box.getMinY())
        );
    }

    @Test
    void testBallLeftCollision() {
        int ballSpeed;
        int tries = 0;
        do{
            ball.ballLeftCollision();
            ballSpeed = ball.getSpeedX();
            tries++;
        }while (ball.getSpeedX() == -ballSpeed && tries < 200);
        assertNotEquals(ball.getSpeedX(), -ballSpeed);
    }

    @Test
    void testBallRightCollision() {
        int ballSpeed;
        int tries = 0;
        do{
            ball.ballRightCollision();
            ballSpeed = ball.getSpeedX();
            tries++;
        }while (ball.getSpeedX() == -ballSpeed && tries < 200);
        assertNotEquals(ball.getSpeedX(), -ballSpeed);
    }

    @Test
    void testBallTopCollision() {
        int ballSpeed;
        int tries = 0;
        do{
            ball.ballTopCollision();
            ballSpeed = ball.getSpeedY();
            tries++;
        }while (ball.getSpeedY() == -ballSpeed && tries < 200);
        assertNotEquals(ball.getSpeedY(), -ballSpeed);
    }

    @Test
    void testBallBottomCollision() {
        int ballSpeed;
        int tries = 0;
        do{
            ball.ballBottomCollision();
            ballSpeed = ball.getSpeedY();
            tries++;
        }while (ball.getSpeedY() == -ballSpeed && tries < 200);
        assertNotEquals(ball.getSpeedY(), -ballSpeed);
    }
}