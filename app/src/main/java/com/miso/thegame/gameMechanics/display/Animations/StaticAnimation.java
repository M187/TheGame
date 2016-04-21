package com.miso.thegame.gameMechanics.display.Animations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.GameObject;

import java.util.ArrayList;

/**
 * Created by Miso on 29.4.2015.
 */
public class StaticAnimation extends GameObject {

    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;


    public ArrayList<Point> getObjectCollisionVertices(){
        return null;
    }

    public void setFrames(Bitmap[] frames)
    {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void setDelay(long d){delay = d;}

    public boolean update()
    {
        long elapsed = (System.nanoTime() - startTime);

        if(elapsed>delay)
        {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length){
            currentFrame = 0;
            return true;
        }
        return false;
    }

    public Bitmap getImage()
    {
        return frames[currentFrame];
    }

    public int getFrame(){return currentFrame;}
    public boolean isPlayedOnce(){return playedOnce;}

    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(this.getImage(),(float) x,(float) y,null);
        }catch(Exception e){
            System.out.println("Aaaa");
        }
    }
}
