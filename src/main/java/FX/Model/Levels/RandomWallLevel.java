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

package FX.Model.Levels;

import FX.Model.Entities.Brick.Brick;
import FX.Model.Entities.Brick.BrickFactory;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.Random;

/**
 * this class is used to create one of the wall level.
 */
public class RandomWallLevel extends FullWallRowsLevels {

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
        BrickFactory brickFactory = new BrickFactory();
        brickCount -= brickCount % lineCount;

        brickCount += lineCount / 2;

        Brick[] brickArray  = createBrickArray(brickCount);

        Dimension2D brickSize = new Dimension2D((int) getDrawBrickLength(drawArea, lineCount, brickCount),(int) getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio));

        int i;
        for(i = 0; i < brickArray.length; i++){
            int line = i / getBrickOnLine(brickCount,lineCount);
            if(line == lineCount)
                break;
            double x = (i % getBrickOnLine(brickCount,lineCount)) * getDrawBrickLength(drawArea, lineCount, brickCount);
            x = (line % 2 == 0) ? x : (x - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2));
            double y = (line) * getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio);
            brickArray[i] = brickFactory.makeBrick(new Point2D(x,y),brickSize, rnd.nextInt(4)+1);
        }

        for(double y = getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio); i < brickArray.length; i++, y += 2 * getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio)){
            double x = (getBrickOnLine(brickCount,lineCount) * getDrawBrickLength(drawArea, lineCount, brickCount)) - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2);
            brickArray[i] = brickFactory.makeBrick(new Point2D(x,y),brickSize, rnd.nextInt(BrickFactory.brickTypes.length)+1);
        }
        return brickArray;
    }
}
