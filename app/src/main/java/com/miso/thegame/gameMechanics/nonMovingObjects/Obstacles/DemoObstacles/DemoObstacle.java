package com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.DemoObstacles;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.Obstacle;

/**
 * Created by michal.hornak on 18.12.2015.
 */
public abstract class DemoObstacle extends Obstacle {

    public DemoObstacle(Point coordPoint, Bitmap image){
        super(coordPoint, image);
    }

    public void initializeRange(){
        int xSize = this.getImage().getWidth() / 2;
        int ySize = this.getImage().getHeight() / 2;
        this.range = (int)(Math.sqrt( xSize * xSize + ySize * ySize ));
    }
}
