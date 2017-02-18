package com.miso.menu.multiplayer;

import android.app.Activity;
import android.content.Intent;

import com.miso.menu.MultiplayerLobby;
import com.miso.thegame.Networking.GameActivityMultiplayer;
import com.miso.thegame.GameData.OptionStrings;
import com.miso.thegame.Networking.NetworkConnectionConstants;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.server.logicExecutors.MessageLogicExecutor;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.AssignColor;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.LeaveGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.OtherPlayerDataMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.StartGameMessage;
import com.miso.menu.R;

import java.util.List;

/**
 * Created by michal.hornak on 29.04.2016.
 * <p/>
 * Actual logic to be performed with the arriving messages.
 * <p/>
 * Class should be assigned to a local server to process incoming massages. <br>
 * This class should be used for LOBBY CLIENT instance. <br>
 */
public class GameLobbyClientLogicExecutor extends MessageLogicExecutor {

    private volatile List<Client> registeredPlayers;
    // watch out for multithreading!!
    private volatile MultiplayerLobby multiplayerLobby;

    public GameLobbyClientLogicExecutor(List<Client> registeredPlayers, MultiplayerLobby multiplayerLobby) {
        this.registeredPlayers = registeredPlayers;
        this.isServerLogicProcessor = false;
        this.multiplayerLobby = multiplayerLobby;
    }

    @Override
    public void processIncomingMessage(TransmissionMessage transmissionMessage) throws StartGameException, DisbandGameException{

        System.out.println(transmissionMessage.getPacket());
        switch (transmissionMessage.getTransmissionType()) {

            // Other player joining game
            case "02":
                this.registeredPlayers.add(
                        new Client(
                                ((OtherPlayerDataMessage) transmissionMessage).getComputerName(),
                                NetworkConnectionConstants.DEFAULT_COM_PORT,
                                ((OtherPlayerDataMessage) transmissionMessage).getNickname()));
                break;

            //Start game signal
            case "04":
                this.multiplayerLobby.saveConnectedPlayers();
                this.multiplayerLobby.runOnUiThread((new Runnable() {
                    private Activity multiplayerLobby;
                    public Runnable init(Activity multiplayerLobby){
                        this.multiplayerLobby = multiplayerLobby;
                        return this;
                    }
                    @Override
                    public void run(){multiplayerLobby.setContentView(R.layout.loading_game);}
                }).init(this.multiplayerLobby));
                this.multiplayerLobby.startActivity(
                        new Intent(this.multiplayerLobby.getApplicationContext(), GameActivityMultiplayer.class)
                                .putExtra(OptionStrings.myNickname, NetworkConnectionConstants.getPlayerNickname())
                                .putExtra(OptionStrings.multiplayerInstance, ((StartGameMessage) transmissionMessage).getIndexOfPlayer())
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                throw new StartGameException();

            // Disbanding game
            case "08":
                this.multiplayerLobby.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        multiplayerLobby.abandonClick(multiplayerLobby.findViewById(R.id.multiplayer_lobby_layout));
                    }
                });
                throw new DisbandGameException();

            // Other player leaving game.
            case "06":
                this.registeredPlayers.remove(
                        new Client(((LeaveGameLobbyMessage) transmissionMessage).getNickname()));
                break;

            //-------- Colors --------

            //Assigning color
            case "011":
                this.multiplayerLobby.getPlayerColors().setMyColor(((AssignColor) transmissionMessage).getColor());
        }
    }
}
