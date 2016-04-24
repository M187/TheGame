package com.miso.thegame.Networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by michal.hornak on 20.04.2016.
 *
 * Server runnable class. Should wait for received messages and save them into Synch. Que.
 */
public class Server implements Runnable{

    ServerSocket myService;
    SynchronousQueue messageHolder;
    MessageProcessor messageProcessor = new MessageProcessor();

    public Server(int port, SynchronousQueue messageHolder) {
        this.messageHolder = messageHolder;
        try {
            this.myService = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void run(){
        this.listen();
    }

    /**
     * Method to listen for incoming messages and connections. Should be used in its own thread.
     */
    private void listen() {
        String receivedMessage;

        while (true) {
            try {
                Socket connectionSocket = this.myService.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                receivedMessage = inFromClient.readLine();
                this.messageHolder.add(messageProcessor.processIncomingMessage(receivedMessage));

                System.out.println("Received: " + receivedMessage);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
