package com.miso.thegame.Networking.transmitionData.beforeGameMessages;

import com.miso.thegame.Networking.PlayerClientPOJO;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by Miso on 22.4.2016.
 */
public class OtherPlayerDataMessage extends TransmissionMessage {

    private String myComputerName;
    private String myNicnkname;

    public OtherPlayerDataMessage(PlayerClientPOJO playerClientData){
        this.myNicnkname = playerClientData.getId();
        this.myComputerName = playerClientData.getHostName();
    }

    public OtherPlayerDataMessage(String myNicnkname, String hostName){
        this.myNicnkname = myNicnkname;
        this.myComputerName = hostName;
    }

    public String getPacket(){
        return "02|" + this.myNicnkname + "|" + this.myComputerName;
    }

    public String getMyComputerName() {
        return myComputerName;
    }

    public String getMyNicnkname() {
        return myNicnkname;
    }
}
