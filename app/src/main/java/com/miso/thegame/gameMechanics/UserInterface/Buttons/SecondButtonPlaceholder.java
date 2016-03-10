package com.miso.thegame.gameMechanics.UserInterface.Buttons;

import android.content.res.Resources;

/**
 * Created by Miso on 6.11.2015.
 */
public class SecondButtonPlaceholder extends ButtonPlaceholder {

    public SecondButtonPlaceholder(Resources res, ButtonAction buttonAction){
        this.lastUse = 0;
        this.iteratorToDrawXAxis = 0;
        this.iteratorToDrawYAxis = 2;
        super.initializeButtonPlaceholder(res, buttonAction);
    }
}
