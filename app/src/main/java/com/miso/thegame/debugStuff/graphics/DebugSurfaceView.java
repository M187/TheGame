package com.miso.thegame.debugStuff.graphics;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.display.Animations.Explosion2;
import com.miso.thegame.gameMechanics.display.Animations.StaticAnimation;
import com.miso.thegame.gameMechanics.display.Background;
import com.miso.thegame.gameMechanics.display.backgroundEffects.BackgroundEffect;
import com.miso.thegame.gameMechanics.display.backgroundEffects.CircleLightning;
import com.miso.thegame.gameMechanics.movingObjects.enemies.groundEnemies.Enemy_basic;
import com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.SquareObstacle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.hornak on 26.06.2016.
 */
public class DebugSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private List<BackgroundEffect> debugThingsManager = new ArrayList<>();
    private DebugDrawThread myThread;
    private Background background;
    private StaticAnimation animation;

    private Point position = new Point(500,300);

    public DebugSurfaceView(Context context) {
        super(context);

        addCircle();
        addAnimation();

        this.background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundgrass), context);
        this.myThread = new DebugDrawThread(getHolder(), this);
        getHolder().addCallback(this);
    }

    private void addCircle(){
        this.debugThingsManager.add(new CircleLightning(this.position, 40, 90, 150, 24));
    }

    private void addAnimation(){
        this.animation = new Explosion2(position, getResources());
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
        this.background.drawCustom(canvas, 0,0);

        //this will draw circle
        if (this.debugThingsManager.get(0).update()){
            this.debugThingsManager.remove(0);
            addCircle();
        }
        //this.debugThingsManager.get(0).draw(canvas);

        if (this.animation.update()){
            addAnimation();
        }
        this.animation.draw(canvas, position);

        new SquareObstacle(getResources(), new Point(400,400)).draw(canvas, 400, 200);

        new Enemy_basic(getResources(), new Point(600,600)).drawObject(canvas, 600, 600);
    }
}











