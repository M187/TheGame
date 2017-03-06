package com.miso.thegame.gameMechanics.map.levels;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.miso.thegame.GameActivity;
import com.miso.thegame.GameData.ButtonTypeEnum;
import com.miso.thegame.GameData.OptionStrings;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.UserInterface.ButtonsTypeData;
import com.miso.thegame.gameMechanics.gameViews.GamePanelSingleplayer;

import java.util.ArrayList;

/**
 * Created by Miso on 14.9.2016.
 */
public class NewLevelActivity extends GameActivity {

    public static boolean isGameOn = false;
    public boolean gameOver = false;
    public ArrayList<Client> registeredPlayers = new ArrayList<>();
    private ButtonsTypeData buttonsTypeData = new ButtonsTypeData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(ConstantHolder.TAG, " --> Entered main game Activity.");
        loadPlayerData();

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        createGameView();

        if (gameOver) {
            this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        NewLevelActivity.isGameOn = false;
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

        this.buttonsTypeData.firstButtonType = ButtonTypeEnum.getButtonTypeFromButtonTypeString(settings.getString(OptionStrings.firstButtonType, ""));
        this.buttonsTypeData.secondButtonType = ButtonTypeEnum.getButtonTypeFromButtonTypeString(settings.getString(OptionStrings.secondButtonType, ""));

        ConstantHolder.loadSettingsData(maxHealth, maxAmmo, maxSpeed);
    }

    /**
     * Creates game view.
     */
    private void createGameView() {

        setContentView(new GamePanelSingleplayer(
                this,
                this.buttonsTypeData,
                getIntent().getIntExtra(getString(R.string.level_number), 1)));
    }
}
