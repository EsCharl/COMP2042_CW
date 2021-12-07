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
 * this class is used for the cement brick used for the walls.
 */
public class CementBrick extends Brick implements Crackable{

    private static final String NAME = "Cement Brick";
    private static final Color DEF_INNER = Color.rgb(147, 147, 147,1);
    private static final Color DEF_BORDER = Color.rgb(217, 199, 175,1);
    private static final int CEMENT_STRENGTH = 2;

    private Path crackPath;
    private Crack crack;
    private Rectangle brickFace;

    /**
     * this constructor is used to create a cement brick object.
     *
     * @param point this is the point where the cement brick is going to be created.
     * @param size this is the size of the cement brick.
     */
    public CementBrick(Point2D point, Dimension2D size){
        super(point,size,DEF_BORDER,DEF_INNER,CEMENT_STRENGTH,NAME);
        crack = new Crack();
        brickFace = super.brickFace;
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
        super.impacted();
        if(!super.isBroken()){
            crack.prepareCrack(point,dir,this);
//            updateBrick();
            return false;
        }
        return true;
    }

    /**
     * this method is used to get the shape information of the brick.
     *
     * @return the graphic of the brick
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

//    /**
//     * this method is used to update the status of the brick on the screen.
//     */
//    private void updateBrick(){
//        if(!super.isBroken()){
//            Path gp = crack.getCrackPath();
//            gp.append(super.brickFace,false);
//            brickFace = gp;
//        }
//    }

//    /**
//     *this method is used to repair the brick.
//     */
//    @Override
//    public void repair(){
//        super.repair();
//        crack.reset();
//        brickFace = super.getBrick();
//    }
}
