package com.miso.thegame.gameMechanics.multiplayer;

import android.graphics.Point;

import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.ReadyToPlayMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerDestroyedMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerHitMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerPositionData;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerShootFreezingProjectile;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerShootProjectile;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.display.Animations.StaticAnimationManager;
import com.miso.thegame.gameMechanics.gameViews.GamePanelMultiplayer;
import com.miso.thegame.gameMechanics.movingObjects.spells.OffensiveSpell;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Miso on 1.5.2016.
 */
public class NetworkGameStateUpdater {

    private volatile ArrayList<TransmissionMessage> recievedUpdates;
    private GamePanelMultiplayer multiplayerViewInstance;

    public NetworkGameStateUpdater(GamePanelMultiplayer multiplayerViewInstance) {
        this.recievedUpdates = multiplayerViewInstance.getArrivingMessagesList();
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

            //Other player position
            case "10":
                this.multiplayerViewInstance.getOtherPlayersManager().updatePlayerData((PlayerPositionData) transmissionMessage);
                break;

            //Other player shooting
            case "20":
                this.multiplayerViewInstance.getSpellManager().spellCreator
                        .fireProjectile(
                                ((PlayerShootProjectile) transmissionMessage).getFromPosition().x,
                                ((PlayerShootProjectile) transmissionMessage).getFromPosition().y,
                                ((PlayerShootProjectile) transmissionMessage).getMovementDelta().x,
                                ((PlayerShootProjectile) transmissionMessage).getMovementDelta().y,
                                ((PlayerShootProjectile) transmissionMessage).getIdentificator(),
                                CollisionObjectType.SpellEnemy
                        );
                break;

            //Other player shooting freezing projectile
            case "21":
                this.multiplayerViewInstance.getSpellManager().spellCreator
                        .fireFreezingProjectile(
                                ((PlayerShootFreezingProjectile) transmissionMessage).getFromPosition().x,
                                ((PlayerShootFreezingProjectile) transmissionMessage).getFromPosition().y,
                                ((PlayerShootFreezingProjectile) transmissionMessage).getMovementDelta().x,
                                ((PlayerShootFreezingProjectile) transmissionMessage).getMovementDelta().y,
                                ((PlayerShootFreezingProjectile) transmissionMessage).getIdentificator(),
                                CollisionObjectType.SpellEnemy
                        );
                break;

            //Other player hit by spell
            case "30":
                Iterator<OffensiveSpell> offensiveSpellIterator = this.multiplayerViewInstance.getSpellManager().getOffensiveSpellList().iterator();
                while (offensiveSpellIterator.hasNext()){
                    OffensiveSpell temp = offensiveSpellIterator.next();
                    if (temp.getIdentificator().equals(((PlayerHitMessage)transmissionMessage).getProjectileId())){
                        StaticAnimationManager.addExplosion(new Point(temp.getX(), temp.getY()), 1);
                        offensiveSpellIterator.remove();
                        break;
                    }
                }

                break;

            //Other player defeated
            case "40":
                String playerNickname = ((PlayerDestroyedMessage) transmissionMessage).getNickname();
                this.multiplayerViewInstance.getOtherPlayersManager().getOtherPlayers().remove(playerNickname);
                removePlayerFromRegisteredPlayersList(playerNickname);
                StaticAnimationManager.addExplosionPlayerDestroyed(this.multiplayerViewInstance.getOtherPlayersManager().getOtherPlayers().get(playerNickname).getPosition());
                if (this.multiplayerViewInstance.getOtherPlayersManager().getOtherPlayers().size() == 0) {
                    this.multiplayerViewInstance.victory = false;
                }
                break;

            //Sync mess
            case "50":
                this.multiplayerViewInstance.getOtherPlayersManager().getOtherPlayers().get(((ReadyToPlayMessage) transmissionMessage).getNickname()).setIsReadyForNextFrame(true);
                break;
        }
    }

    private void removePlayerFromRegisteredPlayersList(String playerNickname){
        Iterator<Client> iterator = this.multiplayerViewInstance.connectionManager.registeredPlayers.iterator();
        while (iterator.hasNext()){
            Client temp = iterator.next();
            if (temp.getNickname().equals(playerNickname)){
                iterator.remove();
                break;
            }
        }
    }
}
