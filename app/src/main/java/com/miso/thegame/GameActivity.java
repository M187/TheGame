package com.miso.thegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.GameData.OptionStrings;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameViews.GamePanelMultiplayer2;
import com.miso.thegame.gameViews.GamePanelSingleplayer2;


public class GameActivity extends Activity {

    public static DisplayMetrics metrics = new DisplayMetrics();
    public boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        GameMapEnum mapToCreate = getMapToCreate();
        loadPlayerData();

        super.onCreate(savedInstanceState);
        //set fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        createGameView(mapToCreate);

        if (gameOver){this.finish();}
    }

    @Override
    protected void onDestroy(){
        MenuActivity.isGameOn = false;
        super.onDestroy();
    }

    /**
     * Loads player data and saves them into constantHolder class.
     * Later on to be used by game to affect gameObjects.
     */
    private void loadPlayerData(){
        SharedPreferences settings = getSharedPreferences("PlayerOptions", 0);

        int maxHealth = settings.getInt(OptionStrings.playerMaxHealth, 0);
        int maxAmmo = settings.getInt(OptionStrings.playerMaxAmmo, 0);
        int maxSpeed = settings.getInt(OptionStrings.playerMaxSpeed, 0);
        ConstantHolder.loadSettingData(maxHealth, maxAmmo, maxSpeed);
    }

    /**
     * Creates game view.
     *
     * @param mapToCreate for game instance.
     */
    private void createGameView(GameMapEnum mapToCreate){
        if(getIntent().getExtras().getBoolean(OptionStrings.multiplayerInstance)){
            setContentView(new GamePanelMultiplayer2(this, mapToCreate));
        } else {
            setContentView(new GamePanelSingleplayer2(this, mapToCreate));
        }
    }

    /**
     * Basic switch to decide what map should be created.
     *
     * @return map for GameView to be created.
     */
    private GameMapEnum getMapToCreate(){
        switch (getIntent().getExtras().getString("Level")) {
            case "Ground":
                return GameMapEnum.Level2;
            case "Space":
                return GameMapEnum.SpaceLevel1;
            default:
                return GameMapEnum.BlankMap;
        }
    }
}
