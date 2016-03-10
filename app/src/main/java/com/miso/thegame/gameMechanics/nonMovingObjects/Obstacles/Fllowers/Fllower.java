package com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.Fllowers;

import android.graphics.Point;

import com.miso.thegame.GamePanel;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.Obstacle;

/**
 * Created by Miso on 20.11.2015.
 */
public abstract class Fllower extends Obstacle {

    public Fllower(){
        super(new Point(GamePanel.randomGenerator.nextInt(MapManager.getWorldWidth()),GamePanel.randomGenerator.nextInt(MapManager.getWorldHeight())));
    }
}
