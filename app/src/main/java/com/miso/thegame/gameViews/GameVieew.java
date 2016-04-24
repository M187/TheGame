package com.miso.thegame.gameViews;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceView;

import com.miso.thegame.GameActivity;
import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.gameMechanics.MainThread;
import com.miso.thegame.gameMechanics.UserInterface.EndgameEvents;
import com.miso.thegame.gameMechanics.UserInterface.InputHandler;
import com.miso.thegame.gameMechanics.UserInterface.Toolbar;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionHandler;
import com.miso.thegame.gameMechanics.display.Animations.StaticAnimationManager;
import com.miso.thegame.gameMechanics.display.Background;
import com.miso.thegame.gameMechanics.display.Borders;
import com.miso.thegame.gameMechanics.display.DrawManager;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.Anchor;
import com.miso.thegame.gameMechanics.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.movingObjects.spells.SpellManager;

import java.util.Random;

/**
 * Created by michal.hornak on 21.04.2016.
 */
public abstract class GameVieew extends SurfaceView {

    public static final int WIDTH = GameActivity.metrics.widthPixels; // X Axis
    public static final int HEIGHT = GameActivity.metrics.heightPixels; // Y Axis
    public static final Random randomGenerator = new Random();
    public static DrawManager drawManager;

    public StaticAnimationManager staticAnimationManager = new StaticAnimationManager();
    protected GameMapEnum mapToCreate;
    protected MainThread thread;
    protected Background bg;
    protected Borders borders;
    public MapManager mapManager;
    public Anchor anchor;
    protected InputHandler inputHandler;
    protected CollisionHandler collisionHandler;
    protected EndgameEvents endgameEvents;

    public Context context;
    public Player player;
    public EnemiesManager enemiesManager;
    public SpellManager spellManager;
    public Toolbar toolbar;

    public GameVieew(Context context){
        super(context);
    }

    public abstract void update();

    public abstract void draw(Canvas canvas);

    public abstract void postDrawTasks();
}
