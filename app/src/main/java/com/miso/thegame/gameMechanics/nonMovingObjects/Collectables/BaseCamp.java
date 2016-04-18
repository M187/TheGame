package com.miso.thegame.gameMechanics.nonMovingObjects.Collectables;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.Player;

/**
 * Created by Miso on 21.12.2015.
 */
public class BaseCamp extends Collectible {

    public BaseCamp(Resources res, Point position){
        super(position, BitmapFactory.decodeResource(res, R.drawable.basecamp));
    }

    public void onInteraction(Player player, MapManager mapManager){
        player.replenishPrimaryAmmo();
        player.addHealth();
    }

}
