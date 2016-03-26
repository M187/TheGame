package com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.DemoObstacles;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;

/**
 * Created by michal.hornak on 18.12.2015.
 *
 * Object to hold data about obstacle - 100x100
 */
public class DemoObstacle1 extends DemoObstacle {

    public DemoObstacle1 (Resources res, Point coordPoint){
        super(coordPoint, BitmapFactory.decodeResource(res, R.drawable.obstacletree1xx100x100));
        initializeRange();
        relativeTilePositions = new Point[]{
                new Point(0,0)
        };
    }
}
