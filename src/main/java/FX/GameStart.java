package FX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class GameStart extends Application {

    /**
     * this method is used to create a window and prepare and display the main menu.
     *
     * @param stage this is the stage which is used insert all the contents for the main menu.
     * @throws IOException this is used just in case there is a problem in loading the fxml file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.TRANSPARENT);
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setTitle("Brick Destroy");
        stage.setScene(new Scene(root,450,300));
        stage.setResizable(false);
        stage.show();

        setScreenOnMiddle(stage);
    }

    /**
     * this method is used to set the main menu window in the middle of the screen.
     *
     * @param stage this is the stage (window) used to be displayed in the middle of the screen.
     */
    private void setScreenOnMiddle(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    /**
     * this method is used to initialize the game.
     *
     * @param args this is the default argument used in every main method.
     */
    public static void main(String[] args) {
        launch();
    }
}