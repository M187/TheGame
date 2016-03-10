package com.miso.thegame.gameMechanics.nonMovingObjects.Collectables;

import android.graphics.Point;

import com.miso.thegame.GameObject;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.collisionHandlers.SATCollisionCalculator;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.Player;
import com.miso.thegame.gameMechanics.nonMovingObjects.StaticObject;

/**
 * Created by Miso on 15.11.2015.
 */
public abstract class Collectable extends StaticObject {

    public Collectable(Point position){
        this.collisionObjectType = CollisionObjectType.Collectible;
        this.x = position.x;
        this.y = position.y;
    }

    public abstract void onInteraction(Player player, MapManager mapManager);

    public boolean intersectsWithMe(GameObject gameObject, SATCollisionCalculator satCollisionCalculator){

        return satCollisionCalculator.performSeparateAxisCollisionCheck(this.getObjectCollisionVertices(), gameObject.getObjectCollisionVertices());

        //return (Rect.intersects(gameObject.getRectangle(), this.getRectangle()));
    }

}
