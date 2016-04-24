package com.miso.thegame.Networking.server;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinRequestMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.OtherPlayerDataMessage;


/**
 * Created by Miso on 22.4.2016.
 */
public class MessageProcessor {

    public TransmissionMessage processIncomingMessage(String recievedMessages){

        switch (recievedMessages.split("|")[0]){
            case "01":
                return new JoinRequestMessage(recievedMessages.split("|")[1]);
            case "02":
                return new OtherPlayerDataMessage(recievedMessages.split("|")[1], recievedMessages.split("|")[2]);

        }
        return null;
    }


}
