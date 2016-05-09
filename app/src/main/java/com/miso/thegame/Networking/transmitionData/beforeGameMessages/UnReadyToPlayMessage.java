package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 29.04.2016.
 */
public class UnReadyToPlayMessage extends TransmissionMessage {

    private String nickname;

    public UnReadyToPlayMessage(String nickname){
        this.transmissionType = "05";
        this.nickname = nickname;
    }

    public String getPacket(){
        return this.transmissionType + "|" + this.getNickname() + "/r";
    }

    public String getNickname() {
        return this.nickname;
    }
}
