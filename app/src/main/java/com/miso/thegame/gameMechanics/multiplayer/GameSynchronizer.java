package com.miso.thegame.gameMechanics.multiplayer;

import android.os.AsyncTask;
import android.os.Build;

import com.miso.thegame.Networking.client.Client;

import java.util.ArrayList;

/**
 * Created by michal.hornak on 19.05.2016.
 *
 * Intention for this class is to synchronize starting of a game as well as to synchronize main game loop.
 *
 */
public class GameSynchronizer {

    private ArrayList<Client> registeredPlayers;

    public GameSynchronizer(ArrayList<Client> registeredPlayers) {
        this.registeredPlayers = registeredPlayers;
    }

    public void createConnectionsWithRegisteredPlayers() throws ConnectionInitializationTimeOut {
        for (Client client : registeredPlayers) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                client.execute();
            }
        }
        waitForClientsConnection();
    }

    private void waitForClientsConnection() throws ConnectionInitializationTimeOut {
        boolean needMoreTime = true;
        long startTime = System.currentTimeMillis();
        System.out.println(" --> Waiting for all client objects connection establishment.");
        while (needMoreTime) {
            needMoreTime = false;
            for (Client client : registeredPlayers) {
                if (!client.isConnectionEstablished()) {
                    needMoreTime = true;
                    if (System.currentTimeMillis() - startTime < 30000) {
                        for (Client client2 : registeredPlayers) {
                            client2.terminate();
                        }
                        throw new ConnectionInitializationTimeOut();
                    }
                }
            }
        }
    }

    public void waitForClientsToSignalizeReadyForNextFrame() {
        boolean needMoreTime = true;
        while (needMoreTime) {
            needMoreTime = false;
            for (Client client : registeredPlayers) {
                if (!client.isReadyForGame) {
                    needMoreTime = true;
                }
            }
        }
        for (Client client : this.registeredPlayers) {
            client.isReadyForGame = false;
        }
    }

    public class ConnectionInitializationTimeOut extends Throwable{}
}
