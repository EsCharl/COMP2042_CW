/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
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

import java.awt.*;

/**
 * this class is for the steel brick which is used for the level.
 */
public class SteelBrick extends Brick {

    private static final Color DEF_INNER = new Color(203, 203, 201);
    private static final Color DEF_BORDER = Color.BLACK;
    private static final int STEEL_STRENGTH = 1;
    private static final double STEEL_PROBABILITY = 0.4;

    /**
     * this method is used to create a steel brick object.
     *
     * @param point this is the point where the steel brick is created.
     * @param size this is for the size of the brick
     */
    public SteelBrick(Point point, Dimension size){
        super(point,size,DEF_BORDER,DEF_INNER,STEEL_STRENGTH);
    }

    /**
     * this method is used to determine if the brick can be broken based on a probability.
     */
    @Override
    public void impact(){
        if(getRnd().nextDouble() < STEEL_PROBABILITY){
            super.impact();
        }
    }

}
