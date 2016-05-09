package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 29.04.2016.
 */
public class ReadyToPlayMessage extends TransmissionMessage {

    private String nickname;

    public ReadyToPlayMessage(String nickname){
        this.transmissionType = "03";
        this.nickname = nickname;
    }

    public String getPacket(){
        return this.transmissionType + "|" + this.getNickname() + "/r";
    }

    public String getNickname() {
        return this.nickname;
    }
}
