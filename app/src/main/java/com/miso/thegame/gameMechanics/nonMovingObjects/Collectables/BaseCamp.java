package com.miso.thegame.gameMechanics.nonMovingObjects.Collectables;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.GameObject;
import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.Player;

/**
 * Created by Miso on 21.12.2015.
 */
public class BaseCamp extends Collectable {

    private int rangeOfInflueance;

    public BaseCamp(Resources res, Point position){
        super(position, BitmapFactory.decodeResource(res, R.drawable.basecamp));
        this.rangeOfInflueance = 200;
    }

    public boolean intersectsWithMe(GameObject gameObject){
        int deltaX = gameObject.getX() - this.getX() + this.getImage().getWidth() / 2;
        int deltaY = gameObject.getY() - this.getY() + this.getImage().getHeight() / 2;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= this.rangeOfInflueance;
    }

    public void onInteraction(Player player, MapManager mapManager){
        player.replenishPrimaryAmmo();
        player.addHealth();
    }

}
