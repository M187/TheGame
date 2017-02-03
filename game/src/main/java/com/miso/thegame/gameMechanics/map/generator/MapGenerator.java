package com.miso.thegame.gameMechanics.map.generator;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.map.mapDefinitions.GameMap;
import com.miso.thegame.gameMechanics.map.mapDefinitions.GeneratedMap;
import com.miso.thegame.gameMechanics.objects.movingObjects.enemies.SingleEnemyInitialData;
import com.miso.thegame.gameMechanics.objects.nonMovingObjects.Collectables.BaseCamp;

import java.util.Random;

/**
 * Created by Miso on 8.9.2016.
 */
public class MapGenerator {

    public static GameMap generateMap(Resources res, Point dim, int level) {

        GeneratedMap generatedMap = new GeneratedMap(dim);

        generateEnemyBaseData(generatedMap, dim, level);

        generatedMap.addStaticElement(new BaseCamp(res, new Point(dim.x/2, dim.y/2)));

        return generatedMap;
    }

    private static void generateEnemyBaseData(GeneratedMap generatedMap, Point dim, int level) {

        switch (level) {
            case 1:
                generateEnemyData1(generatedMap, dim);
                break;
            case 2:
                generateEnemyData2(generatedMap, dim);
                break;
            case 3:
                generateEnemyData3(generatedMap, dim);
                break;
            case 4:
                generateEnemyData4(generatedMap, dim);
                break;
            case 5:
                generateEnemyData5(generatedMap, dim);
                break;
            case 6:
                generateEnemyData6(generatedMap, dim);
                break;
        }
    }

    private static void generateEnemyData1(GeneratedMap generatedMap, Point dim) {
        Random random = new Random();
        int x, y;
        if (random.nextBoolean()) {
            x = dim.x / 2;
            y = (random.nextBoolean()) ? (int) GameView2.scaleSize(50) : dim.y - (int) GameView2.scaleSize(50);
        } else {
            y = dim.y / 2;
            x = (random.nextBoolean()) ? (int) GameView2.scaleSize(50) : dim.x - (int) GameView2.scaleSize(50);
        }
        generatedMap.addEnemyData(x, y, SingleEnemyInitialData.EnemyType.nest);
    }

    private static void generateEnemyData2(GeneratedMap generatedMap, Point dim) {
        Random random = new Random();
        if (random.nextBoolean()) {
            if (random.nextBoolean()) {
                generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);
            } else {
                generatedMap.addEnemyData(dim.x / 2, 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(dim.x / 2, dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            }
        } else {
            if (random.nextBoolean()) {
                generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            } else {
                generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            }
        }
    }

    private static void generateEnemyData3(GeneratedMap generatedMap, Point dim) {
        Random random = new Random();
        if (random.nextBoolean()) {
            if (random.nextBoolean()) {
                generatedMap.addEnemyData(dim.x / 2, 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            } else {
                generatedMap.addEnemyData(dim.x / 2, dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            }
        } else {
            if (random.nextBoolean()) {
                generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            } else {
                generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            }
        }
    }

    private static void generateEnemyData4(GeneratedMap generatedMap, Point dim) {
        Random random = new Random();
        if (random.nextBoolean()) {
            generatedMap.addEnemyData(dim.x / 2, 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(dim.x / 2, dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);
        } else {
            generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50) , dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
        }
    }

    private static void generateEnemyData5(GeneratedMap generatedMap, Point dim) {
        Random random = new Random();
        if (random.nextBoolean()) {
            generatedMap.addEnemyData(dim.x / 2, 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(dim.x / 2, dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);

            switch (random.nextInt(3)){
                case 0:
                    generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y -(int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                    break;
                case 1:
                    generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                    break;
                case 2:
                    generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                    break;
                case 3:
                    generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                    break;
            }

        } else {
            generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50) , dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);

            switch (random.nextInt(3)){
                case 0:
                    generatedMap.addEnemyData(dim.x / 2, dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                    break;
                case 1:
                    generatedMap.addEnemyData(dim.x / 2, 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                    break;
                case 2:
                    generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);
                    break;
                case 3:
                    generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);
                    break;
            }
        }
    }

    private static void generateEnemyData6(GeneratedMap generatedMap, Point dim) {
        Random random = new Random();
        if (random.nextBoolean()) {
            generatedMap.addEnemyData(dim.x / 2, 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(dim.x / 2, dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);

            if (random.nextBoolean()){
                generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            } else {
                generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            }

        } else {
            generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50) , dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);

            if (random.nextBoolean()){
                generatedMap.addEnemyData(dim.x / 2, dim.y - (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(dim.x / 2, 0 + (int) GameView2.scaleSize(50), SingleEnemyInitialData.EnemyType.nest);
            } else {
                generatedMap.addEnemyData(dim.x - (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);
                generatedMap.addEnemyData(0 + (int) GameView2.scaleSize(50), dim.y / 2, SingleEnemyInitialData.EnemyType.nest);
            }
        }
    }
}
