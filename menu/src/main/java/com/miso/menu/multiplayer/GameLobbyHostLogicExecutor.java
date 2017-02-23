package com.miso.menu.multiplayer;

import com.miso.menu.MultiplayerLobby;
import com.miso.thegame.Networking.NetworkConnectionConstants;
import com.miso.thegame.Networking.Sender;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.server.Server;
import com.miso.thegame.Networking.server.logicExecutors.MessageLogicExecutor;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.AssignColor;
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

    private volatile ArrayList<Client> registeredPlayers;
    private MultiplayerLobby mMultiplayerLobby;

    public GameLobbyHostLogicExecutor(MultiplayerLobby multiplayerLobby) {
        this.mMultiplayerLobby = multiplayerLobby;
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
        Client newClient = (
                new Client(
                        joinGameLobbyMessage.getComputerName(),
                        NetworkConnectionConstants.DEFAULT_COM_PORT,
                        joinGameLobbyMessage.getNickname()));
        newClient.start();

        // Assign color to joining player.
        newClient.sendMessage(new AssignColor(mMultiplayerLobby.getPlayerColors().getNextAvailableColor()));
        //Make color unavailable
        mMultiplayerLobby.getPlayerColors().makeColorUnavailable(new PlayerColors.MyColor(mMultiplayerLobby.getPlayerColors().getNextAvailableColor()));

        newClient.sendMessage(new OtherPlayerDataMessage(NetworkConnectionConstants.getPlayerNickname(), Server.myAddress.getHostName()));


        for (Client client : this.registeredPlayers) {
            client.sendMessage(
                    new OtherPlayerDataMessage(
                            joinGameLobbyMessage.getNickname(),
                            joinGameLobbyMessage.getComputerName()));
            newClient.sendMessage(new OtherPlayerDataMessage(client.getPlayerClientPojo()));
        }
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
        this.mMultiplayerLobby.getPlayerColors().makeColorAvailable(new PlayerColors.MyColor(
                this.registeredPlayers.get(
                        this.registeredPlayers.indexOf(
                                new Client(leaveGameLobbyMessage.getComputerName(), NetworkConnectionConstants.DEFAULT_COM_PORT, leaveGameLobbyMessage.getNickname()))
                ).mColor));


        this.registeredPlayers.remove(
                new Client(
                        leaveGameLobbyMessage.getComputerName(),
                        NetworkConnectionConstants.DEFAULT_COM_PORT,
                        leaveGameLobbyMessage.getNickname()));

        //Sending occurs after removal of an player...
        mMultiplayerLobby.getSender().sendMessage(leaveGameLobbyMessage);
        //todo inform players about new available color?
    }
}
