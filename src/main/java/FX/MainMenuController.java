package FX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    private Scene loader;
    @FXML private Button start, info;

    @FXML
    protected void onInfoButtonClick() throws IOException {
        changeScene("Info.fxml");
    }

    private void changeScene(String fxml) throws IOException {
        loader = FXMLLoader.load(getClass().getResource(fxml));
        Stage scene = (Stage) info.getScene().getWindow();
        scene.setScene(loader);
        scene.show();
    }

    @FXML
    protected void onPlayButtonClick() {
//        changeScene();
    }

    @FXML
    protected void onExitButton() {
        System.out.println("Goodbye " + System.getProperty("user.name"));
        System.exit(0);
    }
}