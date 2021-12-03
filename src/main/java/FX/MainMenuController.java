package FX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    private Parent loader;
    @FXML private Button start, info;

    /**
     * this method is used to change the scene to the info scene which shows the user on how to play the game.
     *
     * @throws IOException this is incase there is an issue with the input/ output of the file.
     */
    @FXML
    protected void onInfoButtonClick() throws IOException {
        changeScene("Info.fxml");
    }

    /**
     * this method is used to change the scene based on the fxml file provided.
     *
     * @param fxml this is the fxml file.
     * @throws IOException
     */
    private void changeScene(String fxml) throws IOException {
        loader = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) info.getScene().getWindow();
        stage.setScene(new Scene(loader));
    }

    /**
     * this method is used when the user choose to start the game on the main menu.
     */
    @FXML
    protected void onPlayButtonClick() {
//        changeScene();
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