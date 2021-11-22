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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * this class is used for creating the home menu which is also known as the start page.
 */
public class HomeMenu extends JComponent implements MouseListener, MouseMotionListener {

    private static final String GREETINGS = "Welcome to:";
    private static final String GAME_TITLE = "Brick Destroy";
    private static final String CREDITS = "Version 0.1";
    private static final String START_TEXT = "Start";
    private static final String EXIT_TEXT = "Exit";
    private static final String INFO_TEXT = "Info";

    private static final Color BG_COLOR = Color.GREEN.darker();
    private static final Color BORDER_COLOR = new Color(200,8,21); //Venetian Red
    private static final Color DASH_BORDER_COLOR = new  Color(255, 216, 0);//school bus yellow
    private static final Color TEXT_COLOR = new Color(16, 52, 166);//egyptian blue
    private static final Color CLICKED_BUTTON_COLOR = BG_COLOR.brighter();
    private static final Color CLICKED_TEXT = Color.WHITE;
    private static final int BORDER_SIZE = 5;
    private static final float[] DASHES = {12,6};

    private Rectangle menuFace;
    private Rectangle startButton;
    private Rectangle exitButton;

    private BasicStroke borderStoke;
    private BasicStroke borderStoke_noDashes;

    private Font greetingsFont;
    private Font gameTitleFont;
    private Font creditsFont;
    private Font buttonFont;

    private GameFrame owner;

    private boolean startClicked;
    private boolean menuClicked;

    // for the images
    private static final String MAIN_IMAGE_FILE_NAME = "mainimage.png";
    private static final String INFO_IMAGE_FILE_NAME = "Info.png";

    // for the info button
    private Rectangle infoButton;
    private boolean infoClicked;

    private static HomeMenu uniqueHomeMenu;

    public static HomeMenu singletonHomeMenu(GameFrame owner, Dimension area){
        if(getUniqueHomeMenu() == null){
            setUniqueHomeMenu(new HomeMenu(owner,area));
        }
        return getUniqueHomeMenu();
    }

    /**
     * this constructor creates the home menu object.
     *
     * @param owner this takes in the game frame which is also a frame that is shown on the start page when the game is started
     * @param area is the information that is used to create the window in terms of the dimensions.
     */
    private HomeMenu(GameFrame owner,Dimension area){

        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        setOwner(owner);

        setMenuFace(new Rectangle(new Point(0,0),area));

        this.setPreferredSize(area);

        createButtonObjects(area);

        createStylingObjects();
    }

    /**
     * this is used to create the button objects that is used in the home menu.
     *
     * @param area this is the area dimension where the button could add the buttons.
     */
    private void createButtonObjects(Dimension area){
        Dimension btnDim = new Dimension(area.width / 3, area.height / 12);
        setStartButton(new Rectangle(btnDim));
        setExitButton(new Rectangle(btnDim));

        //for the info button
        setInfoButton(new Rectangle(btnDim));
    }

