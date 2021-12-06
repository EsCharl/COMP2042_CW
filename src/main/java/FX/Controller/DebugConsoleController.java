package FX.Controller;

import FX.Model.Game;
import FX.Model.GameScore;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

public class DebugConsoleController implements Initializable {

    @FXML private Slider xSpeedSlider, ySpeedSlider;
    @FXML private Label textXSpeed, textYSpeed;

    private Game game;
    private GameScore gameScore;

    public DebugConsoleController(){
        game = Game.singletonGame();
        gameScore = GameScore.singletonGameScore();
    }

    @FXML
    public void skipLevelButtonClicked(){
        game.getPlayer().resetPosition();
        game.getBall().resetPosition();
        game.getBall().setRandomBallSpeed();
        game.wallReset();
        game.nextLevel();
        gameScore.restartTimer();
    }

    @FXML
    public void resetBallButtonClicked(){
        game.setBallCount(game.getMAX_BALL_COUNT());
    }

    @FXML
    public void xSpeedSliderMoved(){
        xSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                game.getBall().setXSpeed(t1.intValue());
                textXSpeed.setText("new ball x-axis speed: " + t1);
            }
        });
    }

    @FXML
    public void ySpeedSliderMoved(){
        ySpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                game.getBall().setYSpeed(t1.intValue());
                textYSpeed.setText("new ball y-axis speed: " + t1);
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        xSpeedSlider.setValue(game.getBall().getXSpeed());
        ySpeedSlider.setValue(game.getBall().getYSpeed());
    }
}
