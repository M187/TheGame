package com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.DemoObstacles;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;

/**
 * Created by michal.hornak on 18.12.2015.
 *
 * Object to hold data about obstacle - 400x100
 */
public class DemoObstacle3 extends DemoObstacle{

    public DemoObstacle3 (Resources res, Point coordPoint){
        super(coordPoint);
        this.setImage(BitmapFactory.decodeResource(res, R.drawable.object400x100));
        initializeRange();
        relativeTilePositions = new Point[]{
                new Point(0,0),
                new Point(1,0),
                new Point(2,0),
                new Point(3,0),
        };
    }
}
