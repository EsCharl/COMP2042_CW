package Model;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Rectangle area = new Rectangle(300,450);
    Game game = Game.singletonGame(area,30,3,6/2,new Point(300,430));

//    @Test
//    void positionsReset() {
//        Object ballPosition = game.getBall().getCenterPosition().clone();
//        game.getBall().moveTo(new Point(1,1));
//        game.positionsReset();
//        assertTrue(game.getBall().getCenterPosition().equals(ballPosition));
//    }

    @Test
    void singletonGame(){
        Rectangle totallyDifferentArea = new Rectangle(500,600);
        assertTrue(game.equals(Game.singletonGame(totallyDifferentArea,30,3,6/2,new Point(400,200))));
    }

    @Test
    void nextLevel(){
        game.nextLevel();
        game.setBrickCount(0);
        int bricksBeforeAmount = game.getBrickCount();
        Object bricksCopy = game.getBricks().clone();
        game.nextLevel();
        assertFalse(bricksCopy == game.getBricks());
        assertFalse(bricksBeforeAmount == game.getBrickCount());
    }

    @Test
    void isLevelComplete(){
        game.nextLevel();
        assertFalse(game.isLevelComplete());
    }
}