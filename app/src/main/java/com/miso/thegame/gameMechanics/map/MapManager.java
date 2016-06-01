package com.miso.thegame.gameMechanics.map;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.gameMechanics.map.mapDefinitions.BlankMap;
import com.miso.thegame.gameMechanics.map.mapDefinitions.GameMap;
import com.miso.thegame.gameMechanics.map.mapDefinitions.Level1;
import com.miso.thegame.gameMechanics.map.mapDefinitions.Level2;
import com.miso.thegame.gameMechanics.map.mapDefinitions.SpaceLevel1;
import com.miso.thegame.gameMechanics.map.mapDefinitions.SpaceLevel2;
import com.miso.thegame.gameMechanics.map.pathfinding.MapGrid;
import com.miso.thegame.gameMechanics.map.pathfinding.Pathfinder;
import com.miso.thegame.gameMechanics.movingObjects.enemies.SingleEnemyInitialData;
import com.miso.thegame.gameMechanics.nonMovingObjects.Collectables.Collectible;
import com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.Obstacle;
import com.miso.thegame.gameViews.GameView2;

import java.util.ArrayList;

/**
 * Created by Miso on 8.11.2015.
 */
public class MapManager {

    private static final int mapTileWidth = 100;
    private static final int mapTileHeight = 100;
    private static int worldWidth;
    private static int worldHeight;
    public MapGrid mapGrid;
    public ArrayList<SingleEnemyInitialData> enemyInitialDatas;
    protected Resources res;
    private ArrayList<Obstacle> obstaclesList = new ArrayList<>();
    private ArrayList<Collectible> collectibleList = new ArrayList<>();
    private GameMap currentMap;

    public MapManager(GameMapEnum levelName, Resources res) {

        initializeMap(levelName, res);

        this.mapGrid = new MapGrid();
        new Pathfinder(mapGrid);

        // Iterate through obstacle and set belonging grids obstacle present to true
        for (Obstacle obstacle : obstaclesList) {
            for (Point relativeTilePosition : obstacle.getRelativeTilePositions()) {
                mapGrid.addObstacleToMapGrid(relativeTilePosition, obstacle);
            }
        }
    }

    public static int getWorldWidth() {
        return worldWidth;
    }

    public static int getWorldHeight() {
        return worldHeight;
    }

    public static int getMapTileWidth() {
        return mapTileWidth;
    }

    public static int getMapTileHeight() {
        return mapTileHeight;
    }

    private void initializeMap(GameMapEnum mapName, Resources res) {

        GameMap gameMap = null;
        switch (mapName){
            case BlankMap:
                gameMap = new BlankMap(res);
                break;
            case SpaceLevel1:
                gameMap = new SpaceLevel1(res);
                break;
            case SpaceLevel2:
                gameMap = new SpaceLevel2(res);
                break;
            case Level1:
                gameMap = new Level1(res);
                break;
            case Level2:
                gameMap = new Level2(res);
                break;
            default:
                gameMap = new BlankMap(res);
                break;
        }

        this.currentMap = gameMap;
        this.currentMap.loadNonMovable(this);
        this.worldWidth = this.currentMap.getMapDimensions().x;
        this.worldHeight = this.currentMap.getMapDimensions().y;
        this.enemyInitialDatas = gameMap.enemyDatas;
    }

    public void draw(Canvas canvas) {
        for (Obstacle obstacle : getObstaclesList()) {
            GameView2.drawManager.drawOnDisplay(obstacle, canvas);
        }
        for (Collectible collectible : getCollectibleList()) {
            GameView2.drawManager.drawOnDisplay(collectible, canvas);
        }
    }

    public ArrayList<Obstacle> getObstaclesList() {
        return obstaclesList;
    }

    public ArrayList<Collectible> getCollectibleList() {
        return collectibleList;
    }
}