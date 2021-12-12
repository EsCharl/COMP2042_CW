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

package FX.Model.Entities;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * this is an abstract class which is used to indicate that the classes that extends it is an entity object.
 */
public abstract class Entities {
    private Point2D fixedInitialPosition;
    private int width;
    private int height;
    private BoundingBox bounds;

    private Color borderColor;
    private Color innerColor;

    /**
     * this constructor is used to set the entities variable.
     *
     * @param position this is the top left position of the entity
     * @param borderColor this is the border color of the entity.
     * @param innerColor this is the inner color of the entity.
     * @param width this is the width length of the entity.
     * @param height this is the height length of the entity.
     */
    public Entities(Point2D position, Color borderColor, Color innerColor, int width, int height){
        setFixedInitialPosition(position);
        setBorderColor(borderColor);
        setInnerColor(innerColor);
        setWidth(width);
        setHeight(height);
        setBounds(new BoundingBox(getFixedInitialPosition().getX(), getFixedInitialPosition().getY(), getWidth(),getHeight()));
    }

    /**
     * this method returns true as it is used when there is a collision occurred. (set this false to disable multi-ball)
     *
     * @param point this is the point fo impact between the entities.
     * @param dir this is the direction of impact.
     * @return this returns true.
     */
    public boolean setImpact(Point2D point, int dir){
        return true;
    }

    /**
     * this method is used to reset the position of the entity.
     */
    public void resetPosition(){
        setBounds(new BoundingBox(fixedInitialPosition.getX(), fixedInitialPosition.getY(),width,height));
    }

    /**
     * this method is used to get the bounding box of the entity.
     *
     * @return this returns the bounding box object of the entity.
     */
    public BoundingBox getBounds() {
        return bounds;
    }

    /**
     * this method is used to set the bounding box of the entity into a variable for future reference.
     *
     * @param bounds this is the bounding box object used to set into a variable of the entity for future reference.
     */
    public void setBounds(BoundingBox bounds) {
        this.bounds = bounds;
    }

    /**
     * this method is used to get the initial top left position of the entity.
     *
     * @return this returns the top left position of the entity.
     */
    public Point2D getFixedInitialPosition() {
        return fixedInitialPosition;
    }

    /**
     * this method is used to set the top left position of the entity into a variable which is the initial position of the entity.
     *
     * @param fixedInitialPosition this is the position that is going to be set into a variable which is the initial position of the entity.
     */
    public void setFixedInitialPosition(Point2D fixedInitialPosition) {
        this.fixedInitialPosition = fixedInitialPosition;
    }

    /**
     * this method is used to get the border color of the entity.
     *
     * @return this returns the border color of the entity.
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * this method is used to set the border color of the entity.
     *
     * @param borderColor this is the color used to set it into a variable which is the border color of the entity.
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * this method is used to get the inner color of the entity which is going to be used to draw on the screen.
     *
     * @return this returns the inner color of the entity.
     */
    public Color getInnerColor() {
        return innerColor;
    }

    /**
     * this method is used to set the inner color of the entity for future references.
     *
     * @param innerColor this is the color used to set the inner color of the entity.
     */
    public void setInnerColor(Color innerColor) {
        this.innerColor = innerColor;
    }

    /**
     * this method is used to get the width of the entity
     *
     * @return this is the width of the entity.
     */
    public int getWidth() {
        return width;
    }

    /**
     * this method is used to set the width of the entity.
     *
     * @param width this is the width used to set into a variable for future reference.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * this method is used to get the height of the entity.
     *
     * @return this returns the height of the entity.
     */
    public int getHeight() {
        return height;
    }

    /**
     * this method is used to get set the height of the entity.
     *
     * @param height this is the height used to set into a variable for the entity.
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
