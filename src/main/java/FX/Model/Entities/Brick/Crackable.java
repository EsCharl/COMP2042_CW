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

import javafx.scene.shape.Path;

/**
 * this interface is used to allow the class to have crackable feature
 */
public interface Crackable {

    /**
     * this is used to determine which direction does the collision occur to the brick. (left)
     */
    int LEFT = 10;

    /**
     * this is used to determine which direction does the collision occur to the brick. (right)
     */
    int RIGHT = 20;

    /**
     * this is used to determine which direction does the collision occur to the brick. (up)
     */
    int UP = 30;

    /**
     * this is used to determine which direction does the collision occur to the brick. (down)
     */
    int DOWN = 40;

    /**
     *
     */

    /**
     * this abstract method is used to get the crack object which contains the logic used to create the crack path.
     *
     * @return it returns a crack object
     */
    Crack getCrack();

    /**
     * this method is used to set the crack object.
     *
     * @param crack this is the crack object used to set into a variable.
     */
    void setCrack(Crack crack);

    /**
     * this method is used to set the crack path generated by the crack object.
     *
     * @param path this is the path object which contains the info on how to draw the crack into a variable.
     */
    void setCrackPath(Path path);

    /**
     * this method is used to get the crack path which contains the info on how to draw the crack.
     *
     * @return this returns the path object which contains the information need to draw the crack line.
     */
    Path getCrackPath();
}
