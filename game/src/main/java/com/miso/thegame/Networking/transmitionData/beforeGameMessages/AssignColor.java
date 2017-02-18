package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 2/18/2017.
 */

public class AssignColor extends TransmissionMessage {

    public int getColor() {
        return color;
    }

    private int color;

    public AssignColor(int color){
        this.transmissionType = "011";
        this.color = color;
    }

    @Override
    public String getPacket() {
        return this.transmissionType + "|" + String.valueOf(color);
    }
}
