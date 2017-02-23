package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.PlayerClientPOJO;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by Miso on 22.4.2016.
 *
 */
public class OtherPlayerDataMessage extends TransmissionMessage {

    private String computerName;
    private String nickname;
    private int color;

    public OtherPlayerDataMessage(PlayerClientPOJO playerClientData){
        this.transmissionType = "02";
        this.nickname = playerClientData.getId();
        this.computerName = playerClientData.getHostName();
        this.color = playerClientData.getColor();
    }

    public OtherPlayerDataMessage(String nickname, String hostName, int color){
        this.transmissionType = "02";
        this.nickname = nickname;
        this.computerName = hostName;
        this.color = color;
    }

    public String getPacket(){
        return this.transmissionType + "|" + this.nickname + "|" + this.computerName + "|" + getColor();
    }

    public String getComputerName() {
        return computerName;
    }

    public String getNickname() {
        return nickname;
    }

    public int getColor() {
        return color;
    }
}
