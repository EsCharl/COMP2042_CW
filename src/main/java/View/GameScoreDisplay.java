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

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.ArrayList;

public class GameScoreDisplay {
    /**
     * This method creates a high score panel to show the scores after each game.
     *
     * @param sorted takes in the arraylist of string from getHighScore method to display on the panel.
     * @throws BadLocationException just incase if the insertion of the string into the pop up is an error.
     */
    public void highScorePanel(ArrayList<String> sorted, String playerTime) throws BadLocationException {
        JFrame frame=new JFrame("HIGH SCORE");
        frame.setLayout(new FlowLayout());
        frame.setSize(500,400);

        Container cp = frame.getContentPane();
        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(attributeSet, true);

        // Set the attributes before adding text
        pane.setCharacterAttributes(attributeSet, true);
        pane.setText(String.format("%-20s %s\n\n", "Name", "Time"));

        attributeSet = new SimpleAttributeSet();
        setTextStyle(attributeSet,Color.RED,Color.GREEN);

        Document doc = pane.getStyledDocument();
        for (int i = 0 ; i < sorted.size(); i++){
            String[] list = sorted.get(i).split(",",2);
            String name = list[0];
            String time = list[1];
            if(playerTime.equals(time) && System.getProperty("user.name").equals(name)){
                setTextStyle(attributeSet,Color.BLACK,Color.YELLOW);
                doc.insertString(doc.getLength(),String.format("%-20s %s\n",name, time), attributeSet);
                setTextStyle(attributeSet,Color.RED,Color.GREEN);
            }else
                 doc.insertString(doc.getLength(),String.format("%-20s %s\n",name, time), attributeSet);
        }
        JScrollPane scrollPane = new JScrollPane(pane);
        cp.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /**
     * this method is used to set the text property, namely the background color of the text and the color of the text.
     *
     * @param attributeSet this is the attribute set where text style is going to be set.
     * @param foreground this is the color of the foreground of the text.
     * @param background this is the color of the background of the text.
     */
    private void setTextStyle(SimpleAttributeSet attributeSet, Color foreground, Color background) {
        StyleConstants.setForeground(attributeSet, foreground);
        StyleConstants.setBackground(attributeSet, background);
    }
}
