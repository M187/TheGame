package com.miso.thegame.gameMechanics;


import com.miso.thegame.gameMechanics.gameViews.GameView2;

/**
 * Created by Miso on 21.12.2015.
 */
public class ConstantHolder {

    public static final String TAG = "TheGame";
    //<editor-fold desc="Spell constants">
    public static final int firebalCooldown = 300;
    
    public static final int timestopDuration = 5000;
    public static final int timestopCooldown = 15000;
    public static final int freezeCooldown = 5000;
    public static final int freezeDuration = 3000;
    public static final int shockwaveCooldown = 8000;
    public static final int shockwaveReachFactor = 1;
    public static boolean timestopActive = false;
    public static boolean freezeActive = false;
    public static int maximumPlayerHealth = 50;
    public static int primaryAmunitionMaxValue = 120;

    public static int maximumPlayerSpeed = (int) GameView2.scaleSize(4);
    public static int enemiesCount = 0;
    //</editor-fold>

    public static void loadSettingsData(int playerHealthLevel, int playerAmmoLevel, int playerSpeedLevel){
        maximumPlayerHealth += 10 * playerHealthLevel;
        primaryAmunitionMaxValue += 20 * playerAmmoLevel;
        maximumPlayerSpeed += (int) GameView2.scaleSize(playerSpeedLevel);
    }
}
