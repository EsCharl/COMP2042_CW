import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameScore {

    //for the timer
    private long timer;
    private long totalTime;
    private long pauseTime;

    //for the level saving
    private String levelFilePathName;

    /**
     * This method creates a high score panel to show the scores after each game.
     *
     * @param sorted takes in the arraylist of string from getHighScore method to display on the panel.
     * @throws BadLocationException just incase if the insertion of the string into the pop up is an error.
     */
    void highScorePanel(ArrayList<String> sorted) throws BadLocationException {
        JFrame frame=new JFrame("HIGH SCORE");
        frame.setLayout(new FlowLayout());
        frame.setSize(500,400);

        Container cp = frame.getContentPane();
        JTextPane pane = new JTextPane();
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(attributeSet, true);

        // Set the attributes before adding text
        pane.setCharacterAttributes(attributeSet, true);
        pane.setText(String.format("%-20s %s\n\n", "Name", "Time"));

        attributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attributeSet, Color.RED);
        StyleConstants.setBackground(attributeSet, Color.GREEN);

        Document doc = pane.getStyledDocument();
        for (int i = 0 ; i < sorted.size(); i++){
            String[] list = sorted.get(i).split(",",2);
            String name = list[0];
            String time = list[1];
            doc.insertString(doc.getLength(),String.format("%-20s %s\n",name, time), attributeSet);
        }
        JScrollPane scrollPane = new JScrollPane(pane);
        cp.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /**
     * this is used to set the levelPath file.
     *
     * @param levelPathName this is the file path which will be set into the variable.
     */
    public void setLevelFilePathName(String levelPathName){
        levelFilePathName = levelPathName;
    }

    /**
     * this method is used to get the LevelPath file in String form.
     *
     * @return returns the LevelPath file in String.
     */
    public String getLevelFilePathName(){
        return levelFilePathName;
    }

    /**
     * this method is used to get the scores from the save file and the player score (in terms of time) and rank them.
     *
     * @return it returns an arraylist of string that contains the sorted name and time for the player and the records in the save file.
     * @throws FileNotFoundException just in case if the save file is missing.
     */
    ArrayList<String> getHighScore() throws IOException, URISyntaxException {

        System.out.println(getTotalTime());
        System.out.println(getTimerString());

        Boolean placed = false;

        ArrayList<String> Completed = new ArrayList<String>();

        Scanner scan = new Scanner(new File(GameBoard.class.getResource(getLevelFilePathName()).toURI()));

        while (scan.hasNextLine()){
            // to split the name and time to include inside the highscore.
            String[] line = scan.nextLine().split(",",2);
            String name = line[0];
            String time = line[1];

            // for conversion of the time to seconds.
            String[] preTime = time.split(":",2);
            int minute = Integer.parseInt(preTime[0]);
            int second = Integer.parseInt(preTime[1]);

            int total_millisecond = (minute * 60 + second) * 1000;
            if ((getTotalTime() < total_millisecond) && !placed){
                Completed.add(System.getProperty("user.name") + ',' + getTimerString());
                placed = true;
                Completed.add(name + ',' + time);
                System.out.println("dong");
            }else{
                Completed.add(name + ',' + time);
            }
        }
        if(!placed){
            Completed.add(System.getProperty("user.name") + ',' + getTimerString());
            System.out.println("ding");
        }
        return Completed;
    }

    /**
     * this method is used to get the time passed.
     *
     * @return A time in String format.
     */
    private String getTimerString(){
        long elapsedSeconds = getTotalTime() / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        if (secondsDisplay <= 9){
            return elapsedMinutes + ":0" + secondsDisplay;
        }
        return elapsedMinutes + ":" + secondsDisplay;
    }

    /**
     * This method is used to start the timer.
     */
    void startTimer(GameBoard gameBoard){
        timer = System.currentTimeMillis();
    }

    /**
     * This method is used to pause the timer.
     */
    void pauseTimer(){
        if (getTimer() != 0){
            setPauseTime(System.currentTimeMillis());

            setTotalTime(getTotalTime() + getPauseTime() - getTimer());
        }
    }

    /**
     * this method is used to set the total time variable.
     *
     * @param totalTime the total time which is used to set the total time variable.
     */
    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * this method is used to get the total time value variable.
     *
     * @return returns a long datatype of the total time variable.
     */
    public long getTotalTime(){
        return totalTime;
    }

    /**
     * This method is used when a new level or a restart of the level is selected to restart the total time variable which is used to complete the level.
     */
    void restartTimer(){
        setPauseTime(0);
        setTotalTime(0);
        setTimer(0);
    }

    /**
     * This method is used to save the user record in a .txt save file.
     *
     * @param sorted this is the arraylist of string to store inside the save file.
     * @throws IOException This is in case if there is a problem writing the file.
     */
    void updateSaveFile(ArrayList<String> sorted) throws IOException, URISyntaxException {
        File file = new File(GameBoard.class.getResource(getLevelFilePathName()).toURI());
        FileWriter overwrite = new FileWriter(file,false);
        for (int i = 0; i < sorted.size()-1; i++)
            overwrite.write(sorted.get(i)+"\n");
        overwrite.write(sorted.get(sorted.size()-1));
        overwrite.close();
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public long getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }
}
