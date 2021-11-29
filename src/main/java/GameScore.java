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

/**
 * this class is used to record the user game score for each level.
 */
public class GameScore {

    //for the timer
    private long startTime;
    private long totalTime;
    private long pauseTime;

    //for the level saving
    private String levelFilePathName;

    private static GameScore uniqueGameScore;

    public static GameScore singletonGameScore(){
        if(getUniqueGameScore() == null){
            setUniqueGameScore(new GameScore());
        }
        return getUniqueGameScore();
    }

    private GameScore(){
        setStartTime(0);
        setTotalTime(0);
        setPauseTime(0);
    }

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

        Boolean placed = false;

        ArrayList<String> Completed = new ArrayList<String>();

        Scanner scan = new Scanner(new File(GameBoardController.class.getResource(getLevelFilePathName()).toURI()));

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
            }else{
                Completed.add(name + ',' + time);
            }
        }
        if(!placed){
            Completed.add(System.getProperty("user.name") + ',' + getTimerString());
        }
        return Completed;
    }

    /**
     * this method is used to get the time passed.
     *
     * @return A time in String format.
     */
    public String getTimerString(){
        long elapsedSeconds = getTotalTime() / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        if (secondsDisplay <= 9){
            return elapsedMinutes + ":0" + secondsDisplay;
        }
        return elapsedMinutes + ":" + secondsDisplay;
    }

    /**
     * This method is used to start the timer based on current time in milliseconds.
     */
    void startTimer(){
        setStartTime(System.currentTimeMillis());
    }

    /**
     * This method is used to pause the timer by taking current time(pause time) and subtracting the start time, and setting the value into a variable.
     */
    void pauseTimer(){
        if (getStartTime() != 0){
            setPauseTime(System.currentTimeMillis());

            setTotalTime(getTotalTime() + getPauseTime() - getStartTime());
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
        setStartTime(0);
    }

    /**
     * This method is used to save the user record in a .txt save file.
     *
     * @param sorted this is the arraylist of string to store inside the save file.
     * @throws IOException This is in case if there is a problem writing the file.
     * @throws URISyntaxException this is in case if there is a problem if the resource path to the file can't be converted to String format.
     */
    void updateSaveFile(ArrayList<String> sorted) throws IOException, URISyntaxException {
        File file = new File(GameBoardController.class.getResource(getLevelFilePathName()).toURI());
        FileWriter overwrite = new FileWriter(file,false);
        for (int i = 0; i < sorted.size()-1; i++)
            overwrite.write(sorted.get(i)+"\n");
        overwrite.write(sorted.get(sorted.size()-1));
        overwrite.close();
    }

    /**
     * this method is used to get the time in milliseconds where the timer is started
     *
     * @return returns the start time when the timer starts.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * this method is used to set the start time for the timer start.
     *
     * @param startTime this is the value where the start time is to be set.
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * this method is used to get the pause time of the timer.
     *
     * @return it returns the pause time in milliseconds based on the last pause time.
     */
    public long getPauseTime() {
        return pauseTime;
    }

    /**
     * this method is used to set the pause time based on the value given.
     *
     * @param pauseTime this is the value used to set the pause time.
     */
    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }

    public static GameScore getUniqueGameScore() {
        return uniqueGameScore;
    }

    public static void setUniqueGameScore(GameScore uniqueGameScore) {
        GameScore.uniqueGameScore = uniqueGameScore;
    }
}
