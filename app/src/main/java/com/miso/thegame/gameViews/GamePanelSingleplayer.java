package com.miso.thegame.gameViews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.gameMechanics.MainGameThread;
import com.miso.thegame.gameMechanics.UserInterface.EndgameEvents;
import com.miso.thegame.gameMechanics.UserInterface.InputHandler;
import com.miso.thegame.gameMechanics.UserInterface.Toolbar;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionHandlerSingleplayer;
import com.miso.thegame.gameMechanics.display.Background;
import com.miso.thegame.gameMechanics.display.Borders;
import com.miso.thegame.gameMechanics.display.DrawManager;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.Anchor;
import com.miso.thegame.gameMechanics.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.movingObjects.player.Player_Saucer;
import com.miso.thegame.gameMechanics.movingObjects.spells.SpellManager;

/**
 * Created by Miso on 8.10.2015.
 */
public class GamePanelSingleplayer extends GameView2 implements SurfaceHolder.Callback {

    @Override
    public Resources getResources() {
        return super.getResources();
    }

    public GamePanelSingleplayer(Context context, GameMapEnum mapToCreate) {
        super(context);
        this.mapToCreate = mapToCreate;
        this.context = context;
        this.thread = new MainGameThread(getHolder(), this);
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
        //mapManager also initialize Pathfinder class
        MapManager.getInstance().initializeMapManager(this.mapToCreate, getResources());

        player = new Player_Saucer(getResources(), new Point(MapManager.getWorldWidth() / 2, MapManager.getWorldHeight() / 2), MapManager.getInstance());
        spellManager = new SpellManager(getResources(), getPlayer());

        enemiesManager = new EnemiesManager(getPlayer(), getSpellManager(), MapManager.getInstance().enemyInitialDatas, getResources());
        getSpellManager().enemiesManager = getEnemiesManager();

        toolbar = new Toolbar(getResources(), getPlayer());
        anchor = new Anchor(getPlayer(), WIDTH / 3, HEIGHT / 3);

        bg = new Background(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(this.mapToCreate.getBackgroundImageName(), "drawable", getContext().getPackageName())), anchor);

        borders = new Borders(getResources(), anchor);
        drawManager = new DrawManager(anchor);
        inputHandler = new InputHandler(this);
        collisionHandler = new CollisionHandlerSingleplayer(getPlayer(), getEnemiesManager(), getSpellManager(), MapManager.getInstance(), getResources());
        endgameEvents = new EndgameEvents(getResources());

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
                MapManager.getInstance().draw(canvas);
                borders.draw(canvas);
                getSpellManager().draw(canvas);
                drawManager.drawOnDisplay(getPlayer(), canvas);
                getEnemiesManager().draw(canvas);
                getStaticAnimationManager().draw(canvas);
                toolbar.draw(canvas);
            } else {
                bg.draw(canvas, anchor);
                MapManager.getInstance().draw(canvas);
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