package com.miso.thegame.gameMechanics.movingObjects.enemies.spaceEnemies;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.movingObjects.Player;
import com.miso.thegame.gameMechanics.movingObjects.Waypoint;
import com.miso.thegame.gameMechanics.movingObjects.enemies.EnemiesManager;

import java.util.ArrayList;

/**
 * Created by Miso on 26.1.2016.
 */
public class Enemy_carrier extends EnemySpace {

    private int shootingCd = 0;
    private int imageHalfWidth, imageHalfHeight;

    public Enemy_carrier(Resources res, Point startingPosition, ArrayList<Waypoint> scriptedPath) {
        super(startingPosition);
        this.hitPoints = 80;
        this.playerInRange = false;
        int turnThreshold = 5;
        setSpeed(2);
        setDx(getX());
        setDy(getY());
        setImage(BitmapFactory.decodeResource(res, R.drawable.enemycarrier));
        this.scriptedPath = scriptedPath;

        this.imageHalfHeight = getImage().getHeight()/2;
        this.imageHalfWidth = getImage().getWidth()/2;
    }


    public void update(Player player, EnemiesManager enemiesManager) {
        this.setPositionBeforeMoving();

        this.distanceFromPlayer = (Math.sqrt(Math.pow(player.getX() - this.getX(), 2) + Math.pow(player.getY() - this.getY(), 2)));
        this.shootingCd -= 1;

        Waypoint currentWaypoint = this.scriptedPath.get(this.currentWaypointIndex);
        if (this.distanceFromPlayer < 700) {
            this.playerInRange = true;
            this.performAction(player, enemiesManager);
        }
        this.playerInRange = false;
        if (this.x == currentWaypoint.x && this.y == currentWaypoint.y) {
            this.currentWaypointIndex += 1;
            if (this.currentWaypointIndex == this.scriptedPath.size()) {
                this.currentWaypointIndex = 0;
            }
            currentWaypoint = this.scriptedPath.get(this.currentWaypointIndex);
        }
        setDx(currentWaypoint.x);
        setDy(currentWaypoint.y);

        moveObject();
        turnCheck();
    }

    public void performAction(Player player, EnemiesManager enemiesManager) {
        if (this.shootingCd < 0) {
            enemiesManager.spellManager.addEnemyProjectile(this.x, this.y, player.x, player.y);
            Point a = rotateVertexAroundCurrentPosition(new Point(this.x, this.y + 120));
            enemiesManager.spellManager.addEnemyProjectile(a.x, a.y, player.x, player.y);
            a = rotateVertexAroundCurrentPosition(new Point(this.x, this.y - 120));
            enemiesManager.spellManager.addEnemyProjectile(a.x, a.y, player.x, player.y);
            this.shootingCd = 90;
        }
    }

    /**
     * Current behavior - remove 1hp.
     *
     * @param player         ...
     * @param enemiesManager to remove enemy from.
     */
    public void handleCollisionWithPlayer(Player player, EnemiesManager enemiesManager) {
        player.removeHealth(1);
    }

    @Override
    public boolean hitBySpell() {
        this.hitPoints -= 5;

        if (this.hitPoints < 0) {
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Point> getObjectCollisionVertices() {
        this.objectVertices.clear();
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX(), getY() - this.imageHalfHeight)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() - this.imageHalfWidth, getY() + 117)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX(), getY() + this.imageHalfHeight)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() + this.imageHalfWidth, getY() + 117)));
        return this.objectVertices;
    }

}
