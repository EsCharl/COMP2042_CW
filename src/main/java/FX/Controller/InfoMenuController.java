package FX.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class InfoMenuController {
    private Parent loader;
    @FXML
    private Button mainmenu;

    /**
     * this method is used to change the display to the main menu.
     *
     * @throws IOException this is used just in case if there is a problem in reading the fxml file.
     */
    @FXML
    public void DisplayMainMenu() throws IOException {
        loader = FXMLLoader.load(getClass().getResource("/FX/MainMenu.fxml"));
        Stage stage = (Stage) mainmenu.getScene().getWindow();
        stage.setScene(new Scene(loader));
    }
}
