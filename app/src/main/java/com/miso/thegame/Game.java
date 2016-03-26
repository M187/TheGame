package com.miso.thegame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.app.Activity;
import android.view.WindowManager;

import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.gameMechanics.ConstantHolder;



public class Game extends Activity {

    public static DisplayMetrics metrics = new DisplayMetrics();
    public boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        GameMapEnum mapToCreate = GameMapEnum.Level2;
        switch (getIntent().getExtras().getString("Level")) {
            case "Ground":
                mapToCreate = GameMapEnum.Level2;
                break;
            case "Space":
                mapToCreate = GameMapEnum.SpaceLevel1;
                break;
        }

        loadPlayerData();

        super.onCreate(savedInstanceState);
        //set fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setContentView(new GamePanel(this, mapToCreate));
        if (gameOver){this.finish();}
    }

    @Override
    protected void onDestroy(){
        MenuActivity.isGameOn = false;
        super.onDestroy();
    }

    private void loadPlayerData(){

        SharedPreferences settings = getSharedPreferences("PlayerOptions", 0);

        int maxHealth = settings.getInt("playerMaxHealth", 0);
        int maxAmmo = settings.getInt("playerMaxAmmo", 0);
        int maxSpeed = settings.getInt("playerMaxSpeed", 0);

        ConstantHolder.loadSettingData(maxHealth, maxAmmo, maxSpeed);
    }
}
