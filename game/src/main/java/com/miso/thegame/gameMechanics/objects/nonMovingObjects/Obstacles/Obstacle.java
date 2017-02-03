package com.miso.thegame.gameMechanics.objects.nonMovingObjects.Obstacles;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.objects.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.objects.nonMovingObjects.StaticObject;

/**
 * Created by Miso on 16.11.2015.
 */
public abstract class Obstacle extends StaticObject {

    protected Point[] relativeTilePositions = {new Point(0,0)};

    public Obstacle(){}

    public Obstacle(Point coordPoint, Bitmap image){
        super(coordPoint, image);
        this.collisionObjectType = CollisionObjectType.Obstacle;
    }

    public Point getGridRange() {
        return new Point (
                getImage().getWidth() / MapManager.getMapTileWidth(),
                getImage().getHeight() / MapManager.getMapTileHeight());
    }

    public Point[] getRelativeTilePositions() {
        return relativeTilePositions;
    }
}
