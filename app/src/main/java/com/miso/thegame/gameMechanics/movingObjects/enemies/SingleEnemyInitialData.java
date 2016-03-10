package com.miso.thegame.gameMechanics.movingObjects.enemies;

import com.miso.thegame.gameMechanics.movingObjects.Waypoint;

import java.util.ArrayList;

/**
 * Created by Miso on 26.1.2016.
 */

public class SingleEnemyInitialData {

    public enum EnemyType{
        asteroidBase,
        carrier,
        alienShip,
        nest,
        groundSeeker
    }

    public int x;
    public int y;
    public ArrayList<Waypoint> waypointList = new ArrayList<>();
    public EnemyType enemyType;

    public SingleEnemyInitialData(int x, int y, EnemyType enemyType, ArrayList<Waypoint> waypointList){
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
        this.waypointList = waypointList;
    }

    public SingleEnemyInitialData(int x, int y, EnemyType enemyType){
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
    }
}
