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

package FX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class BrickDestroyer extends Application {
    public static MediaPlayer audio;

    /**
     * this method is used to create a window and prepare and display the main menu.
     *
     * @param stage this is the stage which is used insert all the contents for the main menu.
     * @throws IOException this is used just in case there is a problem in loading the fxml file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.TRANSPARENT); 
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu.fxml")));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GameIcon.png")));
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

        Media media = new Media(Objects.requireNonNull(BrickDestroyer.class.getResource("/Tunes/GameMusic.wav")).toExternalForm());
        audio = new MediaPlayer(media);
        audio.play();
        audio.setCycleCount(MediaPlayer.INDEFINITE);

        launch();
    }
}