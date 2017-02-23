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
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.PlayerChangeColor;
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
    public void processIncomingMessage(TransmissionMessage transmissionMessage) throws StartGameException, DisbandGameException {

        System.out.println(transmissionMessage.getPacket());
        switch (transmissionMessage.getTransmissionType()) {

            // Other player joining game
            case "02":
                this.multiplayerLobby.getPlayerColors().makeColorUnavailable(new PlayerColors.MyColor(((OtherPlayerDataMessage) transmissionMessage).getColor()));
                this.registeredPlayers.add(
                        new Client(
                                ((OtherPlayerDataMessage) transmissionMessage).getComputerName(),
                                NetworkConnectionConstants.DEFAULT_COM_PORT,
                                ((OtherPlayerDataMessage) transmissionMessage).getNickname(),
                                ((OtherPlayerDataMessage) transmissionMessage).getColor()));
                break;

            // Start game signal
            case "04":

                Intent mIntent = new Intent(this.multiplayerLobby.getApplicationContext(), GameActivityMultiplayer.class);

                this.multiplayerLobby.saveConnectedPlayers(mIntent);

                this.multiplayerLobby.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        multiplayerLobby.setContentView(R.layout.loading_game);
                    }
                });

                mIntent.putExtra(OptionStrings.myNickname, NetworkConnectionConstants.getPlayerNickname())
                        .putExtra(OptionStrings.multiplayerInstance, ((StartGameMessage) transmissionMessage).getIndexOfPlayer())
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                this.multiplayerLobby.startActivity(mIntent);
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
                Client temp = new Client(((LeaveGameLobbyMessage) transmissionMessage).getNickname());

                this.multiplayerLobby.getPlayerColors().makeColorAvailable(
                        new PlayerColors.MyColor(
                                this.registeredPlayers.get(
                                        this.registeredPlayers.indexOf(temp)
                                ).mColor));
                this.registeredPlayers.remove(temp);
                break;

            //-------- Colors --------

            // Assigning color
            case "011":
                this.multiplayerLobby.getUiStateHandler().setMyColor(((AssignColor) transmissionMessage).getColor());
                break;

            // Other player change color
            case "012":
                Client temp2 = this.registeredPlayers.get(
                        this.registeredPlayers.indexOf(
                                new Client(((PlayerChangeColor) transmissionMessage).getNickname())
                        )
                );
                // old color is available again. New one is not. Make color change to relevant player.
                this.multiplayerLobby.getPlayerColors().makeColorAvailable(new PlayerColors.MyColor(temp2.mColor));
                this.multiplayerLobby.getPlayerColors().makeColorUnavailable(new PlayerColors.MyColor(((PlayerChangeColor) transmissionMessage).getColor()));
                temp2.mColor = ((PlayerChangeColor) transmissionMessage).getColor();
        }
    }
}
