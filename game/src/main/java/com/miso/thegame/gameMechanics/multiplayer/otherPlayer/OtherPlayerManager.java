package com.miso.thegame.gameMechanics.multiplayer.otherPlayer;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerPositionData;
import com.miso.thegame.gameMechanics.gameViews.GameView2;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by michal.hornak on 05.05.2016.
 */
public class OtherPlayerManager {

    private HashMap<String,OtherPlayer> otherPlayers = new HashMap<>();

    public OtherPlayerManager(ArrayList<Client> registeredPlayers, Resources resources){
        for (Client client : registeredPlayers){
            this.otherPlayers.put(client.getNickname(), new OtherPlayer(resources));
        }
    }

    public void update(){
        for (OtherPlayer otherPlayer : this.getOtherPlayers().values()) {
            otherPlayer.update();
        }
    }

    public void updatePlayerData(PlayerPositionData playerPositionData){
        OtherPlayer targetPlayer = otherPlayers.get(playerPositionData.getNickname());
        targetPlayer.setX(playerPositionData.getPosition().x);
        targetPlayer.setY(playerPositionData.getPosition().y);
        targetPlayer.setHeading(playerPositionData.getHeading());
    }

    public void draw(Canvas canvas){
        for (OtherPlayer otherPlayer : this.getOtherPlayers().values()) {
            GameView2.drawManager.drawOnDisplay(otherPlayer, canvas);
        }
    }

    public HashMap<String, OtherPlayer> getOtherPlayers() {
        return otherPlayers;
    }
}
