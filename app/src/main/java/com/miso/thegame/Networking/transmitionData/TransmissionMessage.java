package com.miso.thegame.Networking.transmitionData;

/**
 * Types of messages: <br>
 *      --- <br>
 *      01 join message <br>
 *      02 other player join data <br>
 *      03 ready to play <br>
 *      04 start game <br>
 *      --- <br>
 *      05 UnReady to play <br>
 *      06 leave game <br>
 *      07 other player leave game <br>
 *      08 disband game <br>
 *      --- <br>
 */
public abstract class TransmissionMessage {

    protected String transmissionType = "00";

    //do it as an enum
    public String getTransmissionType(){
        return this.transmissionType;
    }

    public abstract String getPacket();
}
