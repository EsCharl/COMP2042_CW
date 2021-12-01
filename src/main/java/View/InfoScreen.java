package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class InfoScreen {

    private final String TITLE = "HOW TO PLAY";

    private final String INFO_IMAGE_FILE_NAME = "/Info.png";

    /**
     * this method is used to create another window which contains the image to show the instruction on how to play this game.
     *
     * @throws IOException this is used if the image could not be loaded.
     */
    public void DisplayInfo() throws IOException{
        BufferedImage display = ImageIO.read(HomeMenu.class.getResource(getINFO_IMAGE_FILE_NAME()));
        ImageIcon icon = new ImageIcon(display);
        JFrame frame=new JFrame();
        frame.setTitle(getTITLE());
        frame.setLayout(new FlowLayout());
        frame.setSize(500,400);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
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
