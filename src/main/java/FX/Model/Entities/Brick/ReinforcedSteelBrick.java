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


import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

/**
 * this class is for the reinforced steel brick which is used for the level.
 */
public class ReinforcedSteelBrick extends Brick implements Crackable{

    private static final String NAME = "Reinforced Steel Brick";
    private static final Color DEF_INNER = Color.BLUE;
    private static final Color DEF_BORDER = Color.rgb(203, 203, 201,1);
    private static final int REINFORCED_STEEL_STRENGTH = 2;
    private static final double STEEL_PROBABILITY = 0.3;

    private Rectangle brickFace;
    private Path crackPath;
    private Crack crack;

    private boolean hit;
    /**
     * this method is used to create a reinforced steel brick object.
     *
     * @param point this is the point where the steel brick is created.
     * @param size  this is for the size of the brick
     */
    public ReinforcedSteelBrick(Point2D point, Dimension2D size) {
        super(point,size,DEF_BORDER,DEF_INNER,REINFORCED_STEEL_STRENGTH,NAME);
        crack = new Crack();
        brickFace = super.brickFace;
    }

//    /**
//     * this method is used to update the status of the brick on the screen.
//     */
//    private void updateBrick(){
//        if(!super.isBroken()){
//            GeneralPath gp = crack.getCrackPath();
//            gp.append(super.brickFace,false);
//            brickFace = gp;
//        }
//    }

    /**
     *this method is used to repair the brick.
     */
    @Override
    public void repair(){
        super.repair();
//        crack.reset();
        brickFace = super.getBrick();
    }

    /**
     * this method is used to determine whether the brick should be broken or draw a crack on the brick.
     *
     * @param point the point where the ball comes in contact to
     * @param dir the direction where the ball comes in contact with the object.
     * @return returns a boolean value negative if the brick is broken, true if it is not.
     */
    @Override
    public boolean setImpact(Point2D point, int dir) {
        if(super.isBroken())
            return false;
        hit = getRnd().nextDouble() < STEEL_PROBABILITY;
        if(hit)
            impacted();
        if(!super.isBroken()){
            if(hit){ // remove this if statement for more fun inducing excitement. :D
                crack.prepareCrack(point,dir, this);
//                updateBrick();
            }
            return false;
        }
        return true;
    }

    /**
     * this method is used to get the shape graphic of the brick.
     *
     * @return the shape graphic of the brick
     */
    @Override
    public Rectangle getBrick() {
        return brickFace;
    }

    @Override
    public void setCrackPath(Path path) {
        this.crackPath = path;
    }

    @Override
    public Path getCrackPath() {
        return crackPath;
    }
}
