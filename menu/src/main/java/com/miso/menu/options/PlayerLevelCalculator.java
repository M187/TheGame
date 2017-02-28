package com.miso.menu.options;

import android.content.SharedPreferences;

import com.miso.thegame.GameData.OptionStrings;

/**
 * Created by michal.hornak on 2/28/2017.
 */

public class PlayerLevelCalculator {

    public int getDistributedStatPoints() {
        return this.healthLevel
                + this.ammoLevel
                + this.speedLevel;
    }


    public int getAvailableStatPoints() throws UnsupportedOperationException {
        if (dataReady) return availableStatPoints;
        throw new UnsupportedOperationException();
    }

    public int healthLevel = 0;
    public int ammoLevel = 0;
    public int speedLevel = 0;

    private int availableStatPoints = 0;
    private boolean dataReady = false;

    public void setPlayerStatPoints(int levelPoints) {
        this.dataReady = true;
        if (levelPoints > 15){
            this.availableStatPoints = 15;
        } else {
            // todo adapt calculation
            this.availableStatPoints = 3;
        }
    }

    public PlayerLevelCalculator initialize(SharedPreferences settings) {

        this.healthLevel = settings.getInt(OptionStrings.playerBonusHealth, 0);
        this.ammoLevel = settings.getInt(OptionStrings.playerBonusAmmo, 0);
        this.speedLevel = settings.getInt(OptionStrings.playerMaxSpeed, 0);

        return this;
    }
}
