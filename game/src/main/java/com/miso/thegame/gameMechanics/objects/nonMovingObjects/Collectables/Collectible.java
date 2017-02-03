package com.miso.thegame.gameMechanics.objects.nonMovingObjects.Collectables;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.objects.GameObject;
import com.miso.thegame.gameMechanics.objects.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.objects.collisionHandlers.SATCollisionCalculator;
import com.miso.thegame.gameMechanics.objects.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.objects.nonMovingObjects.StaticObject;

/**
 * Created by Miso on 15.11.2015.
 */
public abstract class Collectible extends StaticObject {

    public Collectible(){}

    public Collectible(Point position, Bitmap image){
        super(position, image);
        this.collisionObjectType = CollisionObjectType.Collectible;
    }

    public abstract void onInteraction(Player player, MapManager mapManager);

    public boolean intersectsWithMe(GameObject gameObject, SATCollisionCalculator satCollisionCalculator){
        return satCollisionCalculator.performSeparateAxisCollisionCheck(this.getObjectCollisionVertices(), gameObject.getObjectCollisionVertices());
    }

}
