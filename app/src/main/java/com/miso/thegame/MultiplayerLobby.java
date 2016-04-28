package com.miso.thegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.server.GameLobbyHostLogicExecutor;
import com.miso.thegame.Networking.server.Server;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.DisbandGameMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miso on 24.4.2016.
 */
public class MultiplayerLobby extends Activity {

    public static final int DEFAULT_COM_PORT = 12371;

    private volatile List<TransmissionMessage> transmissionMessages = new ArrayList<>();
    private Server server;
    private volatile List<Client> joinedPlayers = new ArrayList<>();

    private Client clientConnectionToServer;

    private boolean hosting = false;
    private boolean gameJoined = false;
    private boolean readyForGame = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.server = new Server(12371);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.server.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            this.server.execute();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.multiplayer_lobby_layout);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.server.terminate();
    }

    private void initHostSettings() {
        this.server.setMessageLogicExecutor(new GameLobbyHostLogicExecutor(joinedPlayers));
    }

    private void uninitHostSettings() {

        try {
            for (Client client : joinedPlayers) {
                client.execute(new DisbandGameMessage());
            }
        } catch (NullPointerException e) {
        }
        this.server.setMessageLogicExecutor(null);
        this.joinedPlayers = new ArrayList<>();
    }

    public void hostClick(View view) {
        if (this.hosting) {
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setText("Not hosting any game!");
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            ((Button) findViewById(R.id.button_host)).setText("HOST");
            (findViewById(R.id.button_join)).setEnabled(true);

            uninitHostSettings();
            this.hosting = false;
        } else {
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setText("Hosting Game!");
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            ((Button) findViewById(R.id.button_host)).setText("UN-HOST");
            (findViewById(R.id.button_join)).setEnabled(false);

            initHostSettings();
            this.hosting = true;
        }
    }

    public void joinClick(View view) {

        if (!gameJoined) {
            EditText iP = (EditText) findViewById(R.id.ip);
            EditText port = (EditText) findViewById(R.id.port);
            EditText nickName = (EditText) findViewById(R.id.player_nickname);
            (findViewById(R.id.player_nickname)).setEnabled(true);

            TransmissionMessage joinReq = new JoinGameLobbyMessage(nickName.getText().toString());

            System.out.println("---- > Trying to connect to " + iP.getText().toString());
            this.clientConnectionToServer = new Client(iP.getText().toString(), Integer.parseInt(port.getText().toString()), nickName.getText().toString());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                this.clientConnectionToServer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, joinReq);
            } else {
                this.clientConnectionToServer.execute(joinReq);
            }

            //todo: add check if client really joins game!
            (findViewById(R.id.button_join)).setEnabled(false);
            (findViewById(R.id.button_ready)).setEnabled(true);
            (findViewById(R.id.button_abandon)).setEnabled(true);
            (findViewById(R.id.button_host)).setEnabled(false);
            (findViewById(R.id.player_nickname)).setEnabled(false);
            this.gameJoined = true;
        }
    }

    public void readyClick(View view) {
        if (readyForGame) {
            ((Button) findViewById(R.id.button_ready)).setText("READY");
            //todo: send ready signal
            this.readyForGame = false;
        } else {
            ((Button) findViewById(R.id.button_ready)).setText("UN-READY");
            //todo send un-ready signal
            this.readyForGame = true;
        }
    }

    public void abandonClick(View view) {
        if (gameJoined) {
            (findViewById(R.id.button_join)).setEnabled(true);
            (findViewById(R.id.button_ready)).setEnabled(false);
            (findViewById(R.id.button_abandon)).setEnabled(false);
            (findViewById(R.id.button_host)).setEnabled(true);
            if (readyForGame) {
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
            if (joinedPlayers.get(i) != null) {
                editor.putString("Player" + i, joinedPlayers.get(i).getStringForExtras());
            } else {
                editor.putString("Player" + i, "free slot");
            }
        }
        editor.commit();
    }
}
