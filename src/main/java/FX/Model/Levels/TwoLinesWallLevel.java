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
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;


/**
 * this class is used to create one of the levels.
 */
public class TwoLinesWallLevel extends FullWallRowsLevels implements WallLevelTemplates{

    /**
     * this method is one of the template used for the wall (level). this creates a level that looks like an almost straight line of bricks on the left and right of the center.
     *
     * @param drawArea this is the area which the bricks could be placed
     * @param brickCount this is the amount of bricks which will be in for the level.
     * @param lineCount this is the number of rows of bricks for the level.
     * @param brickSizeRatio this is the size ratio of the brick.
     * @param typeA this is one of the type of brick used for this level.
     * @param typeB this is one of the type of brick used for this level.
     * @return it returns the bricks for the wall (level) in the form of a brick array.
     */
    public Brick[] level(Rectangle drawArea, int brickCount, int lineCount, double brickSizeRatio, int typeA, int typeB){
        brickCount -= brickCount % lineCount;

        int centerLeft = getBrickOnLine(brickCount,lineCount) / 2 - 1;
        int centerRight = getBrickOnLine(brickCount,lineCount) / 2 + 1;

        brickCount += lineCount / 2;

        Brick[] brickArray  = createBrickArray(brickCount);

        Dimension2D brickSize = new Dimension2D((int) getDrawBrickLength(drawArea, lineCount, brickCount),(int) getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio));

        int i;
        for(i = 0; i < brickArray.length; i++){
            int line = i / getBrickOnLine(brickCount,lineCount);
            if(line == lineCount)
                break;
            int posX = i % getBrickOnLine(brickCount,lineCount);
            double x = posX * getDrawBrickLength(drawArea, lineCount, brickCount);
            x = (line % 2 == 0) ? x : (x - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2));
            double y = (line) * getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio);

            boolean b = ((i % 2 == 0) || (posX > centerLeft && posX <= centerRight));
            brickArray[i] = b ? makeBrick(new Point2D(x,y),brickSize,typeA) : makeBrick(new Point2D(x,y),brickSize,typeB);
        }

        for(double y = getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio);i < brickArray.length;i++, y += 2*getDrawBrickHeight(drawArea, brickCount, lineCount, brickSizeRatio)){
            double x = (getBrickOnLine(brickCount,lineCount) * getDrawBrickLength(drawArea, lineCount, brickCount)) - (getDrawBrickLength(drawArea, lineCount, brickCount) / 2);
            brickArray[i] = makeBrick(new Point2D(x,y),brickSize,typeA);
        }

        return brickArray;
    }
}

