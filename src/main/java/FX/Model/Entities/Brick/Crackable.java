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

public interface Crackable {

    int LEFT = 10;
    int RIGHT = 20;
    int UP = 30;
    int DOWN = 40;

    int DEF_CRACK_DEPTH = 1;
    int DEF_STEPS = 35;

    int VERTICAL = 100;
    int HORIZONTAL = 200;

    Crack crack = null;

    Crack getCrack();

    void setCrack(Crack crack);

    void setCrackPath(Path path);

    Path getCrackPath();
}
