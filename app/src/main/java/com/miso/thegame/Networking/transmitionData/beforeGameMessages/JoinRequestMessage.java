package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Created by Miso on 22.4.2016.
 *
 * Sends client to server in order to register itself to a game.
 */
public class JoinRequestMessage extends TransmissionMessage {

    private String myComputerName;
    private String myNicnkname;

    public JoinRequestMessage(String nickname){
        this.myNicnkname = nickname;
        try {
            //This may not be necessary. Find out IP at time when client joins server.
            this.myComputerName = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e){

        }
    }

    public String getPacket(){
        return "01|" + this.myNicnkname;
    }
}
