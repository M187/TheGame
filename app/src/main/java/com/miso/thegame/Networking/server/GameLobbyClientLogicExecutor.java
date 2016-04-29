package com.miso.thegame.Networking.server;

import com.miso.thegame.MultiplayerLobby;
import com.miso.thegame.Networking.MessageLogicExecutor;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.hornak on 29.04.2016.
 * <p/>
 * Actual logic to be performed with the arriving messages.
 * <p/>
 * Class should be assigned to a local server to process incoming massages. <br>
 * This class should be used for LOBBY CLIENT instance. <br>
 */
public class GameLobbyClientLogicExecutor extends MessageLogicExecutor {

    private volatile List<Client> joinedPlayers = new ArrayList<>();
    private volatile MultiplayerLobby multiplayerLobby;

    public GameLobbyClientLogicExecutor(List<Client> joinedPlayers, MultiplayerLobby multiplayerLobby) {
        this.joinedPlayers = joinedPlayers;
        this.isServerLogicProcessor = false;
        this.multiplayerLobby = multiplayerLobby;
    }

    @Override
    public void processIncomingMessage(TransmissionMessage transmissionMessage) {

        switch (transmissionMessage.getTransmissionType()) {

            // Other player joining game
            case "02":
                this.joinedPlayers.add(
                        new Client(
                                ((JoinGameLobbyMessage) transmissionMessage).getComputerName(),
                                MultiplayerLobby.DEFAULT_COM_PORT,
                                ((JoinGameLobbyMessage) transmissionMessage).getNickname()));
                break;

            // Disbanding game
            case "05":
                this.multiplayerLobby.abandonClick(this.multiplayerLobby.findViewById(R.id.multiplayer_lobby_layout));
                break;

            // Other player leaving game.
            case "07":
                this.joinedPlayers.remove(
                        new Client(((JoinGameLobbyMessage) transmissionMessage).getComputerName(),
                                MultiplayerLobby.DEFAULT_COM_PORT,
                                ((JoinGameLobbyMessage) transmissionMessage).getNickname()));
                break;
        }
    }
}
