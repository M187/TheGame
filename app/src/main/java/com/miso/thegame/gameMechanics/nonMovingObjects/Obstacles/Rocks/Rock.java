package com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.Rocks;

import android.graphics.Point;

import com.miso.thegame.GamePanel;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.Obstacle;

/**
 * Created by Miso on 20.11.2015.
 */
public abstract class Rock extends Obstacle {

    public Rock(){
        super(new Point(GamePanel.randomGenerator.nextInt(MapManager.getWorldWidth()),GamePanel.randomGenerator.nextInt(MapManager.getWorldHeight())));
    }

    public Rock(Point coordPoint){
        super(coordPoint);
    }

    public void initializeRange(){
        int xSize = this.getImage().getWidth() / 2;
        int ySize = this.getImage().getHeight() / 2;
        this.range = (int)(Math.sqrt( xSize * xSize + ySize * ySize ));
    }
}
