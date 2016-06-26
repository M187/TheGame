package com.miso.thegame.debugStuff.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.miso.thegame.gameMechanics.display.backgroundEffects.BackgroundEffect;
import com.miso.thegame.gameMechanics.display.backgroundEffects.CircleLightning;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.hornak on 26.06.2016.
 */
public class DebugSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private List<BackgroundEffect> debugThingsManager = new ArrayList<>();
    private DebugDrawThread myThread;

    public DebugSurfaceView(Context context) {
        super(context);

        this.debugThingsManager.add(new CircleLightning(new Point(500, 300), 40, 90, 75, 25));

        this.myThread = new DebugDrawThread(getHolder(), this);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry & counter < 1000) {
            counter++;
            try {
                myThread.setRunning(false);
                myThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surface) {
        myThread.setRunning(true);
        myThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //todo: switch back to layout.
        return true;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if (this.debugThingsManager.get(0).update()){
            this.debugThingsManager.remove(0);
            this.debugThingsManager.add(new CircleLightning(new Point(500, 300), 40, 90, 75, 25));
        }

        this.debugThingsManager.get(0).draw(canvas);
    }
}











