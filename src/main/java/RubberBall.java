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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * this class is for the rubber ball, an implementation from ball class.
 */
public class RubberBall extends Ball {


    private static final int DEF_DIAMETER = 10;
    private static final Color DEF_INNER_COLOR = new Color(255, 219, 88);
    private static final Color DEF_BORDER_COLOR = DEF_INNER_COLOR.darker().darker();

    /**
     * this constructor is used to create a rubber ball.
     *
     * @param center this is the position where the ball is created.
     */
    public RubberBall(Point2D center){
        super(center,DEF_DIAMETER,DEF_DIAMETER,DEF_INNER_COLOR,DEF_BORDER_COLOR);
    }

    /**
     * this method is used to create a shape of the ball.
     *
     * @param center this is the position where the ball is formed
     * @param diameterA this is the diameter of the ball based on x-axis
     * @param diameterB this is the diameter of the ball based on y-axis.
     * @return it returns a shape of the ball on a position specified in center.
     */
    @Override
    protected Shape makeBall(Point2D center, int diameterA, int diameterB) {

        return new Ellipse2D.Double(getXUpperLeftCorner(center, diameterA),getYUpperLeftCorner(center, diameterB),diameterA,diameterB);
    }

    private double getXUpperLeftCorner(Point2D center, int diameterA){
        return center.getX() - (diameterA / 2);
    }

    private double getYUpperLeftCorner(Point2D center, int diameterB){
        return center.getY() - (diameterB / 2);
    }
}
