package com.miso.thegame.gameMechanics.multiplayer;

import android.view.View;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Miso on 1.5.2016.
 */
public class NetworkGameStateUpdater {

    private volatile ArrayList<TransmissionMessage> recievedUpdates;
    private View multiplayerViewInstance;

    public NetworkGameStateUpdater(ArrayList<TransmissionMessage> recievedUpdates, View multiplayerViewInstance){
        this.recievedUpdates = recievedUpdates;
        this.multiplayerViewInstance = multiplayerViewInstance;
    }

    public void processRecievedMessages(){

        Iterator messageIterator = this.recievedUpdates.iterator();
        while (messageIterator.hasNext()){
            processEvent((TransmissionMessage)messageIterator.next());
            messageIterator.remove();
        }
    }

    private void processEvent(TransmissionMessage transmissionMessage){
        switch (transmissionMessage.getTransmissionType()){
            case "10":
        }
    }
}
