package com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.Rocks;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;

/**
 * Created by Miso on 17.11.2015.
 *
 * Class for Rock1 object.
 *
 * Size of an rock object is 200x200
 */
public class Rock1 extends Rock {

    public Rock1 (Resources res){
        super();
        this.setImage(BitmapFactory.decodeResource(res, R.drawable.rock1a));
        relativeTilePositions = new Point[]{
                new Point(0,0),
                new Point(0,1),
                new Point(1,0),
        };
        initializeRange();
    }

    public Rock1 (Resources res, Point coordPoint){
        super(coordPoint);
        this.setImage(BitmapFactory.decodeResource(res, R.drawable.rock1a));
        initializeRange();
    }
}
