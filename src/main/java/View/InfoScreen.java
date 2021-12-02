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

package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class InfoScreen {

    private final String TITLE = "HOW TO PLAY";

    private final String INFO_IMAGE_FILE_NAME = "/Info.png";
    private JFrame frame;

    public InfoScreen(){
        BufferedImage display = null;
        try {
            display = ImageIO.read(HomeMenu.class.getResource(getINFO_IMAGE_FILE_NAME()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon icon = new ImageIcon(display);
        frame=new JFrame();
        frame.setTitle(getTITLE());
        frame.setLayout(new FlowLayout());
        frame.setSize(500,400);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
    }

    /**
     * this method is used to create another window which contains the image to show the instruction on how to play this game.
     *
     * @throws IOException this is used if the image could not be loaded.
     */
    public void DisplayInfo() throws IOException{
        frame.setVisible(true);
    }

    /**
     * this methos is used to get the string constant which is used to be displayed as the window title.
     *
     * @return this is the string that is going to be displayed on the window title.
     */
    public String getTITLE() {
        return TITLE;
    }

    /**
     * this method is used to get the information image on how to play the game.
     *
     * @return this returns a string which is the file path to the image.
     */
    public String getINFO_IMAGE_FILE_NAME() {
        return INFO_IMAGE_FILE_NAME;
    }
}
