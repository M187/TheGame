package com.miso.thegame.gameMechanics.movingObjects.enemies.groundEnemies;

import android.graphics.Point;

import com.miso.thegame.GameObject;
import com.miso.thegame.gameMechanics.map.pathfinding.MapTile;
import com.miso.thegame.gameMechanics.map.pathfinding.Pathfinder;
import com.miso.thegame.gameMechanics.movingObjects.Waypoint;
import com.miso.thegame.gameMechanics.movingObjects.enemies.Enemy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Miso on 26.1.2016.
 */
public abstract class EnemyGround extends Enemy {

    protected double distanceFromPlayer;
    protected List<MapTile> pathTowardsTarget = new LinkedList<>();
    protected GameObject movementTarget = this;
    protected MapTile currentDstinationTile;
    protected ArrayList<Waypoint> scriptedPath = new ArrayList<>();
    protected int currentWaypointIndex;

    public EnemyGround(Point starttingPosition){
        super(starttingPosition);
    }

    public void findPathTowardsTarget(GameObject gameObject) {
        pathTowardsTarget = Pathfinder.FindPath.findPath(this, gameObject);
        pathTowardsTarget.remove(pathTowardsTarget.size() - 1);
    }

    public void proceedWithPath() {
        if (currentDstinationTile == null || currentDstinationTile.getTileMiddlePosition().equals(this.x, this.y)) {
            currentDstinationTile = pathTowardsTarget.get(pathTowardsTarget.size() - 1);
            pathTowardsTarget.remove(currentDstinationTile);
        }
        //move this into if
        setDx(currentDstinationTile.getTileMiddlePosition().x);
        setDy(currentDstinationTile.getTileMiddlePosition().y);
    }

    /**
     * If  "final destination object" (most probably player) leaves tile for which path was calculated we need to pop last tile from path.
     */
    public void removeLastTileFromPath() {
        try {
            this.pathTowardsTarget.remove(0);
        } catch (Exception e) {
            System.out.print("Null path. Error while removing last tile.");
        }
    }

    public double getDistanceFromPlayer() {
        return distanceFromPlayer;
    }

    public void setDistanceFromPlayer(double distanceFromPlayer) {
        this.distanceFromPlayer = distanceFromPlayer;
    }
}
