package com.miso.thegame.Networking.server;

import com.miso.thegame.Networking.PlayerClientPOJO;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.OtherPlayerDataMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by michal.hornak on 24.04.2016.
 * <p/>
 * Actual logic to be performed with the arriving messages.
 * <p/>
 * Each period check transmissionMessages and do actions based on its content.
 * Once message has been processed, pop it out.
 */

public class GameLobbyMessageProcessorThread implements Runnable {

    private volatile boolean running = true;
    private volatile boolean startGame = false;
    private volatile boolean serverMode = false;

    private SynchronousQueue<TransmissionMessage> transmissionMessages;
    private List<PlayerClientPOJO> joinedPlayers = new ArrayList<>();

    public GameLobbyMessageProcessorThread(SynchronousQueue<TransmissionMessage> messagesStack, List<PlayerClientPOJO> joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
        this.transmissionMessages = messagesStack;
    }

    //<editor-fold desc="Thread teardown methods - start game, abandon lobby etc.">
    public void terminate() {
        this.running = false;
        if (this.serverMode = true) {
            //todo: inform clients that lobby was dismantled by server(me).
        }
    }
    public void sendStartGameSignal(){
        this.startGame = true;
        this.running = false;
    }
    public void serverModeEnabled(){
        this.serverMode = true;
    }
    public void serverModeDisabled(){
        this.serverMode = false;
    }
    //</editor-fold>

    public void run() {
        while (this.running) {
            checkIncomingMessages();
        }
        if (this.startGame && this.serverMode){
            //todo: As a server you should send all registered clients "start game" message, and start actual game yourself.
        } else if (this.startGame && !this.serverMode){
            //todo: As a client, you should just start game as instructed by server.
        }
    }

    private void checkIncomingMessages() {
        try {
            processIncomingMessages(transmissionMessages.take());
        } catch (InterruptedException e){}
    }

    private void processIncomingMessages(JoinGameLobbyMessage joinGameLobbyMessage) {
        if (serverMode = true) {
            this.joinedPlayers.add(new PlayerClientPOJO(joinGameLobbyMessage.getComputerName(), joinGameLobbyMessage.getNickname()));
            //todo: inform other clients that new player joined game lobby
        } else {
            System.out.println("-> Instance received join request but not in server mode.");
        }
    }

    private void processIncomingMessages(OtherPlayerDataMessage otherPlayerDataMessage) {
        if (this.serverMode = true){
            System.out.println("-> Instance received other player data message but is running as a server.");
        } else {
            this.joinedPlayers.add(new PlayerClientPOJO(otherPlayerDataMessage.getComputerName(), otherPlayerDataMessage.getNickname()));
        }
    }

    private void processIncomingMessages(TransmissionMessage transmissionMessage) {

    }

}
