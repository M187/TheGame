package com.miso.thegame.gameMechanics.display.Animations;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;

/**
 * Created by michal.hornak on 18.04.2016.
 */
public class CollectibleRotatingStar extends StaticAnimation {

    public CollectibleRotatingStar(Point position , Resources res){
        Bitmap spritesheet = BitmapFactory.decodeResource(res, R.drawable.gameobjective_spritee);
        int height = 35;
        int width = 40;
        int numFrames = 7;

        this.x = position.x;
        this.y = position.y;

        Bitmap[] image = new Bitmap[numFrames];

        for(int i = 0; i< numFrames; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        this.setFrames(image);
        this.setDelay(100 * 1000000);
    }
}
