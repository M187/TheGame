package com.miso.thegame.gameMechanics.display;

import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.Anchor;
import com.miso.thegame.gameViews.GameView2;

/**
 * Created by Miso on 17.11.2015.
 */
public class Borders{

    protected static final Paint paint = new Paint();
    private Resources resources;
    private  Anchor anchor;

    public Borders(Resources resources, Anchor anchor){
        this.resources = resources;
        this.anchor = anchor;
        paint.setColor(Color.RED);
        paint.setStrokeWidth(20);
        paint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
    }

    public void draw(Canvas canvas){
        if (anchor.getX() < 0 ){
            canvas.drawLine((float)Math.abs(anchor.getX()),0,(float)Math.abs(anchor.getX()), GameView2.HEIGHT, paint);
        }
        if (anchor.getX() > MapManager.getWorldWidth() - GameView2.WIDTH){
            canvas.drawLine((float) MapManager.getWorldWidth() - anchor.getX(), 0, (float) MapManager.getWorldWidth() - anchor.getX(), GameView2.HEIGHT, paint);
        }
        if (anchor.getY() < 0){
            canvas.drawLine(0,(float)Math.abs(anchor.getY()), GameView2.WIDTH,(float)Math.abs(anchor.getY()), paint);
        }
        if (anchor.getY() > MapManager.getWorldHeight() - GameView2.HEIGHT){
            canvas.drawLine(0,(float) MapManager.getWorldHeight() - anchor.getY(), GameView2.WIDTH, (float) MapManager.getWorldHeight() - anchor.getY(), paint);
        }
    }
}
