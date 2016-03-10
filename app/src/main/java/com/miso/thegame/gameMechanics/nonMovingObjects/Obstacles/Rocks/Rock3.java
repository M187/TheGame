package com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.Rocks;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;

/**
 * Created by Miso on 20.11.2015.
 */
public class Rock3 extends Rock{

    public Rock3 (Resources res){
        super();
        this.setImage(BitmapFactory.decodeResource(res, R.drawable.rock3a));
        initializeRange();
    }

    public Rock3 (Resources res, Point coordPoint){
        super(coordPoint);
        this.setImage(BitmapFactory.decodeResource(res, R.drawable.rock3a));
        initializeRange();
    }
}
