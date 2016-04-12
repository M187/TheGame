package com.miso.thegame.gameMechanics.movingObjects.enemies.spaceEnemies;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.movingObjects.Player;
import com.miso.thegame.gameMechanics.movingObjects.enemies.EnemiesManager;

/**
 * Created by Miso on 11.10.2015.
 */
public class Enemy_alienShip extends EnemySpace{

    public Enemy_alienShip(Resources res, Point startingPosition) {
        super(startingPosition);
        setSpeed(8);
        setDx(getX());
        setDy(getY());
        setImage(BitmapFactory.decodeResource(res, R.drawable.enemyalien));
    }

    public void update(Player player, EnemiesManager enemiesManager) {
        setDx(player.getX());
        setDy(player.getY());

        moveObject();
        setDistanceFromPlayer(Math.sqrt(Math.pow(player.getX() - this.getX(), 2) + Math.pow(player.getY() - this.getY(), 2)));
    }

    /**
     * Current behavior - remove 5hp and discard enemy.
     * @param player ...
     * @param enemiesManager to remove enemy from.
     */
    public void handleCollisionWithPlayer(Player player, EnemiesManager enemiesManager){
        player.removeHealth(5);
        enemiesManager.getEnemyList().remove(this);
    }
}
