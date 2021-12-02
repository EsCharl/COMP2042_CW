package Controller;

import Model.Game;
import Model.GameScore;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardControllerTest {

    Rectangle area = new Rectangle(300,450);
    Game game = Game.singletonGame(area,30,3,6/2,new Point(300,430));
    JFrame jFrame = new JFrame();
    GameBoardController gameBoardController = GameBoardController.singletonGameBoardController();
    GameScore gameScore = GameScore.singletonGameScore();

    @Test
    void skipLevelTriggered() {
        int currentLevel = game.getCurrentLevel();
        game.getBall().moveTo(new Point(5,5));
        gameScore.setTotalTime(50);
        long getBeforeTotalTime = gameScore.getTotalTime();
        Object ballPosition = game.getBall().getCenterPosition().clone();
        game.nextLevel();
        gameBoardController.skipLevelTriggered();
        assertAll(  ()-> assertFalse(ballPosition.equals(game.getBall().getCenterPosition())),
                    ()-> assertTrue(currentLevel != game.getCurrentLevel()),
                    ()-> assertTrue(getBeforeTotalTime != gameScore.getTotalTime())
        );
    }

    @Test
    void resetBallCountTriggered(){
        game.setBallCount(1);
        int previousBallCount = game.getBallCount();
        game.resetBallCount();
        assertFalse(previousBallCount == game.getBallCount());
    }
}