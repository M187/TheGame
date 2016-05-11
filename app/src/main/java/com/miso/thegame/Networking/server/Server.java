package com.miso.thegame.Networking.server;

import android.os.AsyncTask;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by michal.hornak on 20.04.2016.
 * <p/>
 * Server runnable class. Should wait for received messages and process them.
 * <p/>
 * Both 'Client' and 'Host' will have server instance running and listening on socket.
 * <p/>
 * Difference will be in message processor they will have assigned.
 * 2 processors are used - client and server.
 * Since every player must support 1:n connections in game itself, i chose this approach. (P2P / non-Authoritative)
 * Though - in game lobby it is Host - Client connection only. Not any Client - Client connection. (Authoritative)
 */
public class Server extends AsyncTask<Void, Void, Void> {

    ServerSocket myService;
    private volatile MessageLogicExecutor messageLogicExecutor;
    private volatile BlockingQueue<TransmissionMessage> receivedMessages = new LinkedBlockingDeque<>();
    private volatile boolean running = true;
    private Thread messageLogicExecutorThread;

    public Server(int port) {
        try {
            this.myService = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void terminate() {
        this.messageLogicExecutorThread.interrupt();
        try {
            this.messageLogicExecutorThread.join();
        } catch (InterruptedException e) {}
        this.running = false;
    }

    /**
     * Method to listen for incoming messages and connections. Should be used in its own thread.
     */
    public Void doInBackground(Void... params) {
        while (running) try {
            Socket connectionSocket = this.myService.accept();
            System.out.println(" - > Connection established with: " + connectionSocket.getInetAddress() + " Thread will be crated to handle this connection. (Only incoming messages are accepted)");
            //new Thread(new ClientHandler(connectionSocket, this.receivedMessages)).start();
            (new ClientHandler(connectionSocket, this.receivedMessages)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.myService.close();
        } catch (IOException e) {}
        return null;
    }

    /**
     * Method to set and run Thread for message logic executor.
     * This object should process incoming transmissions and process them - perform game state changes.
     *
     * @param messageLogicExecutor to be used in thread
     */
    public void setMessageLogicExecutor(MessageLogicExecutor messageLogicExecutor) {

        this.messageLogicExecutor = messageLogicExecutor;
        if (this.messageLogicExecutorThread != null) {
            try{
                this.messageLogicExecutorThread.interrupt();
                this.messageLogicExecutorThread.join();
            } catch (InterruptedException e) {
            }
        }

        this.messageLogicExecutorThread = (new Thread() {
            private MessageLogicExecutor messageLogicExecutor;
            public void run() {
                while (!this.isInterrupted()) {
                    try {
                        messageLogicExecutor.processIncomingMessage(receivedMessages.take());
                    } catch (InterruptedException e) {
                    }
                }
            }
            private Thread init(MessageLogicExecutor messageLogicExecutor) {
                this.messageLogicExecutor = messageLogicExecutor;
                return this;
            }
        }.init(messageLogicExecutor));

        this.messageLogicExecutorThread.start();
    }
}
