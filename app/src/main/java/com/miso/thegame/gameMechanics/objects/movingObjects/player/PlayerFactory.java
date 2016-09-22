package com.miso.thegame.gameMechanics.objects.movingObjects.player;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.GameData.GamePlayerTypeEnum;
import com.miso.thegame.gameMechanics.map.MapManager;

/**
 * Created by michal.hornak on 10.06.2016.
 */
public class PlayerFactory {

    public static Player createPlayer(Resources resources, Point startingPosition, MapManager mapManager, GamePlayerTypeEnum playerType){
        switch (playerType){

            case PlayerSpaceship:
                return new PlayerSpaceship(resources, startingPosition, mapManager);
            case PlayerSaucer:
                return new PlayerSaucer(resources, startingPosition, mapManager);
            case PlayerTriangle:
                return new PlayerTriangle(startingPosition, mapManager);
        }
        return new PlayerSpaceship(resources, startingPosition, mapManager);
    }
}
