package com.miso.thegame.Networking;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.AssignColor;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.DisbandGameMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.LeaveGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.OtherPlayerDataMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.PlayerChangeColor;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.ReadyToPlayMessage;
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
            case "011":
                return new AssignColor(Integer.parseInt(recievedMessage.split("\\|")[1]));
            case "012":
                return new PlayerChangeColor(Integer.parseInt(recievedMessage.split("\\|")[1]), recievedMessage.split("\\|")[2]);
            case "01":
                return new JoinGameLobbyMessage(recievedMessage.split("\\|")[1], clientAddress.toString().substring(1));
            case "02":
                return new OtherPlayerDataMessage(recievedMessage.split("\\|")[1], recievedMessage.split("\\|")[2], Integer.valueOf(recievedMessage.split("\\|")[3]));
            case "03":
                return new ReadyToPlayMessage(recievedMessage.split("\\|")[1]);
            case "04":
                return StartGameMessage.unmarshal(recievedMessage);
            case "06":
                return new LeaveGameLobbyMessage(recievedMessage.split("\\|")[1]);
            case "08":
                return new DisbandGameMessage();
            case "10":
                return PlayerPositionData.unmarshal(recievedMessage);
            case "20":
                return PlayerShootProjectile.unmarshal(recievedMessage);
            case "21":
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
