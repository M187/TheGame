package com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.Fllowers;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.miso.thegame.R;

/**
 * Created by Miso on 17.11.2015.
 */
public class Fllower1 extends Fllower {

    public Fllower1(Resources res){
        super();
        this.setImage(BitmapFactory.decodeResource(res, R.drawable.fllower1));
    }
}
