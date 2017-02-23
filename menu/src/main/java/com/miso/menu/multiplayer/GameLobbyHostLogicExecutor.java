package com.miso.menu.multiplayer;

import com.miso.menu.MultiplayerLobby;
import com.miso.thegame.Networking.NetworkConnectionConstants;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.server.Server;
import com.miso.thegame.Networking.server.logicExecutors.MessageLogicExecutor;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.AssignColor;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.LeaveGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.OtherPlayerDataMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.PlayerChangeColor;
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

    private volatile ArrayList<Client> registeredPlayers;
    private MultiplayerLobby multiplayerLobby;

    public GameLobbyHostLogicExecutor(MultiplayerLobby multiplayerLobby) {
        this.multiplayerLobby = multiplayerLobby;
        this.registeredPlayers = multiplayerLobby.getRegisteredPlayers();
        this.isServerLogicProcessor = true;
    }

    public void processIncomingMessage(TransmissionMessage transmissionMessage) throws StartGameException, DisbandGameException {

        switch (transmissionMessage.getTransmissionType()) {
            // Player joining game.
            case "01":
                // Tell other players about joining player.
                this.joinMessageProcessing((JoinGameLobbyMessage) transmissionMessage);
                break;

            // Player ready for game.
            case "03":
                this.registeredPlayers.get(
                        this.registeredPlayers.indexOf(new Client(((ReadyToPlayMessage) transmissionMessage).getNickname())))
                        .isReadyForGame = true;
                break;

            // Player un-ready for game.
            case "05":
                this.registeredPlayers.get(
                        this.registeredPlayers.indexOf(new Client(((ReadyToPlayMessage) transmissionMessage).getNickname())))
                        .isReadyForGame = false;
                break;

            // Player leaving game.
            case "06":
                // Tell other players about leaving player.
                this.otherPlayerLeaveMessageProcessing((LeaveGameLobbyMessage) transmissionMessage);
                break;

            //Other player change color
            case "012":
                Client temp = this.registeredPlayers.get(
                        this.registeredPlayers.indexOf(
                                new Client(((PlayerChangeColor) transmissionMessage).getNickname())
                        )
                );
                // old color is available again. New one is not. Make color change to relevant player.
                this.multiplayerLobby.getPlayerColors().makeColorAvailable(new PlayerColors.MyColor(temp.mColor));
                this.multiplayerLobby.getPlayerColors().makeColorUnavailable(new PlayerColors.MyColor(((PlayerChangeColor) transmissionMessage).getColor()));
                temp.mColor = ((PlayerChangeColor) transmissionMessage).getColor();

                this.multiplayerLobby.getSender().sendMessage(transmissionMessage);
        }
    }

    /**
     * Process join game message.
     * Also forwards to registered players.
     *
     * @param joinGameLobbyMessage created by a relevant player and forwarded by host.
     */
    public void joinMessageProcessing(JoinGameLobbyMessage joinGameLobbyMessage) {
        // Create new client based on incoming data.

        // get the next available color
        int assignedColor = multiplayerLobby.getPlayerColors().getNextAvailableColor();
        // create representation of a new Client and start thread. Thread will try to connect to client listener.
        Client newClient = (
                new Client(
                        joinGameLobbyMessage.getComputerName(),
                        NetworkConnectionConstants.DEFAULT_COM_PORT,
                        joinGameLobbyMessage.getNickname(),
                        assignedColor));
        newClient.start();
        // Assign color to joining player.
        newClient.sendMessage(new AssignColor(assignedColor));
        // Make color unavailable
        multiplayerLobby.getPlayerColors().makeColorUnavailable(new PlayerColors.MyColor(assignedColor));
        // inform client about my data
        newClient.sendMessage(new OtherPlayerDataMessage(NetworkConnectionConstants.getPlayerNickname(), Server.myAddress.getHostName(), multiplayerLobby.myCurrentColor));
        // inform client about other players data
        for (Client client : this.registeredPlayers) {
            client.sendMessage(
                    new OtherPlayerDataMessage(
                            joinGameLobbyMessage.getNickname(),
                            joinGameLobbyMessage.getComputerName(),
                            assignedColor));
            newClient.sendMessage(new OtherPlayerDataMessage(client.getPlayerClientPojo()));
        }
        // finally add new client to registered players
        this.registeredPlayers.add(newClient);
    }

    /**
     * Process leave game message.
     * Also forwards to registered players.
     *
     * @param leaveGameLobbyMessage created by a relevant player and forwarded by host.
     */
    public void otherPlayerLeaveMessageProcessing(LeaveGameLobbyMessage leaveGameLobbyMessage) {

        // Set color of an leaving player as unoccupied
        this.multiplayerLobby.getPlayerColors().makeColorAvailable(new PlayerColors.MyColor(
                this.registeredPlayers.get(
                        this.registeredPlayers.indexOf(
                                new Client(leaveGameLobbyMessage.getNickname()))
                ).mColor));

        this.registeredPlayers.remove(
                new Client(leaveGameLobbyMessage.getNickname()));

        // Sending occurs after removal of an player...
        multiplayerLobby.getSender().sendMessage(leaveGameLobbyMessage);
    }
}
