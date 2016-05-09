package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 25.04.2016.
 */
public class StartGameMessage extends TransmissionMessage {

    public StartGameMessage(){
        this.transmissionType = "04";
    }

    public String getPacket(){ return this.transmissionType + "/r"; }
}
