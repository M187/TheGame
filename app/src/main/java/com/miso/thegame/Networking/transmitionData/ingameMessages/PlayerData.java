package com.miso.thegame.Networking.transmitionData.ingameMessages;

import com.miso.thegame.Networking.transmitionData.Actions;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 20.04.2016.
 */
public class PlayerData extends TransmissionMessage {

    private int deltaX;
    private int deltaY;
    private Actions action;

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public Actions getAction() {
        return action;
    }

    public String getPacket(){
        return "Message not defined yet!";
    }
}
