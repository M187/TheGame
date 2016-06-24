package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 25.04.2016.
 */
public class StartGameMessage extends TransmissionMessage {

    private int indexNumber;

    public StartGameMessage() {
        this.transmissionType = "04";
    }

    public StartGameMessage(int indexNumber) {
        this.transmissionType = "04";
        this.indexNumber = indexNumber;
    }

    public static StartGameMessage unmarshal(String rawTransmissionString) {
        return new StartGameMessage(
            Integer.parseInt(rawTransmissionString.split("\\|")[1])
        );
    }

    public void setIndexForPlayer(int indexForPlayer) {
        this.indexNumber = indexForPlayer;
    }

    public String getIndexOfPlayer(){
        return Integer.toString(this.indexNumber);
    }

    public String getPacket() {
        return this.transmissionType + "|" + this.indexNumber;
    }
}
