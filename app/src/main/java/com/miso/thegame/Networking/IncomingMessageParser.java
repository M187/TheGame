package com.miso.thegame.Networking;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.DisbandGameMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.LeaveGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.OtherPlayerDataMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.StartGameMessage;

import java.net.InetAddress;


/**
 * Created by Miso on 22.4.2016.
 */
public class IncomingMessageParser {

    public TransmissionMessage unmarshalIncomingMessage(String recievedMessage, InetAddress clientAddress){

        switch ((recievedMessage.split("\\|"))[0]){
            case "01":
                return new JoinGameLobbyMessage(recievedMessage.split("\\|")[1], clientAddress.toString().substring(1));
            case "02":
                return new OtherPlayerDataMessage(recievedMessage.split("\\|")[1], recievedMessage.split("\\|")[2]);
            case "03":
                return new LeaveGameLobbyMessage(recievedMessage.split("\\|")[1], recievedMessage.split("\\|")[2]);
            case "04":
                return new StartGameMessage();
            case "05":
                return new DisbandGameMessage();

        }
        return null;
    }


}
