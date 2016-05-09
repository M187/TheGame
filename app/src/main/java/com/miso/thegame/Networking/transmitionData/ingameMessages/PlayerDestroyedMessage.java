package com.miso.thegame.Networking.transmitionData.ingameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 03.05.2016.
 */
public class PlayerDestroyedMessage extends TransmissionMessage {

    private String nickname;

    public PlayerDestroyedMessage(String nickname) {
        this.transmissionType = "40";
        this.nickname = nickname;
    }

    public static PlayerDestroyedMessage unmarshal(String rawTransmissionString) {
        return new PlayerDestroyedMessage(
                rawTransmissionString.split("\\|")[1]);
    }

    public String getPacket() {
        return this.transmissionType + "|" + this.getNickname() + "/r";
    }

    public String getNickname() {
        return nickname;
    }
}
