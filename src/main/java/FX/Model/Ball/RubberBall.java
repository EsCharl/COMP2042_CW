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

package FX.Model.Ball;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * this class is for the rubber ball, an extension from ball abstract class.
 */
public class RubberBall extends Ball {


    private static final int DEF_RADIUS = 5;

    private static final Color DEF_INNER_COLOR = new Color(255, 219, 88,0);
    private static final Color DEF_BORDER_COLOR = DEF_INNER_COLOR.darker().darker();

    /**
     * this constructor is used to create a rubber ball.
     *
     * @param center this is the position where the ball is created.
     */
    public RubberBall(Point2D center){
        super(center,DEF_RADIUS,DEF_INNER_COLOR,DEF_BORDER_COLOR);
    }
}
