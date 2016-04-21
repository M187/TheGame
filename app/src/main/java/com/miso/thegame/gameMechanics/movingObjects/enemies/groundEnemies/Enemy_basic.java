package com.miso.thegame.gameMechanics.movingObjects.enemies.groundEnemies;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.GameObject;
import com.miso.thegame.gameMechanics.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;

/**
 * Created by Miso on 11.10.2015.
 */
public class Enemy_basic extends EnemyGround implements Comparable<Enemy_basic> {


    public Enemy_basic(Resources res, Point startingPosition) {
        super(startingPosition);
        setSpeed(5);
        setDx(getX());
        setDy(getY());
        setImage(BitmapFactory.decodeResource(res, R.drawable.object));
    }

    public void setMovementTarget(GameObject gameObject) {
        this.movementTarget = gameObject;
    }

    public void update(Player player, EnemiesManager enemiesManager) {
        if (player.changingTile) {
            this.removeLastTileFromPath();
        }
        this.setPositionBeforeMoving();
        if (this.getGridCoordinates().equals(player.getGridCoordinates())) {
            setDx(player.getX());
            setDy(player.getY());
            pathTowardsTarget.clear();
        } else if (this.isGameObjectInNeighbourhood(player)) {
            setDx(player.getX());
            setDy(player.getY());
            pathTowardsTarget.clear();
        } else {
            // Do we have tile path initialized?
            if (this.pathTowardsTarget.size() == 0) {
                currentDstinationTile = null;
                findPathTowardsTarget(player);
            }
            this.proceedWithPath();
        }
        moveObject();
        setDistanceFromPlayer(Math.sqrt(Math.pow(player.getX() - this.getX(), 2) + Math.pow(player.getY() - this.getY(), 2)));
    }

    @Override
    public int compareTo(Enemy_basic another) {
        return (int) (this.distanceFromPlayer - another.distanceFromPlayer);
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
