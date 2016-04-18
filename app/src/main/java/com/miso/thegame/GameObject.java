package com.miso.thegame;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.map.MapManager;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Miso on 29.4.2015.
 */
public abstract class GameObject {

    private Bitmap image;
    public int x;
    public int y;
    protected ArrayList<Point> objectVertices = new ArrayList<>();
    protected Random randomGenerator = GamePanel.randomGenerator;
    protected int displayXCoord;
    protected int displayYCoord;
    protected Point gridCoords;

    protected CollisionObjectType collisionObjectType = CollisionObjectType.NonCollidable;

    public abstract ArrayList<Point> getObjectCollisionVertices();

    protected void setCollisionObjectType(CollisionObjectType collisionObjectType){
        this.collisionObjectType = collisionObjectType;
    }

    public CollisionObjectType getCollisionObjectType(){
        return this.collisionObjectType;
    }

    /**
     * Get current grid coordinates
     * @return Point with coordinates
     *
     * * Need to debug if proper values are used.
     */
    public Point getGridCoordinates(){
        try {
            return (new Point(x / MapManager.getMapTileWidth(), y / MapManager.getMapTileHeight()));
        } catch (Exception e){
            System.out.print("Error while getting grid coordinates.");
            throw e;
        }
    }

     //<editor-fold desc="Setting/Getting global coordinates."
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    //</editor-fold>

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}