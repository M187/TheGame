package com.miso.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.miso.menu.multiplayer.FindMyIp;
import com.miso.menu.multiplayer.MultiplayerLobbyStateHandler;
import com.miso.menu.multiplayer.PlayerColors;
import com.miso.thegame.Networking.GameActivityMultiplayer;
import com.miso.thegame.GameData.OptionStrings;
import com.miso.thegame.Networking.NetworkConnectionConstants;
import com.miso.menu.multiplayer.PlayerListUpdater;
import com.miso.thegame.Networking.Sender;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.server.Server;
import com.miso.menu.multiplayer.GameLobbyClientLogicExecutor;
import com.miso.menu.multiplayer.GameLobbyHostLogicExecutor;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.DisbandGameMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.LeaveGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.PlayerChangeColor;
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

    public MultiplayerLobbyStateHandler.LobbyState lobbyState = MultiplayerLobbyStateHandler.LobbyState.Default;
    public volatile Server server;
    private Sender sender;
    private volatile ArrayList<Client> registeredPlayers = new ArrayList<>();
    private Client clientConnectionToServer;
    private MultiplayerLobbyStateHandler uiStateHandler;
    private PlayerListUpdater playerListUpdater;
    private PlayerColors mPlayerColors = new PlayerColors();
    public int myCurrentColor = 0;

    public MultiplayerLobbyStateHandler getUiStateHandler() {
        return uiStateHandler;
    }

    public PlayerColors getPlayerColors() {
        return mPlayerColors;
    }

    public Sender getSender() {
        return sender;
    }

    public ArrayList<Client> getRegisteredPlayers() {
        return registeredPlayers;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.multiplayer_lobby_layout);
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

        this.uiStateHandler.unHostClickUiChanges();
        ((TextView) findViewById(R.id.text_info_game_state_events)).setText(R.string.welcome_message);
        this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Default;

        System.out.println(" --> Calling onResume for Multiplayer lobby.");
        this.playerListUpdater = new PlayerListUpdater(this, registeredPlayers);
        this.playerListUpdater.start();
    }

    private void initHostServer() throws UnableToBindPortException {
        this.createAndPortToLocalServer();
        this.registeredPlayers.clear();
        this.sender = new Sender(this.registeredPlayers);
        this.server.setMessageLogicExecutor(new GameLobbyHostLogicExecutor(this));
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

            NetworkConnectionConstants.setPlayerNickname("");
            this.uiStateHandler.unHostClickUiChanges();

            sender.sendMessage(new DisbandGameMessage());
            uninitLocalServerAndData();
            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Default;
        } else {
            try {
                initHostServer();

                NetworkConnectionConstants.setPlayerNickname(((EditText) this.findViewById(R.id.player_nickname)).getText().toString());
                this.uiStateHandler.hostClickUiChanges();

                this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Hosting;
            } catch (UnableToBindPortException e) {
                //TODO: Inform player that server was unable to Bind port.
                Log.d(ConstantHolder.TAG, "Port for server is already in use!");
            }
        }
    }

    public void joinClick(View view) {
        System.out.println(" --> Clicked on Joined button.");
        if (this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.Default) {
            try {
                initClientServer();

                NetworkConnectionConstants.setPlayerNickname(((EditText) findViewById(R.id.player_nickname)).getText().toString());
                TransmissionMessage joinReq = new JoinGameLobbyMessage(NetworkConnectionConstants.getPlayerNickname());

                Client newC = (new Client(
                        ((EditText) findViewById(R.id.ip)).getText().toString(),
                        Integer.parseInt(((EditText) findViewById(R.id.port)).getText().toString()),
                        NetworkConnectionConstants.getPlayerNickname(),
                        0));
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
                } else {
                    ((TextView) findViewById(R.id.text_info_game_state_events)).setText(R.string.join_message_fail);
                    ((TextView) findViewById(R.id.text_info_game_state_events)).setTextColor(getResources().getColor(android.R.color.holo_red_dark));

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

            this.clientConnectionToServer.sendMessage(new ReadyToPlayMessage(NetworkConnectionConstants.getPlayerNickname()));
            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.JoinedAndReadyForGame;

        } else if (lobbyState == MultiplayerLobbyStateHandler.LobbyState.JoinedAndReadyForGame) {

            this.uiStateHandler.unReadyClickChanges();
            this.clientConnectionToServer.sendMessage(new UnReadyToPlayMessage(NetworkConnectionConstants.getPlayerNickname()));
            this.lobbyState = MultiplayerLobbyStateHandler.LobbyState.Joined;
        }
    }

    public void abandonClick(View view) {

        if (this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.Joined || this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.JoinedAndReadyForGame) {

            this.uiStateHandler.abandonClickUiEvents();

            if (this.lobbyState == MultiplayerLobbyStateHandler.LobbyState.JoinedAndReadyForGame) {
                LeaveGameLobbyMessage leaveGameLobbyMessage = new LeaveGameLobbyMessage(NetworkConnectionConstants.getPlayerNickname());
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
            Intent startingIntent = new Intent(this, GameActivityMultiplayer.class)
                    .putExtra(OptionStrings.myNickname, NetworkConnectionConstants.getPlayerNickname())
                    .putExtra(OptionStrings.multiplayerInstance, "0");
            saveConnectedPlayers(startingIntent);
            setContentView(R.layout.loading_game);
            startActivity(startingIntent);

            this.uninitLocalServerAndData();
            this.finish();
        } else {
            (findViewById(R.id.button_main_2)).setEnabled(false);
        }
    }

    public void saveConnectedPlayers(Intent intent) {

        for (int i = 0; i < 8; i++) {
            try {
                // Debug part - inject artificial address.
//                if (i == 0) {
//                    intent.putExtra("Player" + i + "networkData", "test|10.0.2.2:12371");
//                } else
                if (registeredPlayers.get(i) != null) {
                    intent.putExtra("Player" + i + "networkData", this.registeredPlayers.get(i).getStringForExtras());
                } else {
                    intent.putExtra("Player" + i + "networkData", "free slot");
                }
            } catch (IndexOutOfBoundsException e) {
                intent.putExtra("Player" + i + "networkData", "free slot");
            }
        }
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
            ((TextView) findViewById(R.id.text_info_game_state_events)).setText(R.string.port_not_available);
            ((TextView) findViewById(R.id.text_info_game_state_events)).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            throw new UnableToBindPortException();
        }
    }

    private class UnableToBindPortException extends Throwable {
    }

    public void informOfColorChange(int newColor){
        switch (this.lobbyState) {
            case Joined:
                this.clientConnectionToServer.sendMessage(new PlayerChangeColor(newColor, NetworkConnectionConstants.getPlayerNickname()));
                break;
            case Hosting:
                this.sender.sendMessage(new PlayerChangeColor(newColor, NetworkConnectionConstants.getPlayerNickname()));
                break;
        }
    }

    public void getMyIp(View view){
        new FindMyIp(this).execute();
    }
}
