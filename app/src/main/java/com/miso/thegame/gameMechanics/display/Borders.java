package com.miso.thegame.gameMechanics.display;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.miso.thegame.GamePanel;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.Anchor;

/**
 * Created by Miso on 17.11.2015.
 */
public class Borders{

    private Resources resources;
    private  Anchor anchor;
    protected static final Paint paint = new Paint();

    public Borders(Resources resources, Anchor anchor){
        this.resources = resources;
        this.anchor = anchor;
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(5);
        paint.setTextSize(10);
    }

    public void draw(Canvas canvas){
        if (anchor.getX() < 0 ){
            canvas.drawLine((float)Math.abs(anchor.getX()),0,(float)Math.abs(anchor.getX()),GamePanel.HEIGHT, paint);
        }
        if (anchor.getX() > MapManager.getWorldWidth() - GamePanel.WIDTH){
            canvas.drawLine((float) MapManager.getWorldWidth() - anchor.getX(), 0, (float) MapManager.getWorldWidth() - anchor.getX(), GamePanel.HEIGHT, paint);
        }
        if (anchor.getY() < 0){
            canvas.drawLine(0,(float)Math.abs(anchor.getY()), GamePanel.WIDTH,(float)Math.abs(anchor.getY()), paint);
        }
        if (anchor.getY() > MapManager.getWorldHeight() - GamePanel.HEIGHT){
            canvas.drawLine(0,(float) MapManager.getWorldHeight() - anchor.getY(), GamePanel.WIDTH, (float) MapManager.getWorldHeight() - anchor.getY(), paint);
        }
    }
}
