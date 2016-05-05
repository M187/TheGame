package com.miso.thegame.gameMechanics.multiplayer;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerPositionData;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerShootProjectile;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameViews.GamePanelMultiplayer;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Miso on 1.5.2016.
 */
public class NetworkGameStateUpdater {

    private volatile ArrayList<TransmissionMessage> recievedUpdates;
    private GamePanelMultiplayer multiplayerViewInstance;

    public NetworkGameStateUpdater(ArrayList<TransmissionMessage> recievedUpdates, GamePanelMultiplayer multiplayerViewInstance) {
        this.recievedUpdates = recievedUpdates;
        this.multiplayerViewInstance = multiplayerViewInstance;
    }

    public void processRecievedMessages() {

        Iterator messageIterator = this.recievedUpdates.iterator();
        while (messageIterator.hasNext()) {
            processEvent((TransmissionMessage) messageIterator.next());
            messageIterator.remove();
        }
    }

    private void processEvent(TransmissionMessage transmissionMessage) {
        switch (transmissionMessage.getTransmissionType()) {

            //Player position
            case "10":
                //TODO: update relevant player.
                this.multiplayerViewInstance.getOtherPlayerManager().updatePlayerData((PlayerPositionData) transmissionMessage);
                break;

            //Shooting
            case "20":
                this.multiplayerViewInstance.getSpellManager().spellCreator
                        .fireProjectile(
                                ((PlayerShootProjectile) transmissionMessage).getFromPosition().x,
                                ((PlayerShootProjectile) transmissionMessage).getFromPosition().y,
                                ((PlayerShootProjectile) transmissionMessage).getDelta().x,
                                ((PlayerShootProjectile) transmissionMessage).getDelta().y,
                                CollisionObjectType.SpellEnemy
                        );
                break;

            //Hit by spell
            case "30":
                //TODO: remove relevant projectile and add animation.

                break;

            //Player defeated
            case "40":
                //TODO: remove relevant player and add animation.
                break;

            //Sync mess
            case "50":
                //TODO: mark player as ready for next frame.
                break;
        }
    }
}
