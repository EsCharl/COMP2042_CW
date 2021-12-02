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

package Model.Brick;

import Model.Ball.Ball;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * This class is an abstract class which is going to be used for implementation. (Model.Brick.CementBrick, Model.Brick.ClayBrick, Model.Brick.SteelBrick, Model.Brick.ReinforcedSteelBrick)
 */
abstract public class Brick  {

    public static final int DEF_CRACK_DEPTH = 1;
    public static final int DEF_STEPS = 35;

    public static final int UP_IMPACT = 100;
    public static final int DOWN_IMPACT = 200;
    public static final int LEFT_IMPACT = 300;
    public static final int RIGHT_IMPACT = 400;

    private static Random rnd;

    Shape brickFace;

    private Color borderColor;
    private Color innerColor;

    private int maxStrength;
    private int currentStrength;

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
    public Brick(Point pos,Dimension size,Color border,Color inner,int strength){
        setRnd(new Random());
        setBroken(false);
        setBrickFace(makeBrickFace(pos,size));
        setBorderColor(border);
        setInnerColor(inner);
        setMaxStrength(strength);
        setCurrentStrength(strength);
    }

    /**
     * this method is used to set the shape of the brick.
     *
     * @param pos the position where the shape of the brick is formulated.
     * @param size the size of the brick shape.
     * @return the shape of the brick and on that position.
     */
    protected Shape makeBrickFace(Point pos,Dimension size){
        return new Rectangle(pos,size);
    }

    /**
     * this method is used to determine if the brick is broken.
     *
     * @param point the point where the ball comes in contact to
     * @param dir the direction where the ball comes in contact with the object.
     * @return returns a boolean value negative if the brick is broken, true if it is not.
     */
    public boolean setImpact(Point2D point , int dir){
        if(isBroken())
            return false;
        impacted();
        return  isBroken();
    }

    /**
     * this method is used to get the shape of the brick.
     *
     * @return it returns the shape of the brick.
     */
    public Shape getBrick(){
        return brickFace;
    }


    /**
     * this method is used to get the border color of the brick.
     *
     * @return returns the Color of the border of the brick.
     */
    public Color getBorderColor(){
        return borderColor;
    }

    /**
     * this method is used to get the internal color of he brick.
     *
     * @return returns the Color of the internal of the brick.
     */
    public Color getInnerColor(){
        return innerColor;
    }

    /**
     * this method is used to get the direction of impact where the ball comes in contact with the brick,
     *
     * @param b the ball object.
     * @return an Integer where the impact direction is decided based on a constant integer.
     */
    public final int findImpact(Ball b){
        if(isBroken())
            return 0;
        if(getBrickFace().contains(b.getRight()))
            return LEFT_IMPACT;
        else if(getBrickFace().contains(b.getLeft()))
            return RIGHT_IMPACT;
        else if(getBrickFace().contains(b.getUp()))
            return DOWN_IMPACT;
        else if(getBrickFace().contains(b.getDown()))
            return UP_IMPACT;
        return 0;
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
     * this method is used to repair the brick to full strength.
     */
    public void repair() {
        setBroken(false);
        setCurrentStrength(getMaxStrength());
    }

    /**
     * this method is used to decrement the strength of the brick and update the broken variable if it is broken or not.
     */
    public void impacted(){
        setCurrentStrength(getCurrentStrength()-1);
        setBroken(getCurrentStrength() == 0);
    }

    /**
     * this method is used to get the random object.
     *
     * @return it returns a random object.
     */
    public static Random getRnd() {
        return rnd;
    }

    /**
     * this method is used to set a random object into a variable for future usage.
     *
     * @param rnd this is the random object used to set into a variable.
     */
    public static void setRnd(Random rnd) {
        Brick.rnd = rnd;
    }

    /**
     * this method is used to get the brick face which is the shape of the brick.
     *
     * @return this returns the brick face shape.
     */
    public Shape getBrickFace() {
        return brickFace;
    }

    /**
     * this method is used to set the brick face object into a variable for future usage.
     *
     * @param brickFace this is the brick face shape used to set into a variable.
     */
    public void setBrickFace(Shape brickFace) {
        this.brickFace = brickFace;
    }

    /**
     * this method is used to set the brick border color.
     *
     * @param border this is the color of the border for the brick.
     */
    public void setBorderColor(Color border) {
        this.borderColor = border;
    }

    /**
     * this method is used to set the inner color of the brick.
     *
     * @param inner this method is used to set the inner color of the brick.
     */
    public void setInnerColor(Color inner) {
        this.innerColor = inner;
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





