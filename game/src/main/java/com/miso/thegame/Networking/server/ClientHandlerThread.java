package com.miso.thegame.Networking.server;

import android.util.Log;

import com.miso.thegame.Networking.IncomingMessageParser;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.gameMechanics.ConstantHolder;

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

    private volatile Socket clientSocket;
    private InetAddress clientAddress;
    private volatile BlockingQueue<TransmissionMessage> messageHolder;
    private IncomingMessageParser incomingMessageParser = new IncomingMessageParser();
    private volatile boolean running = false;

    public ClientHandlerThread(Socket socket, BlockingQueue<TransmissionMessage> messageHolder) {
        this.clientSocket = socket;
        this.messageHolder = messageHolder;
        this.clientAddress = socket.getInetAddress();
    }

    public void terminate(){
        this.running = false;
        try {
            this.clientSocket.close();
        } catch (IOException e){
            Log.d(ConstantHolder.TAG, "Connection lost with :" + this.clientAddress);
        }
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
                    Log.d(ConstantHolder.TAG, " -- > Received: " + receivedMessage + " from client: " + this.clientAddress);
                } else {
                    this.messageHolder.add(
                            this.incomingMessageParser.unmarshalIncomingMessage(receivedMessage, this.clientAddress));
                    Log.d(ConstantHolder.TAG, " -- > Received: " + receivedMessage + " from client: " + this.clientAddress);
                }
            }
        } catch (IOException e) {
            this.running = false;
            Log.d(ConstantHolder.TAG, "Connection lost with :" + this.clientAddress);
        }
        catch (NullPointerException es){}
    }
}
