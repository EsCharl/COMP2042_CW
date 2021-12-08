/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2021  Leong Chang Yung
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package FX.Controller;

import FX.Model.Game;
import FX.Model.GameScore;
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
        game.setRandomBallSpeed(game.getBall());
        game.wallReset();
        game.nextLevel();
        gameScore.setLevelFilePathName("/scores/Level"+ game.getCurrentLevel()+".txt");
        gameScore.restartTimer();
    }

    @FXML
    public void resetBallButtonClicked(){
        game.setBallCount(game.getMAX_BALL_COUNT());
    }

    @FXML
    public void xSpeedSliderMoved(){
        xSpeedSlider.valueProperty().addListener((observableValue, number, t1) -> {
            game.getBall().setSpeedX(t1.intValue());
            textXSpeed.setText("new ball x-axis speed: " + t1);
        });
    }

    @FXML
    public void ySpeedSliderMoved(){
        ySpeedSlider.valueProperty().addListener((observableValue, number, t1) -> {
            game.getBall().setSpeedY(t1.intValue());
            textYSpeed.setText("new ball y-axis speed: " + t1);
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        xSpeedSlider.setValue(game.getBall().getSpeedX());
        ySpeedSlider.setValue(game.getBall().getSpeedY());
    }
}
