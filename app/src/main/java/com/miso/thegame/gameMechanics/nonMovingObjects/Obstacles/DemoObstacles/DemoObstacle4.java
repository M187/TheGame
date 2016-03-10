package com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.DemoObstacles;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;

/**
 * Created by michal.hornak on 18.12.2015.
 *
 * Object to hold data about obstacle - 200x200
 */
public class DemoObstacle4 extends DemoObstacle {

    public DemoObstacle4 (Resources res, Point coordPoint){
        super(coordPoint);
        this.setImage(BitmapFactory.decodeResource(res, R.drawable.rock1a));
        initializeRange();
        relativeTilePositions = new Point[]{
                new Point(0,0),
                new Point(0,1),
                new Point(1,0),
                new Point(1,1),
        };
    }
}
