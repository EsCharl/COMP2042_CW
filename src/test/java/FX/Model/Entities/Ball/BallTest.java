package FX.Model.Entities.Ball;

import FX.Model.Entities.Brick.Brick;
import FX.Model.Entities.Brick.ClayBrick;
import FX.Model.Entities.Paddle;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
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

    @Test
    void testGameWindowCollision() {
        Bounds bounds = new BoundingBox(0,0,50,50);
        ball.setBounds(new BoundingBox(-1,-1,10,10));
        int initialXspeed = -5;
        int initialYspeed = -4;
        ball.setSpeedX(initialXspeed);
        ball.setSpeedY(initialYspeed);
        ball.gameWindowCollision(bounds);
        assertTrue(ball.gameWindowCollision(bounds));
        assertTrue(ball.getSpeedX() >= initialXspeed);
        assertTrue(ball.getSpeedY() >= initialYspeed);
    }

    @Test
    void testImpactEntity() {
        Brick brick = new ClayBrick(new Point2D(300,200), new Dimension2D(120,200));
        brick.setBounds(new BoundingBox(300,200,120,200));
        ball.setBounds(new BoundingBox(296,200,5,5));
        ball.setSpeedX(5);
        ball.impactEntity(brick);
        assertTrue(ball.getSpeedX() < 5);
    }

    @Test
    void testTwoBallsCollide(){
        BallClone ballClone = new BallClone(new Point2D(295,200));
        int ballCloneInitialspeed = 6;
        int ballClone1Initialspeed = -6;
        ballClone.setSpeedY(ballCloneInitialspeed);
        BallClone ballClone1 = new BallClone(new Point2D(299,200));
        ballClone1.setSpeedY(ballClone1Initialspeed);
        ballClone.impactEntity(ballClone1);
        assertNotEquals(ballClone.getSpeedY(), ballCloneInitialspeed);
        assertNotEquals(ballClone1.getSpeedY(), ballClone1Initialspeed);
    }
}