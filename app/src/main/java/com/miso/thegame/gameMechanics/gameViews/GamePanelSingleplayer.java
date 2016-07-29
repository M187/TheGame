package com.miso.thegame.gameMechanics.gameViews;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.GameData.GamePlayerTypeEnum;
import com.miso.thegame.gameMechanics.MainGameThread;
import com.miso.thegame.gameMechanics.UserInterface.ButtonsTypeData;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionHandlerSingleplayer;

/**
 * Created by Miso on 8.10.2015.
 */
public class GamePanelSingleplayer extends GameView2 implements SurfaceHolder.Callback {

    protected CollisionHandlerSingleplayer collisionHandler;

    public GamePanelSingleplayer(Context context, GameMapEnum mapToCreate, GamePlayerTypeEnum playerType, ButtonsTypeData buttonsTypeData) {
        super(context, playerType, buttonsTypeData);
        this.mapToCreate = mapToCreate;
        this.context = context;
        this.thread = new MainGameThread(getHolder(), this);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surface) {
        super.surfaceCreated(surface);
        collisionHandler = new CollisionHandlerSingleplayer(getPlayer(), getEnemiesManager(), getSpellManager(), this.mapManager, getResources());

        thread.setRunning(true);
        thread.start();
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
                endgameEvents.draw(canvas, false);
            }
            canvas.restoreToCount(savedState);
        }
    }

    public void postDrawTasks(){
        collisionHandler.performCollisionCheck();
    }
}