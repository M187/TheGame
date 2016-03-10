package com.miso.thegame.gameMechanics.UserInterface.Buttons;

import android.content.res.Resources;

/**
 * Created by Miso on 6.11.2015.
 */
public class FirstButtonPlaceholder extends ButtonPlaceholder {

    public FirstButtonPlaceholder(Resources res, ButtonAction buttonAction) {
        this.lastUse = 0;
        this.iteratorToDrawXAxis = 0;
        this.iteratorToDrawYAxis = 1;
        super.initializeButtonPlaceholder(res, buttonAction);
    }
}
