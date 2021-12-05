package FX.Controller;

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
    protected void onPlayButtonClick() throws IOException {
        Stage stage = (Stage) start.getScene().getWindow();
        stage.hide();

        loader = FXMLLoader.load(getClass().getResource("/FX/GameState.fxml"));

        Stage newStage = new Stage();
        newStage.setScene(new Scene(loader));
        newStage.setResizable(false);
        newStage.setTitle("Brick Destroy");
        newStage.show();
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