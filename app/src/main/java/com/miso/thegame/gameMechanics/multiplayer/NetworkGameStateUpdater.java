package com.miso.thegame.gameMechanics.multiplayer;

import android.graphics.Point;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.ReadyToPlayMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerDestroyedMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerHitMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerPositionData;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerShootProjectile;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.display.Animations.StaticAnimationManager;
import com.miso.thegame.gameMechanics.movingObjects.spells.OffensiveSpell;
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
                this.multiplayerViewInstance.getOtherPlayersManager().updatePlayerData((PlayerPositionData) transmissionMessage);
                break;

            //Shooting
            case "20":
                this.multiplayerViewInstance.getSpellManager().spellCreator
                        .fireProjectile(
                                ((PlayerShootProjectile) transmissionMessage).getFromPosition().x,
                                ((PlayerShootProjectile) transmissionMessage).getFromPosition().y,
                                ((PlayerShootProjectile) transmissionMessage).getMovementDelta().x,
                                ((PlayerShootProjectile) transmissionMessage).getMovementDelta().y,
                                CollisionObjectType.SpellEnemy
                        );
                break;

            //Hit by spell
            case "30":
                Iterator<OffensiveSpell> offensiveSpellIterator = this.multiplayerViewInstance.getSpellManager().getOffensiveSpellList().iterator();
                while (offensiveSpellIterator.hasNext()){
                    OffensiveSpell temp = offensiveSpellIterator.next();
                    if (temp.getIdentificator() == ((PlayerHitMessage)transmissionMessage).getProjectileId()){
                        StaticAnimationManager.addExplosion(new Point(temp.getX(), temp.getY()));
                        offensiveSpellIterator.remove();
                        break;
                    }
                }

                break;

            //Player defeated
            case "40":
                this.multiplayerViewInstance.getOtherPlayersManager().getOtherPlayers().remove(((PlayerDestroyedMessage) transmissionMessage).getNickname());
                //TODO: add animation.
                break;

            //Sync mess
            case "50":
                this.multiplayerViewInstance.getOtherPlayersManager().getOtherPlayers().get(((ReadyToPlayMessage) transmissionMessage).getNickname()).setIsReadyForNextFrame(true);
                break;
        }
    }
}
