package com.miso.thegame.Networking;

import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.StartGameMessage;

import java.util.ArrayList;

/**
 * Created by Miso on 1.5.2016.
 *
 * Class which purpose is to propagate messages to other players that have registered into game.
 *
 * Main method should iterate through registered clients and sends them a message.
 * Sending message is done via executor.
 *
 * In game lobby, this should use only server.
 * In game instance, it will be simpler - I hope:).
 */
public class Sender {

    private volatile ArrayList<Client> registeredPlayers;

    public Sender(ArrayList<Client> registeredPlayers){
        this.registeredPlayers = registeredPlayers;
    }

    public void sendMessage(TransmissionMessage transmissionMessage){
        for (Client client : this.registeredPlayers){
            if (client.isAlive()) {
                client.sendMessage(transmissionMessage);
            } else {
                client.start();
                client.sendMessage(transmissionMessage);
            }
        }
    }

    public void sendMessage(StartGameMessage startGameMessage){

        int playerIndexNumber = 1;

        for (Client player : this.registeredPlayers){
            startGameMessage.setIndexForPlayer(playerIndexNumber);
            if (player.isAlive()) {
                player.sendMessage(startGameMessage);
            } else {
                player.start();
                player.sendMessage(startGameMessage);
            }
            playerIndexNumber++;
        }
    }
}