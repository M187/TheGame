package com.miso.thegame.gameMechanics.map.generator;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.map.mapDefinitions.GameMap;
import com.miso.thegame.gameMechanics.map.mapDefinitions.GeneratedMap;
import com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.SquareObstacle;

import java.util.Random;

/**
 * Created by Miso on 8.9.2016.
 */
public class MapGenerator {

    public static GameMap generateMap(Resources res, Point dimensions, int level) {

        Random random = new Random();
        GeneratedMap generatedMap = new GeneratedMap(dimensions);

        for (int i = 0; i < dimensions.x / 500; i++) {
            for (int j = 0; j < dimensions.y / 500; j++) {

                generatedMap.addStaticElement(
                        new SquareObstacle(
                                res,
                                new Point(
                                        500 * i + random.nextInt(5) * 100,
                                        500 * j + random.nextInt(5) * 100)
                        )
                );
            }
        }


        return generatedMap;
    }

}
