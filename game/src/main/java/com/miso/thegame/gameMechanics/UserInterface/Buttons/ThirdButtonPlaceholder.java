package com.miso.thegame.gameMechanics.UserInterface.Buttons;

import android.content.res.Resources;

import com.miso.thegame.GameData.ButtonTypeEnum;

/**
 * Created by Miso on 6.11.2015.
 */
public class ThirdButtonPlaceholder extends ButtonPlaceholder {

    public ThirdButtonPlaceholder(Resources res, ButtonTypeEnum buttonType) {
        this.lastUse = 0;
        this.iteratorToDrawXAxis = 0;
        this.iteratorToDrawYAxis = 3;
        super.initializeButtonPlaceholder(res, buttonType);
    }
}