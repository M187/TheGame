package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 25.04.2016.
 */
public class DisbandGameMessage extends TransmissionMessage {

    public String getPacket(){
        return "05";
    }
}
