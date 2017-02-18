package com.miso.thegame.Networking.transmitionData;

/**
 * Types of messages: <br>
 *      --- --- ---<br>
 *      Before game: <br>
 *      --- --- --- <br>
 *      01 join message <br>
 *      02 other player join data <br>
 *      03 ready to play <br>
 *      04 start game <br>
 *      011 assign color <br>
 *      --- <br>
 *      05 UnReady to play <br>
 *      06 leave game <br>
 *      07 other player leave game <br> - NOT NEEDED!
 *      08 disband game <br>
 *      --- --- ---<br>
 *      In-game: <br>
 *      --- --- --- <br>
 *      10 player move/position <br>
 *      20 player shoot projectile <br>
 *      30 player hit by projectile <br>
 *      40 player destroyed <br>
 *      50 sync message - game loop finished <br>
 */
public abstract class TransmissionMessage {

    protected String transmissionType = "00";

    //do it as an enum
    public String getTransmissionType(){
        return this.transmissionType;
    }

    public abstract String getPacket();

}
