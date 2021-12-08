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

package FX.Model.Entities.Brick;

import FX.Model.Entities.Entities;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

/**
 * This class is an abstract class which is going to be used for implementation. (Model.Brick.CementBrick, Model.Brick.ClayBrick, Model.Brick.SteelBrick, Model.Brick.ReinforcedSteelBrick)
 */
abstract public class Brick extends Entities {

    private int maxStrength;
    private int currentStrength;
    private double hitProbability;
    private String brickName;

    private boolean broken;

    /**
     * this method is used to create a brick object.
     *
     * @param pos the position of the brick
     * @param size the size of the break
     * @param border the color of the brick border
     * @param inner the inside color of brick
     * @param strength the strength of the brick. (how many hits can it take before it break)
     */
    public Brick(Point2D pos, Dimension2D size, Color border, Color inner, int strength, double hitProbability,String brickName){
        super(pos,border,inner,(int)size.getWidth(),(int)size.getHeight());
        setBroken(false);
        setHitProbability(hitProbability);
        setMaxStrength(strength);
        setCurrentStrength(strength);
        setBrickName(brickName);
    }

    /**
     * this method is used to get the name of the brick which is going to be used to select which audio to be played during collision.
     *
     * @return this is the name of the brick being returned.
     */
    public String getBrickName() {
        return brickName;
    }

    /**
     * this method is used to set the brick name of the brick
     *
     * @param brickName this is the string used to set the name of the brick.
     */
    public void setBrickName(String brickName) {
        this.brickName = brickName;
    }

    public double getHitProbability() {
        return hitProbability;
    }

    public void setHitProbability(double hitProbability) {
        this.hitProbability = hitProbability;
    }

    /**
     * this method is used to return the brick if it is broken or not.
     *
     * @return returns a boolean value if the brick is broken or not.
     */
    public final boolean isBroken(){
        return broken;
    }

    /**
     * this method is used get the brick maximum strength.
     *
     * @return it returns an integer of the max strength of th brick.
     */
    public int getMaxStrength() {
        return maxStrength;
    }

    /**
     * this method is used to set the maximum brick strength.
     *
     * @param maxStrength this is the maximum integer set for the brick strength.
     */
    public void setMaxStrength(int maxStrength) {
        this.maxStrength = maxStrength;
    }

    /**
     * this method is used to get the brick current strength in integer.
     *
     * @return this the integer of the brick current strength.
     */
    public int getCurrentStrength() {
        return currentStrength;
    }

    /**
     * this method is used to set the current strength of the brick.
     *
     * @param currentStrength this is the integer used to set the strength of the brick.
     */
    public void setCurrentStrength(int currentStrength) {
        this.currentStrength = currentStrength;
    }

    /**
     * this is method is used to set if the brick is broken or not.
     *
     * @param broken this is the boolean value used to set if the brick is broken or not.
     */
    public void setBroken(boolean broken) {
        this.broken = broken;
    }
}





