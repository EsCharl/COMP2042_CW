package FX;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class InfoMenu {
    public void DisplayMenu() throws IOException {
        Node loader = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
    }
}
