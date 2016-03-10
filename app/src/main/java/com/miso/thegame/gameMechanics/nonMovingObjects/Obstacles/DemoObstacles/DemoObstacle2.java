package com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.DemoObstacles;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;

/**
 * Created by michal.hornak on 18.12.2015.
 *
 * Object to hold data about obstacle - 100x400
 */
public class DemoObstacle2 extends DemoObstacle{

    public DemoObstacle2 (Resources res, Point coordPoint){
        super(coordPoint);
        this.setImage(BitmapFactory.decodeResource(res, R.drawable.object100x400));
        initializeRange();
        relativeTilePositions = new Point[]{
                new Point(0,0),
                new Point(0,1),
                new Point(0,2),
                new Point(0,3),
        };
    }
}
