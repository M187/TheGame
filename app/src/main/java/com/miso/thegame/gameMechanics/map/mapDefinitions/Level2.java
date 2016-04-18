package com.miso.thegame.gameMechanics.map.mapDefinitions;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.nonMovingObjects.Collectables.BaseCamp;
import com.miso.thegame.gameMechanics.nonMovingObjects.Collectables.StarCollectible;

import static com.miso.thegame.gameMechanics.movingObjects.enemies.SingleEnemyInitialData.EnemyType;

/**
 * Created by Miso on 20.1.2016.
 */
public class Level2 extends GameMap {

    public Level2(Resources res){

        this.mapDimensions = new Point(4000,4000);

        //<editor-fold desc=" -↓ Unpassable obstacles ↓-">
        addHorizontalLineOfStaticObstacles(0, 6, 0, res);
        addHorizontalLineOfStaticObstacles(0, 15, 16, res);
        addHorizontalLineOfStaticObstacles(0, 19, 20, res);

        addStaticObstacle(0, 1, res);
        addHorizontalLineOfStaticObstacles(1, 4, 5, res);
        addStaticObstacle(20, 1, res);
        addHorizontalLineOfStaticObstacles(1, 28, 36, res);

        addStaticObstacle(0, 2, res);
        addStaticObstacle(5, 2, res);
        addStaticObstacle(6, 2, res);
        addStaticObstacle(9, 2, res);
        addStaticObstacle(13, 2, res);
        addStaticObstacle(17, 2, res);
        addStaticObstacle(28, 2, res);
        addStaticObstacle(36, 2, res);

        addStaticObstacle(0, 3, res);
        addHorizontalLineOfStaticObstacles(3, 6, 10, res);
        addStaticObstacle(17, 3, res);
        addStaticObstacle(23, 3, res);
        addStaticObstacle(27, 3, res);
        addStaticObstacle(28, 3, res);
        addHorizontalLineOfStaticObstacles(3, 31, 36, res);

        addStaticObstacle(0, 4, res);
        addStaticObstacle(1, 4, res);
        addHorizontalLineOfStaticObstacles(4, 7, 9, res);
        addHorizontalLineOfStaticObstacles(4, 16, 18, res);
        addHorizontalLineOfStaticObstacles(4, 23, 27, res);
        addStaticObstacle(31, 4, res);

        addHorizontalLineOfStaticObstacles(5, 0, 2, res);
        addHorizontalLineOfStaticObstacles(5, 15, 18, res);
        addStaticObstacle(24, 5, res);
        addStaticObstacle(30, 5, res);
        addStaticObstacle(31, 5, res);
        addHorizontalLineOfStaticObstacles(5, 33, 37, res);

        addHorizontalLineOfStaticObstacles(6, 0, 3, res);
        addStaticObstacle(12, 6, res);
        addStaticObstacle(16, 6, res);
        addStaticObstacle(17, 6, res);
        addStaticObstacle(29, 6, res);
        addStaticObstacle(30, 6, res);
        addStaticObstacle(33, 6, res);
        addHorizontalLineOfStaticObstacles(6, 36, 38, res);

        addStaticObstacle(0, 7, res);
        addStaticObstacle(1, 7, res);
        addStaticObstacle(3, 7, res);
        addStaticObstacle(4, 7, res);
        addStaticObstacle(16, 7, res);
        addHorizontalLineOfStaticObstacles(7, 27, 30, res);
        addStaticObstacle(32, 7, res);
        addStaticObstacle(33, 7, res);
        addStaticObstacle(37, 7, res);
        addStaticObstacle(38, 7, res);

        addStaticObstacle(0, 8, res);
        addHorizontalLineOfStaticObstacles(8, 4, 9, res);
        addStaticObstacle(15, 8, res);
        addStaticObstacle(16, 8, res);
        addStaticObstacle(26, 8, res);
        addStaticObstacle(27, 8, res);
        addHorizontalLineOfStaticObstacles(8, 32, 34, res);
        addStaticObstacle(37, 8, res);

        addHorizontalLineOfStaticObstacles(9, 5, 9, res);
        addStaticObstacle(14, 9, res);
        addStaticObstacle(15, 9, res);
        addStaticObstacle(19, 9, res);
        addStaticObstacle(20, 9, res);
        addStaticObstacle(26, 9, res);
        addStaticObstacle(27, 9, res);
        addHorizontalLineOfStaticObstacles(9, 31, 33, res);
        addStaticObstacle(37, 9, res);

        addStaticObstacle(6, 10, res);
        addStaticObstacle(13, 10, res);
        addStaticObstacle(14, 10, res);
        addStaticObstacle(19, 10, res);
        addStaticObstacle(20, 10, res);
        addHorizontalLineOfStaticObstacles(10, 25, 27, res);
        addStaticObstacle(31, 10, res);
        addStaticObstacle(36, 10, res);
        addStaticObstacle(37, 10, res);

        addStaticObstacle(12, 11, res);
        addStaticObstacle(13, 11, res);
        addHorizontalLineOfStaticObstacles(11, 24, 31, res);
        addStaticObstacle(35, 11, res);
        addStaticObstacle(36, 11, res);

        addHorizontalLineOfStaticObstacles(12, 4, 8, res);
        addStaticObstacle(12, 12, res);
        addStaticObstacle(13, 12, res);
        addHorizontalLineOfStaticObstacles(12, 25, 30, res);
        addStaticObstacle(34, 12, res);
        addStaticObstacle(35, 12, res);

        addHorizontalLineOfStaticObstacles(13, 3, 4, res);
        addHorizontalLineOfStaticObstacles(13, 7, 17, res);
        addHorizontalLineOfStaticObstacles(13, 24, 29, res);
        addHorizontalLineOfStaticObstacles(13, 34, 36, res);

        addStaticObstacle(0, 14, res);
        addHorizontalLineOfStaticObstacles(14, 2, 3, res);
        addHorizontalLineOfStaticObstacles(14, 9, 14, res);
        addStaticObstacle(17, 14, res);
        addStaticObstacle(18, 14, res);
        addHorizontalLineOfStaticObstacles(14, 26, 28, res);
        addHorizontalLineOfStaticObstacles(14, 33, 37, res);

        addStaticObstacle(0, 15, res);
        addStaticObstacle(3, 15, res);
        addStaticObstacle(4, 15, res);
        addHorizontalLineOfStaticObstacles(15, 11, 14, res);
        addStaticObstacle(17, 15, res);
        addHorizontalLineOfStaticObstacles(15, 27, 29, res);
        addHorizontalLineOfStaticObstacles(15, 34, 38, res);

        addStaticObstacle(0, 16, res);
        addStaticObstacle(1, 16, res);
        addHorizontalLineOfStaticObstacles(16, 4, 7, res);
        addHorizontalLineOfStaticObstacles(16, 12, 14, res);
        addStaticObstacle(28, 16, res);
        addHorizontalLineOfStaticObstacles(16, 35, 37, res);

        addStaticObstacle(0, 17, res);
        addStaticObstacle(5, 17, res);
        addStaticObstacle(6, 17, res);
        addStaticObstacle(13, 17, res);
        addStaticObstacle(13, 36, res);

    // vertical half of a map reached

        addStaticObstacle(29, 21, res);

        addStaticObstacle(7, 22, res);
        addHorizontalLineOfStaticObstacles(22, 28, 30, res);

        addStaticObstacle(5, 23, res);
        addStaticObstacle(9, 23, res);
        addHorizontalLineOfStaticObstacles(23, 26, 29, res);

        addStaticObstacle(3, 24, res);
        addStaticObstacle(7, 24, res);
        addStaticObstacle(27, 24, res);
        addStaticObstacle(39, 24, res);

        addStaticObstacle(1, 25, res);
        addStaticObstacle(5, 25, res);
        addStaticObstacle(9, 25, res);
        addStaticObstacle(38, 25, res);
        addStaticObstacle(39, 25, res);

        addStaticObstacle(3, 26, res);
        addStaticObstacle(7, 26, res);
        addStaticObstacle(36, 26, res);
        addStaticObstacle(39, 26, res);

        addStaticObstacle(1, 27, res);
        addStaticObstacle(5, 27, res);
        addStaticObstacle(11, 27, res);
        addStaticObstacle(29, 27, res);
        addStaticObstacle(32, 27, res);
        addStaticObstacle(33, 27, res);
        addHorizontalLineOfStaticObstacles(27, 35, 37, res);

        addStaticObstacle(3, 28, res);
        addStaticObstacle(8, 28, res);
        addStaticObstacle(22, 28, res);
        addHorizontalLineOfStaticObstacles(28, 27, 32, res);
        addStaticObstacle(36, 28, res);

        addStaticObstacle(5, 29, res);
        addStaticObstacle(17, 29, res);
        addStaticObstacle(18, 29, res);
        addStaticObstacle(29, 29, res);
        addStaticObstacle(30, 29, res);

        addStaticObstacle(10, 30, res);
        addStaticObstacle(17, 30, res);
        addStaticObstacle(18, 30, res);
        addStaticObstacle(19, 30, res);
        addStaticObstacle(29, 30, res);

        addHorizontalLineOfStaticObstacles(31, 15, 20, res);
        addStaticObstacle(28, 31, res);
        addStaticObstacle(29, 31, res);

        addHorizontalLineOfStaticObstacles(32, 15, 22, res);
        addStaticObstacle(29, 32, res);
        addStaticObstacle(36, 32, res);
        addStaticObstacle(37, 32, res);
        addStaticObstacle(39, 32, res);

        addHorizontalLineOfStaticObstacles(33, 13, 21, res);
        addStaticObstacle(29, 33, res);
        addStaticObstacle(36, 33, res);
        addStaticObstacle(39, 33, res);

        addStaticObstacle(5, 34, res);
        addHorizontalLineOfStaticObstacles(34, 14, 20, res);

        addHorizontalLineOfStaticObstacles(35, 4, 6, res);
        addHorizontalLineOfStaticObstacles(35, 15, 20, res);
        addStaticObstacle(29, 35, res);
        addStaticObstacle(33, 35, res);
        addStaticObstacle(34, 35, res);

        addStaticObstacle(0, 36, res);
        addStaticObstacle(1, 36, res);
        addStaticObstacle(5, 36, res);
        addStaticObstacle(17, 36, res);
        addStaticObstacle(18, 36, res);
        addStaticObstacle(28, 36, res);
        addStaticObstacle(29, 36, res);
        addStaticObstacle(33, 36, res);

        addStaticObstacle(11, 37, res);
        addStaticObstacle(17, 37, res);
        addStaticObstacle(18, 37, res);
        addStaticObstacle(27, 37, res);
        addStaticObstacle(28, 37, res);

        addStaticObstacle(2, 38, res);
        addHorizontalLineOfStaticObstacles(38, 10, 12, res);
        addHorizontalLineOfStaticObstacles(38, 25, 27, res);
        addStaticObstacle(33, 38, res);
        addStaticObstacle(34, 38, res);

        addStaticObstacle(2, 39, res);
        addHorizontalLineOfStaticObstacles(39, 9, 13, res);
        addHorizontalLineOfStaticObstacles(39, 24, 26, res);
        addStaticObstacle(33, 39, res);
        addStaticObstacle(34, 39, res);
    //</editor-fold>

        //Collectibles
        addStaticElement(new BaseCamp(res, new Point(2000, 2000)));

        addStaticElement(new StarCollectible(res, new Point(200, 200)));// top left
        addStaticElement(new StarCollectible(res, new Point(2900, 900)));// top right
        addStaticElement(new StarCollectible(res, new Point(100, 3800)));// bottom left
        addStaticElement(new StarCollectible(res, new Point(3600, 3600)));// bottom right
        addStaticElement(new StarCollectible(res, new Point(2200, 2000)));// middle - test purposes

        //Enemies data
        this.addEnemyData(200, 200, EnemyType.nest);
        this.addEnemyData(2900, 900, EnemyType.nest);
        this.addEnemyData(100, 3800, EnemyType.nest);
        this.addEnemyData(3600, 3600, EnemyType.nest);
    }
}
