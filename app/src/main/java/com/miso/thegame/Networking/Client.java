package com.miso.thegame.Networking;

import com.miso.thegame.Networking.transmitionData.PlayerData;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by michal.hornak on 20.04.2016.
 */
public class Client {

    Socket myClient;
    DataInputStream dataInputStream;
    String recievedFrameData;

    public Client(String hostname, int portNumber){
        try {
            this.myClient = new Socket(hostname, portNumber);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    public void getDataFromServer(){

        try {
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(this.myClient.getInputStream()));
            this.recievedFrameData = inFromServer.readLine();
        }
        catch (IOException e) {
            this.dataInputStream = null;
            System.out.println(e);
        }
    }

    public void sendPlayerDataToServer(PlayerData playerData){
        try {
            DataOutputStream output = new DataOutputStream(this.myClient.getOutputStream());

            output.writeChars(
                    Integer.toString(playerData.getDeltaX()) + ":" +
                    Integer.toString(playerData.getDeltaY()) + ":" +
                    playerData.getAction().toString());
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

}
