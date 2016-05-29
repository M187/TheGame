package com.miso.thegame.Networking;

import android.widget.TextView;

import com.miso.thegame.MultiplayerLobby;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.R;

import java.util.ArrayList;

/**
 * Created by Miso on 28.5.2016.
 */
public class PlayerListUpdater extends Thread {

    private volatile ArrayList<Client> registeredPlayers;
    private MultiplayerLobby multiplayerLobby;
    private volatile boolean running;
    private String entry;

    public PlayerListUpdater(MultiplayerLobby multiplayerLobby, ArrayList<Client> registeredPlayers){
        this.multiplayerLobby = multiplayerLobby;
        this.registeredPlayers = registeredPlayers;
    }

    public void terminate(){
        this.running = false;
    }

    public void run(){
        this.running = true;
        while (running) {
            this.entry = "";
            for (Client client : registeredPlayers) {
                entry = entry + client.getStringForExtras() + "\n";
            }

            this.multiplayerLobby.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ((TextView) multiplayerLobby.findViewById(R.id.list_of_players)).setText(entry);
                    } catch (NullPointerException e){}
                }
            });
        }
    }
}
