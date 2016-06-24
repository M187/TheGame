package com.miso.thegame.gameMechanics.multiplayer;

import android.app.Activity;
import android.util.Log;

import com.miso.thegame.GameActivity;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameViews.GamePanelMultiplayer;

/**
 * Created by michal.hornak on 21.06.2016.
 * <p/>
 * Class designed to run loop that waits for initialization of all connections with other players.
 * Must be done as async task since otherwise it blocks UiThread.
 * After connections are created, content view is set to multiplayerGameSurfaceView.
 */
public class WaiterForAllConnections extends Thread {

    private Activity gameActivity;
    private GamePanelMultiplayer gamePanelMultiplayer;

    public WaiterForAllConnections(GamePanelMultiplayer gamePanelMultiplayer, Activity gameActivity) {
        this.gameActivity = gameActivity;
        this.gamePanelMultiplayer = gamePanelMultiplayer;
    }

    public void run() {
        try {
            this.gamePanelMultiplayer.connectionManager.initializeAllConnectionsToOtherPlayersServers();
            GameActivity.areAllPlayersConnected = true;
            Log.d(ConstantHolder.TAG, " --> All players have connected.");
            this.gameActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameActivity.setContentView(gamePanelMultiplayer);
                }
            });
        } catch (GameSynchronizer.ConnectionInitializationTimeOut e) {
            //inform player that connections was not established.
            //this.gamePanelMultiplayer.connectionManager.localServer.terminate();
            Log.d(ConstantHolder.TAG, " --> All players have NOT connected in provided time.");
            this.gameActivity.finish();
        }
    }

}
