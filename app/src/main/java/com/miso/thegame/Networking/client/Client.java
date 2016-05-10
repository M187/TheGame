package com.miso.thegame.Networking.client;

import android.os.AsyncTask;

import com.miso.thegame.Networking.PlayerClientPOJO;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by michal.hornak on 20.04.2016.
 * <p/>
 * Client class to communicate with other game instances.
 * Each registered server should have one instance ready to go.
 * When relevant event occurs, propagate it to every Client to send it.
 */
public class Client extends AsyncTask<TransmissionMessage, Void, Void> {

    // Server needs this information
    public boolean isReadyForGame = false;
    public LinkedBlockingQueue<TransmissionMessage> messagesToBeSent = new LinkedBlockingQueue<>();
    DataInputStream dataInputStream;
    String recievedFrameData;
    private Socket myClient;
    private String hostName;
    private String nickname;
    private int portNumber;
    private volatile boolean running = true;

    public Client(String nickname) {
        this.nickname = nickname;
    }

    public Client(String hostName, int portNumber, String nickname) {
        this.nickname = nickname;
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;

        Client client = (Client) o;

        return nickname.equals(client.nickname);
    }

    @Override
    public int hashCode() {
        return hostName.hashCode();
    }

    @Override
    public Void doInBackground(TransmissionMessage... a) {
        while (running) {
            try {
                if (this.myClient == null) {
                    this.myClient = new Socket(this.hostName, this.portNumber);
                    System.out.println(" - > Connection to server " + this.hostName + " established!");
                }
                sendDataToServer(messagesToBeSent.poll(30, TimeUnit.SECONDS));
            } catch (IOException e) {
                try {
                    this.myClient = new Socket(this.hostName, this.portNumber);
                    sendDataToServer(messagesToBeSent.poll(30, TimeUnit.SECONDS));
                } catch (IOException ex) {
                } catch (InterruptedException ex) {
                }
            } catch (InterruptedException e) {
            } catch (NullPointerException e){
                return null;
            }
        }
        return null;
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

    public void sendMessage(TransmissionMessage transmissionMessage) {
        this.messagesToBeSent.add(transmissionMessage);
    }

    /**
     * Sends data string message to defined server.
     *
     * @param messageData to be sent.
     */
    private void sendDataToServer(TransmissionMessage messageData) {
        try {
            DataOutputStream output = new DataOutputStream(this.myClient.getOutputStream());
            output.writeChars(messageData.getPacket());
            output.flush();
            System.out.println(" -- > Message sent: " + messageData.getPacket() + " Receiver address: " + this.hostName);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void teardown() {
        this.running = false;
        try {
            this.myClient.close();
        } catch (Exception e) {
        }
    }

    public PlayerClientPOJO getPlayerClientPojo() {
        return new PlayerClientPOJO(this.hostName, this.nickname);
    }

    public String getStringForExtras() {
        return (this.nickname + "|" + this.hostName + ":" + this.portNumber);
    }
}
