package FX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    private Parent loader;
    @FXML private Button start, info;

    /**
     * this method is used to change the scene to the info scene which shows the user on how to play the game.
     *
     */
    @FXML
    protected void onInfoButtonClick() {
        changeScene("Info.fxml", info);
    }

    /**
     * this method is used to change the scene based on the fxml file provided.
     *
     * @param fxml this is the fxml file.
     * @param button this is the button where it will be used to get the stage.
     */
    private void changeScene(String fxml, Button button) {
        try {
            loader = FXMLLoader.load(getClass().getResource(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) button.getScene().getWindow();
        stage.setScene(new Scene(loader));
    }

    /**
     * this method is used when the user choose to start the game on the main menu.
     */
    @FXML
    protected void onPlayButtonClick() {
        changeScene("GameState.fxml", start);
    }

    /**
     * this method is used when the user choose to quit/exit the game in the main menu.
     */
    @FXML
    protected void onExitButton() {
        System.out.println("Goodbye " + System.getProperty("user.name"));
        System.exit(0);
    }
}