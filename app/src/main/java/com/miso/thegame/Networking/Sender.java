package com.miso.thegame.Networking;

import android.os.AsyncTask;
import android.os.Build;

import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

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

    //?send one message after game update?
    public void afterUpdateMessage(){

    }

    public void sendMessage(TransmissionMessage transmissionMessage){
        for (Client player : this.registeredPlayers){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                player.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, transmissionMessage);
            } else {
                player.execute(transmissionMessage);
            }
        }
    }
}