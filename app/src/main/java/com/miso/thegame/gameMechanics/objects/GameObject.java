package com.miso.thegame.gameMechanics.objects;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.objects.collisionHandlers.CollisionObjectType;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Miso on 29.4.2015.
 */
public abstract class GameObject {

    public int x;
    public int y;
    protected ArrayList<Point> objectVertices = new ArrayList<>();
    protected Random randomGenerator = GameView2.randomGenerator;
    protected int displayXCoord;
    protected int displayYCoord;
    protected Point gridCoords;
    protected CollisionObjectType collisionObjectType = CollisionObjectType.NonCollidable;
    private Bitmap image;

    public abstract ArrayList<Point> getObjectCollisionVertices();

    public CollisionObjectType getCollisionObjectType(){
        return this.collisionObjectType;
    }

    protected void setCollisionObjectType(CollisionObjectType collisionObjectType){
        this.collisionObjectType = collisionObjectType;
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

    public int getX() {
        return x;
    }

     //<editor-fold desc="Setting/Getting global coordinates."
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(Point position){
        this.x = position.x;
        this.y = position.y;
    }

    public Point getPosition(){
        return new Point(this.x,this.y);
    }
    //</editor-fold>

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getHalfWidth(){
        try {
            return this.getImage().getWidth() / 2;
        } catch (Exception e){
            return 0;
        }
    }

    public int getHalfHeight(){
        try {
            return this.getImage().getHeight() / 2;
        } catch (Exception e){
            return 0;
        }
    }
}