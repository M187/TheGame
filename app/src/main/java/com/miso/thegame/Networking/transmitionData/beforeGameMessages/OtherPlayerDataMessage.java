package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.PlayerClientPOJO;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by Miso on 22.4.2016.
 * todo: redundant message?
 */
public class OtherPlayerDataMessage extends TransmissionMessage {

    private String computerName;
    private String nickname;

    public OtherPlayerDataMessage(PlayerClientPOJO playerClientData){
        this.nickname = playerClientData.getId();
        this.computerName = playerClientData.getHostName();
    }

    public OtherPlayerDataMessage(String nickname, String hostName){
        this.nickname = nickname;
        this.computerName = hostName;
    }

    public String getPacket(){
        return "02|" + this.nickname + "|" + this.computerName;
    }

    public String getComputerName() {
        return computerName;
    }

    public String getNickname() {
        return nickname;
    }
}
