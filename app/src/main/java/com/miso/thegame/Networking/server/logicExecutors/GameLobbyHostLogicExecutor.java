package com.miso.thegame.Networking.server.logicExecutors;

import com.miso.thegame.MultiplayerLobby;
import com.miso.thegame.Networking.Sender;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.server.Server;
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

    private volatile ArrayList<Client> registeredPlayers;
    private Sender sender;

    public GameLobbyHostLogicExecutor(ArrayList<Client> registeredPlayers, Sender sender) {
        this.sender = sender;
        this.registeredPlayers = registeredPlayers;
        this.isServerLogicProcessor = true;
    }

    public void processIncomingMessage(TransmissionMessage transmissionMessage) throws StartGameException, DisbandGameException{

        switch (transmissionMessage.getTransmissionType()) {
            // Player joining game.
            case "01":
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
        Client newPlayer = (
                new Client(
                        joinGameLobbyMessage.getComputerName(),
                        MultiplayerLobby.DEFAULT_COM_PORT,
                        joinGameLobbyMessage.getNickname()));
        newPlayer.execute();
        //TODO: inform player here if the connection has not been successful (via terminate message? / via disband game message?)
        newPlayer.sendMessage(new OtherPlayerDataMessage(MultiplayerLobby.myNickname, Server.myAddress.getHostName()));

        for (Client client : this.registeredPlayers) {
            client.sendMessage(
                    new OtherPlayerDataMessage(
                            joinGameLobbyMessage.getNickname(),
                            joinGameLobbyMessage.getComputerName()));
            newPlayer.sendMessage(new OtherPlayerDataMessage(client.getPlayerClientPojo()));
        }
        this.registeredPlayers.add(newPlayer);
    }

    /**
     * Process leave game message.
     * Also forwards to registered players.
     *
     * @param leaveGameLobbyMessage created by a relevant player and forwarded by host.
     */
    public void otherPlayerLeaveMessageProcessing(LeaveGameLobbyMessage leaveGameLobbyMessage){
        sender.sendMessage(leaveGameLobbyMessage);
        this.registeredPlayers.remove(
                new Client(
                        leaveGameLobbyMessage.getComputerName(),
                        MultiplayerLobby.DEFAULT_COM_PORT,
                        leaveGameLobbyMessage.getNickname()));
    }
}
