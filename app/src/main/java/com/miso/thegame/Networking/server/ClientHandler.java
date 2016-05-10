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
 */
public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private InetAddress clientAddress;
    private volatile BlockingQueue<TransmissionMessage> messageHolder;
    private IncomingMessageParser incomingMessageParser = new IncomingMessageParser();

    public ClientHandler(Socket socket, BlockingQueue<TransmissionMessage> messageHolder) {
        this.clientSocket = socket;
        this.messageHolder = messageHolder;
        this.clientAddress = socket.getInetAddress();
    }

    @Override
    public void run() {
        String receivedMessage;
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            while (true) {
                receivedMessage = inFromClient.readLine();
                this.messageHolder.add(
                        this.incomingMessageParser.unmarshalIncomingMessage(receivedMessage, this.clientAddress));
                System.out.println(" -- > Received: " + receivedMessage + " from client: " + this.clientAddress);
            }
        } catch (IOException e) {
        }
    }
}
