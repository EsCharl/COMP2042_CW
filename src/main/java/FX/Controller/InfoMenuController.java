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
import javafx.stage.Stage;

import java.io.IOException;

public class InfoMenuController {
    @FXML
    private Button mainmenu;

    /**
     * this method is used to change the display to the main menu.
     *
     * @throws IOException this is used just in case if there is a problem in reading the fxml file.
     */
    @FXML
    public void DisplayMainMenu() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("/FX/MainMenu.fxml"));
        Stage stage = (Stage) mainmenu.getScene().getWindow();
        stage.setScene(new Scene(loader));
    }
}
