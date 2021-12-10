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

package FX.Model;

import java.io.File;
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

    private boolean canGetTime;

    //for the level saving
    private String levelFileName;
    private String levelFilePathName;
    private ArrayList<String> lastLevelCompletionRecord;

    private static GameScore uniqueGameScore;

    /**
     * this method is used to create the one and only game score object based on singleton design.
     *
     * @return it returns the one and only game score object.
     */
    public static GameScore singletonGameScore(){
        if(getUniqueGameScore() == null){
            setUniqueGameScore(new GameScore());
        }
        return getUniqueGameScore();
    }

    /**
     * this constructor is to create an object and set the variable that count is used to record the time score to 0.
     */
    private GameScore(){
        setStartTime(0);
        setTotalTime(0);
        setPauseTime(0);
        setCanGetTime(false);
    }

    /**
     * this method is used to get the record on the last level that is completed to be displayed on a new window.
     *
     * @return this returns an array list which contains the score for that level, the first element being the highest (fastest) player try.
     */
    public ArrayList<String> getLastLevelCompletionRecord() {
        return lastLevelCompletionRecord;
    }

    /**
     * this method is used to set the record into an arraylist for future reference.
     *
     * @param lastLevelCompletionRecord this is the array list of records being saved into a variable for displaying in the new window.
     */
    public void setLastLevelCompletionRecord(ArrayList<String> lastLevelCompletionRecord) {
        this.lastLevelCompletionRecord = lastLevelCompletionRecord;
    }

    /**
     * this is used to set the levelPath file.
     *
     * @param levelPathName this is the file path which will be set into the variable.
     */
    public void setLevelFileName(String levelPathName){
        levelFileName = levelPathName;
    }

    /**
     * this method is used to get the LevelPath file in String form.
     *
     * @return returns the LevelPath file in String.
     */
    public String getLevelFileName(){
        return levelFileName;
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
    public void startTimer(){
        setStartTime(System.currentTimeMillis());
    }

    /**
     * This method is used to pause the timer by taking current time(pause time) and subtracting the start time, and setting the value into a variable.
     */
    public void pauseTimer(){
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
    public void restartTimer(){
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
    public void updateSaveFile(ArrayList<String> sorted) throws IOException, URISyntaxException {
        File file = new File(levelFilePathName);
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

    /**
     * this method is used to check if the game score is created. if it is then it will return the game score object (singleton design).
     *
     * @return returns a game score object if it is present.
     */
    public static GameScore getUniqueGameScore() {
        return uniqueGameScore;
    }

    /**
     * this method is used to set a game score object into a variable for future reference. (singleton design).
     *
     * @param uniqueGameScore this is the game score used to set into a variable.
     */
    public static void setUniqueGameScore(GameScore uniqueGameScore) {
        GameScore.uniqueGameScore = uniqueGameScore;
    }

    /**
     * this method is used to set the variable if it can get the time to set to a scoring variable.
     *
     * @param canGetTime this is the variable used to set if it can take the time for the scoring.
     */
    public void setCanGetTime(boolean canGetTime) {
        this.canGetTime = canGetTime;
    }

    /**
     * this method is used to check if it can get the time to set it to a scoring variable.
     *
     * @return it returns true based on the variable value.
     */
    public boolean isCanGetTime() {
        return canGetTime;
    }

    /**
     * this method is used to get the scores from the save file and the player score (in terms of time) and rank them.
     *
     * @return it returns an arraylist of string that contains the sorted name and time for the player and the records in the save file.
     * @throws IOException this is an exception used when there is a problem with the input and output file.
     * @throws URISyntaxException this exception is used to check if there is a problem in the string could not be parsed as URI reference.
     */
    public ArrayList<String> getHighScore() throws IOException, URISyntaxException {

        boolean placed = false;

        ArrayList<String> Completed = new ArrayList<>();

        String path = "scores/";

        File savedFile = new File(path);
        if(!savedFile.exists()){
            savedFile.mkdir();
        }

        savedFile = new File(path,getLevelFileName());

        savedFile.createNewFile();
        Scanner scan = new Scanner(savedFile);

        levelFilePathName = savedFile.getPath();

        while (scan.hasNextLine()){
            String[] line = scan.nextLine().split(",",2);
            String name = line[0];
            String time = line[1];

            String[] preTime = time.split(":",2);
            int minute = Integer.parseInt(preTime[0]);
            int second = Integer.parseInt(preTime[1]);

            int total_millisecond = (minute * 60 + second) * 1000;
            if ((getTotalTime() < total_millisecond) && !placed){
                Completed.add(System.getProperty("user.name") + ',' + getTimerString());
                placed = true;
            }
            Completed.add(name + ',' + time);
        }
        if(!placed){
            Completed.add(System.getProperty("user.name") + ',' + getTimerString());
        }
        return Completed;
    }
}