    /**
     * this is the method used to create and instantiate the styling variables.
     */
    private void createStylingObjects(){
        setBorderStoke(new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,DASHES,0));
        setBorderStoke_noDashes(new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));

        setGreetingsFont(new Font("Noto Mono",Font.PLAIN,25));
        setGameTitleFont(new Font("Noto Mono",Font.BOLD,40));
        setCreditsFont(new Font("Monospaced",Font.PLAIN,10));
        setButtonFont(new Font("Monospaced",Font.PLAIN,getStartButton().height-2));
    }

    /**
     * this method is used to draw call the drawMenu method and pass the Graphics g to Graphics2D.
     *
     * @param g this the Graphics information that is going to be used to be converted to Graphics2D and passing through to drawMenu method.
     */
    public void paint(Graphics g){
        drawMenu((Graphics2D)g);
    }


    /**
     * this method is used to call the drawButton method. and also needs to set the dimension for g2d that is going to be used for drawText and drawButton method.
     *
     * @param g2d this is the Graphics2D that is being used to store information to be used for drawing methods.
     */
    public void drawMenu(Graphics2D g2d){

        drawContainer(g2d);

        /*
        all the following method calls need a relative
        painting directly into the HomeMenu rectangle,
        so the translation is made here so the other methods do not do that.
         */
        g2d.translate(getMenuFaceXCoordinate(),getMenuFaceYCoordinate());

        //methods calls
        drawText(g2d);
        drawButton(g2d);
        //end of methods calls

        g2d.translate(-getMenuFaceXCoordinate(),-getMenuFaceYCoordinate());
        g2d.setFont(getPrevFont(g2d));
        g2d.setColor(getPrevColor(g2d));
    }

    private Color getPrevColor(Graphics2D g2d){
        return g2d.getColor();
    }

    private Font getPrevFont(Graphics2D g2d){
        return g2d.getFont();
    }

    /**
     * this method is used to get the X coordinate of the menu face.
     *
     * @return returns a double value of the X coordinate of the menu face.
     */
    private double getMenuFaceXCoordinate(){
        return getMenuFace().getX();
    }

    /**
     * this method is used to get the Y coordinate of the menu face.
     *
     * @return returns a double value of the Y coordinate of the menu face.
     */
    private double getMenuFaceYCoordinate(){
        return getMenuFace().getY();
    }

    /**
     * this method is used to draw the background of the home menu.
     *
     * @param g2d the graphics2d that is used to draw on.
     */
    private void drawContainer(Graphics2D g2d){
        Color prev = g2d.getColor();

        //code for an image in the start game menu.
        BufferedImage image = null;
        try{
            image = ImageIO.read(HomeMenu.class.getResource(MAIN_IMAGE_FILE_NAME));
        }catch(IOException e){
            g2d.setColor(BG_COLOR);
        }

        g2d.fill(getMenuFace());
        g2d.drawImage(image,0,0,this);

        Stroke tmp = g2d.getStroke();

        g2d.setStroke(getBorderStoke_noDashes());
        g2d.setColor(DASH_BORDER_COLOR);
        g2d.draw(getMenuFace());

        g2d.setStroke(getBorderStoke());
        g2d.setColor(BORDER_COLOR);
        g2d.draw(getMenuFace());

        g2d.setStroke(tmp);

        g2d.setColor(prev);
    }

    /**
     * this method is used to draw the text to the home menu.
     *
     * @param g2d this is Graphics2D that have the information on how to style the texts
     */
    private void drawText(Graphics2D g2d){

        g2d.setColor(TEXT_COLOR);

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D greetingsRect = getGreetingsFont().getStringBounds(GREETINGS,frc);
        Rectangle2D gameTitleRect = getGameTitleFont().getStringBounds(GAME_TITLE,frc);
        Rectangle2D creditsRect = getCreditsFont().getStringBounds(CREDITS,frc);

        int sX,sY;

        sX = (int)(getMenuFace().getWidth() - greetingsRect.getWidth()) / 2;
        sY = (int)(getMenuFace().getHeight() / 4);

        g2d.setFont(getGreetingsFont());
        g2d.drawString(GREETINGS,sX,sY);

        sX = (int)(getMenuFace().getWidth() - gameTitleRect.getWidth()) / 2;
        sY += (int) gameTitleRect.getHeight() * 1.1;//add 10% of String height between the two strings

        g2d.setFont(getGameTitleFont());
        g2d.drawString(GAME_TITLE,sX,sY);

        sX = (int)(getMenuFace().getWidth() - creditsRect.getWidth()) / 2;
        sY += (int) creditsRect.getHeight() * 1.1;

        g2d.setFont(getCreditsFont());
        g2d.drawString(CREDITS,sX,sY);
    }



    /**
     * this is used to draw the button on the home menu.
     *
     * @param g2d this is used to get the information on how to style it.
     */
    private void drawButton(Graphics2D g2d){

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D txtRect = buttonFont.getStringBounds(START_TEXT,frc);
        Rectangle2D mTxtRect = buttonFont.getStringBounds(EXIT_TEXT,frc);

        // additional lines of code for the info button
        Rectangle2D m2TxtRect = buttonFont.getStringBounds(INFO_TEXT,frc);

        g2d.setFont(buttonFont);

        int x = (getMenuFace().width - getInfoButton().width) / 2;
        int y = (int) ((getMenuFace().height - getInfoButton().height) * 0.65);

        // these codes are for the implementation of the info button
        getInfoButton().setLocation(x,y);

        x = (int)(getInfoButton().getWidth() - m2TxtRect.getWidth()) / 2;
        y = (int)(getInfoButton().getHeight() - m2TxtRect.getHeight()) / 2;

        x += getInfoButton().x;
        y += getInfoButton().y + (getInfoButton().height * 0.9);

        if(isInfoClicked()){
            drawClickedButton(g2d, getInfoButton(), INFO_TEXT, x, y);
        }
        else{
            drawNormalButton(g2d,getInfoButton(),INFO_TEXT,x,y);
        }

        x = (getMenuFace().width - getStartButton().width) / 2;
        y = (int) ((getMenuFace().height - getStartButton().height) * 0.8);

        getStartButton().setLocation(x,y);

        x = (int)(getStartButton().getWidth() - txtRect.getWidth()) / 2;
        y = (int)(getStartButton().getHeight() - txtRect.getHeight()) / 2;

        x += getStartButton().x;
        y += getStartButton().y + (getStartButton().height * 0.9);

        if(isStartClicked()){
            drawClickedButton(g2d, getStartButton(), START_TEXT, x, y);
        }
        else{
            drawNormalButton(g2d,getStartButton(),START_TEXT,x,y);
        }

        x = getStartButton().x;
        y = getStartButton().y;

        y *= 1.2;

        getExitButton().setLocation(x,y);

        x = (int)(getExitButton().getWidth() - mTxtRect.getWidth()) / 2;
        y = (int)(getExitButton().getHeight() - mTxtRect.getHeight()) / 2;

        x += getExitButton().x;
        y += getExitButton().y + (getStartButton().height * 0.9);

        if(isMenuClicked()){
            drawClickedButton(g2d,getExitButton(),EXIT_TEXT,x,y);
        }
        else{
            drawNormalButton(g2d,getExitButton(),EXIT_TEXT,x,y);
        }

    }

    /**
     * this method is used to set the graphics2d properties for the button when it is being clicked/hold.
     *
     * @param g2d this is the graphics2D for the button
     * @param button this is the button that is going to be changed
     * @param text this is the text of the button.
     * @param xCoordinate this is the position of the button on x-axis.
     * @param yCoordinate this is the position of the button on y-axis.
     */
    private void drawNormalButton (Graphics2D g2d,Rectangle button, String text, int xCoordinate, int yCoordinate){
        g2d.draw(button);
        g2d.drawString(text,xCoordinate,yCoordinate);
    }

    /**
     * this method is used to set the graphics2d properties for the button when it is being clicked/hold.
     *
     * @param g2d this is the graphics2D for the button
     * @param button this is the button that is going to be changed
     * @param text this is the text of the button.
     * @param xCoordinate this is the position of the button on x-axis.
     * @param yCoordinate this is the position of the button on y-axis.
     */
    private void drawClickedButton(Graphics2D g2d,Rectangle button, String text, int xCoordinate, int yCoordinate){
        g2d.setColor(CLICKED_BUTTON_COLOR);
        g2d.draw(button);
        g2d.setColor(CLICKED_TEXT);
        g2d.drawString(text,xCoordinate,yCoordinate);
        g2d.setColor(getGraphics2DColor(g2d));
    }

    /**
     * this method is used to get the Color from the Graphics2D object
     *
     * @param g2d the graphics2D object
     * @return returns the color of the graphics2D.
     */
    private Color getGraphics2DColor(Graphics2D g2d){
        return g2d.getColor();
    }

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse button is clicked. In this case, it will act accordingly if the button is being clicked.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(getStartButton().contains(p)){
           getOwner().enableGameBoard();
        }
        else if(getExitButton().contains(p)){
            System.out.println("Goodbye " + System.getProperty("user.name"));
            System.exit(0);
        }
        else if(getInfoButton().contains(p)){
            try {
                this.DisplayInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse button is pressed. In this case it will repaint the button once it is being pressed and set the variable to be true which will be used in mouseRelease method.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(getStartButton().contains(p)){
            setStartClicked(true);
            repaint(getStartButton().x,getStartButton().y,getStartButton().width+1,getStartButton().height+1);
        }
        else if(getExitButton().contains(p)){
            setMenuClicked(true);
            repaint(getExitButton().x,getExitButton().y,getExitButton().width+1,getExitButton().height+1);
        }
        else if(getInfoButton().contains(p)){
            setInfoClicked(true);
            repaint(getInfoButton().x, getInfoButton().y, getInfoButton().width+1,getInfoButton().height+1);
        }
    }

    /**
     * this is used to set the start clicked variable.
     *
     * @param startClicked the boolean value that is used to set the start click variable
     */
    public void setStartClicked(boolean startClicked) {
        this.startClicked = startClicked;
    }

    /**
     * this is used to set the menu clicked variable.
     *
     * @param menuClicked the boolean value that is used to set the menu click variable
     */
    public void setMenuClicked(boolean menuClicked) {
        this.menuClicked = menuClicked;
    }

    /**
     * this is used to set info clicked variable.
     *
     * @param infoClicked the boolean value that is used to set the info click variable
     */
    public void setInfoClicked(boolean infoClicked) {
        this.infoClicked = infoClicked;
    }

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse button is released. this will repaint the button once it is being released and set the variable to be false..
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(isStartClicked()){
            setStartClicked(false);
            repaint(getStartButton().x,getStartButton().y,getStartButton().width+1,getStartButton().height+1);
        }
        else if(isMenuClicked()){
            setMenuClicked(false);
            repaint(getExitButton().x,getExitButton().y,getExitButton().width+1,getExitButton().height+1);
        }
        else if(isInfoClicked()){
            setInfoClicked(false);
            repaint(getInfoButton().x, getInfoButton().y, getInfoButton().width+1,getInfoButton().height+1);
        }
    }

    /**
     * this method is used to get the start clicked variable
     *
     * @return returns the boolean value of start clicked variable.
     */
    public boolean isStartClicked(){
        return this.startClicked;
    }

    /**
     * this method is used to get the menu clicked variable.
     *
     * @return returns the boolean value of the menu clicked variable.
     */
    public boolean isMenuClicked(){
        return this.menuClicked;
    }

    /**
     * this method is used to get the info clicked variable.
     *
     * @return returns the boolean value of info clicked variable.
     */
    public boolean isInfoClicked(){
        return this.infoClicked;
    }

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse enters a component.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    /**
     * this is initially a MouseListener Interface class method, and it is being implemented. It is called when a mouse exits a component.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    /**
     * this is initially a MouseMotionListener Interface class method, and it is being implemented. It is called when a mouse pressed on a component and dragged.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    /**
     * this is initially a MouseMotionListener Interface class method, and it is being implemented. It is called when a mouse moved onto a component but no buttons are pushed. In this case it will change the cursor when the mouse is on the button.
     *
     * @param mouseEvent this records the input from the mouse.
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if (checkIfMouseMovedToButton(p))
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else
            this.setCursor(Cursor.getDefaultCursor());
    }

    /**
     * this method is used to check if the mouse is on the button.
     *
     * @param p this takes the event generated by the mouse.
     * @return returns true if the mouse is on the button.
     */
    private boolean checkIfMouseMovedToButton(Point p){
        return (startButton.contains(p) || exitButton.contains(p) || infoButton.contains(p));
    }

    /**
     * this method is used to create another window which contains the image to show the instruction on how to play this game.
     *
     * @throws IOException this is used if the image could not be loaded.
     */
    public void DisplayInfo() throws IOException{
        BufferedImage display = ImageIO.read(HomeMenu.class.getResource(INFO_IMAGE_FILE_NAME));
        ImageIcon icon = new ImageIcon(display);
        JFrame frame=new JFrame();
        frame.setTitle(INFO_TEXT);
        frame.setLayout(new FlowLayout());
        frame.setSize(500,400);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
    }

    public static HomeMenu getUniqueHomeMenu() {
        return uniqueHomeMenu;
    }

    public static void setUniqueHomeMenu(HomeMenu uniqueHomeMenu) {
        HomeMenu.uniqueHomeMenu = uniqueHomeMenu;
    }

    public Rectangle getMenuFace() {
        return menuFace;
    }

    public void setMenuFace(Rectangle menuFace) {
        this.menuFace = menuFace;
    }

    public Rectangle getStartButton() {
        return startButton;
    }

    public void setStartButton(Rectangle startButton) {
        this.startButton = startButton;
    }

    public Rectangle getExitButton() {
        return exitButton;
    }

    public void setExitButton(Rectangle exitButton) {
        this.exitButton = exitButton;
    }

    public Rectangle getInfoButton() {
        return infoButton;
    }

    public void setInfoButton(Rectangle infoButton) {
        this.infoButton = infoButton;
    }

    public Font getGreetingsFont() {
        return greetingsFont;
    }

    public void setGreetingsFont(Font greetingsFont) {
        this.greetingsFont = greetingsFont;
    }

    public Font getGameTitleFont() {
        return gameTitleFont;
    }

    public void setGameTitleFont(Font gameTitleFont) {
        this.gameTitleFont = gameTitleFont;
    }

    public Font getCreditsFont() {
        return creditsFont;
    }

    public void setCreditsFont(Font creditsFont) {
        this.creditsFont = creditsFont;
    }

    public Font getButtonFont() {
        return buttonFont;
    }

    public void setButtonFont(Font buttonFont) {
        this.buttonFont = buttonFont;
    }

    public BasicStroke getBorderStoke() {
        return borderStoke;
    }

    public void setBorderStoke(BasicStroke borderStoke) {
        this.borderStoke = borderStoke;
    }

    public BasicStroke getBorderStoke_noDashes() {
        return borderStoke_noDashes;
    }

    public void setBorderStoke_noDashes(BasicStroke borderStoke_noDashes) {
        this.borderStoke_noDashes = borderStoke_noDashes;
    }

    public GameFrame getOwner() {
        return owner;
    }

    public void setOwner(GameFrame owner) {
        this.owner = owner;
    }
}
