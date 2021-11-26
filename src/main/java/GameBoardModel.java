import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class GameBoardModel {
    GameScore gameScore = GameScore.singletonGameScore();

    void startGame(GameBoardController gameBoardController) {
        gameBoardController.getWall().nextLevel();
        gameScore.setLevelFilePathName("/scores/Level"+ gameBoardController.getWall().getWallLevel()+".txt");

        gameBoardController.setGameTimer(new Timer(10, e ->{
            gameBoardController.getWall().move();
            gameBoardController.getWall().findImpacts();
            gameBoardController.setMessage(String.format("Bricks: %d Balls %d", gameBoardController.getWall().getBrickCount(), gameBoardController.getWall().getBallCount()));
            if(gameBoardController.getWall().isBallLost()){
                if(gameBoardController.getWall().ballEnd()){
                    gameBoardController.getWall().wallReset();
                    gameBoardController.updateGameText("Game over");
                    gameScore.restartTimer();
                }
                gameBoardController.getWall().positionsReset();
                gameBoardController.getGameTimer().stop();
                gameScore.pauseTimer();
            }
            else if(gameBoardController.getWall().isDone()){
                if(gameBoardController.getWall().hasLevel()){
                    gameBoardController.updateGameText("Go to Next Level");
                    gameBoardController.restartGameCondition();
                }
                else{
                    gameBoardController.updateGameText("ALL WALLS DESTROYED");
                }

                gameScore.pauseTimer();

                //for save file saving and high score pop up
                try {
                    ArrayList<String> sorted = gameScore.getHighScore();
                    gameScore.updateSaveFile(sorted);
                    gameScore.highScorePanel(sorted);
                } catch (IOException | BadLocationException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
                gameScore.restartTimer();
            }
            gameScore.setLevelFilePathName("/scores/Level"+ gameBoardController.getWall().getWallLevel()+".txt");

            gameBoardController.updateGameView();
        }));
    }
}
