package com.miso.thegame.gameMechanics;


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
    public static final int shockwaveCooldown = 8000;
    public static final int shockwaveReachFactor = 2;
    public static int maximumPlayerHealth = 50;
    public static int primaryAmunitionMaxValue = 120;
    public static int maximumPlayerSpeed = 8;
    public static int enemiesCount = 0;
    public static boolean timestopActive = false;
    public static boolean freezeActive = false;
    //</editor-fold>

    public static void loadSettingData(int playerHealthLevel, int playerAmmoLevel, int playerSpeedLevel){
        maximumPlayerHealth += 10 * playerHealthLevel;
        primaryAmunitionMaxValue += 20 * playerAmmoLevel;
        maximumPlayerSpeed += playerSpeedLevel;
    }
}
