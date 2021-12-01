package Model;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovementsTest {
    Rectangle area = new Rectangle(300,450);
    Game game = Game.singletonGame(area,30,3,6/2,new Point(300,430));
    Movements movements = Movements.singletonMovements(game);

    @Test
    void setRandomBallSpeed() {
        movements.setRandomBallSpeed();

        assertTrue(game.getBall().getXSpeed() != 0);
        assertTrue(game.getBall().getYSpeed() <= -1);
    }

    @Test
    void impactSideBorder(){
        assertTrue(movements.impactSideBorder());
    }

    @Test
    void findImpacts(){
        int beforeImpact = game.getBall().getXSpeed();
        movements.findImpacts();
        assertTrue(beforeImpact != game.getBall().getXSpeed());
    }
}