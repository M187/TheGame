package com.miso.thegame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.gameMechanics.Anchor;
import com.miso.thegame.gameMechanics.MainThread;
import com.miso.thegame.gameMechanics.UserInterface.EndgameEvents;
import com.miso.thegame.gameMechanics.UserInterface.InputHandler;
import com.miso.thegame.gameMechanics.UserInterface.Toolbar;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionHandler;
import com.miso.thegame.gameMechanics.display.Background;
import com.miso.thegame.gameMechanics.display.Borders;
import com.miso.thegame.gameMechanics.display.DrawManager;
import com.miso.thegame.gameMechanics.display.StaticAnimations.StaticAnimationManager;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.Player;
import com.miso.thegame.gameMechanics.movingObjects.Player_Saucer;
import com.miso.thegame.gameMechanics.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.movingObjects.spells.SpellManager;

import java.util.Random;

/**
 * Created by Miso on 8.10.2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = Game.metrics.widthPixels; // X Axis
    public static final int HEIGHT = Game.metrics.heightPixels; // Y Axis
    public static final Random randomGenerator = new Random();

    public StaticAnimationManager staticAnimationManager = new StaticAnimationManager();

    private GameMapEnum mapToCreate;
    private MainThread thread;
    private Background bg;
    private Borders borders;
    public Player player = null;
    public EnemiesManager enemiesManager;
    public SpellManager spellManager;
    public MapManager mapManager;
    public Toolbar toolbar;
    public Anchor anchor;
    public static DrawManager drawManager;
    private InputHandler inputHandler;
    private CollisionHandler collisionHandler;
    private EndgameEvents endgameEvents;
    public Context context;


    @Override
    public Resources getResources() {
        return super.getResources();
    }

    public GamePanel(Context context, GameMapEnum mapToCreate) {
        super(context);
        this.mapToCreate = mapToCreate;
        this.context = context;
        thread = new MainThread(getHolder(), this);
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
        spellManager = new SpellManager(getResources(), player);

        enemiesManager = new EnemiesManager(player, spellManager, MapManager.getInstance().enemyInitialDatas, getResources());
        spellManager.enemiesManager = enemiesManager;

        toolbar = new Toolbar(getResources(), player);
        anchor = new Anchor(player, WIDTH / 3, HEIGHT / 3);

        bg = new Background(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(this.mapToCreate.getBackgroundImageName(), "drawable", context.getPackageName())), anchor);

        borders = new Borders(getResources(), anchor);
        drawManager = new DrawManager(anchor);
        inputHandler = new InputHandler(this);
        collisionHandler = new CollisionHandler(player, enemiesManager, spellManager, MapManager.getInstance(), getResources());
        endgameEvents = new EndgameEvents(getResources());

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //System.out.println(Float.toString(event.getX()) + "  --  " + Float.toString(event.getY()));
        if (player.playing) {
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
        if (player.playing) {
            inputHandler.processFrameInput();
            player.update();
            anchor.update();
            spellManager.update();
            enemiesManager.update();
            staticAnimationManager.update();
            //collisionHandler.performCollisionCheck();
        } else {
            enemiesManager.update();
            spellManager.update();
            //collisionHandler.performCollisionCheck();
        }

    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            final int savedState = canvas.save();
            if (player.playing) {
                bg.draw(canvas, anchor);
                MapManager.getInstance().draw(canvas);
                borders.draw(canvas);
                spellManager.draw(canvas);
                drawManager.drawOnDisplay(player, canvas);
                enemiesManager.draw(canvas);
                staticAnimationManager.draw(canvas);
                toolbar.draw(canvas);
            } else {
                bg.draw(canvas, anchor);
                MapManager.getInstance().draw(canvas);
                borders.draw(canvas);
                spellManager.draw(canvas);
                enemiesManager.draw(canvas);
                endgameEvents.draw(canvas);
            }
            canvas.restoreToCount(savedState);
        }
    }

    public void postDrawTasks(){
        collisionHandler.performCollisionCheck();
    }
}
