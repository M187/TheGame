package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by Miso on 22.4.2016.
 *
 * Sends client to server in order to register itself to a game.
 */
public class JoinGameLobbyMessage extends TransmissionMessage {

    private String computerName = "";
    private String nickname;

    public JoinGameLobbyMessage(String nickname){
        this.transactionType = "01";
        this.nickname = nickname;
    }

    public JoinGameLobbyMessage(String nickname, String hostName){
        this.transactionType = "01";
        this.computerName = hostName;
        this.nickname = nickname;
    }



    public String getPacket(){
        return this.transactionType + "|" + this.getNickname();
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getComputerName() {
        return computerName;
    }
}
