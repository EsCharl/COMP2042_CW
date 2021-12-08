package FX.Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GameScoreTest {
    GameScore gameScore = GameScore.singletonGameScore();

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

    @Test
    void singletonGameScore() {
        GameScore gameScore2 = GameScore.singletonGameScore();
        assertTrue(gameScore2 == gameScore);
    }
}