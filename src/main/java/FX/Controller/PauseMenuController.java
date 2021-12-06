package FX.Controller;

import FX.Model.Game;
import FX.Model.GameScore;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PauseMenuController {
    @FXML private Button exit, restart, resume;

    private Game game;
    private GameScore gameScore;

    public PauseMenuController(){
        game = Game.singletonGame();
        gameScore = GameScore.singletonGameScore();
    }

    @FXML
    public void resumeButton(){
        Parent loader = null;
        try {
            loader = FXMLLoader.load(getClass().getResource("/FX/GameState.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) resume.getScene().getWindow();
        stage.setScene(new Scene(loader));
    }

    @FXML
    public void restartButton(){
        game.resetBallCount();
        game.getBall().resetPosition();
        game.getPlayer().resetPosition();
        game.getBall().setRandomBallSpeed();
        gameScore.restartTimer();
        game.wallReset();

        resumeButton();
    }

    @FXML
    public void exitButton(){
        System.out.println("Goodbye " + System.getProperty("user.name"));
        System.exit(0);
    }
}
