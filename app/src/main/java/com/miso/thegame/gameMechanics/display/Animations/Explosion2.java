package com.miso.thegame.gameMechanics.display.Animations;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.display.backgroundEffects.CircleLightning;

/**
 * Created by Miso on 6.6.2016.
 */
public class Explosion2 extends StaticAnimation {

    private CircleLightning circleLightning;
    private boolean circleLightningActive = true;

    public Explosion2(Point position, Resources res){
        Bitmap spritesheet = BitmapFactory.decodeResource(res, R.drawable.explosion2);
        int height = 96;
        int width = 96;
        int numFrames = 12;

        this.x = position.x-height/2;
        this.y = position.y-width/2;

        Bitmap[] image = new Bitmap[numFrames];

        for(int i = 0; i< numFrames; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        this.setFrames(image);
        this.setDelayInFrames(3);

        this.circleLightning = new CircleLightning(position, 20, 40, 150, 24);
    }

    @Override
    public boolean update(){
        if (this.circleLightning.update()){
            this.circleLightningActive = false;
        }
        return super.update();
    }

    @Override
    public void draw(Canvas canvas){
        if (circleLightningActive) {
            this.circleLightning.draw(canvas);
        }
        super.draw(canvas);
    }
}
