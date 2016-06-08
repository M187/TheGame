package com.miso.thegame.gameMechanics.display.Animations;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.miso.thegame.R;

/**
 * Created by Miso on 6.6.2016.
 */
public class Explosion2 extends StaticAnimation {

    public Explosion2(int x, int y, Resources res){
        Bitmap spritesheet = BitmapFactory.decodeResource(res, R.drawable.explosion2);
        int height = 96;
        int width = 96;
        int numFrames = 12;

        this.x = x-height/2;
        this.y = y-width/2;

        Bitmap[] image = new Bitmap[numFrames];

        for(int i = 0; i< numFrames; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        this.setFrames(image);
        this.setDelay(600);
    }
}
