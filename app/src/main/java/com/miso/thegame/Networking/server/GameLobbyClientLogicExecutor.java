package com.miso.thegame.Networking.server;

import android.content.Intent;

import com.miso.thegame.GameActivity;
import com.miso.thegame.GameData.OptionStrings;
import com.miso.thegame.MultiplayerLobby;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.JoinGameLobbyMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.OtherPlayerDataMessage;
import com.miso.thegame.R;

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
                                MultiplayerLobby.DEFAULT_COM_PORT,
                                ((OtherPlayerDataMessage) transmissionMessage).getNickname()));
                break;

            //Start game signal
            case "04":
                this.multiplayerLobby.uninitLocalServerAndData();
                this.multiplayerLobby.saveConnectedPlayers();
                this.multiplayerLobby.startActivity(
                        new Intent(this.multiplayerLobby.getApplicationContext(), GameActivity.class)
                                .putExtra(OptionStrings.multiplayerInstance, this.multiplayerLobby.myNickname)
                                .putExtra(OptionStrings.multiplayerInstance, true)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                //// TODO: 16.05.2016 instruct server to stop.
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
            case "07":
                this.registeredPlayers.remove(
                        new Client(((JoinGameLobbyMessage) transmissionMessage).getComputerName(),
                                MultiplayerLobby.DEFAULT_COM_PORT,
                                ((JoinGameLobbyMessage) transmissionMessage).getNickname()));
                break;
        }
    }
}
