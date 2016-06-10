package com.miso.thegame.gameMechanics.movingObjects.player;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.map.MapManager;

/**
 * Created by michal.hornak on 10.06.2016.
 */
public class PlayerSpaceship extends Player {

    public PlayerSpaceship(Resources res, Point startingPosition, MapManager mapManager) {
        setX(startingPosition.x);
        setY(startingPosition.y);
        this.collisionObjectType = CollisionObjectType.Player;
        setSpeed(ConstantHolder.maximumPlayerSpeed);
        score = 0;
        dx = (getX());
        dy = (getY());

        this.mapManager = mapManager;
        setImage(BitmapFactory.decodeResource(res, R.drawable.player));
        this.playerEngineMotors = new PlayerEngineMotors(res);
        this.setCurrentGridCoordinates(super.getGridCoordinates());
    }
}
