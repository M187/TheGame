package com.miso.thegame.gameMechanics.display.Animations;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;

/**
 * Created by michal.hornak on 19.04.2016.
 */
public class FlyingSaucer extends StaticAnimation{

    public FlyingSaucer(Point position , Resources res){
        Bitmap spritesheet = BitmapFactory.decodeResource(res, R.drawable.player_saucer);
        int height = 70;
        int width = 70;
        int numFrames = 8;

        this.x = position.x;
        this.y = position.y;

        Bitmap[] image = new Bitmap[numFrames];

        for(int i = 0; i< numFrames; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        this.setFrames(image);
        this.setDelayInFrames(25 * 1000000);
    }
}
