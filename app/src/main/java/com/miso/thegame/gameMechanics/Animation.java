package com.miso.thegame.gameMechanics;

import android.graphics.Bitmap;

/**
 * Created by Miso on 29.4.2015.
 */
public class Animation {

    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;

    public void initializeSprites(Bitmap spriteSheet, int height, int width, int numFrames, int delayBetweenSprites){
        Bitmap[] image = new Bitmap[numFrames];
        for(int i = 0; i< numFrames; i++)
        {
            image[i] = Bitmap.createBitmap(spriteSheet, i * width, 0, width, height);
        }
        this.setFrames(image);
        this.setDelay(delayBetweenSprites * 1000000);
    }

    public void setFrames(Bitmap[] frames)
    {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void setDelay(long d){delay = d;}

    public void update()
    {
        long elapsed = (System.nanoTime() - startTime);

        if(elapsed>delay)
        {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length){
            currentFrame = 0;
        }
    }

    public Bitmap getImage()
    {
        return frames[currentFrame];
    }
}
