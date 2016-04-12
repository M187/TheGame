package com.miso.thegame.gameMechanics.map.mapDefinitions;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.movingObjects.Waypoint;
import com.miso.thegame.gameMechanics.nonMovingObjects.Collectables.BaseCamp;

import java.util.ArrayList;

import static com.miso.thegame.gameMechanics.movingObjects.enemies.SingleEnemyInitialData.EnemyType;

/**
 * Created by Miso on 26.1.2016.
 */
public class SpaceLevel2 extends GameMap {

    public SpaceLevel2(Resources resources){

        this.mapDimensions = new Point(10000, 10000);

        //Stvorec
        ArrayList<Waypoint> a = new ArrayList<>();
        a.add(new Waypoint(2500,1500));
        a.add(new Waypoint(2500,2500));
        a.add(new Waypoint(1500,2500));
        a.add(new Waypoint(1500, 1500));

        this.addEnemyData(1500, 1500, EnemyType.carrier, a);

        ArrayList<Waypoint> b = new ArrayList<>();
        b.add(new Waypoint(1500, 2500));
        b.add(new Waypoint(1500, 1500));
        b.add(new Waypoint(2500, 1500));
        b.add(new Waypoint(2500, 2500));

        this.addEnemyData(2500, 2500, EnemyType.carrier, b);


        this.addEnemyData(500,500, EnemyType.asteroidBase );
        this.addEnemyData(mapDimensions.x - 500, mapDimensions.y -500, EnemyType.asteroidBase);
        this.addEnemyData(mapDimensions.x - 500 ,500, EnemyType.asteroidBase);
        this.addEnemyData(500, mapDimensions.y - 500, EnemyType.asteroidBase);
        this.addEnemyData(6000, 7000, EnemyType.asteroidBase);

        this.addStaticElement(new BaseCamp(resources, new Point(mapDimensions.x/2, mapDimensions.y/2)));
    }

}
