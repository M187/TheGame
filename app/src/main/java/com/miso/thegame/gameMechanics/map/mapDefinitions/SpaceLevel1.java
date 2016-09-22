package com.miso.thegame.gameMechanics.map.mapDefinitions;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.objects.movingObjects.Waypoint;
import com.miso.thegame.gameMechanics.objects.nonMovingObjects.Collectables.BaseCampWithTimer;

import java.util.ArrayList;

import static com.miso.thegame.gameMechanics.objects.movingObjects.enemies.SingleEnemyInitialData.EnemyType;

/**
 * Created by Miso on 26.1.2016.
 */
public class SpaceLevel1 extends GameMap {

    public SpaceLevel1(Resources resources){

        this.mapDimensions = new Point(4000, 4000);

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
        this.addEnemyData(3500,3500, EnemyType.asteroidBase);
        this.addEnemyData(3500,500, EnemyType.asteroidBase);
        this.addEnemyData(500,3500, EnemyType.asteroidBase);

        this.addStaticElement(new BaseCampWithTimer(resources, new Point(mapDimensions.x / 2, mapDimensions.y / 2)));
    }

    public String getBackgroundImageName(){
        return "backgroundspace";
    }
}
