package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 25.04.2016.
 */
public class LeaveGameLobbyMessage extends TransmissionMessage {

    private String computerName = "";
    private String nickname;

    public LeaveGameLobbyMessage(String nickname) {
        this.transmissionType = "06";
        this.nickname = nickname;
    }

    public LeaveGameLobbyMessage(String nickname, String hostName) {
        this.transmissionType = "06";
        this.computerName = hostName;
        this.nickname = nickname;
    }

    public String getPacket() {
        return this.transmissionType + "|" + this.getNickname() + "/r";
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getComputerName() {
        return computerName;
    }
}


