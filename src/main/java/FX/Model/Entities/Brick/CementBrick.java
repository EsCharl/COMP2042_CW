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

/**
 * this class is used for the cement brick used for the walls.
 */
public class CementBrick extends Brick implements Crackable{

    private static final String NAME = "Cement Brick";
    private static final Color DEF_INNER = Color.rgb(147, 147, 147,1);
    private static final Color DEF_BORDER = Color.rgb(217, 199, 175,1);
    private static final int CEMENT_STRENGTH = 2;
    private static final double CEMENT_PROBABILITY = 1;

    private Path crackPath;
    private Crack crack;

    /**
     * this constructor is used to create a cement brick object.
     *
     * @param point this is the point where the cement brick is going to be created.
     * @param size this is the size of the cement brick.
     */
    public CementBrick(Point2D point, Dimension2D size){
        super(point,size,DEF_BORDER,DEF_INNER,CEMENT_STRENGTH, CEMENT_PROBABILITY, NAME);
        setCrack(Crack.singletonCrack());
    }

    /**
     * this method is used to get the crack object which is used to create the crack path.
     *
     * @return this returns the crack object which contains the object logic to create the crack Path.
     */
    @Override
    public Crack getCrack() {
        return crack;
    }

    /**
     * this method is used to set the crack object into a variable for future reference.
     *
     * @param crack this is the crack object used to set into a variable.
     */
    @Override
    public void setCrack(Crack crack) {
        this.crack = crack;
    }

    /**
     * this method is used to set the crack path used to draw on the game.
     *
     * @param path this is the Path object used to set into a variable for future reference
     */
    @Override
    public void setCrackPath(Path path) {
        this.crackPath = path;
    }

    /**
     * this method is used to get the crack path used to draw on the game.
     *
     * @return this is the Path object which contains the information on how to draw the crack.
     */
    @Override
    public Path getCrackPath() {
        return crackPath;
    }
}
