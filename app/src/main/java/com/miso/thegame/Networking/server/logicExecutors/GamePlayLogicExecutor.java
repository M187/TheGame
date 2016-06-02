package com.miso.thegame.Networking.server.logicExecutors;

import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.ReadyToPlayMessage;

import java.util.ArrayList;

/**
 * Created by michal.hornak on 03.05.2016.
 */
public class GamePlayLogicExecutor extends MessageLogicExecutor {

    private volatile ArrayList<TransmissionMessage> recievedMessages;
    private volatile ArrayList<Client> joinedPlayers;

    public GamePlayLogicExecutor(ArrayList<TransmissionMessage> recievedMessages, ArrayList<Client> joinedPlayers){
        this.recievedMessages = recievedMessages;
        this.joinedPlayers = joinedPlayers;
    }

    public void processIncomingMessage(TransmissionMessage transmissionMessage){

        switch (transmissionMessage.getTransmissionType()){
            // Player ready for game.
            case "03":
                this.joinedPlayers.get(
                        this.joinedPlayers.indexOf(new Client(((ReadyToPlayMessage) transmissionMessage).getNickname())))
                        .isReadyForGame = true;
                break;
            default:
                this.recievedMessages.add(transmissionMessage);
        }
    }
}
