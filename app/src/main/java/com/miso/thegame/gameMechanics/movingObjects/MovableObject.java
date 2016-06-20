package com.miso.thegame.gameMechanics.movingObjects;

import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.GameObject;
import com.miso.thegame.gameMechanics.movingObjects.spells.EffectTimeout;

import java.util.ArrayList;

/**
 * Created by Miso on 20.11.2015.
 */
public abstract class MovableObject extends GameObject {

    public int heading = 0;
    public boolean movementDisabled;
    public EffectTimeout movementDisabledTimeout;
    protected int speed = 0;
    protected int frameDeltaX;
    protected int frameDeltaY;
    protected int dx;
    protected int dy;
    protected int deltaX;
    protected int deltaY;
    protected int xPositionBeforeMoving;
    protected int yPositionBeforeMoving;

    protected void setPositionBeforeMoving() {
        this.xPositionBeforeMoving = this.x;
        this.yPositionBeforeMoving = this.y;
    }

    public void restorePositionBeforeMoving() {
        this.x = this.xPositionBeforeMoving;
        this.y = this.yPositionBeforeMoving;
    }

    /**
     * Set heading based on delta x and y coordinates.
     * Using arcus tangens to calculate degrees of heading.
     */
    public void calculateHeading() {
        if (!(this.frameDeltaX == 0 && this.frameDeltaY == 0)) {
            int currentSegment = calculateSegment();
            if (this.frameDeltaY == 0) {
                heading = ((frameDeltaX > 0) ? -270 : -90);
            } else if (this.frameDeltaX == 0) {
                heading = ((frameDeltaY > 0) ? -180 : 0);
            } else if (currentSegment == 1 || currentSegment == 3) {
                heading = (int)(90 - Math.toDegrees(Math.atan((double) this.frameDeltaX / this.frameDeltaY)) - (90 * currentSegment));
            } else {
                heading = (int)(0 - Math.toDegrees(Math.atan((double) this.frameDeltaX / this.frameDeltaY)) - (90 * currentSegment));
            }
        }
    }

    public int getHeading() {
        return heading;
    }

    public void moveObject() {
        this.deltaX = getDx() - getX();
        this.deltaY = getDy() - getY();
        double speedFactor = this.getSpeed() / Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        //TODO: initialize only once for 1 movement - improve speed
        //get x,y coordinates using delta ratio
        frameDeltaX = (int) (this.deltaX * speedFactor);
        frameDeltaY = (int) (this.deltaY * speedFactor);
        setX(setCoord(getX(), getDx(),frameDeltaX));
        setY(setCoord(getY(), getDy(), frameDeltaY));
        //todo add for zero delta.
        calculateHeading();
    }

    /**
     * Function to decide what to do with delta value.
     * As an input takes 3 values - current coordinate, destination coordinate and value to be applied to current coordinate.
     * <p/>
     * There are 2 sectors on a display when we use current coordinate as a [0] point for a new axis.
     * Based where is our destination point located we will then increment, or decrement current coordinate based on movementPerFrame ratio.
     * If the distance between current and destination coordinates are lower than movementPerFrame, set destination coordinate as a current coordinate.
     */
    public int setCoord(int currentCoord, int destinationCoord, int frameDeltaCoord) {
        if (destinationCoord != currentCoord) {
            if (Math.abs(currentCoord - destinationCoord) < Math.abs(frameDeltaCoord)) {
                return (destinationCoord);
            } else {
                return (currentCoord += frameDeltaCoord);
            }
        } else {
            return destinationCoord;
        }
    }

    /**
     * Calculate in which of 4 segments of grid are destination coordinates.
     * Grid has [0.0] point equal to current position.
     * <p/>
     * (-0 ↔ -90)   | (-270 ↔ -360)
     * ----------|-----→ X
     * (-90 ↔ -180) | (-180 ↔ -270)
     * ↓Y
     *
     * @return int from interval  <1,4>
     */
    public int calculateSegment() {

        // Y -> vertical axis. From top to bottom.
        // X -> horizontal axis. From left to right.
        if (getX() - getDx() > 0) {
            if (getY() - getDy() > 0) {
                return 1;
            } else {
                return 2;
            }
        } else {
            if (getY() - getDy() > 0) {
                return 4;
            } else {
                return 3;
            }
        }

    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    /**
     * Calculate middle coord in the hitbox and rotate relevant point to correct position.
     *
     * @param vertex default position of a vertex to be rotated
     * @return rotated vertex
     */
    public Point rotateVertexAroundCurrentPosition(Point vertex) {

        double s = Math.sin(Math.toRadians(this.heading));
        double c = Math.cos(Math.toRadians(this.heading));

        int x1 = vertex.x - this.x;
        int y1 = vertex.y - this.y;

        return new Point(
                (int) (x1 * c - y1 * s + this.x),
                (int) (x1 * s + y1 * c + this.y));
    }

    @Override
    public ArrayList<Point> getObjectCollisionVertices() {
        this.objectVertices.clear();
        int halfWidth = getImage().getWidth() / 2;
        int halfHeight = getImage().getHeight() / 2;
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() - halfWidth, getY() - halfHeight)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() - halfWidth, getY() + halfHeight)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() + halfWidth, getY() + halfHeight)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() + halfWidth, getY() - halfHeight)));
        return this.objectVertices;
    }

    public void drawObject(Canvas canvas, int x, int y){
        canvas.drawBitmap(this.getImage(),x,y,null);
    }

}
