package com.miso.thegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.miso.thegame.GameData.ButtonTypeEnum;
import com.miso.thegame.GameData.GamePlayerTypeEnum;
import com.miso.thegame.GameData.OptionStrings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.hornak on 14.01.2016.
 */
public class PlayerOptions extends Activity {

    private int playerMaxHealth = 0;
    private int playerMaxAmmo = 0;
    private int playerMaxSpeed = 0;
    private ProgressBar healthProgBar;
    private ProgressBar ammoProgBar;
    private ProgressBar speedProgBar;

    private Spinner firstButtonTypeSpinner;
    private String firstButtonType;
    private Spinner secondButtonTypeSpinner;
    private String secondButtonType;
    private Spinner playerTypeSpinner;
    private String playerType;
    
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

        initializeFirstButtonSpinner();
        initializeSecondButtonSpinner();
        initializePlayerSpinner();
    }

    //<editor-fold desc="Spinner stuff">
    private void initializeFirstButtonSpinner(){
        this.firstButtonTypeSpinner = (Spinner) findViewById(R.id.first_button_type_spinner);

        List<String> firstButtonTypeList = new ArrayList<>();
        for (ButtonTypeEnum buttonTypeString : ButtonTypeEnum.values()){
            firstButtonTypeList.add(buttonTypeString.getButtonTypeString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, firstButtonTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.firstButtonTypeSpinner.setAdapter(adapter);

        if (!this.firstButtonType.equals(null) | this.firstButtonType.equals("")) {
            int spinnerPosition = adapter.getPosition(this.firstButtonType);
            this.firstButtonTypeSpinner.setSelection(spinnerPosition);
        }        
    }

    private void initializeSecondButtonSpinner(){
        this.secondButtonTypeSpinner = (Spinner) findViewById(R.id.second_button_type_spinner);

        List<String> secondButtonTypeList = new ArrayList<>();
        for (ButtonTypeEnum buttonTypeString : ButtonTypeEnum.values()){
            secondButtonTypeList.add(buttonTypeString.getButtonTypeString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, secondButtonTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.secondButtonTypeSpinner.setAdapter(adapter);

        if (!this.secondButtonType.equals(null) | this.secondButtonType.equals("")) {
            int spinnerPosition = adapter.getPosition(this.secondButtonType);
            this.secondButtonTypeSpinner.setSelection(spinnerPosition);
        }
    }
    
    private void initializePlayerSpinner() {
        this.playerTypeSpinner = (Spinner) findViewById(R.id.player_type_spinner);

        List<String> playerTypeList = new ArrayList<>();
        for (GamePlayerTypeEnum playerTypeString : GamePlayerTypeEnum.values()){
            playerTypeList.add(playerTypeString.getTypeString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, playerTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.playerTypeSpinner.setAdapter(adapter);

        if (!this.playerType.equals(null) | this.playerType.equals("")) {
            int spinnerPosition = adapter.getPosition(this.playerType);
            this.playerTypeSpinner.setSelection(spinnerPosition);
        }
    }
    //</editor-fold>

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
        this.firstButtonType = settings.getString(OptionStrings.firstButtonType, "");
        this.secondButtonType = settings.getString(OptionStrings.secondButtonType, "");
        this.playerType = settings.getString(OptionStrings.playerType, "");
    }

    private void saveSettings(){
        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(OptionStrings.playerMaxHealth, playerMaxHealth);
        editor.putInt(OptionStrings.playerMaxAmmo, playerMaxAmmo);
        editor.putInt(OptionStrings.playerMaxSpeed, playerMaxSpeed);

        editor.putString(OptionStrings.firstButtonType, String.valueOf(this.firstButtonTypeSpinner.getSelectedItem()));
        editor.putString(OptionStrings.secondButtonType, String.valueOf(this.secondButtonTypeSpinner.getSelectedItem()));
        editor.putString(OptionStrings.playerType, String.valueOf(this.playerTypeSpinner.getSelectedItem()));

        // Commit the edits!
        editor.commit();
    }
}

