package com.miso.thegame.gameMechanics.multiplayer;

import android.app.Activity;

import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.server.Server;

import java.util.ArrayList;

/**
 * Created by michal.hornak on 15.06.2016.
 */
public class ConnectionManager {

    public static final int PORT = 12372;
    public Server localServer = new Server(this.PORT);
    public volatile ArrayList<Client> registeredPlayers = new ArrayList<>();
    public GameSynchronizer gameSynchronizer;

    public ConnectionManager(ArrayList<Client> registeredPlayers) {
        this.registeredPlayers = registeredPlayers;
    }

    public void initializeAllConnectionsToOtherPlayersServers(Activity activity) throws GameSynchronizer.ConnectionInitializationTimeOut{
        this.gameSynchronizer = new GameSynchronizer(this.registeredPlayers);
        this.gameSynchronizer.createConnectionsWithRegisteredPlayers(activity);
    }

    public void waitForPlayersToReady() {
        boolean somePlayerNotReady = true;
        while (somePlayerNotReady) {
            somePlayerNotReady = false;
            for (Client player : this.registeredPlayers) {
                somePlayerNotReady = (player.isReadyForGame) ? somePlayerNotReady : true;
            }
        }
    }

}
