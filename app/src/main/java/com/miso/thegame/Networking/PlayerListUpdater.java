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
    private volatile boolean synchronousWait;
    private volatile String entry;

    public PlayerListUpdater(MultiplayerLobby multiplayerLobby, ArrayList<Client> registeredPlayers) {
        this.multiplayerLobby = multiplayerLobby;
        this.registeredPlayers = registeredPlayers;
    }

    public void terminate() {
        this.running = false;
    }

    public void run() {

        Runnable editPlayerList = new Runnable() {
            @Override
            public void run() {
                try {
                    ((TextView) multiplayerLobby.findViewById(R.id.list_of_players)).setText(entry);
                    synchronousWait = false;
                } catch (NullPointerException e) {}
            }
        };

        this.running = true;
        while (running) {
            this.entry = "";
            for (Client client : registeredPlayers) {
                if (client.isReadyForGame){
                    this.entry = entry + "   READY   : ";
                } else {
                    this.entry = entry + " NOT READY : ";
                }
                entry = entry + client.getStringForExtras() + "\n";
            }

            this.synchronousWait = true;
            this.multiplayerLobby.runOnUiThread(editPlayerList);
            while (synchronousWait);
        }
    }
}
