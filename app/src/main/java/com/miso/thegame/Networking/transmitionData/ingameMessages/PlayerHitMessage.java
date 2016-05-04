package com.miso.thegame.Networking.transmitionData.ingameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 03.05.2016.
 */
public class PlayerHitMessage extends TransmissionMessage {

    private String nickname;

    public PlayerHitMessage(String nickname) {
        this.transmissionType = "30";
        this.nickname = nickname;
    }

    public static PlayerHitMessage unmarshal(String rawTransmissionString) {
        return new PlayerHitMessage(
                rawTransmissionString.split("\\|")[1]);
    }

    public String getPacket() {
        return this.transmissionType + "|" + this.nickname;
    }
}