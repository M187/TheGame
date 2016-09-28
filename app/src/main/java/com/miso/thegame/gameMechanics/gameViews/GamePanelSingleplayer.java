package com.miso.thegame.gameMechanics.gameViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.GameData.GamePlayerTypeEnum;
import com.miso.thegame.gameMechanics.MainGameThread;
import com.miso.thegame.gameMechanics.UserInterface.ButtonsTypeData;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.map.generator.MapGenerator;
import com.miso.thegame.gameMechanics.map.levels.LevelHandler;
import com.miso.thegame.gameMechanics.objects.collisionHandlers.CollisionHandlerSingleplayer;

/**
 * Created by Miso on 8.10.2015.
 */
public class GamePanelSingleplayer extends GameView2 implements SurfaceHolder.Callback {

    protected CollisionHandlerSingleplayer collisionHandler;
    protected LevelHandler levelHandler = new LevelHandler();

    public GamePanelSingleplayer(Context context, GameMapEnum mapToCreate, GamePlayerTypeEnum playerType, ButtonsTypeData buttonsTypeData) {
        super(context, playerType, buttonsTypeData);
        this.mapToCreate = MapManager.initializeMap(mapToCreate, getResources());
        this.context = context;
        this.thread = new MainGameThread(getHolder(), this);
        getHolder().addCallback(this);
    }

    public GamePanelSingleplayer(Context context, GamePlayerTypeEnum playerType, ButtonsTypeData buttonsTypeData, int levelNumber) {
        super(context, playerType, buttonsTypeData);
        this.levelHandler = new LevelHandler(levelNumber);
        this.mapToCreate = MapGenerator.generateMap(getResources(), new Point(2000, 2000), this.levelHandler.getLevelNumber());
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

        switch (this.gameState.getGameState()) {
            case playing:
                inputHandler.processFrameInput();
                getPlayer().update();
                anchor.update();
                getPlayer().updateMiddleDrawCoords(anchor);
                getSpellManager().update();
                getEnemiesManager().update();
                getStaticAnimationManager().update();
                collisionHandler.performCollisionCheck();
                if (getEnemiesManager().getEnemyList().isEmpty()){
                    this.gameState.victory();
                    levelHandler.increaseLevel();
                }
                break;
            case victory:
                getSpellManager().update();
                collisionHandler.performCollisionCheck();
                break;
            case defeated:
                getEnemiesManager().update();
                getSpellManager().update();
                collisionHandler.performCollisionCheck();
        }

        //<editor-fold @desc="Old functionality">
//        if (getPlayer().playing && this.levelComplete == false) {
//
//            if (getEnemiesManager().getEnemyList().isEmpty()) {
//                this.levelComplete = true;
//                levelHandler.increaseLevel();
//            } else {
//
//                inputHandler.processFrameInput();
//                {
//                    getPlayer().update();
//                    anchor.update();
//                    getPlayer().updateMiddleDrawCoords(anchor);
//                }
//                getSpellManager().update();
//                getEnemiesManager().update();
//                getStaticAnimationManager().update();
//                collisionHandler.performCollisionCheck();
//            }
//        } else {
//            getEnemiesManager().update();
//            getSpellManager().update();
//            collisionHandler.performCollisionCheck();
//        }
        //<editor-fold>
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            final int savedState = canvas.save();

            switch (this.gameState.getGameState()){
                case playing:
                    bg.draw(canvas, anchor);
                    this.mapManager.draw(canvas);
                    borders.draw(canvas);
                    getSpellManager().draw(canvas);
                    drawManager.drawOnDisplay(getPlayer(), canvas);
                    getEnemiesManager().draw(canvas);
                    getStaticAnimationManager().draw(canvas);
                    toolbar.draw(canvas);
                    break;
                case victory:
                    bg.draw(canvas, anchor);
                    this.mapManager.draw(canvas);
                    borders.draw(canvas);
                    getSpellManager().draw(canvas);
                    getEnemiesManager().draw(canvas);
                    endgameEvents.drawLevelCleared(canvas);
                    break;
                case defeated:
                    bg.draw(canvas, anchor);
                    this.mapManager.draw(canvas);
                    borders.draw(canvas);
                    getSpellManager().draw(canvas);
                    getEnemiesManager().draw(canvas);
                    endgameEvents.draw(canvas, this.gameState.getGameState());
                    break;
            }

            //<editor-fold @desc="Old functionality">
//            if (getPlayer().playing && this.levelComplete == false) {
//                bg.draw(canvas, anchor);
//                this.mapManager.draw(canvas);
//                borders.draw(canvas);
//                getSpellManager().draw(canvas);
//                drawManager.drawOnDisplay(getPlayer(), canvas);
//                getEnemiesManager().draw(canvas);
//                getStaticAnimationManager().draw(canvas);
//                toolbar.draw(canvas);
//            } else if (getPlayer().playing) {
//                bg.draw(canvas, anchor);
//                this.mapManager.draw(canvas);
//                borders.draw(canvas);
//                getSpellManager().draw(canvas);
//                getEnemiesManager().draw(canvas);
//                endgameEvents.drawLevelCleared(canvas);
//            } else {
//                bg.draw(canvas, anchor);
//                this.mapManager.draw(canvas);
//                borders.draw(canvas);
//                getSpellManager().draw(canvas);
//                getEnemiesManager().draw(canvas);
//                endgameEvents.draw(canvas, false);
//            }
//<editor-fold>

            canvas.restoreToCount(savedState);
        }
    }

    public void postDrawTasks() {
        //collisionHandler.performCollisionCheck();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //System.out.println(Float.toString(event.getX()) + "  --  " + Float.toString(event.getY()));

        switch (this.gameState.getGameState()){
            case playing:
                return inputHandler.processEvent(event);
            case victory:
                return this.gameState.eventTimedOut() ? inputHandler.processLevelCompleteEvent(this.levelHandler) : true;
            case defeated:
                return (this.gameState.eventTimedOut()) ? inputHandler.processEndgameEvent() : true;
            default:
                return true;
        }

        //<editor-fold @desc="Old functionality">
//        if (levelComplete) {
//            return inputHandler.processLevelCompleteEvent(event, this.levelHandler);
//        } else if (getPlayer().playing) {
//            return inputHandler.processEvent(event);
//        } else {
//            return inputHandler.processEndgameEvent(event);
//        }
        //<editor-fold>
    }
}