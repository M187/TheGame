package com.miso.thegame.gameViews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.gameMechanics.MainGameThread;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionHandlerSingleplayer;

/**
 * Created by Miso on 8.10.2015.
 */
public class GamePanelSingleplayer extends GameView2 implements SurfaceHolder.Callback {

    protected CollisionHandlerSingleplayer collisionHandler;

    public GamePanelSingleplayer(Context context, GameMapEnum mapToCreate) {
        super(context);
        this.mapToCreate = mapToCreate;
        this.context = context;
        this.thread = new MainGameThread(getHolder(), this);
        getHolder().addCallback(this);
    }

    @Override
    public Resources getResources() {
        return super.getResources();
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
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surface) {
        super.surfaceCreated(surface);
        collisionHandler = new CollisionHandlerSingleplayer(getPlayer(), getEnemiesManager(), getSpellManager(), this.mapManager, getResources());

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //System.out.println(Float.toString(event.getX()) + "  --  " + Float.toString(event.getY()));
        if (getPlayer().playing) {
            return inputHandler.processEvent(event);
        } else {
            return inputHandler.processEndgameEvent(event);
        }
    }

    /**
     * Function to update game state.
     * Called for each frame/lifecycle of a game.
     * Take care, order depends!
     */
    public void update() {
        if (getPlayer().playing) {
            inputHandler.processFrameInput();
            {
                getPlayer().update();
                anchor.update();
                getPlayer().updateMiddleDrawCoords(anchor);
            }
            getSpellManager().update();
            getEnemiesManager().update();
            getStaticAnimationManager().update();
            //collisionHandler.performCollisionCheck();
        } else {
            getEnemiesManager().update();
            getSpellManager().update();
            //collisionHandler.performCollisionCheck();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            final int savedState = canvas.save();
            if (getPlayer().playing) {
                bg.draw(canvas, anchor);
                this.mapManager.draw(canvas);
                borders.draw(canvas);
                getSpellManager().draw(canvas);
                drawManager.drawOnDisplay(getPlayer(), canvas);
                getEnemiesManager().draw(canvas);
                getStaticAnimationManager().draw(canvas);
                toolbar.draw(canvas);
            } else {
                bg.draw(canvas, anchor);
                this.mapManager.draw(canvas);
                borders.draw(canvas);
                getSpellManager().draw(canvas);
                getEnemiesManager().draw(canvas);
                endgameEvents.draw(canvas);
            }
            canvas.restoreToCount(savedState);
        }
    }

    public void postDrawTasks(){
        collisionHandler.performCollisionCheck();
    }
}