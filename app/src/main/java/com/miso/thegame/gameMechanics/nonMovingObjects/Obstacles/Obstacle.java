package com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles;

import android.graphics.Point;

import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.nonMovingObjects.StaticObject;

/**
 * Created by Miso on 16.11.2015.
 */
public abstract class Obstacle extends StaticObject {

    public Obstacle(Point coordPoint){
        super(coordPoint);
        this.collisionObjectType = CollisionObjectType.Obstacle;
    }

    protected Point[] relativeTilePositions = {new Point(0,0)};

    public Point getGridRange() {
        return new Point (
                getImage().getWidth() / MapManager.getMapTileWidth(),
                getImage().getHeight() / MapManager.getMapTileHeight());
    }

    public Point[] getRelativeTilePositions() {
        return relativeTilePositions;
    }
}
