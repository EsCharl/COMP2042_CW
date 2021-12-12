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

package FX.Model.Entities.Ball;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * this class is an addition which creates a clone ball to increase the game enjoyability.
 */
public class BallClone extends Ball {
    private static final int DEF_RADIUS = 5;

    public final static int MAX_CLONE_BALL = 3;
    public static final double CLONE_BALL_GENERATION_PROBABILITY = 0.3;

    private static final Color DEF_INNER_COLOR = Color.rgb(10, 100, 150, 1);
    private static final Color DEF_BORDER_COLOR = DEF_INNER_COLOR.darker().darker();

    /**
     * this constructor is used to create a ball clone which is going to be used in the game.
     *
     * @param center this is the starting position where the ball clone will have.
     */
    public BallClone(Point2D center) {
        super(center, DEF_RADIUS, DEF_INNER_COLOR, DEF_BORDER_COLOR);
    }
}
