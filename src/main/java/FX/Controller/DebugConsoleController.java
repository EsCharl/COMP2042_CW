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

import FX.Model.GameData;
import FX.Model.GameScore;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * this controller is used to deal with the user input on the debug console.
 */
public class DebugConsoleController implements Initializable {

    @FXML private Slider xSpeedSlider, ySpeedSlider;
    @FXML private Label textXSpeed, textYSpeed;
    @FXML private ChoiceBox levelSelector;

    private GameData gameData;
    private GameScore gameScore;

    /**
     * this is the constructor used to prepare the debug console by preparing the objects used into variables.
     */
    public DebugConsoleController(){
        gameData = GameData.singletonGame();
        gameScore = GameScore.singletonGameScore();
    }

    /**
     * this method is used to skip the current level and reset the game status.
     */
    @FXML
    private void skipLevelButtonClicked(){
        gameData.getPlayer().resetPosition();
        gameData.getMainBall().resetPosition();
        gameData.getMainBall().setRandomBallSpeed();
        gameData.resetBallCount();
        gameData.wallReset();
        gameData.nextLevel();
        gameData.getCloneBall().clear();
        gameScore.setLevelFileName("/scores/Level" + gameData.getCurrentLevel()+".txt");
        gameScore.restartTimer();
    }

    /**
     * this method is called to reset the ball count to the max ball count.
     */
    @FXML
    private void resetBallButtonClicked(){
        gameData.setBallCount(gameData.getMAX_BALL_COUNT());
    }

    /**
     * this method is used to change the speed value on the x-axis of the ball and to set the text on the debug console
     */
    @FXML
    private void xSpeedSliderMoved(){
        xSpeedSlider.valueProperty().addListener((observableValue, number, t1) -> {
            gameData.getMainBall().setSpeedX(t1.intValue());
            textXSpeed.setText("new ball x-axis speed: " + t1);
        });
    }

    /**
     * this method is used to change the speed value on the y-axis of the ball and to set the text on the debug console
     */
    @FXML
    private void ySpeedSliderMoved(){
        ySpeedSlider.valueProperty().addListener((observableValue, number, t1) -> {
            gameData.getMainBall().setSpeedY(t1.intValue());
            textYSpeed.setText("new ball y-axis speed: " + t1);
        });
    }

    /**
     * this method is called to set the level.
     */
    @FXML
    private void setLevelSelector(){

        levelSelector.setOnAction(actionEvent -> {
            int selectedIndex = levelSelector.getSelectionModel().getSelectedIndex();

            gameData.setCurrentLevel(selectedIndex);
            skipLevelButtonClicked();
        });
    }

    /**
     * this method is used to initialize the values for the slider and choice box.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        xSpeedSlider.setValue(gameData.getMainBall().getSpeedX());
        ySpeedSlider.setValue(gameData.getMainBall().getSpeedY());

        levelSelector.setValue(gameData.getCurrentLevel());

        levelSelector.getItems().add("Level 1");
        levelSelector.getItems().add("Level 2");
        levelSelector.getItems().add("Level 3");
        levelSelector.getItems().add("Level 4");
        levelSelector.getItems().add("Level 5");
        levelSelector.getItems().add("Level 6");
        levelSelector.getItems().add("Level 7");
    }
}
