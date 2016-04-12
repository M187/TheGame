package com.miso.thegame.gameMechanics.nonMovingObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.GameObject;

import java.util.ArrayList;

/**
 * Created by Miso on 15.11.2015.
 *
 * abstract object to hold common functionality for NON MOVING OBJECTS!
 * Objects extending this class can not move under any circumstances!
 */
public abstract class StaticObject extends GameObject {

    protected int range;

    public StaticObject(){}

    public StaticObject(Point coordPoint, Bitmap image){
        this.x = coordPoint.x;
        this.y = coordPoint.y;
        this.setImage(image);
        initializeGridCoords();
        initializeVertices();
    }

    /**
     * Set display coordinates - ! not a global coord grid, just display grid
     * @param displayMiddleXCoord middle position of an layoutImage. It is adjusted so that it corresponds with top left position based on a layoutImage width.
     */
    public void setDisplayXCoord(int displayMiddleXCoord) {
        this.displayXCoord = displayMiddleXCoord;
    }
    public void setDisplayYCoord(int displayMiddleYCoord) {
        this.displayYCoord = displayMiddleYCoord;
    }

    @Override
    /**
     * Return grid coordinates as a Point.
     *
     * Overriding super functionality with static data, since Static objects CAN NOT MOVE!
     */
    public Point getGridCoordinates(){
        return gridCoords;
    }

    protected void initializeGridCoords(){
        this.gridCoords = super.getGridCoordinates();
    }

    protected void initializeVertices(){
        this.objectVertices.clear();
        this.objectVertices.add(new Point(getX(), getY()));
        this.objectVertices.add(new Point(getX(), getY() + getImage().getHeight()));
        this.objectVertices.add(new Point(getX() + getImage().getWidth(), getY() + getImage().getHeight()));
        this.objectVertices.add(new Point(getX() + getImage().getWidth(), getY()));
    }

    @Override
    public ArrayList<Point> getObjectCollisionVertices(){
        return this.objectVertices;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(this.getImage(), this.getDisplayXCoord(), this.getDisplayYCoord(), null);
    }

}
