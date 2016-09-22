package com.miso.thegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.miso.thegame.GameData.ButtonTypeEnum;
import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.GameData.GamePlayerTypeEnum;
import com.miso.thegame.GameData.OptionStrings;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.UserInterface.ButtonsTypeData;
import com.miso.thegame.gameMechanics.gameViews.GamePanelSingleplayer;

/**
 * Created by michal.hornak on 21.09.2016.
 */
public class GameActivityvSingleplayer extends Activity {

    public static DisplayMetrics metrics = new DisplayMetrics();
    public boolean gameOver = false;
    public GamePlayerTypeEnum playerType;
    private ButtonsTypeData buttonsTypeData = new ButtonsTypeData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(ConstantHolder.TAG, " --> Entered main game Activity.");
        GameMapEnum mapToCreate = getMapToCreate();
        loadPlayerData();

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        createGameView(mapToCreate);

        if (gameOver) {
            this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MenuActivity.isGameOn = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Loads player data and saves them into constantHolder class.
     * Later on to be used by game to affect gameObjects.
     */
    private void loadPlayerData() {
        SharedPreferences settings = getSharedPreferences("PlayerOptions", 0);

        int maxHealth = settings.getInt(OptionStrings.playerBonusHealth, 0);
        int maxAmmo = settings.getInt(OptionStrings.playerBonusAmmo, 0);
        int maxSpeed = settings.getInt(OptionStrings.playerMaxSpeed, 0);

        this.buttonsTypeData.firstButtonType = ButtonTypeEnum.getButtonTypeFromButtonTypeString(settings.getString(OptionStrings.firstButtonType, "Shockwave"));
        this.buttonsTypeData.secondButtonType = ButtonTypeEnum.getButtonTypeFromButtonTypeString(settings.getString(OptionStrings.secondButtonType, "Timestop"));
        this.playerType = GamePlayerTypeEnum.getPlayerTypeFromTypeString(settings.getString(OptionStrings.playerType, "Saucer"));

        ConstantHolder.loadSettingsData(maxHealth, maxAmmo, maxSpeed);
    }

    /**
     * Creates game view.
     *
     * @param mapToCreate for game instance.
     */
    private void createGameView(GameMapEnum mapToCreate) {
        setContentView(new GamePanelSingleplayer(this, mapToCreate, this.playerType, this.buttonsTypeData));
    }

    /**
     * Basic switch to decide what map should be created.
     *
     * @return map for GameView to be created.
     */
    private GameMapEnum getMapToCreate() {
        try {
            switch (getIntent().getExtras().getString("Level")) {
                case "Ground":
                    return GameMapEnum.Level1;
                case "Space":
                    return GameMapEnum.SpaceLevel1;
                default:
                    return GameMapEnum.BlankMap;
            }
        } catch (NullPointerException e) {
            return GameMapEnum.BlankMap;
        }
    }
}
