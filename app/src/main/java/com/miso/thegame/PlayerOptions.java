package com.miso.thegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.miso.thegame.GameData.OptionStrings;

/**
 * Created by michal.hornak on 14.01.2016.
 */
public class PlayerOptions extends Activity {

    public static String firstButtonFunctionality = null;
    public static String secondButtonFunctionality = null;
    private int playerMaxHealth = 0;
    private int playerMaxAmmo = 0;
    private int playerMaxSpeed = 0;
    private ProgressBar healthProgBar;
    private ProgressBar ammoProgBar;
    private ProgressBar speedProgBar;
    private SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeSettings();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.player_options_layout);

        healthProgBar = (ProgressBar) findViewById(R.id.health_bar);
        healthProgBar.setMax(5);
        healthProgBar.setProgress(playerMaxHealth);
        ammoProgBar = (ProgressBar) findViewById(R.id.max_ammo_bar);
        ammoProgBar.setMax(5);
        ammoProgBar.setProgress(playerMaxAmmo);
        speedProgBar = (ProgressBar) findViewById(R.id.max_speed_bar);
        speedProgBar.setMax(5);
        speedProgBar.setProgress(playerMaxSpeed);
    }

    @Override
    protected void onStop(){
        super.onStop();
        saveSettings();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 1:
                return true;
            case 2:
                return true;
            default:
                return false;
        }
    }

    public void decHealth(View view) {
        if (playerMaxHealth >= 0) {
            playerMaxHealth -= 1;
            healthProgBar.setProgress(playerMaxHealth);
        }
    }

    public void incHealth(View view) {
        if (playerMaxHealth <= 5) {
            playerMaxHealth += 1;
            healthProgBar.setProgress(playerMaxHealth);
        }
    }

    public void decAmmo(View view) {
        if (playerMaxAmmo >= 0) {
            playerMaxAmmo -= 1;
            ammoProgBar.setProgress(playerMaxAmmo);
        }
    }

    public void incAmmo(View view) {
        if (playerMaxAmmo <= 5) {
            playerMaxAmmo += 1;
            ammoProgBar.setProgress(playerMaxAmmo);
        }
    }

    public void decSpeed(View view) {
        if (playerMaxSpeed >= 0) {
            playerMaxSpeed -= 1;
            speedProgBar.setProgress(playerMaxSpeed);
        }
    }

    public void incSpeed(View view) {
        if (playerMaxSpeed <= 5) {
            playerMaxSpeed += 1;
            speedProgBar.setProgress(playerMaxSpeed);
        }
    }

    private void initializeSettings(){
        this.settings = getPreferences(0);

        this.playerMaxHealth = settings.getInt(OptionStrings.playerMaxHealth, 0);
        this.playerMaxAmmo = settings.getInt(OptionStrings.playerMaxAmmo, 0);
        this.playerMaxSpeed = settings.getInt(OptionStrings.playerMaxSpeed, 0);
    }

    private void saveSettings(){
        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(OptionStrings.playerMaxHealth, playerMaxHealth);
        editor.putInt(OptionStrings.playerMaxAmmo, playerMaxAmmo);
        editor.putInt(OptionStrings.playerMaxSpeed, playerMaxSpeed);

        // Commit the edits!
        editor.commit();
    }
}

