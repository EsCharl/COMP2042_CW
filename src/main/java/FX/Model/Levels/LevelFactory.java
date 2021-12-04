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

public class LevelFactory {

        public WallLevelTemplates getLevel(String levelType){
            if (levelType.equalsIgnoreCase("CHAINLEVEL")) {
                return new ChainWallLevel();
            } else if (levelType.equalsIgnoreCase("TWOLINESLEVEL")) {
                return new TwoLinesWallLevel();
            } else if (levelType.equalsIgnoreCase("RANDOMLEVEL")) {
                return new RandomWallLevel();
            }
            return null;
        }


}
