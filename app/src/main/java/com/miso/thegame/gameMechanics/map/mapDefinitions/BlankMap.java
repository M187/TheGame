package com.miso.thegame.gameMechanics.map.mapDefinitions;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.nonMovingObjects.Collectables.BaseCamp;
import com.miso.thegame.gameMechanics.nonMovingObjects.Collectables.StarCollectible;

/**
 * Created by michal.hornak on 22.01.2016.
 */
public class BlankMap extends GameMap {

    public BlankMap(Resources res) {
        this.mapDimensions = new Point(1000, 1000);
        addStaticElement(new BaseCamp(res, new Point(400,400)));

        addStaticElement(new StarCollectible(res, new Point(300, 300)));//, StarCollectible.GameObjectivePosition.North));
    }
}
