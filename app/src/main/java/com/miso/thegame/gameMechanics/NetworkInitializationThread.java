package com.miso.thegame.gameMechanics;

import android.app.Activity;

import com.miso.thegame.gameMechanics.gameViews.GamePanelMultiplayer;

/**
 * Created by michal.hornak on 15.06.2016.
 */
public class NetworkInitializationThread extends Thread {

    private GamePanelMultiplayer gamePanelMultiplayer;

    public NetworkInitializationThread(GamePanelMultiplayer gamePanelMultiplayer){
        this.gamePanelMultiplayer = gamePanelMultiplayer;
    }

    public void runMe(Activity activity){


    }

}
