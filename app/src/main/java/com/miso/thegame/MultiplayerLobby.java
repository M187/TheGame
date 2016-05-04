package com.miso.thegame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miso.thegame.GameData.OptionStrings;
import com.miso.thegame.Networking.Sender;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.server.GameLobbyClientLogicExecutor;
import com.miso.thegame.Networking.server.GameLobbyHostLogicExecutor;
import com.miso.thegame.Networking.server.Server;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.DisbandGameMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.LeaveGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.ReadyToPlayMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.StartGameMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.UnReadyToPlayMessage;

import java.util.ArrayList;

/**
 * Created by Miso on 24.4.2016.
 */
public class MultiplayerLobby extends Activity {

    public static final int DEFAULT_COM_PORT = 12371;
    public LobbyState lobbyState = LobbyState.Default;

    private Server server;
    private Sender sender;
    private volatile ArrayList<Client> joinedPlayers = new ArrayList<>();

    private Client clientConnectionToServer;
    public volatile String myNickname = null;

    public enum LobbyState{
        Default,
        Joined,
        JoinedAndReadyForGame,
        Hosting
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start server only when host/join game?
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
        resetAndGetJoinedPlayersList();
        this.sender = new Sender(this.joinedPlayers);
        this.server.setMessageLogicExecutor(new GameLobbyHostLogicExecutor(this.joinedPlayers, sender));
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
        if (this.lobbyState == LobbyState.Hosting) {
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setText("Not hosting any game!");
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            ((Button) findViewById(R.id.button_host)).setText("HOST");
            (findViewById(R.id.button_join)).setEnabled(true);
            (findViewById(R.id.button_start)).setEnabled(false);

            sender.sendMessage(new DisbandGameMessage());
            uninitHostSettings();
            this.lobbyState = LobbyState.Default;
        } else {
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setText("Hosting Game!");
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            ((Button) findViewById(R.id.button_host)).setText("UN-HOST");
            (findViewById(R.id.button_join)).setEnabled(false);
            (findViewById(R.id.button_start)).setEnabled(true);

            initHostSettings();
            this.lobbyState = LobbyState.Hosting;
        }
    }

    public void joinClick(View view) {

        if (this.lobbyState == LobbyState.Default) {
            EditText iP = (EditText) findViewById(R.id.ip);
            EditText port = (EditText) findViewById(R.id.port);
            EditText nickName = (EditText) findViewById(R.id.player_nickname);
            (findViewById(R.id.player_nickname)).setEnabled(true);

            TransmissionMessage joinReq = new JoinGameLobbyMessage(nickName.getText().toString());
            this.myNickname = nickName.getText().toString();

            System.out.println("---- > Trying to connect to " + iP.getText().toString());
            this.clientConnectionToServer = new Client(iP.getText().toString(), Integer.parseInt(port.getText().toString()), this.myNickname);

            sendMessageViaExecutorUsingMyClient(joinReq);

            this.server.setMessageLogicExecutor(new GameLobbyClientLogicExecutor(this.joinedPlayers, this));

            //todo: add check if client really joins game!??
            (findViewById(R.id.button_join)).setEnabled(false);
            (findViewById(R.id.button_ready)).setEnabled(true);
            (findViewById(R.id.button_abandon)).setEnabled(true);
            (findViewById(R.id.button_host)).setEnabled(false);
            (findViewById(R.id.player_nickname)).setEnabled(false);
            this.lobbyState = LobbyState.Joined;
        }
    }

    public void readyClick(View view) {
        if (lobbyState == LobbyState.Joined) {
            ((Button) findViewById(R.id.button_ready)).setText("READY");
            sendMessageViaExecutorUsingMyClient(new ReadyToPlayMessage(this.myNickname));
            this.lobbyState = LobbyState.JoinedAndReadyForGame;
        } else if (lobbyState == LobbyState.JoinedAndReadyForGame){
            ((Button) findViewById(R.id.button_ready)).setText("UN-READY");
            sendMessageViaExecutorUsingMyClient(new UnReadyToPlayMessage(this.myNickname));
            this.lobbyState = LobbyState.Joined;
        }
    }

    public void abandonClick(View view) {
        if (this.lobbyState == LobbyState.Joined || this.lobbyState == LobbyState.JoinedAndReadyForGame) {
            (findViewById(R.id.button_join)).setEnabled(true);
            (findViewById(R.id.button_ready)).setEnabled(false);
            (findViewById(R.id.button_abandon)).setEnabled(false);
            (findViewById(R.id.button_host)).setEnabled(true);
            if (this.lobbyState == LobbyState.JoinedAndReadyForGame) {
                ((Button) findViewById(R.id.button_ready)).setText("READY");

                LeaveGameLobbyMessage leaveGameLobbyMessage = new LeaveGameLobbyMessage(this.myNickname);
                sendMessageViaExecutorUsingMyClient(leaveGameLobbyMessage);
            }
            this.server.setMessageLogicExecutor(null);
            this.clientConnectionToServer.teardown();
            this.clientConnectionToServer = null;
            this.lobbyState = LobbyState.Default;
        }
    }

    public void startGame(View view){
        if (this.lobbyState == LobbyState.Hosting){
            for (Client joinedPlayer : this.joinedPlayers){
                if (!joinedPlayer.isReadyForGame){
                    return;
                }
            }
            sender.sendMessage(new StartGameMessage());
            saveConnectedPlayerDataAndStuff();
            startActivity(new Intent(this, GameActivity.class)
                    .putExtra(OptionStrings.multiplayerInstance, true)
                    .putExtra(OptionStrings.multiplayerInstance, this.myNickname));
        } else {
            (findViewById(R.id.button_start)).setEnabled(false);
        }
    }

    public void saveConnectedPlayerDataAndStuff() {
        SharedPreferences.Editor editor = getPreferences(0).edit();
        for (int i = 0; i < 8; i++) {
            try {
                if (joinedPlayers.get(i) != null) {
                    editor.putString("Player" + i + "networkData", joinedPlayers.get(i).getStringForExtras());
                } else {
                    editor.putString("Player" + i + "networkData", "free slot");
                }
            } catch (IndexOutOfBoundsException e){
                editor.putString("Player" + i + "networkData", "free slot");
            }
        }
        editor.commit();
    }

    private ArrayList<Client> resetAndGetJoinedPlayersList(){
        this.joinedPlayers = new ArrayList<>();
        return this.joinedPlayers;
    }

    private void sendMessageViaExecutorUsingMyClient(TransmissionMessage transmissionMessage){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.clientConnectionToServer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, transmissionMessage);
        } else {
            this.clientConnectionToServer.execute(transmissionMessage);
        }
    }
}
