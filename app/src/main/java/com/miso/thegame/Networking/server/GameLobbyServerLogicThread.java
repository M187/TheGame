package com.miso.thegame.Networking.server;

import com.miso.thegame.Networking.PlayerClientPOJO;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
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

public class GameLobbyServerLogicThread implements Runnable {

    private volatile boolean running = true;
    private volatile boolean startGame = false;

    private volatile List<TransmissionMessage> transmissionMessages;
    private List<PlayerClientPOJO> joinedPlayers = new ArrayList<>();

    public GameLobbyServerLogicThread(List<TransmissionMessage> messagesStack, List<PlayerClientPOJO> joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
        this.transmissionMessages = messagesStack;
    }

    //<editor-fold desc="Thread teardown methods - start game, abandon lobby etc.">
    public void terminate() {
        this.running = false;
    }

    public void sendStartGameSignal(){
        this.startGame = true;
        this.running = false;
    }

    //</editor-fold>

    public void run() {
        while (this.running) {
            checkIncomingMessages();
        }
    }

    private void checkIncomingMessages() {
        try {
            processIncomingMessages(transmissionMessages.get(0));
            transmissionMessages.remove(0);
        } catch (IndexOutOfBoundsException e){}
    }

    private void processIncomingMessages(JoinGameLobbyMessage joinGameLobbyMessage) {
        this.joinedPlayers.add(new PlayerClientPOJO(joinGameLobbyMessage.getComputerName(), joinGameLobbyMessage.getNickname()));
        //todo: inform other clients that new player joined game lobby

    }

    private void processIncomingMessages(OtherPlayerDataMessage otherPlayerDataMessage) {
            System.out.println("-> Instance received other player data message but is running as a server.");
    }

    private void processIncomingMessages(TransmissionMessage transmissionMessage) {

    }

}
