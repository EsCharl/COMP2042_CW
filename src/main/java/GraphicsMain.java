import Controller.GameFrame;

import java.awt.*;

/**
 * this class is the main class which allows the game to start.
 */
public class GraphicsMain {

    public static void main(String[] args){
        EventQueue.invokeLater(() -> GameFrame.singletonGameFrame().initialize());
    }

}
