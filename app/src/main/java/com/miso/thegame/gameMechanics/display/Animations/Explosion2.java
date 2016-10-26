package com.miso.thegame.gameMechanics.display.Animations;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.MenuActivity;
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

        int numFrames = 12;
        int height =(spritesheet.getWidth() / numFrames);
        int width = (spritesheet.getWidth() / numFrames);

        this.x = position.x-height/2;
        this.y = position.y-width/2;

        Bitmap[] image = new Bitmap[numFrames];

        for(int i = 0; i< numFrames; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        this.setFrames(image);
        this.setDelayInFrames(3);

        this.circleLightning = new CircleLightning(new Point(this.x, this.y), 10, 35, 200, 28);
    }

    @Override
    public boolean update(){
        if (this.circleLightning.update()){
            this.circleLightningActive = false;
        }
        return super.update();
    }

    @Override
    public void draw(Canvas canvas, Point position){
        if (circleLightningActive) {
            this.circleLightning.draw(canvas, new Point(
                    position.x + this.getImage().getWidth() / 2,
                    position.y + this.getImage().getHeight() / 2));
        }
        super.draw(canvas, position);
    }
}
