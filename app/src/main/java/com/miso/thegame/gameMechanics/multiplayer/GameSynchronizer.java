package com.miso.thegame.gameMechanics.multiplayer;

import com.miso.thegame.Networking.client.Client;

import java.util.ArrayList;

/**
 * Created by michal.hornak on 19.05.2016.
 *
 * Intention for this class is to synchronize starting of a game as well as to synchronize main game loop.
 *
 */
public class GameSynchronizer{

    private ArrayList<Client> registeredPlayers;

    public GameSynchronizer(ArrayList<Client> registeredPlayers){
        this.registeredPlayers = registeredPlayers;
        for (Client client: registeredPlayers){
            client.execute();
        }
        waitForClientsConnection();
    }

    public void waitForClientsConnection(){
        boolean needMoreTime = true;
        System.out.println("Waiting for all client objects connection establishment.");
        while (needMoreTime){
            needMoreTime = false;
            for (Client client : registeredPlayers){
                if (!client.isConnectionEstablished()){
                    needMoreTime = true;
                }
            }
        }
    }

    public void waitForClientsToSignalizeReadyForNextFrame(){
        boolean needMoreTime = true;
        while (needMoreTime){
            needMoreTime = false;
            for (Client client : registeredPlayers){
                if (!client.isReadyForGame){
                    needMoreTime = true;
                }
            }
        }
    }

}
