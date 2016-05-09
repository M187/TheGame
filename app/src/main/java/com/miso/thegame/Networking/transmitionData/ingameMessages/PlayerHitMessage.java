package com.miso.thegame.Networking.transmitionData.ingameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 03.05.2016.
 */
public class PlayerHitMessage extends TransmissionMessage {

    private String nickname;
    private String projectileId;

    public PlayerHitMessage(String nickname, String projectileId) {
        this.transmissionType = "30";
        this.nickname = nickname;
        this.projectileId = projectileId;
    }

    public static PlayerHitMessage unmarshal(String rawTransmissionString) {
        return new PlayerHitMessage(
                rawTransmissionString.split("\\|")[1],
                rawTransmissionString.split("\\|")[2]);
    }

    public String getPacket() {
        return this.transmissionType + "|" + this.nickname + "|" + this.projectileId;
    }

    public String getProjectileId() {
        return projectileId;
    }
}