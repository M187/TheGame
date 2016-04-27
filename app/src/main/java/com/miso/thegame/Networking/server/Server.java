package com.miso.thegame.Networking.server;

import android.os.AsyncTask;

import com.miso.thegame.Networking.MessageProcessor;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by michal.hornak on 20.04.2016.
 *
 * Server runnable class. Should wait for received messages and save them into Synch. Que.
 */
public class Server extends AsyncTask<Void, Void, Void>{

    ServerSocket myService;
    private volatile List<TransmissionMessage>  messageHolder;
    MessageProcessor messageProcessor = new MessageProcessor();
    private volatile boolean running = true;

    public Server(int port, List<TransmissionMessage> messageHolder) {
        this.messageHolder = messageHolder;
        try {
            this.myService = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void terminate(){
        this.running = false;
    }

    /**
     * Method to listen for incoming messages and connections. Should be used in its own thread.
     */
    public Void doInBackground(Void...params) {
        while (running) {
            String receivedMessage;
            try {
                Socket connectionSocket = this.myService.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                receivedMessage = inFromClient.readLine();
                inFromClient.close();
                try {
                    this.messageHolder.add(messageProcessor.processIncomingMessage(receivedMessage, connectionSocket.getRemoteSocketAddress()));
                } catch (NullPointerException e){}

                System.out.println(" --- > Received: " + receivedMessage);


            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
