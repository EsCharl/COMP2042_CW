package Model;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameScoreTest {
    GameScore gameScore = GameScore.singletonGameScore();
    Game game = Game.singletonGame(30,3,6/2,new Point(300,430));

    @Test
    void getTimerString() {
        gameScore.setTotalTime(60000);
        assertTrue(gameScore.getTimerString().equals("1:00"));
    }

    @Test
    void restartTimer() {
        gameScore.setTotalTime(50);
        gameScore.setPauseTime(80);
        gameScore.setStartTime(909);
        gameScore.restartTimer();
        assertAll(  ()->assertTrue(gameScore.getTotalTime() == 0),
                    ()->assertTrue(gameScore.getPauseTime() == 0),
                    ()->assertTrue(gameScore.getStartTime() == 0)
        );
    }
}