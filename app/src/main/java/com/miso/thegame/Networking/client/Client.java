package com.miso.thegame.Networking.client;

import android.os.AsyncTask;
import android.util.Log;

import com.miso.thegame.Networking.PlayerClientPOJO;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.gameMechanics.ConstantHolder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by michal.hornak on 20.04.2016.
 * <p/>
 * Client class to communicate with other game instances.
 * Each registered server should have one instance ready to go.
 * When relevant event occurs, propagate it to every Client to send it.
 */
public class Client extends AsyncTask<Void, Void, Void> {

    // Server needs this information
    public volatile boolean isReadyForGame = false;
    public LinkedBlockingQueue<TransmissionMessage> messagesToBeSent = new LinkedBlockingQueue<>();
    private Socket myClient;
    private String hostName;
    private String nickname;
    private int portNumber;
    private volatile boolean running = true;
    private volatile boolean isConnectionEstablished = false;
    private boolean isGameClient = false;

    public Client(String nickname) {
        this.nickname = nickname;
    }

    public Client(String hostName, int portNumber, String nickname) {
        this.nickname = nickname;
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    public Client(String hostName, int portNumber, String nickname, boolean isGameClient) {
        this.nickname = nickname;
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.isGameClient = isGameClient;
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
    public Void doInBackground(Void... a) {
        try {
            createConnection();
            while (running) try {
                sendDataToServer(this.messagesToBeSent.take());
            } catch (InterruptedException e) { }
        } catch (IOException e) {
            Log.d(ConstantHolder.TAG, "Can't initialize client to connect to server!");
            running = false;
            e.printStackTrace();
        }
        return null;
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
            PrintWriter outputWriter = new PrintWriter(this.myClient.getOutputStream(), true);
            outputWriter.println(messageData.getPacket());
            //outputWriter.flush();
            Log.d(ConstantHolder.TAG, " -- > Message sent: " + messageData.getPacket() + " Receiver address: " + this.hostName);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void terminate() {
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

    /**
     * Create connection.
     * If client is for game purpose, try periodically to create it so that other server has time to start.
     * @throws IOException
     */
    public void createConnection() throws IOException{
        // Timeout for connection
        boolean repeat = true;
        long timeoutStart = System.nanoTime() / 1000000;

        while (repeat) {
            repeat = false;
            try {

                this.myClient = new Socket();
                this.myClient.connect(new InetSocketAddress(this.hostName, this.portNumber), 3000);

                System.out.println(" -- > Connection to server " + this.hostName + " established!");
                this.isConnectionEstablished = true;

            } catch (IOException e){
                if (!isGameClient){ throw e; }
            }
            if (isGameClient && (System.nanoTime()/1000000) - timeoutStart < 30000 && !this.isConnectionEstablished){
                repeat = true;
            } else if (isGameClient && !this.isConnectionEstablished){
                throw new IOException();
            }
        }
    }

    public boolean isConnectionEstablished() {
        return isConnectionEstablished;
    }

    public boolean isRunning() {
        return running;
    }

    public String getNickname() {
        return nickname;
    }
}
