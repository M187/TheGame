package com.miso.thegame.Networking;

import com.miso.thegame.Networking.transmitionData.PlayerData;
import com.miso.thegame.Networking.transmitionData.ProjectileEventData;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by michal.hornak on 20.04.2016.
 * <p/>
 * Client class to communicate with other game instances.
 * Each registered server should have one instance ready to go.
 * When relevant event occurs, propagate it to every Client to send it.
 */
public class Client {

    Socket myClient;
    DataInputStream dataInputStream;
    String recievedFrameData;

    public Client(String hostname, int portNumber) {
        try {
            this.myClient = new Socket(hostname, portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void getDataFromServer() {

        try {
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(this.myClient.getInputStream()));
            this.recievedFrameData = inFromServer.readLine();
        } catch (IOException e) {
            this.dataInputStream = null;
            System.out.println(e);
        }
    }

    /**
     * Sends player data string message to defined server.
     * Should occur every frame to propagate this player position.
     * @param playerData ...
     */
    public void sendPlayerDataToServer(PlayerData playerData) {
        sendString(Integer.toString(playerData.getDeltaX()) + ":" + Integer.toString(playerData.getDeltaY()) + ":" + playerData.getAction().toString());
    }

    /**
     * Sends projectile data to defined server.
     * Should occur each time this player shoots / creates projectile object.
     * @param projectileEventData ...
     */
    public void sendProjectileData(ProjectileEventData projectileEventData) {

    }

    public void sendString(String string) {
        try {
            DataOutputStream output = new DataOutputStream(this.myClient.getOutputStream());
            output.writeChars(string);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
