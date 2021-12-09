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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PauseMenuController {
    @FXML private Button resume;

    private GameData gameData;
    private GameScore gameScore;

    /**
     * this constructor is used to prepare the pause menu controller which is allows the user to resume the game, restart the game or quit the game.
     */
    public PauseMenuController(){
        gameData = GameData.singletonGame();
        gameScore = GameScore.singletonGameScore();
    }

    /**
     * this method is used to resume the game.
     */
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

    /**
     * this method is used to restart the game. which will restart the status of the ball, player position, ball count and reset the game timer for the score.
     */
    @FXML
    public void restartButton(){
        gameData.resetBallCount();
        gameData.getBall().resetPosition();
        gameData.getPlayer().resetPosition();
        gameData.setRandomBallSpeed(gameData.getBall());
        gameScore.restartTimer();
        gameData.wallReset();

        resumeButton();
    }

    /**
     * this method is used to quit the game.
     */
    @FXML
    public void exitButton(){
        System.out.println("Goodbye " + System.getProperty("user.name"));
        System.exit(0);
    }
}
