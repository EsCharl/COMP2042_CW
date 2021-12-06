package FX.Model.Entities;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public abstract class Entities {
    private Point2D position;
    private int width;
    private int height;
    private BoundingBox bounds;

    private Color borderColor;
    private Color innerColor;

    public Entities(Point2D position, Color borderColor, Color innerColor, int width, int height){
        setPosition(position);
        setBorderColor(borderColor);
        setInnerColor(innerColor);
        setWidth(width);
        setHeight(height);
        setBounds(new BoundingBox(position.getX(), position.getY(), width,height));
    }

    public void resetPosition(){
        setBounds(new BoundingBox(position.getX(),position.getY(),width,height));
    }

    public BoundingBox getBounds() {
        return bounds;
    }

    public void setBounds(BoundingBox bounds) {
        this.bounds = bounds;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
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
