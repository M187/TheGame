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
import com.miso.thegame.Networking.PlayerListUpdater;
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
 *
 * Activity managing Multiplayer Lobby layout.
 *
 * Spawns several threads:
 *
 *  Server,
 *  Client/s,
 *  PlayerListUpdater
 *
 *
 */
public class MultiplayerLobby extends Activity {

    public static final int DEFAULT_COM_PORT = 12371;
    public static volatile String myNickname = null;
    public MultiplayerLobbyStateHandler.LobbyState lobbyState = MultiplayerLobbyStateHandler.LobbyState.Default;
    public volatile Server server;
    private Sender sender;
    private volatile ArrayList<Client> registeredPlayers = new ArrayList<>();
    private Client clientConnectionToServer;
    private MultiplayerLobbyStateHandler uiStateHandler;
    private PlayerListUpdater playerListUpdater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerListUpdater = new PlayerListUpdater(this, registeredPlayers);
        this.uiStateHandler = new MultiplayerLobbyStateHandler(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.multiplayer_lobby_layout);
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println(" --> Calling onStop for Multiplayer lobby.");
        this.uninitLocalServerAndData();
    }

    @Override
    protected void onPause(){
        /**
         * Here should go uninit all threads not required for MultiplayerGame (all threads)
         */
        super.onPause();
        System.out.println(" --> Calling onPause for Multiplayer lobby.");
        this.playerListUpdater.terminate();
        this.uninitLocalServerAndData();
    }

    @Override
    protected void onResume(){
        super.onResume();

        this.uiStateHandler.unHostClickUiChanges();
        this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Default;

        System.out.println(" --> Calling onResume for Multiplayer lobby.");
        this.playerListUpdater = new PlayerListUpdater(this, registeredPlayers);
        this.playerListUpdater.start();
    }

    private void initHostServer() {
        this.server = new Server(12371);
        this.registeredPlayers.clear();
        this.sender = new Sender(this.registeredPlayers);
        this.server.setMessageLogicExecutor(new GameLobbyHostLogicExecutor(this.registeredPlayers, this.sender));
        this.executeServerListener();
    }

    public void uninitLocalServerAndData() {
        this.server.terminate();
        this.server.setMessageLogicExecutor(null);
        this.registeredPlayers.clear();
    }

    private void initClientServer(){
        this.server = new Server(12371);
        this.registeredPlayers.clear();
        this.server.setMessageLogicExecutor(new GameLobbyClientLogicExecutor(this.registeredPlayers, this));
        executeServerListener();
    }

    public void hostClick(View view) {

        System.out.println(" --> Clicked on Host/Unhost button.");

        if (this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.Hosting) {

            this.myNickname = "";
            this.uiStateHandler.unHostClickUiChanges();

            sender.sendMessage(new DisbandGameMessage());
            uninitLocalServerAndData();
            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Default;
        } else {
            this.myNickname = ((EditText) findViewById(R.id.player_nickname)).getText().toString();
            this.uiStateHandler.hostClickUiChanges();

            initHostServer();

            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Hosting;
        }
    }

    public void joinClick(View view) {

        System.out.println(" --> Clicked on Joined button.");
        if (this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.Default) {
            initClientServer();

            this.myNickname = ((EditText) findViewById(R.id.player_nickname)).getText().toString();
            TransmissionMessage joinReq = new JoinGameLobbyMessage(this.myNickname);

            Client newC = (new Client(
                    ((EditText) findViewById(R.id.ip)).getText().toString(),
                    Integer.parseInt(((EditText) findViewById(R.id.port)).getText().toString()),
                    this.myNickname));
            executeMyClient(newC);
            //Wait for connection.
            while (newC.isRunning() && !(newC.isConnectionEstablished())){
                //System.out.print(".");
            }
            System.out.println();

            if (newC.isConnectionEstablished()) {
                this.clientConnectionToServer.sendMessage(joinReq);
                this.uiStateHandler.joinClickUiEvents();
                this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Joined;
                ((TextView) findViewById(R.id.textinfo_hosting_game)).setText("Join successful!");
                ((TextView) findViewById(R.id.textinfo_hosting_game)).setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                ((TextView) findViewById(R.id.textinfo_hosting_game)).setText("Join unsuccessful!");
                ((TextView) findViewById(R.id.textinfo_hosting_game)).setTextColor(getResources().getColor(android.R.color.holo_red_dark));

                uninitLocalServerAndData();
                this.clientConnectionToServer = null;
            }
        }
    }

    public void readyClick(View view) {
        if (lobbyState == MultiplayerLobbyStateHandler.LobbyState.Joined) {

            this.uiStateHandler.readyClickUiChanges();

            this.clientConnectionToServer.sendMessage(new ReadyToPlayMessage(this.myNickname));
            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.JoinedAndReadyForGame;

        } else if (lobbyState == MultiplayerLobbyStateHandler.LobbyState.JoinedAndReadyForGame){

            this.uiStateHandler.unReadyClickChanges();
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
            uninitLocalServerAndData();
            this.clientConnectionToServer.terminate();
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
            saveConnectedPlayers();
            startActivity(new Intent(this, GameActivity.class)
                    .putExtra(OptionStrings.multiplayerInstance, this.myNickname)
                    .putExtra(OptionStrings.multiplayerInstance, true));
            this.uninitLocalServerAndData();
        } else {
            (findViewById(R.id.button_start)).setEnabled(false);
        }
    }

    public void saveConnectedPlayers() {

        SharedPreferences.Editor editor = getPreferences(0).edit();
        for (int i = 0; i < 8; i++) {
            try {
                if (registeredPlayers.get(i) != null) {
                    editor.putString("Player" + i + "networkData", this.registeredPlayers.get(i).getStringForExtras());
                } else {
                    editor.putString("Player" + i + "networkData", "free slot");
                }
            } catch (IndexOutOfBoundsException e){
                editor.putString("Player" + i + "networkData", "free slot");
            }
        }
        editor.commit();
    }

    private void executeMyClient(Client client){
        this.clientConnectionToServer = client;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.clientConnectionToServer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            this.clientConnectionToServer.execute();
        }
    }

    private void executeServerListener(){
        // Start server only when host/join game?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.server.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            this.server.execute();
        }
    }
}
