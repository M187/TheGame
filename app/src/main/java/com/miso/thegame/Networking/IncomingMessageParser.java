package com.miso.thegame.Networking;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.DisbandGameMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.LeaveGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.OtherPlayerDataMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.StartGameMessage;

import java.net.SocketAddress;


/**
 * Created by Miso on 22.4.2016.
 */
public class IncomingMessageParser {

    public TransmissionMessage processIncomingMessage(String recievedMessages, SocketAddress clientAddress){

        switch (recievedMessages.split("|")[0]){
            case "01":
                return new JoinGameLobbyMessage(recievedMessages.split("|")[1], clientAddress.toString());
            case "02":
                return new OtherPlayerDataMessage(recievedMessages.split("|")[1], recievedMessages.split("|")[2]);
            case "03":
                return new LeaveGameLobbyMessage(recievedMessages.split("|")[1], recievedMessages.split("|")[2]);
            case "04":
                return new StartGameMessage();
            case "05":
                return new DisbandGameMessage();

        }
        return null;
    }


}
