package com.miso.thegame.gameMechanics.display.Animations;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.miso.thegame.R;

/**
 * Created by Miso on 9.2.2016.
 */
public class Explosion extends StaticAnimation {

    public Explosion(int x, int y, Resources res){
        Bitmap spritesheet = BitmapFactory.decodeResource(res, R.drawable.explosion);
        int height = 64;
        int width = 64;
        int numFrames = 16;

        this.x = x-height/2;
        this.y = y-width/2;

        Bitmap[] image = new Bitmap[numFrames];

        for(int i = 0; i< numFrames; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        this.setFrames(image);
        this.setDelayInFrames(2);
    }
}
