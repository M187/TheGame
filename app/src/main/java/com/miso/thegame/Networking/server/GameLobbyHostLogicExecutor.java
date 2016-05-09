package com.miso.thegame.Networking.server;

import com.miso.thegame.MultiplayerLobby;
import com.miso.thegame.Networking.Sender;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.LeaveGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.OtherPlayerDataMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.ReadyToPlayMessage;

import java.util.ArrayList;

/**
 * Created by michal.hornak on 24.04.2016. <br>
 * Actual logic to be performed with the arriving messages.
 * <p/>
 * Class should be assigned to a local server to process incoming massages. <br>
 * This class should be used for SERVER instance. <br>
 */

public class GameLobbyHostLogicExecutor extends MessageLogicExecutor {

    private volatile ArrayList<Client> joinedPlayers = new ArrayList<>();
    private Sender sender;

    public GameLobbyHostLogicExecutor(ArrayList<Client> joinedPlayers, Sender sender) {
        this.sender = sender;
        this.joinedPlayers = joinedPlayers;
        this.isServerLogicProcessor = true;
    }

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
                //// TODO: 1.5.2016 do this via sender?
                for (Client client : this.joinedPlayers) {
                    client.sendMessage(
                            new OtherPlayerDataMessage(
                                    ((JoinGameLobbyMessage) transmissionMessage).getNickname(),
                                    ((JoinGameLobbyMessage) transmissionMessage).getComputerName()));
                    newPlayer.sendMessage(new OtherPlayerDataMessage(client.getPlayerClientPojo()));
                }
                // add new player.
                this.joinedPlayers.add(newPlayer);
                break;

            // Player ready for game.
            case "03":
                this.joinedPlayers.get(
                        this.joinedPlayers.indexOf(new Client(((ReadyToPlayMessage) transmissionMessage).getNickname())))
                        .isReadyForGame = true;
                break;

            // Player un-ready for game.
            case "05":
                this.joinedPlayers.get(
                        this.joinedPlayers.indexOf(new Client(((ReadyToPlayMessage) transmissionMessage).getNickname())))
                        .isReadyForGame = false;
                break;

            // Player leaving game.
            case "06":
                // Tell other players about leaving player.
                //// TODO: 1.5.2016 do this via sender?
                for (Client client : this.joinedPlayers) {
                    client.sendMessage(transmissionMessage);
                }
                this.joinedPlayers.remove(
                        new Client(
                                ((LeaveGameLobbyMessage) transmissionMessage).getComputerName(),
                                MultiplayerLobby.DEFAULT_COM_PORT,
                                ((LeaveGameLobbyMessage) transmissionMessage).getNickname()));
                break;
        }
    }
}
