package com.miso.thegame.gameMechanics.display.backgroundEffects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

/**
 * Created by Miso on 25.6.2016.
 */
public class CircleLightning extends BackgroundEffect{

    private final int DEFAULT_ALPHA = 30;

    public CircleLightning(Point position){
        this.position = position;

        this.myPaint.setColor(Color.WHITE);
        this.myPaint.setAlpha(DEFAULT_ALPHA);

        //TODO: add timeout based on frames
        //TODO: increase and decrease radius based on frame
    }

    public boolean removeMe(){
        return this.myTimeout.isActive();
    }

    public void draw(Canvas canvas){
        int radius = 80;
        while (radius > 40) {
            canvas.drawCircle(this.position.x, this.position.y, radius, this.myPaint);
            radius -= 10;
            this.myPaint.setAlpha(this.myPaint.getAlpha() + 10);
        }
        this.myPaint.setAlpha(DEFAULT_ALPHA);
    }
}
