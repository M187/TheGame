package com.miso.thegame.Networking;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.gameViews.GamePanelMultiplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miso on 22.4.2016.
 */
public class Sender {

    public static ArrayList<Client> registeredPlayers = new ArrayList<>();

    public Sender(List<PlayerClientPOJO> registeredPlayers){
        for (PlayerClientPOJO playerClientData: registeredPlayers){
            this.registeredPlayers.add(new Client(playerClientData.getHostName(), GamePanelMultiplayer.PORT));
        }
    }

    /**
     * Iterate through all clients and sends them defined message.
     * todo: assign thread to each sending?
     * @param message
     */
    public static void send(TransmissionMessage message){
        for (Client client: registeredPlayers){
            client.sendDataToServer(message);
        }
    }
}
