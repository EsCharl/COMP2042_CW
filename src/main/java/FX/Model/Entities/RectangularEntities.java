package FX.Model.Entities;

import javafx.scene.paint.Color;

public class RectangularEntities {
    private int positionX;
    private int positionY;
    private int width;
    private int height;

    private Color borderColor;
    private Color innerColor;

    public RectangularEntities(int positionX, int positionY, Color borderColor, Color innerColor, int width, int height){
        setPositionX(positionX);
        setPositionY(positionY);
        setBorderColor(borderColor);
        setInnerColor(innerColor);
        setWidth(width);
        setHeight(height);
    }

    /**
     * this method is used to get the entity current position in X coordinate.
     *
     * @return this returns the integer value where the entity is currently at.
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * this method is used to set the entity current position in X coordinate.
     *
     * @param positionX this is the integer used to set the position of the entity for the X coordinate.
     */
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    /**
     * this method is used to get the entity current position in Y coordinate.
     *
     * @return this returns the integer value where the entity is currently at.
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * this method is used to set the entity current position in Y coordinate.
     *
     * @param positionY this is the integer used to set the position of the entity for the Y coordinate.
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getInnerColor() {
        return innerColor;
    }

    public void setInnerColor(Color innerColor) {
        this.innerColor = innerColor;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
