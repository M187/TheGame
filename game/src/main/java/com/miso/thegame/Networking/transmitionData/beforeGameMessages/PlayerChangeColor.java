package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 2/23/2017.
 */

public class PlayerChangeColor extends TransmissionMessage {

    public int getColor() {
        return color;
    }

    private int color;

    public String getNickname() {
        return nickname;
    }

    private String nickname;

    public PlayerChangeColor(int color, String nickname){
        this.transmissionType = "012";
        this.color = color;
        this.nickname = nickname;
    }

    @Override
    public String getPacket() {
        return this.transmissionType + "|" + String.valueOf(color) + "|" + getNickname();
    }
}
