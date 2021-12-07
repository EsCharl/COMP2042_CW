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
