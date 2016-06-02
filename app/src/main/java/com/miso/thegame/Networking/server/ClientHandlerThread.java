package com.miso.thegame.Networking.server;

import com.miso.thegame.Networking.IncomingMessageParser;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by michal.hornak on 10.05.2016.
 *
 * Thread designed to handle incoming messages from other clients.
 * Server opens socket and forward it to this thread. Afterwards it start this thread.
 *
 * All data arriving from client are handled in this thread.
 * Each client will spawn one thread during connection phase.
 *
 * Arriving data are parsed after receiving and pushed into BlockingQueue.
 * Messages are then processed further with logic executors which poll for new messages.
 */
public class ClientHandlerThread extends Thread{

    private Socket clientSocket;
    private InetAddress clientAddress;
    private volatile BlockingQueue<TransmissionMessage> messageHolder;
    private IncomingMessageParser incomingMessageParser = new IncomingMessageParser();
    private boolean running = false;

    public ClientHandlerThread(Socket socket, BlockingQueue<TransmissionMessage> messageHolder) {
        this.clientSocket = socket;
        this.messageHolder = messageHolder;
        this.clientAddress = socket.getInetAddress();
    }

    @Override
    public void run() {
        String receivedMessage;
        this.running = true;
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream(), "UTF-8"));
            while (this.running) {
                receivedMessage = inFromClient.readLine();
                if (receivedMessage.startsWith("04") | receivedMessage.startsWith("08")){
                    this.clientSocket.close();
                    this.running = false;
                    this.messageHolder.add(
                            this.incomingMessageParser.unmarshalIncomingMessage(receivedMessage, this.clientAddress));
                    System.out.println(" -- > Received: " + receivedMessage + " from client: " + this.clientAddress);
                } else {
                    this.messageHolder.add(
                            this.incomingMessageParser.unmarshalIncomingMessage(receivedMessage, this.clientAddress));
                    System.out.println(" -- > Received: " + receivedMessage + " from client: " + this.clientAddress);
                }
            }
        } catch (IOException e) { //todo: lost connection with client.
            try {
                this.clientSocket.close();
            } catch (IOException e1) {}
            this.running = false;
        }
        catch (NullPointerException es){}
    }
}
