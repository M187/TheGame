package com.miso.thegame.gameMechanics.gameViews;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.miso.thegame.GameData.GamePlayerTypeEnum;
import com.miso.thegame.MenuActivity;
import com.miso.thegame.Networking.Sender;
import com.miso.thegame.gameMechanics.MainGameThread;
import com.miso.thegame.gameMechanics.UserInterface.ButtonsTypeData;
import com.miso.thegame.gameMechanics.UserInterface.EndgameEvents;
import com.miso.thegame.gameMechanics.UserInterface.InputHandler;
import com.miso.thegame.gameMechanics.UserInterface.Toolbar;
import com.miso.thegame.gameMechanics.display.Animations.StaticAnimationManager;
import com.miso.thegame.gameMechanics.display.Background;
import com.miso.thegame.gameMechanics.display.Borders;
import com.miso.thegame.gameMechanics.display.DrawManager;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.map.mapDefinitions.GameMap;
import com.miso.thegame.gameMechanics.objects.movingObjects.Anchor;
import com.miso.thegame.gameMechanics.objects.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.objects.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.objects.movingObjects.player.PlayerFactory;
import com.miso.thegame.gameMechanics.objects.movingObjects.spells.SpellManager;

import java.util.Random;

/**
 * Created by michal.hornak on 21.04.2016.
 */
public abstract class GameView2 extends SurfaceView implements SurfaceHolder.Callback{

    public static final int WIDTH = MenuActivity.metrics.widthPixels; // X Axis
    public static final int HEIGHT = MenuActivity.metrics.heightPixels; // Y Axis
    public static final Random randomGenerator = new Random();
    public static DrawManager drawManager;

    public static Sender sender = null;
    public static String myNickname = "";
    public static boolean isMultiplayerGame = false;
    public MapManager mapManager;
    public Anchor anchor;
    public Toolbar toolbar;
    public MainGameThread thread;
    protected GameMap mapToCreate;
    protected Background bg;
    protected Borders borders;
    protected InputHandler inputHandler;
    protected EndgameEvents endgameEvents;
    protected Context context;

    protected Player player;
    protected GamePlayerTypeEnum playerType;
    protected Point playerStartingPosition;

    protected EnemiesManager enemiesManager;
    protected SpellManager spellManager;
    protected StaticAnimationManager staticAnimationManager = new StaticAnimationManager();
    protected ButtonsTypeData buttonsTypeData;

    public GameView2(Context context, GamePlayerTypeEnum playerType, ButtonsTypeData buttonsTypeData){
        super(context);
        StaticAnimationManager.resources = this.getResources();
        this.playerType = playerType;
        this.buttonsTypeData = buttonsTypeData;
    }

    public abstract void update();

    public abstract void draw(Canvas canvas);

    public abstract void postDrawTasks();

    @Override
    public void surfaceCreated(SurfaceHolder surface){
        //mapManager also initialize Pathfinder class
        this.mapManager = new MapManager(this.mapToCreate);

        this.playerStartingPosition = (this.playerStartingPosition == null) ? new Point(MapManager.getWorldWidth() / 2, MapManager.getWorldHeight() / 2) : this.playerStartingPosition;
        player = PlayerFactory.createPlayer(getResources(), this.playerStartingPosition, this.mapManager, this.playerType);

        spellManager = new SpellManager(getResources(), getPlayer());
        enemiesManager = new EnemiesManager(getPlayer(), getSpellManager(), this.mapManager.enemyInitialDatas, getResources());
        getSpellManager().enemiesManager = getEnemiesManager();

        toolbar = new Toolbar(getResources(), getPlayer(), this.buttonsTypeData);

        anchor = new Anchor(getPlayer(), WIDTH / 3, HEIGHT / 3);
        bg = new Background(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(this.mapToCreate.getBackgroundImageName(), "drawable", getContext().getPackageName())), anchor);
        borders = new Borders(getResources(), anchor);

        drawManager = new DrawManager(anchor);
        inputHandler = new InputHandler(this);
        endgameEvents = new EndgameEvents(getResources());
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
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public StaticAnimationManager getStaticAnimationManager() {
        return staticAnimationManager;
    }

    public Player getPlayer() {
        return player;
    }

    public EnemiesManager getEnemiesManager() {
        return enemiesManager;
    }

    public SpellManager getSpellManager() {
        return spellManager;
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
}
