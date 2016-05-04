package com.miso.thegame.Networking;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.DisbandGameMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.LeaveGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.OtherPlayerDataMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.StartGameMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerDestroyedMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerHitMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerPositionData;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerShootProjectile;

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
            case "08":
                return new DisbandGameMessage();
            case "10":
                return PlayerPositionData.unmarshal(recievedMessage);
            case "20":
                return PlayerShootProjectile.unmarshal(recievedMessage);
            case "30":
                return PlayerHitMessage.unmarshal(recievedMessage);
            case "40":
                return PlayerDestroyedMessage.unmarshal(recievedMessage);
            case "50":
                return null;
        }
        return null;
    }


}
