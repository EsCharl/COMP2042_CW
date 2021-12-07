package FX.View;

import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameScoreDisplay {
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
