package com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.defensiveSpells;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.Anchor;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;

/**
 * Created by Miso on 28.11.2015.
 */
public class Blink extends DeffensiveSpell {

    private int totalFrameLivingTime = 30;
    private int frameLivingTime = 0;
    private int deltaX;
    private int deltaY;
    private Player player;
    private Anchor anchor;

    public Blink(Player player, Anchor anchor, int deltaX, int deltaY, Resources res){
        player.setMovementDisabled(true);
        setImage(BitmapFactory.decodeResource(res, R.drawable.blink));
        this.setX(player.getX());
        this.setY(player.getY());
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.player = player;
        this.anchor = anchor;
    }

    public boolean removeSpell(){
        if (frameLivingTime >= totalFrameLivingTime){
            player.setMovementDisabled(false);
            return true;
        } else {
            return false;
        }
    }

    public void update(){
        if (this.frameLivingTime == 15){
            performBlink();
        }
        this.frameLivingTime++;
    }

    /**
     * Blinks player to click position. Change destination [X,Y] to current position, so that player is ot moving.
     * Player position due to position on display will stay same.
     */
    public void performBlink(){
        int newX = player.getX() - deltaX;
        if (newX > 0 & newX < MapManager.getWorldWidth()){player.setX(newX);}
        else if (newX < 0){
            deltaX = 0 - player.getX();
            player.setX(0);
        } else {
            deltaX = MapManager.getWorldWidth() - player.getX();
            player.setX(MapManager.getWorldWidth());
        }

        int newY = player.getY() - deltaY;
        if (newY > 0 & newY < MapManager.getWorldHeight()){player.setY(newY);}
        else if (newY < 0){
            deltaY = 0 - player.getY();
            player.setY(0);
        } else {
            deltaY = MapManager.getWorldHeight() - player.getY();
            player.setY(MapManager.getWorldHeight());
        }

        player.setDxViaBlink(player.getX());
        player.setDyViaBlink(player.getY());
        player.setCurrentGridCoordinates(player.getGridCoordinates());
        this.setX(player.getX());
        this.setY(player.getY());
        anchor.setX(anchor.getX() - deltaX);
        anchor.setY(anchor.getY() - deltaY);
    }
}
