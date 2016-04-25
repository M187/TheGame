package com.miso.thegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miso.thegame.Networking.PlayerClientPOJO;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.server.GameLobbyServerLogicThread;
import com.miso.thegame.Networking.server.Server;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Miso on 24.4.2016.
 */
public class MultiplayerLobby extends Activity {

    private SynchronousQueue<TransmissionMessage> transmissionMessages = new SynchronousQueue<>();
    private Server server;
    private Thread serverThread, GameLobbyServerLogicThread, clientThread;
    private List<PlayerClientPOJO> registeredClients = new ArrayList<>();

    private Client clientConnectionToServer;

    private GameLobbyServerLogicThread lobbyServer = new GameLobbyServerLogicThread(transmissionMessages, registeredClients);

    private boolean hosting = false;
    private boolean gameJoined = false;
    private boolean readyForGame = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.GameLobbyServerLogicThread = new Thread(this.lobbyServer);
        startServerThread(12371);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.multiplayer_lobby_layout);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            this.server.terminate();
            this.serverThread.join();
            this.lobbyServer.terminate();
            this.GameLobbyServerLogicThread.join();
        } catch (InterruptedException e) {
        }
        saveConnectedPlayerData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return super.onOptionsItemSelected(menuItem);
    }

    private void startServerThread(int port) {
        this.server = new Server(port, this.transmissionMessages);
        this.serverThread = new Thread(this.server);
        serverThread.start();
    }

    public void hostClick(View view){
        if (this.hosting){
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setText("Not hosting any game!");
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            ((Button) findViewById(R.id.button_host)).setText("HOST");

            ((Button) findViewById(R.id.button_join)).setEnabled(true);

            try {
                this.lobbyServer.terminate();
                this.GameLobbyServerLogicThread.join();
            } catch (InterruptedException e){}

            this.hosting = false;
        } else {
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setText("Hosting Game!");
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            ((Button) findViewById(R.id.button_host)).setText("UN-HOST");

            ((Button) findViewById(R.id.button_join)).setEnabled(false);

            this.GameLobbyServerLogicThread.start();

            this.hosting = true;
        }
    }

    public void joinClick(View view) {

        if (!gameJoined) {
            EditText iP = (EditText) findViewById(R.id.ip);
            EditText port = (EditText) findViewById(R.id.port);
            EditText nickName = (EditText) findViewById(R.id.player_nickname);

            TransmissionMessage joinReq = new JoinGameLobbyMessage(nickName.getText().toString());

            System.out.println("---- > Trying to connect to " + iP.getText().toString());
            this.clientConnectionToServer = new Client(iP.getText().toString(), Integer.parseInt(port.getText().toString()));
            this.clientConnectionToServer.execute(joinReq);

            //todo: add check if client really joins game!
            ((Button) findViewById(R.id.button_join)).setEnabled(false);
            ((Button) findViewById(R.id.button_ready)).setEnabled(true);
            ((Button) findViewById(R.id.button_abandon)).setEnabled(true);
            ((Button) findViewById(R.id.button_host)).setEnabled(false);
            this.gameJoined = true;
        }
    }

    public void readyClick(View view){
        if (readyForGame){
            ((Button) findViewById(R.id.button_ready)).setText("READY");
            //todo: send ready signal
            this.readyForGame = false;
        } else {
            ((Button) findViewById(R.id.button_ready)).setText("UN-READY");
            //todo send un-ready signal
            this.readyForGame = true;
        }
    }

    public void abandonClick(View view){
        if (gameJoined) {
            ((Button) findViewById(R.id.button_join)).setEnabled(true);
            ((Button) findViewById(R.id.button_ready)).setEnabled(false);
            ((Button) findViewById(R.id.button_abandon)).setEnabled(false);
            ((Button) findViewById(R.id.button_host)).setEnabled(true);
            if (readyForGame){
                ((Button) findViewById(R.id.button_ready)).setText("READY");
                //todo: send ready signal
                this.readyForGame = false;
            }

            this.clientConnectionToServer.teardown();
            this.clientConnectionToServer = null;
            this.gameJoined = false;
        }
    }

    public void saveConnectedPlayerData() {
        SharedPreferences.Editor editor = getPreferences(0).edit();

        for (int i = 0; i < 8; i++) {
            if (registeredClients.get(i) != null) {
                editor.putString("Player" + i, registeredClients.get(i).getStringForExtras());
            } else {
                editor.putString("Player" + i, "free slot");
            }
        }
        editor.commit();
    }
}
