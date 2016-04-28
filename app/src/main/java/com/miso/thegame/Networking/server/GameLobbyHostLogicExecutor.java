package com.miso.thegame.Networking.server;

import com.miso.thegame.MultiplayerLobby;
import com.miso.thegame.Networking.MessageLogicExecutor;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.LeaveGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.OtherPlayerDataMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.hornak on 24.04.2016.
 * <p/>
 * Actual logic to be performed with the arriving messages.
 * <p/>
 * Each period check transmissionMessages and do actions based on its content.
 * Once message has been processed, pop it out.
 */

public class GameLobbyHostLogicExecutor extends MessageLogicExecutor {

    private volatile List<Client> joinedPlayers = new ArrayList<>();

    public GameLobbyHostLogicExecutor(List<Client> joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
        this.isServerLogicProcessor = true;
    }

    //</editor-fold>

    public void processIncomingMessage(TransmissionMessage transmissionMessage) {
        switch (transmissionMessage.getTransmissionType()) {

            // Player joining game.
            case "01":
                // Create new client based on incoming data.
                Client newPlayer = (
                        new Client(
                                ((JoinGameLobbyMessage) transmissionMessage).getComputerName(),
                                MultiplayerLobby.DEFAULT_COM_PORT,
                                ((JoinGameLobbyMessage) transmissionMessage).getNickname()));
                // Propagate new client parameters to other players.
                // Propagate other client data to new player.
                for (Client client : joinedPlayers) {
                    client.execute(
                            new OtherPlayerDataMessage(
                                    ((JoinGameLobbyMessage) transmissionMessage).getNickname(),
                                    ((JoinGameLobbyMessage) transmissionMessage).getComputerName()));
                    newPlayer.execute(new OtherPlayerDataMessage(client.getPlayerClientPojo()));
                }
                // add new player.
                joinedPlayers.add(newPlayer);
                break;

            // Player leaving game.
            case "03":
                // Tell other players about leaving player.
                for (Client client : joinedPlayers) {
                    client.execute(transmissionMessage);
                }
                joinedPlayers.remove(
                        new Client(
                                ((LeaveGameLobbyMessage) transmissionMessage).getComputerName(),
                                MultiplayerLobby.DEFAULT_COM_PORT,
                                ((LeaveGameLobbyMessage) transmissionMessage).getNickname()));
                break;
        }

    }
}
