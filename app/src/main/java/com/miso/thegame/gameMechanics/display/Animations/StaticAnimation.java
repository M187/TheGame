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
    private int currentImageFrame;
    private long currentFramesFrame;
    private long delayInFrames;
    private boolean playedOnce;


    public ArrayList<Point> getObjectCollisionVertices(){
        return null;
    }

    public void setFrames(Bitmap[] frames)
    {
        this.frames = frames;
        currentImageFrame = 0;
        currentFramesFrame = 0;
    }

    public void setDelayInFrames(long d){
        delayInFrames = d;
    }

    public boolean update()
    {
        this.currentFramesFrame++;

        if(this.currentFramesFrame >= delayInFrames)
        {
            currentImageFrame++;
            currentFramesFrame = 0;
        }
        if(currentImageFrame == frames.length){
            currentImageFrame = 0;
            return true;
        }
        return false;
    }

    public Bitmap getImage()
    {
        return frames[currentImageFrame];
    }

    public int getFrame(){return currentImageFrame;}
    public boolean isPlayedOnce(){return playedOnce;}

    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(this.getImage(),(float) x,(float) y,null);
        }catch(Exception e){
            System.out.println("Aaaa");
        }
    }
}
