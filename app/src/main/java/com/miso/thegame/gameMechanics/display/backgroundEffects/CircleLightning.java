package com.miso.thegame.gameMechanics.display.backgroundEffects;

import android.graphics.BlurMaskFilter;
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

        this.myTimeout = new FrameDependantTimeout(duration);

        this.position = position;
        this.currentRadius = radiusBegin;
        this.radiusBegin = radiusBegin;
        this.radiusMax = radiusMax;
        this.alpha = alpha;
        this.duration = duration;

        this.myPaint.setColor(Color.WHITE);
        this.myPaint.setAlpha(this.alpha);
        this.myPaint.setMaskFilter(new BlurMaskFilter(radiusMax, BlurMaskFilter.Blur.NORMAL));

        this.frameRadiusDelta = (this.radiusMax - this.radiusBegin) / (this.duration / 4);
    }

    public boolean update(){
        return this.myTimeout.update();
    }

    public void draw(Canvas canvas, Point position){

        //move this to update??
        if (this.myTimeout.getCurrentFrameCount() < this.duration / 4) {
            this.currentRadius += this.frameRadiusDelta;
        } else if (this.myTimeout.getCurrentFrameCount() < (this.duration / 8) *3){
        } else {
            this.currentRadius -= this.frameRadiusDelta / 2;
            if (this.currentRadius < 1 ) {this.currentRadius = 1;}
        }
        int halfRadius = this.currentRadius / 2;

        canvas.drawCircle(position.x, position.y, this.currentRadius, this.myPaint);
    }
}
