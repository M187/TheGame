package com.miso.thegame.gameMechanics.map.mapDefinitions;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.objects.movingObjects.Waypoint;
import com.miso.thegame.gameMechanics.objects.movingObjects.enemies.SingleEnemyInitialData;
import com.miso.thegame.gameMechanics.objects.nonMovingObjects.Collectables.Collectible;
import com.miso.thegame.gameMechanics.objects.nonMovingObjects.Obstacles.Obstacle;
import com.miso.thegame.gameMechanics.objects.nonMovingObjects.Obstacles.SquareObstacle;
import com.miso.thegame.gameMechanics.objects.nonMovingObjects.StaticObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by michal.hornak on 16.12.2015.
 */
public abstract class GameMap {

    public static int numberOfObjectivesPresent = 4;
    public ArrayList<SingleEnemyInitialData> enemyDatas = new ArrayList<>();
    protected Point mapDimensions;
    private List<StaticObject> listOfStaticElements = new ArrayList<>();

    public abstract String getBackgroundImageName();

    /**
     * Add new enemies data.
     * @param x
     * @param y
     * @param enemyType
     */
    public void addEnemyData(int x, int y, SingleEnemyInitialData.EnemyType enemyType){
        enemyDatas.add(new SingleEnemyInitialData(x,y,enemyType));
    }
    public void addEnemyData(int x, int y, SingleEnemyInitialData.EnemyType enemyType, ArrayList<Waypoint> waypoints){
        enemyDatas.add(new SingleEnemyInitialData(x,y,enemyType,waypoints));
    }

    /**
     * Add single static element to listOfStaticElements
     * @param staticObject to be added
     */
    protected void addStaticElement(StaticObject staticObject){
        listOfStaticElements.add(staticObject);
    }

    protected void addStaticObstacle(int xGrid, int yGrid, Resources res){
        addStaticElement(new SquareObstacle(res, new Point(xGrid * 100, yGrid * 100)));
    }

    /**
     * Method to iterate through range and add obstacle.
     * @param yCoord contant coordinate for row
     * @param startX coordinate
     * @param finishX coordinate
     * @param res needed to initialize obstacle
     */
    protected void addHorizontalLineOfStaticObstacles( int yCoord, int startX, int finishX, Resources res){
        for (int iter = startX; iter <= finishX; iter++){
            addStaticElement(new SquareObstacle(res, new Point(iter * 100, yCoord * 100)));
        }
    }

    public List<StaticObject> getListOfStaticElements() {
        return listOfStaticElements;
    }

    public void loadNonMovable(MapManager mapManager){
        for (StaticObject staticObject : this.getListOfStaticElements()) {
            if (staticObject instanceof Obstacle) {
                mapManager.getObstaclesList().add((Obstacle) staticObject);
            } else if (staticObject instanceof Collectible){
                mapManager.getCollectibleList().add((Collectible) staticObject);
            }
        }
    }

    public Point getMapDimensions() {
        return mapDimensions;
    }
}
