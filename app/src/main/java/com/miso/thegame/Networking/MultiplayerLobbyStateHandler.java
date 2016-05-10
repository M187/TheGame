package com.miso.thegame.Networking;

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
    }

    public enum LobbyState{
        Default,
        Joined,
        JoinedAndReadyForGame,
        Hosting
    }
}
