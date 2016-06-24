package com.miso.thegame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.miso.thegame.Networking.server.Server;
import com.miso.thegame.Networking.server.logicExecutors.GameLobbyClientLogicExecutor;
import com.miso.thegame.Networking.server.logicExecutors.GameLobbyHostLogicExecutor;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.DisbandGameMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.LeaveGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.ReadyToPlayMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.StartGameMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.UnReadyToPlayMessage;
import com.miso.thegame.gameMechanics.ConstantHolder;

import java.util.ArrayList;

/**
 * Created by Miso on 24.4.2016.
 * <p/>
 * Activity managing Multiplayer Lobby layout.
 * <p/>
 * Spawns several threads:
 * <p/>
 * Server,
 * Client/s,
 * PlayerListUpdater
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println(" --> Calling onStop for Multiplayer lobby.");
        this.uninitLocalServerAndData();
    }

    @Override
    protected void onPause() {
        /**
         * Here should go uninit all threads not required for MultiplayerGame (all threads)
         */
        super.onPause();
        System.out.println(" --> Calling onPause for Multiplayer lobby.");
        this.playerListUpdater.terminate();
        this.uninitLocalServerAndData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.multiplayer_lobby_layout);
        this.uiStateHandler.unHostClickUiChanges();
        this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Default;

        System.out.println(" --> Calling onResume for Multiplayer lobby.");
        this.playerListUpdater = new PlayerListUpdater(this, registeredPlayers);
        this.playerListUpdater.start();
    }

    private void initHostServer() throws UnableToBindPortException {
        this.createAndPortToLocalServer();
        this.registeredPlayers.clear();
        this.sender = new Sender(this.registeredPlayers);
        this.server.setMessageLogicExecutor(new GameLobbyHostLogicExecutor(this.registeredPlayers, this.sender));
        this.startServerListener();
    }

    public void uninitLocalServerAndData() {
        try {
            this.server.terminate();
            this.server.setMessageLogicExecutor(null);
        } catch (NullPointerException e) {
        }
        this.registeredPlayers.clear();
    }

    private void initClientServer() throws UnableToBindPortException {
        this.createAndPortToLocalServer();
        this.registeredPlayers.clear();
        this.server.setMessageLogicExecutor(new GameLobbyClientLogicExecutor(this.registeredPlayers, this));
        startServerListener();
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
            try {
                initHostServer();
                this.myNickname = ((EditText) findViewById(R.id.player_nickname)).getText().toString();

                this.uiStateHandler.hostClickUiChanges();

                this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Hosting;
            } catch (UnableToBindPortException e) {
                //TODO: Inform player that server was unable to bind port.
                Log.d(ConstantHolder.TAG, "Port for server is already in use!");
            }
        }
    }

    public void joinClick(View view) {

        System.out.println(" --> Clicked on Joined button.");
        if (this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.Default) {
            try {
                initClientServer();

                this.myNickname = ((EditText) findViewById(R.id.player_nickname)).getText().toString();
                TransmissionMessage joinReq = new JoinGameLobbyMessage(this.myNickname);

                Client newC = (new Client(
                        ((EditText) findViewById(R.id.ip)).getText().toString(),
                        Integer.parseInt(((EditText) findViewById(R.id.port)).getText().toString()),
                        this.myNickname));
                startMyClient(newC);
                //Wait for connection.
                while (newC.isRunning() && !(newC.isConnectionEstablished())) {
                    //System.out.print(".");
                }
                System.out.println();

                if (newC.isConnectionEstablished()) {
                    this.clientConnectionToServer = newC;
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
            } catch (UnableToBindPortException e) {
                //TODO: notify player that port required to play game is occupied / try to establish connection on different port?
            }
        }
    }

    public void readyClick(View view) {
        if (lobbyState == MultiplayerLobbyStateHandler.LobbyState.Joined) {

            this.uiStateHandler.readyClickUiChanges();

            this.clientConnectionToServer.sendMessage(new ReadyToPlayMessage(this.myNickname));
            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.JoinedAndReadyForGame;

        } else if (lobbyState == MultiplayerLobbyStateHandler.LobbyState.JoinedAndReadyForGame) {

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

    public void startGame(View view) {

        if (this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.Hosting) {

            for (Client joinedPlayer : this.registeredPlayers) {
                if (!joinedPlayer.isReadyForGame) {
                    return;
                }
            }
            sender.sendMessage(new StartGameMessage());
            saveConnectedPlayers();
            setContentView(R.layout.loading_game);
            startActivity(new Intent(this, GameActivity.class)
                    .putExtra(OptionStrings.myNickname, this.myNickname)
                    .putExtra(OptionStrings.multiplayerInstance, "0"));
            this.uninitLocalServerAndData();
        } else {
            (findViewById(R.id.button_start)).setEnabled(false);
        }
    }

    public void saveConnectedPlayers() {

        SharedPreferences.Editor editor = getPreferences(0).edit();

        for (int i = 0; i < 8; i++) {
            try {
                // Debug part - inject artificial address.
//                if (i == 0) {
//                    editor.putString("Player" + i + "networkData", "test|10.0.2.2:12371");
//                } else
                if (registeredPlayers.get(i) != null) {
                    editor.putString("Player" + i + "networkData", this.registeredPlayers.get(i).getStringForExtras());
                } else {
                    editor.putString("Player" + i + "networkData", "free slot");
                }
            } catch (IndexOutOfBoundsException e) {
                editor.putString("Player" + i + "networkData", "free slot");
            }
        }
        editor.commit();
    }

    private void startMyClient(Client client) {
        client.start();
    }

    private void startServerListener() {
        this.server.start();
    }

    private void createAndPortToLocalServer() throws UnableToBindPortException {
        long timeoutStart = System.nanoTime() / 1000000;
        this.server = new Server(12371);

        while (!this.server.serverBindsPort && System.nanoTime() / 1000000 - timeoutStart < 30000) {
        }

        if (!this.server.serverBindsPort) {
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setText("Unable to bind port to server.!");
            ((TextView) findViewById(R.id.textinfo_hosting_game)).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            throw new UnableToBindPortException();
        }
    }

    private class UnableToBindPortException extends Throwable {
    }
}
