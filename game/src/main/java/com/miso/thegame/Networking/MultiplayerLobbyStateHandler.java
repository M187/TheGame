package com.miso.thegame.Networking;

import android.widget.Button;
import android.widget.TextView;

import com.miso.thegame.MultiplayerLobby;
import com.miso.thegame.R;

/**
 * Created by michal.hornak on 10.05.2016.
 */
public class MultiplayerLobbyStateHandler {

    private MultiplayerLobby multiplayerLobby;

    public MultiplayerLobbyStateHandler(MultiplayerLobby multiplayerLobby){
        this.multiplayerLobby = multiplayerLobby;
    }

    public void joinClickUiEvents(){
        (this.multiplayerLobby.findViewById(R.id.button_join)).setEnabled(false);
        (this.multiplayerLobby.findViewById(R.id.button_ready)).setEnabled(true);
        (this.multiplayerLobby.findViewById(R.id.button_abandon)).setEnabled(true);
        (this.multiplayerLobby.findViewById(R.id.button_host)).setEnabled(false);
        (this.multiplayerLobby.findViewById(R.id.player_nickname)).setEnabled(false);
    }

    public void abdandonClickUiEvents(){
        (this.multiplayerLobby.findViewById(R.id.button_join)).setEnabled(true);
        (this.multiplayerLobby.findViewById(R.id.button_ready)).setEnabled(false);
        (this.multiplayerLobby.findViewById(R.id.button_abandon)).setEnabled(false);
        (this.multiplayerLobby.findViewById(R.id.button_host)).setEnabled(true);
        (this.multiplayerLobby.findViewById(R.id.player_nickname)).setEnabled(true);
    }

    public void hostClickUiChanges(){
        ((TextView) this.multiplayerLobby.findViewById(R.id.textinfo_hosting_game)).setText("Hosting Game!");
        ((TextView) this.multiplayerLobby.findViewById(R.id.textinfo_hosting_game)).setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_red_dark));
        ((Button) this.multiplayerLobby.findViewById(R.id.button_host)).setText("UN-HOST");
        (this.multiplayerLobby.findViewById(R.id.button_join)).setEnabled(false);
        (this.multiplayerLobby.findViewById(R.id.button_start)).setEnabled(true);
        (this.multiplayerLobby.findViewById(R.id.player_nickname)).setEnabled(false);
    }

    public void unHostClickUiChanges(){
        ((TextView) this.multiplayerLobby.findViewById(R.id.textinfo_hosting_game)).setText("Not hosting any game!");
        ((TextView) this.multiplayerLobby.findViewById(R.id.textinfo_hosting_game)).setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_green_dark));
        ((Button) this.multiplayerLobby.findViewById(R.id.button_host)).setText("HOST");
        (this.multiplayerLobby.findViewById(R.id.button_join)).setEnabled(true);
        (this.multiplayerLobby.findViewById(R.id.button_start)).setEnabled(false);
        (this.multiplayerLobby.findViewById(R.id.player_nickname)).setEnabled(true);
    }

    public void readyClickUiChanges(){
        ((Button) this.multiplayerLobby.findViewById(R.id.button_ready)).setText("UN-READY");
    }

    public void unReadyClickChanges(){
        ((Button) this.multiplayerLobby.findViewById(R.id.button_ready)).setText("READY");
    }

    public enum LobbyState{
        Default,
        Joined,
        JoinedAndReadyForGame,
        Hosting
    }
}
