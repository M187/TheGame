package com.miso.thegame.gameMechanics.map.mapDefinitions;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.objects.nonMovingObjects.Collectables.BaseCampWithTimer;
import com.miso.thegame.gameMechanics.objects.nonMovingObjects.Obstacles.SquareObstacle;

/**
 * Created by michal.hornak on 15.07.2016.
 */
public class MultiplayerMap1 extends GameMap {

    public MultiplayerMap1(Resources res){

        this.mapDimensions = new Point((int) GameView2.scaleSize(2000), (int) GameView2.scaleSize(2000));

        addStaticElement(new SquareObstacle(res, new Point((int)GameView2.scaleSize(600), (int)GameView2.scaleSize(600))));
        addStaticElement(new SquareObstacle(res, new Point((int)GameView2.scaleSize(1400), (int)GameView2.scaleSize(600))));
        addStaticElement(new SquareObstacle(res, new Point((int)GameView2.scaleSize(600), (int)GameView2.scaleSize(1400))));
        addStaticElement(new SquareObstacle(res, new Point((int)GameView2.scaleSize(1400), (int)GameView2.scaleSize(1400))));

        //Collectibles
        addStaticElement(new BaseCampWithTimer(res, new Point((int)GameView2.scaleSize(1000),(int)GameView2.scaleSize(1000))));

        addStaticElement(new SquareObstacle(res, new Point((int)GameView2.scaleSize(1100), (int)GameView2.scaleSize(1100))));
        addStaticElement(new SquareObstacle(res, new Point((int)GameView2.scaleSize(900), (int)GameView2.scaleSize(900))));
        addStaticElement(new SquareObstacle(res, new Point((int)GameView2.scaleSize(1100), (int)GameView2.scaleSize(900))));
        addStaticElement(new SquareObstacle(res, new Point((int)GameView2.scaleSize(900), (int)GameView2.scaleSize(1100))));
    }

    public String getBackgroundImageName(){
        return "backgroundspace";
    }
}