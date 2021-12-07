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

package FX.View;

import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameScoreDisplay {

    /**
     * this method is used to create a window which will display the high scores and the current user score (time used) for the level in a Color.
     *
     * @param recordsArray this is the array of records that are processed and pass into this method for display.
     * @param levelPlayTime this is the string of the user current time spent in the form of (XX:xx).
     */
    public void generateLevelCompleteWindow(ArrayList<String> recordsArray, String levelPlayTime){
        Stage levelCompleteWindow = new Stage();

        GridPane gridPane = new GridPane();

        gridPane.getColumnConstraints().add(new ColumnConstraints(200));
        gridPane.getColumnConstraints().add(new ColumnConstraints(100));

        gridPane.add(new Text("Name"),0,0);
        gridPane.add(new Text("Time"),1,0);

        for (int i = 0 ; i < recordsArray.size(); i++) {
            String[] list = recordsArray.get(i).split(",", 2);
            String name = list[0];
            String time = list[1];

            if(levelPlayTime.equals(time) && System.getProperty("user.name").equals(name)){
                Text playerName = new Text(System.getProperty("user.name"));
                playerName.setFill(Color.DARKCYAN);
                gridPane.add(playerName,0,i+1);
                Text playerTime = new Text(time);
                playerTime.setFill(Color.DARKCYAN);
                gridPane.add(playerTime,1,1+i);
            }else{
                gridPane.add(new Text(name), 0,i+1);
                gridPane.add(new Text(time),1,i+1);
            }
        }
        levelCompleteWindow.setScene(new Scene(gridPane));
        levelCompleteWindow.setTitle("Game Score");
        levelCompleteWindow.show();
    }
}
