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
import com.miso.thegame.Networking.MultiplayerLobbyStateHandler;
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
    public MultiplayerLobbyStateHandler.LobbyState lobbyState = MultiplayerLobbyStateHandler.LobbyState.Default;
    public volatile String myNickname = null;
    private Server server;
    private Sender sender;
    private volatile ArrayList<Client> registeredPlayers = new ArrayList<>();
    private Client clientConnectionToServer;
    private MultiplayerLobbyStateHandler uiStateHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.uiStateHandler = new MultiplayerLobbyStateHandler(this);

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
        this.sender = new Sender(this.registeredPlayers);
        this.server.setMessageLogicExecutor(new GameLobbyHostLogicExecutor(this.registeredPlayers, sender));
    }

    private void uninitHostSettings() {
        try {
            for (Client client : registeredPlayers) {
                client.execute(new DisbandGameMessage());
            }
        } catch (NullPointerException e) {
        }
        this.server.setMessageLogicExecutor(null);
        this.registeredPlayers = new ArrayList<>();
    }

    public void hostClick(View view) {
        if (this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.Hosting) {
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setText("Not hosting any game!");
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            ((Button) findViewById(R.id.button_host)).setText("HOST");
            (findViewById(R.id.button_join)).setEnabled(true);
            (findViewById(R.id.button_start)).setEnabled(false);

            sender.sendMessage(new DisbandGameMessage());
            uninitHostSettings();
            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Default;
        } else {
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setText("Hosting Game!");
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            ((Button) findViewById(R.id.button_host)).setText("UN-HOST");
            (findViewById(R.id.button_join)).setEnabled(false);
            (findViewById(R.id.button_start)).setEnabled(true);

            initHostSettings();
            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Hosting;
        }
    }

    public void joinClick(View view) {

        if (this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.Default) {

            EditText iP = (EditText) findViewById(R.id.ip);
            EditText port = (EditText) findViewById(R.id.port);
            EditText nickName = (EditText) findViewById(R.id.player_nickname);

            TransmissionMessage joinReq = new JoinGameLobbyMessage(nickName.getText().toString());
            this.myNickname = nickName.getText().toString();

            Client newC = (new Client(iP.getText().toString(), Integer.parseInt(port.getText().toString()), this.myNickname));
            executeMyClient(newC);
            this.registeredPlayers.add(newC);

            this.clientConnectionToServer.sendMessage(joinReq);
            this.server.setMessageLogicExecutor(new GameLobbyClientLogicExecutor(this.registeredPlayers, this));

            //todo: add check if client really joins game!??
            this.uiStateHandler.joinClickUiEvents();
            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Joined;
        }
    }

    public void readyClick(View view) {
        if (lobbyState == MultiplayerLobbyStateHandler.LobbyState.Joined) {

            ((Button) findViewById(R.id.button_ready)).setText("UN-READY");
            this.clientConnectionToServer.sendMessage(new ReadyToPlayMessage(this.myNickname));
            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.JoinedAndReadyForGame;

        } else if (lobbyState == MultiplayerLobbyStateHandler.LobbyState.JoinedAndReadyForGame){

            ((Button) findViewById(R.id.button_ready)).setText("READY");
            this.clientConnectionToServer.sendMessage(new UnReadyToPlayMessage(this.myNickname));
            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Joined;
        }
    }

    public void abandonClick(View view) {

        if (this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.Joined || this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.JoinedAndReadyForGame) {

            this.uiStateHandler.abdandonClickUiEvents();

            if (this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.JoinedAndReadyForGame) {
                ((Button) findViewById(R.id.button_ready)).setText("READY");

                LeaveGameLobbyMessage leaveGameLobbyMessage = new LeaveGameLobbyMessage(this.myNickname);
                this.clientConnectionToServer.sendMessage(leaveGameLobbyMessage);
            }
            this.server.setMessageLogicExecutor(null);
            this.clientConnectionToServer.teardown();
            this.clientConnectionToServer = null;
            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Default;
        }
    }

    public void startGame(View view){

        if (this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.Hosting){

            for (Client joinedPlayer : this.registeredPlayers){
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
                if (registeredPlayers.get(i) != null) {
                    editor.putString("Player" + i + "networkData", registeredPlayers.get(i).getStringForExtras());
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
        this.registeredPlayers = new ArrayList<>();
        return this.registeredPlayers;
    }

    private void executeMyClient(Client client){
        this.clientConnectionToServer = client;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.clientConnectionToServer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            this.clientConnectionToServer.execute();
        }
    }

}
