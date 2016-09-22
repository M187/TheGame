package com.miso.thegame.gameMechanics.map.mapDefinitions;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.objects.nonMovingObjects.Collectables.BaseCampWithTimer;
import com.miso.thegame.gameMechanics.objects.nonMovingObjects.Obstacles.SquareObstacle;

/**
 * Created by michal.hornak on 15.07.2016.
 */
public class MultiplayerMap1 extends GameMap {

    public MultiplayerMap1(Resources res){

        this.mapDimensions = new Point(2000,2000);

        addStaticElement(new SquareObstacle(res, new Point(600,600)));
        addStaticElement(new SquareObstacle(res, new Point(1400,600)));
        addStaticElement(new SquareObstacle(res, new Point(600,1400)));
        addStaticElement(new SquareObstacle(res, new Point(1400,1400)));

        //Collectibles
        addStaticElement(new BaseCampWithTimer(res, new Point(1000,1000)));

        addStaticElement(new SquareObstacle(res, new Point(1100,1100)));
        addStaticElement(new SquareObstacle(res, new Point(900,900)));
        addStaticElement(new SquareObstacle(res, new Point(1100,900)));
        addStaticElement(new SquareObstacle(res, new Point(900,1100)));
    }

    public String getBackgroundImageName(){
        return "backgroundspace";
    }
}