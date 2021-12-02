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

package Model.Levels;

import Model.Brick.Brick;

import java.awt.*;
import java.util.Random;

/**
 * this class is used to create one of the wall level.
 */
public class RandomWallLevel extends FullWallRowsLevels implements WallLevelTemplates{

    Random rnd = new Random();

    /**
     * this method is one of the template used for the wall (level). this creates a random brick level.
     *
     * @param drawArea this is the area which the bricks could be placed
     * @param brickCount this is the amount of bricks which will be in for the level.
     * @param lineCount this is the number of rows of bricks for the level.
     * @param brickSizeRatio this is the size ratio of the brick.
     * @return it returns the bricks for the wall (level) in the form of a brick array.
     */
    public Brick[] level(Rectangle drawArea, int brickCount, int lineCount, double brickSizeRatio, int typeA, int typeB){
        brickCount -= brickCount % lineCount;

        brickCount += lineCount / 2;

        Brick[] brickArray  = createBrickArray(brickCount);

        Dimension brickSize = new Dimension((int) getDrawBrickLength(drawArea, lineCount,brickCount),(int) getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio));
        Point p = new Point();

        int i;
        for(i = 0; i < brickArray.length; i++){
            int line = i / getBrickOnLine(brickCount,lineCount);
            if(line == lineCount)
                break;
            double x = (i % getBrickOnLine(brickCount,lineCount)) * getDrawBrickLength(drawArea, lineCount, brickCount);
            x = (line % 2 == 0) ? x : (x - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2));
            double y = (line) * getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio);
            p.setLocation(x,y);
            brickArray[i] = makeBrick(p,brickSize, rnd.nextInt(4)+1);
        }

        for(double y = getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio); i < brickArray.length; i++, y += 2 * getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio)){
            double x = (getBrickOnLine(brickCount,lineCount) * getDrawBrickLength(drawArea, lineCount, brickCount)) - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2);
            p.setLocation(x,y);
            brickArray[i] = makeBrick(p,brickSize, rnd.nextInt(4)+1);
        }
        return brickArray;
    }
}
