package com.miso.thegame.Networking.server;

import android.os.AsyncTask;

import com.miso.thegame.Networking.IncomingMessageParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by michal.hornak on 20.04.2016.
 *
 * Server runnable class. Should wait for received messages and process them.
 *
 * Both 'Client' and 'Host' will have server instance running and listening on socket.
 *
 * Difference will be in message processor they will have assigned.
 * 2 processors are used - client and server.
 * Since every player must support 1:n connections in game itself, i chose this approach. (P2P / non-Authoritative)
 * Though - in game lobby it is Host - Client connection only. Not any Client - Client connection. (Authoritative)
 */
public class Server extends AsyncTask<Void, Void, Void>{

    ServerSocket myService;
    private IncomingMessageParser incomingMessageParser = new IncomingMessageParser();
    private volatile MessageLogicExecutor messageLogicExecutor;
    private volatile boolean running = true;

    public Server(int port) {
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
        String receivedMessage;
        while (running) {
            try {
                Socket connectionSocket = this.myService.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                receivedMessage = inFromClient.readLine();
                // don't close socket in game communication?
                inFromClient.close();
                try {
                    messageLogicExecutor.processIncomingMessage(
                            this.incomingMessageParser.unmarshalIncomingMessage(receivedMessage, connectionSocket.getInetAddress()));
                } catch (NullPointerException e){}

                System.out.println(" --- > Received: " + receivedMessage);


            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setMessageLogicExecutor(MessageLogicExecutor messageLogicExecutor) {
        this.messageLogicExecutor = messageLogicExecutor;
    }
}
