package com.miso.thegame.gameMechanics.movingObjects.enemies.spaceEnemies;

import android.graphics.Point;

import com.miso.thegame.gameMechanics.GameObject;
import com.miso.thegame.gameMechanics.map.pathfinding.MapTile;
import com.miso.thegame.gameMechanics.movingObjects.Waypoint;
import com.miso.thegame.gameMechanics.movingObjects.enemies.Enemy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Miso on 26.1.2016.
 */
public abstract class EnemySpace extends Enemy {

    protected double distanceFromPlayer;
    protected List<MapTile> pathTowardsTarget = new LinkedList<>();
    protected GameObject movementTarget = this;
    protected MapTile currentDstinationTile;
    protected ArrayList<Waypoint> scriptedPath = new ArrayList<>();
    protected int currentWaypointIndex = 0;

    public EnemySpace(Point startingPosition){
        super(startingPosition);
    }
}
