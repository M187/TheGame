package com.miso.thegame.Networking.transmitionData.ingameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 03.05.2016.
 */
public class PlayerHitMessage extends TransmissionMessage {

    public PlayerHitMessage() {
        this.transmissionType = "30";
    }

    public String getPacket() {
        return this.transmissionType;
    }
}