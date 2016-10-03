package com.miso.thegame.gameMechanics.objects.movingObjects.enemies.spaceEnemies;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.objects.movingObjects.Waypoint;
import com.miso.thegame.gameMechanics.objects.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.objects.movingObjects.enemies.groundEnemies.Shooter;
import com.miso.thegame.gameMechanics.objects.movingObjects.enemies.groundEnemies.Shooting;
import com.miso.thegame.gameMechanics.objects.movingObjects.player.Player;

import java.util.ArrayList;

/**
 * Created by Miso on 26.1.2016.
 */
public class Enemy_carrier extends EnemySpace implements Shooting {

    private int imageHalfWidth, imageHalfHeight;
    private Shooter shooter;

    public Enemy_carrier(Resources res, Point startingPosition, ArrayList<Waypoint> scriptedPath) {
        super(startingPosition);
        this.hitPoints = 80;
        this.shooter = new Shooter(45, this);
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

        Waypoint currentWaypoint = this.scriptedPath.get(this.currentWaypointIndex);

        getShooter().takeShot(enemiesManager.spellManager.spellCreator);

        //This is to get rotated position of non center point. ↓↓
        // Was used as a custom starting point for projectile. ↓↓
        Point a = rotateVertexAroundCurrentPosition(new Point(this.x, this.y + 120));


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

    @Override
    public Shooter getShooter() {
        return this.shooter;
    }

    @Override
    public int getShootingDistanceThreshold() {
        return 700;
    }
}
