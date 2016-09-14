package com.miso.thegame.gameMechanics.map.mapDefinitions;

import android.graphics.Point;

import com.miso.thegame.gameMechanics.nonMovingObjects.StaticObject;

/**
 * Created by Miso on 8.9.2016.
 */
public class GeneratedMap extends GameMap {

    public GeneratedMap(Point dimensions){
        this.mapDimensions = dimensions;
    }

    public void addStaticElement(StaticObject staticObject){
        super.addStaticElement(staticObject);
    }

    public String getBackgroundImageName(){
        return "backgroundgrass";
    }
}
