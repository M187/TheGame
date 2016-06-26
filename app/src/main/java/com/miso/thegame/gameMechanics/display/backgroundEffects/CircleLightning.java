package com.miso.thegame.gameMechanics.display.backgroundEffects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

/**
 * Created by Miso on 25.6.2016.
 */
public class CircleLightning extends BackgroundEffect{

    private int currentRadius = 0;
    private int radiusBegin;
    private int radiusMax;
    private int alpha;
    private int duration;
    private int frameRadiusDelta;

    /**
     * Creates circle lightning
     * @param position ..
     * @param radiusBegin ..
     * @param radiusMax ..
     * @param alpha ..
     * @param duration - frame count to timeout.
     */
    public CircleLightning(Point position, int radiusBegin, int radiusMax, int alpha, int duration ){

        this.myTimeout = new BackgroundEffectTimeout(duration);

        this.position = position;
        this.currentRadius = radiusBegin;
        this.radiusBegin = radiusBegin;
        this.radiusMax = radiusMax;
        this.alpha = alpha;

        this.myPaint.setColor(Color.WHITE);
        this.myPaint.setAlpha(this.alpha);

        this.frameRadiusDelta = (this.radiusMax - this.radiusBegin) / (this.duration / 2);
    }

    public boolean update(){
        return this.myTimeout.update();
    }

    public void draw(Canvas canvas){

        if (this.myTimeout.getCurrentFrameCount() < this.myTimeout.getCurrentFrameCount() / 2){
            this.currentRadius += this.frameRadiusDelta;
        } else {
            this.currentRadius -= this.frameRadiusDelta;
        }
        canvas.drawCircle(this.position.x, this.position.y, this.currentRadius, this.myPaint);
    }
}
