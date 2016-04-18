package com.miso.thegame.gameMechanics.movingObjects.enemies;

import android.graphics.Point;

import com.miso.thegame.GameObject;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.movingObjects.MovableObject;
import com.miso.thegame.gameMechanics.movingObjects.Player;

/**
 * Created by Miso on 26.1.2016.
 */
public abstract class Enemy extends MovableObject {

    protected double distanceFromPlayer;
    protected boolean playerInRange;
    protected int hitPoints;

    public Enemy(Point startingPosition){
        super();
        this.x = startingPosition.x;
        this.y = startingPosition.y;
        this.collisionObjectType = CollisionObjectType.Enemy;
    }

    public abstract void update(Player player, EnemiesManager enemiesManager);

    public abstract void handleCollisionWithPlayer(Player player, EnemiesManager enemiesManager);

    public double getDistanceFromPlayer() {
        return distanceFromPlayer;
    }

    public void setDistanceFromPlayer(double distanceFromPlayer) {
        this.distanceFromPlayer = distanceFromPlayer;
    }

    public int compareTo(Enemy another) {
        return (int) (this.distanceFromPlayer - another.distanceFromPlayer);
    }

    /**
     * Is gameObject is on a neighbour position?
     *
     * @param gO game object which we compare to.
     * @return true or false if objects are neighbours.
     */
    public boolean isGameObjectInNeighbourhood(GameObject gO) {
        return  Math.abs(this.getGridCoordinates().x - gO.getGridCoordinates().x) == 0 && Math.abs(this.getGridCoordinates().y - gO.getGridCoordinates().y) == 1 ||
                Math.abs(this.getGridCoordinates().x - gO.getGridCoordinates().x) == 1 && Math.abs(this.getGridCoordinates().y - gO.getGridCoordinates().y) == 0;
    }

    public boolean hitBySpell(){
        return false;
    }

    protected int turnThreshold = 5;
    protected int lastHeading = 0;
    
    /**
     *
     */
    protected void turnCheck() {
        int deltaDegrees = Math.abs(Math.abs(this.lastHeading) - Math.abs(this.heading));
        if (deltaDegrees > this.turnThreshold && (Math.abs(Math.abs(this.lastHeading) - Math.abs(this.heading) + 360) > this.turnThreshold) && (Math.abs(Math.abs(this.lastHeading) - Math.abs(this.heading) - 360) > turnThreshold)){
            int newDegrees;
            if (this.lastHeading > this.heading) {
                if (deltaDegrees > 180) {
                    newDegrees = this.lastHeading + this.turnThreshold;
                    if (newDegrees > 0) {
                        newDegrees -= 360;
                    }
                    this.heading = (newDegrees);
                } else {
                    newDegrees = this.lastHeading - this.turnThreshold;
                    if (newDegrees < -360) {
                        newDegrees += 360;
                    }
                    this.heading = (newDegrees);
                }
            } else {
                if (deltaDegrees < 180) {
                    newDegrees = this.lastHeading + this.turnThreshold;
                    if (newDegrees > 0) {
                        newDegrees -= 360;
                    }
                    this.heading = (newDegrees);
                } else {
                    newDegrees = this.lastHeading - this.turnThreshold;
                    if (newDegrees < -360) {
                        newDegrees += 360;
                    }
                    this.heading = (newDegrees);
                }
            }
            this.restorePositionBeforeMoving();
        }
        this.lastHeading = this.heading;
    }

}
