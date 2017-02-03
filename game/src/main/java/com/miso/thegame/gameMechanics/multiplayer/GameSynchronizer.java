package com.miso.thegame.gameMechanics.multiplayer;

import com.miso.thegame.Networking.client.Client;

import java.util.ArrayList;

/**
 * Created by michal.hornak on 19.05.2016.
 * <p/>
 * Intention for this class is to synchronize starting of a game as well as to synchronize main game loop.
 */
public class GameSynchronizer {

    private ArrayList<Client> registeredPlayers;
    private boolean running = true;

    public GameSynchronizer(ArrayList<Client> registeredPlayers) {
        this.registeredPlayers = registeredPlayers;
    }

    public void createConnectionsWithRegisteredPlayers() throws ConnectionInitializationTimeOut {
        for (Client client : registeredPlayers) {
            client.start();
        }
        waitForClientsConnection();
    }

    private void waitForClientsConnection() throws ConnectionInitializationTimeOut {
        boolean needMoreTime = true;
        long startTime = System.currentTimeMillis();
        System.out.println(" --> Waiting for all client objects connection establishment.");
        while (needMoreTime && this.running) {
            needMoreTime = false;
            for (Client client : registeredPlayers) {
                if (!client.isConnectionEstablished()) {
                    needMoreTime = true;
                    if (System.currentTimeMillis() - startTime > 60000) {
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
        while (needMoreTime && this.running) {
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

    public void terminate(){
        this.running = false;
    }

    public class ConnectionInitializationTimeOut extends Throwable {
    }
}
