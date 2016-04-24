package com.miso.thegame;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import com.miso.thegame.Networking.Client;
import com.miso.thegame.Networking.PlayerClientPOJO;
import com.miso.thegame.Networking.server.Server;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinRequestMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Miso on 24.4.2016.
 */
public class MultiplayerLobby extends Activity{

    private SynchronousQueue<TransmissionMessage> transmissionMessages = new SynchronousQueue<>();
    private Server server;
    private Thread serverThread;
    private List<PlayerClientPOJO> registeredClients = new ArrayList<>();
    private Client client;


    private boolean startGame = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startServerThread(12371);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.multiplayer_lobby_layout);


    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return super.onOptionsItemSelected(menuItem);
    }

    private void startServerThread(int port){
        this.server = new Server(port, this.transmissionMessages);
        this.serverThread = new Thread(this.server);
        serverThread.start();
    }

    public void joinClick(){
        EditText iP = (EditText)findViewById(R.id.ip);
        EditText port = (EditText)findViewById(R.id.port);
        EditText nickName = (EditText)findViewById(R.id.player_nickname);

        TransmissionMessage joinReq = new JoinRequestMessage(nickName.getText().toString());

        new Client(iP.getText().toString(), Integer.parseInt(port.getText().toString())).sendDataToServer(joinReq);
    }

    /**
     * Actual logic to be performed with the arriving messages.

     * Each period check transmissionMessages and do actions based on its content.
     * Once message has been processed, pop it out.
     */
    private class ServerLogicThread implements Runnable{

        public void run(){}
    }
}
