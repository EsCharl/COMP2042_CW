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

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * this class is used to display and allow the user to select an option on the main menu.
 */
public class MainMenuController {
    private Parent loader;
    @FXML private Button start, info;

    /**
     * this method is used to change the scene to the info scene which shows the user on how to play the game.
     */
    @FXML
    private void onInfoButtonClick() {
        try {
            loader = FXMLLoader.load(getClass().getResource("/FX/Info.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) info.getScene().getWindow();
        stage.setScene(new Scene(loader));
    }

    /**
     * this method is used when the user choose to start the game on the main menu.
     */
    @FXML
    private void onPlayButtonClick() throws IOException {
        Stage stage = (Stage) start.getScene().getWindow();
        stage.hide();

        loader = FXMLLoader.load(getClass().getResource("/FX/GameState.fxml"));

        Stage newStage = new Stage();
        newStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GameIcon.png")));
        newStage.setScene(new Scene(loader));
        newStage.setResizable(false);
        newStage.setOnCloseRequest(windowEvent -> onExitButton());
        newStage.setTitle("Brick Destroy");
        newStage.show();
    }

    /**
     * this method is used when the user choose to quit/exit the game in the main menu.
     */
    @FXML
    private void onExitButton() {
        System.out.println("Goodbye " + System.getProperty("user.name"));
        System.exit(0);
    }
}