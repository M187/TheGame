package com.miso.thegame.gameMechanics.map.mapDefinitions;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.nonMovingObjects.Collectables.BaseCamp;
import com.miso.thegame.gameMechanics.nonMovingObjects.Collectables.StarCollectible;
import com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.DemoObstacles.DemoObstacle1;

/**
 * Created by michal.hornak on 28.12.2015.
 */
public class Level1 extends GameMap {

    public Level1(Resources res){

        this.mapDimensions = new Point(2000,2000);

        //<editor-fold desc=" -↓ Unpassable obstacles ↓-">
        addStaticElement(new DemoObstacle1(res, new Point(0,600)));

        addStaticElement(new DemoObstacle1(res, new Point(100,600)));
        addStaticElement(new DemoObstacle1(res, new Point(100,700)));
        addStaticElement(new DemoObstacle1(res, new Point(100,1000)));
        addStaticElement(new DemoObstacle1(res, new Point(100,1100)));
        addStaticElement(new DemoObstacle1(res, new Point(100,1200)));
        addStaticElement(new DemoObstacle1(res, new Point(100,1300)));
        addStaticElement(new DemoObstacle1(res, new Point(100,1400)));

        addStaticElement(new DemoObstacle1(res, new Point(200,100)));
        addStaticElement(new DemoObstacle1(res, new Point(200,300)));
        addStaticElement(new DemoObstacle1(res, new Point(200,600)));
        addStaticElement(new DemoObstacle1(res, new Point(200,1400)));
        addStaticElement(new DemoObstacle1(res, new Point(200,1500)));
        addStaticElement(new DemoObstacle1(res, new Point(200,1600)));

        addStaticElement(new DemoObstacle1(res, new Point(300,300)));
        addStaticElement(new DemoObstacle1(res, new Point(300,600)));
        addStaticElement(new DemoObstacle1(res, new Point(300,900)));
        addStaticElement(new DemoObstacle1(res, new Point(300,1000)));
        addStaticElement(new DemoObstacle1(res, new Point(300,1100)));
        addStaticElement(new DemoObstacle1(res, new Point(300,1400)));

        addStaticElement(new DemoObstacle1(res, new Point(400,100)));
        addStaticElement(new DemoObstacle1(res, new Point(400,200)));
        addStaticElement(new DemoObstacle1(res, new Point(400,300)));
        addStaticElement(new DemoObstacle1(res, new Point(400,800)));
        addStaticElement(new DemoObstacle1(res, new Point(400,900)));
        addStaticElement(new DemoObstacle1(res, new Point(400,1400)));

        addStaticElement(new DemoObstacle1(res, new Point(500,600)));
        addStaticElement(new DemoObstacle1(res, new Point(500,700)));
        addStaticElement(new DemoObstacle1(res, new Point(500,800)));
        addStaticElement(new DemoObstacle1(res, new Point(500,1400)));
        addStaticElement(new DemoObstacle1(res, new Point(500,1500)));
        addStaticElement(new DemoObstacle1(res, new Point(500,1600)));
        addStaticElement(new DemoObstacle1(res, new Point(500,1700)));

        addStaticElement(new DemoObstacle1(res, new Point(600,500)));
        addStaticElement(new DemoObstacle1(res, new Point(600,600)));
        addStaticElement(new DemoObstacle1(res, new Point(600,800)));
        addStaticElement(new DemoObstacle1(res, new Point(600,1200)));
        addStaticElement(new DemoObstacle1(res, new Point(600,1700)));
        addStaticElement(new DemoObstacle1(res, new Point(600,1900)));

        addStaticElement(new DemoObstacle1(res, new Point(700,0)));
        addStaticElement(new DemoObstacle1(res, new Point(700,100)));
        addStaticElement(new DemoObstacle1(res, new Point(700,300)));
        addStaticElement(new DemoObstacle1(res, new Point(700,400)));
        addStaticElement(new DemoObstacle1(res, new Point(700,500)));
        addStaticElement(new DemoObstacle1(res, new Point(700,800)));
        addStaticElement(new DemoObstacle1(res, new Point(700,900)));
        addStaticElement(new DemoObstacle1(res, new Point(700,1000)));
        addStaticElement(new DemoObstacle1(res, new Point(700,1200)));
        addStaticElement(new DemoObstacle1(res, new Point(700,1400)));
        addStaticElement(new DemoObstacle1(res, new Point(700,1500)));
        addStaticElement(new DemoObstacle1(res, new Point(700,1700)));
        addStaticElement(new DemoObstacle1(res, new Point(700,1900)));

        addStaticElement(new DemoObstacle1(res, new Point(800,400)));
        addStaticElement(new DemoObstacle1(res, new Point(800,1200)));
        addStaticElement(new DemoObstacle1(res, new Point(800,1400)));
        addStaticElement(new DemoObstacle1(res, new Point(800,1500)));
        addStaticElement(new DemoObstacle1(res, new Point(800,1700)));
        addStaticElement(new DemoObstacle1(res, new Point(800,1900)));

        addStaticElement(new DemoObstacle1(res, new Point(900,400)));
        addStaticElement(new DemoObstacle1(res, new Point(900,600)));
        addStaticElement(new DemoObstacle1(res, new Point(900,700)));
        addStaticElement(new DemoObstacle1(res, new Point(900,1200)));

        addStaticElement(new DemoObstacle1(res, new Point(1000,100)));
        addStaticElement(new DemoObstacle1(res, new Point(1000,200)));
        addStaticElement(new DemoObstacle1(res, new Point(1000,300)));
        addStaticElement(new DemoObstacle1(res, new Point(1000,400)));
        addStaticElement(new DemoObstacle1(res, new Point(1000,700)));
        addStaticElement(new DemoObstacle1(res, new Point(1000,1200)));
        addStaticElement(new DemoObstacle1(res, new Point(1000,1300)));

        addStaticElement(new DemoObstacle1(res, new Point(1100,400)));
        addStaticElement(new DemoObstacle1(res, new Point(1100,700)));
        addStaticElement(new DemoObstacle1(res, new Point(1100,1600)));

        addStaticElement(new DemoObstacle1(res, new Point(1200,100)));
        addStaticElement(new DemoObstacle1(res, new Point(1200,200)));
        addStaticElement(new DemoObstacle1(res, new Point(1200,700)));
        addStaticElement(new DemoObstacle1(res, new Point(1200,800)));
        addStaticElement(new DemoObstacle1(res, new Point(1200,900)));
        addStaticElement(new DemoObstacle1(res, new Point(1200,1000)));
        addStaticElement(new DemoObstacle1(res, new Point(1200,1100)));
        addStaticElement(new DemoObstacle1(res, new Point(1200,1500)));
        addStaticElement(new DemoObstacle1(res, new Point(1200,1600)));
        addStaticElement(new DemoObstacle1(res, new Point(1200,1700)));
        addStaticElement(new DemoObstacle1(res, new Point(1200,1800)));

        addStaticElement(new DemoObstacle1(res, new Point(1300,400)));
        addStaticElement(new DemoObstacle1(res, new Point(1300,500)));
        addStaticElement(new DemoObstacle1(res, new Point(1300,600)));
        addStaticElement(new DemoObstacle1(res, new Point(1300,700)));
        addStaticElement(new DemoObstacle1(res, new Point(1300,1100)));
        addStaticElement(new DemoObstacle1(res, new Point(1300,1200)));
        addStaticElement(new DemoObstacle1(res, new Point(1300,1300)));
        addStaticElement(new DemoObstacle1(res, new Point(1300,1600)));

        addStaticElement(new DemoObstacle1(res, new Point(1400,100)));
        addStaticElement(new DemoObstacle1(res, new Point(1400,200)));
        addStaticElement(new DemoObstacle1(res, new Point(1400,300)));
        addStaticElement(new DemoObstacle1(res, new Point(1400,400)));
        addStaticElement(new DemoObstacle1(res, new Point(1400,1300)));
        addStaticElement(new DemoObstacle1(res, new Point(1400,1400)));

        addStaticElement(new DemoObstacle1(res, new Point(1500,100)));
        addStaticElement(new DemoObstacle1(res, new Point(1500,400)));
        addStaticElement(new DemoObstacle1(res, new Point(1500,700)));
        addStaticElement(new DemoObstacle1(res, new Point(1500,800)));
        addStaticElement(new DemoObstacle1(res, new Point(1500,900)));
        addStaticElement(new DemoObstacle1(res, new Point(1500,1000)));
        addStaticElement(new DemoObstacle1(res, new Point(1500,1100)));
        addStaticElement(new DemoObstacle1(res, new Point(1500,1300)));
        addStaticElement(new DemoObstacle1(res, new Point(1500,1400)));
        addStaticElement(new DemoObstacle1(res, new Point(1500,1600)));
        addStaticElement(new DemoObstacle1(res, new Point(1500,1700)));

        addStaticElement(new DemoObstacle1(res, new Point(1600,100)));
        addStaticElement(new DemoObstacle1(res, new Point(1600,400)));
        addStaticElement(new DemoObstacle1(res, new Point(1600,700)));
        addStaticElement(new DemoObstacle1(res, new Point(1600,1100)));
        addStaticElement(new DemoObstacle1(res, new Point(1600,1400)));
        addStaticElement(new DemoObstacle1(res, new Point(1600,1600)));

        addStaticElement(new DemoObstacle1(res, new Point(1700,100)));
        addStaticElement(new DemoObstacle1(res, new Point(1700,400)));
        addStaticElement(new DemoObstacle1(res, new Point(1700,700)));
        addStaticElement(new DemoObstacle1(res, new Point(1700,900)));
        addStaticElement(new DemoObstacle1(res, new Point(1700,1100)));
        addStaticElement(new DemoObstacle1(res, new Point(1700,1400)));
        addStaticElement(new DemoObstacle1(res, new Point(1700,1500)));
        addStaticElement(new DemoObstacle1(res, new Point(1700,1600)));
        addStaticElement(new DemoObstacle1(res, new Point(1700,1800)));
        addStaticElement(new DemoObstacle1(res, new Point(1700,1900)));

        addStaticElement(new DemoObstacle1(res, new Point(1800,400)));
        addStaticElement(new DemoObstacle1(res, new Point(1800,700)));
        addStaticElement(new DemoObstacle1(res, new Point(1800,800)));
        addStaticElement(new DemoObstacle1(res, new Point(1800,900)));
        addStaticElement(new DemoObstacle1(res, new Point(1800,1100)));
        addStaticElement(new DemoObstacle1(res, new Point(1800,1800)));

        addStaticElement(new DemoObstacle1(res, new Point(1900,400)));
        addStaticElement(new DemoObstacle1(res, new Point(1900,1100)));
        addStaticElement(new DemoObstacle1(res, new Point(1900,1200)));
    //</editor-fold>

        //Collectibles
        addStaticElement(new BaseCamp(res, new Point(700,700)));

        addStaticElement(new StarCollectible(res, new Point(1500, 300)));
        addStaticElement(new StarCollectible(res, new Point(400, 1500)));
        addStaticElement(new StarCollectible(res, new Point(300, 200)));
        addStaticElement(new StarCollectible(res, new Point(1700, 800)));
    }
}

