package com.miso.thegame.Networking;

import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

import java.util.List;

/**
 * Created by michal.hornak on 28.04.2016.
 */
public abstract class MessageLogicExecutor {

    protected boolean isServerLogicProcessor;
    // Use this variable as a destination for our messages.
    private volatile List<Client> registeredPlayers;

    public boolean isServerLogicProcessor(){
        return this.isServerLogicProcessor;
    }

    public abstract void processIncomingMessage(TransmissionMessage transmissionMessage);
}
