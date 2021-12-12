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

package FX.Model.Entities;

/**
 * this class is used to store the information about the user current level, tries left and current mode.
 */
public class Player {
    public static final int MAX_BALL_COUNT = 3;

    private int currentLevel;
    private boolean botMode;
    private boolean pauseMode;
    private int ballCount;

    private static Player uniquePlayer;

    /**
     * this method is used to create and return the one and only player object.
     *
     * @return this returns the one and only player object.
     */
    public static Player singletonPlayer(){
        if(getUniquePlayer() == null){
            setUniquePlayer(new Player());
        }
        return getUniquePlayer();
    }
    /**
     * this constructor is used to set the player initial values.
     */
    private Player(){
        setPauseMode(true);
        setBotMode(false);

        setCurrentLevel(0);
        setBallCount(MAX_BALL_COUNT);
    }

    /**
     * this is the method used to get the current level variable.
     *
     * @return this returns the currentLevel variable
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * this method is used to set the current level variable.
     *
     * @param level this is the integer used to set the level.
     */
    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    /**
     * this method is used to check if the bot mode (AI) is activated for the bot to play in behalf of the player.
     *
     * @return this returns a boolean value to see if the game is enabled for the bot to play.
     */
    public boolean isBotMode() {
        return botMode;
    }

    /**
     * this method is used to change between the player mode and AI mode.
     *
     * @param botMode this is the boolean value to set if it's player mode (false) or AI mode (true).
     */
    public void setBotMode(boolean botMode) {
        this.botMode = botMode;
    }

    /**
     * this method is used to get if the game is paused.
     *
     * @return this returns a boolean variable to confirm if it is paused or not paused.
     */
    public boolean isPauseMode() {
        return pauseMode;
    }

    /**
     * this method is used to check if the game is pause or no.
     *
     * @param pauseMode this is used to set the if the game paused or no.
     */
    public void setPauseMode(boolean pauseMode) {
        this.pauseMode = pauseMode;
    }

    /**
     * this is used to get the amount of balls (tries) for the level.
     *
     * @return this returns the amount of balls (tries) in integer.
     */
    public int getBallCount(){
        return ballCount;
    }

    /**
     * this is used to set the ball counter which is the amount of tries the player have before losing,
     *
     * @param ballCount this is the amount of balls (tries) for the level.
     */
    public void setBallCount(int ballCount) {
        this.ballCount = ballCount;
    }

    /**
     * this method is used to reset the ball count (tries count) to 3.
     */
    public void resetBallCount(){
        setBallCount(MAX_BALL_COUNT);
    }

    /**
     * this method is used to return a one and only player object. as this is a single player game it makes sense to only have one player object.
     *
     * @return this is returns the one and only player object.
     */
    private static Player getUniquePlayer() {
        return uniquePlayer;
    }

    /**
     * this method is used to set a player object into a variable to be referenced in the future.
     *
     * @param uniquePlayer this is the player object used get the player object.
     */
    private static void setUniquePlayer(Player uniquePlayer) {
        Player.uniquePlayer = uniquePlayer;
    }
}
