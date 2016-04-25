package com.miso.thegame.Networking.transmitionData;

/**
 * Created by michal.hornak on 21.04.2016.
 *
 * types of message:
 *      01 join message
 *      02 other player data
 *      03 ready to play
 *      04 start game
 *
 */
public abstract class TransmissionMessage {

    protected String transactionType = "00";

    //do it as an enum
    public String getTransmissionType(){
        return this.transactionType;
    }

    public abstract  String getPacket();
}
