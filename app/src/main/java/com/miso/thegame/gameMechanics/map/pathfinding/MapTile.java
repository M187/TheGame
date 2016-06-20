package com.miso.thegame.gameMechanics.map.pathfinding;

import android.graphics.Point;

import com.miso.thegame.gameMechanics.map.MapManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.hornak on 10.12.2015.
 *
 * x - coordinte for width of world
 * y - coordinate for height of world
 */
public class MapTile {

    private final int diagonalMovementCost = 18;
    private final int straightMovementCost = 10;
    private final int tileConstant = 10;

    public boolean obstaclePresent = false;
    public int distanceFromTarget;
    public MapTile parentTile;
    public int xTileCoord;
    public int yTileCoord;
    public int totalMovementCost = 0;

    public List<Point> neighbourPositions = new ArrayList<>();
    public List<Point> notDiagonalNeighbours = new ArrayList<>();
    private List<MapTile> neighbourTiles = new ArrayList<>();

    public MapTile(int xTileCoord, int yTileCoord, int maxXTileCount, int maxYTileCount){
        this.xTileCoord = xTileCoord;
        this.yTileCoord = yTileCoord;

        initializeNeighbourPositionListBasedOnX(maxXTileCount, maxYTileCount);
        initializeNotDiagonalNeighbours(maxXTileCount, maxYTileCount);
    }

    /**
     * Creates list of Points with x/y axis values of neighboring positions.
     * Only vertical/horizontal neighbours.
     * @param maxX - maximal value for x axis.
     * @param maxY - maximal value for y axis
     */
    private void initializeNotDiagonalNeighbours(int maxX, int maxY){
        if (this.xTileCoord == 0){
            if (this.yTileCoord == 0){
                notDiagonalNeighbours.add(new Point(this.xTileCoord + 1,this.yTileCoord));
                notDiagonalNeighbours.add(new Point(this.xTileCoord,this.yTileCoord + 1));
            } else if (this.yTileCoord == maxY){
                notDiagonalNeighbours.add(new Point(this.xTileCoord + 1,this.yTileCoord));
                notDiagonalNeighbours.add(new Point(this.xTileCoord,this.yTileCoord - 1));
            } else {
                notDiagonalNeighbours.add(new Point(this.xTileCoord + 1,this.yTileCoord));
                notDiagonalNeighbours.add(new Point(this.xTileCoord,this.yTileCoord + 1));
                notDiagonalNeighbours.add(new Point(this.xTileCoord,this.yTileCoord - 1));
            }
        } else if (this.xTileCoord == maxX){
            if (this.yTileCoord == 0){
                notDiagonalNeighbours.add(new Point(this.xTileCoord,this.yTileCoord + 1));
                notDiagonalNeighbours.add(new Point(this.xTileCoord - 1,this.yTileCoord));
            } else if (this.yTileCoord == maxY){
                notDiagonalNeighbours.add(new Point(this.xTileCoord - 1,this.yTileCoord));
                notDiagonalNeighbours.add(new Point(this.xTileCoord,this.yTileCoord - 1));
            } else {
                notDiagonalNeighbours.add(new Point(this.xTileCoord,this.yTileCoord + 1));
                notDiagonalNeighbours.add(new Point(this.xTileCoord - 1,this.yTileCoord));
                notDiagonalNeighbours.add(new Point(this.xTileCoord,this.yTileCoord - 1));
            }
        } else {
            if (this.yTileCoord == 0){
                notDiagonalNeighbours.add(new Point(this.xTileCoord + 1,this.yTileCoord));
                notDiagonalNeighbours.add(new Point(this.xTileCoord,this.yTileCoord + 1));
                notDiagonalNeighbours.add(new Point(this.xTileCoord - 1,this.yTileCoord));
            } else if (this.yTileCoord == maxY){
                notDiagonalNeighbours.add(new Point(this.xTileCoord + 1,this.yTileCoord));
                notDiagonalNeighbours.add(new Point(this.xTileCoord - 1,this.yTileCoord));
                notDiagonalNeighbours.add(new Point(this.xTileCoord,this.yTileCoord - 1));
            } else {
                notDiagonalNeighbours.add(new Point(this.xTileCoord + 1,this.yTileCoord));
                notDiagonalNeighbours.add(new Point(this.xTileCoord,this.yTileCoord + 1));
                notDiagonalNeighbours.add(new Point(this.xTileCoord - 1,this.yTileCoord));
                notDiagonalNeighbours.add(new Point(this.xTileCoord,this.yTileCoord - 1));
            }
        }
    }

    /**
     * Creates list of Points with x/y axis values of neighboring positions.
     * @param maxX - maximal value for x axis.
     * @param maxY - maximal value for y axis
     */
    private void initializeNeighbourPositionListBasedOnX(int maxX, int maxY){
        if (this.xTileCoord == 0){
            initializeNeighbourPositionListBasedOnY(maxY, new int[]{0,1});
        } else if (this.xTileCoord == maxX - 1){
            initializeNeighbourPositionListBasedOnY(maxY, new int[]{0,-1});
        } else {
            initializeNeighbourPositionListBasedOnY(maxY, new int[]{-1,0,1});
        }
        neighbourPositions.remove(new Point(this.xTileCoord, this.yTileCoord));
    }

    private void initializeNeighbourPositionListBasedOnY(int maxY, int[] xCoords){
        for (int xCoord : xCoords){
            if (this.yTileCoord == 0){
                neighbourPositions.add(new Point(this.xTileCoord + xCoord, this.yTileCoord + 1));
                neighbourPositions.add(new Point(this.xTileCoord + xCoord, this.yTileCoord));
            } else if (this.yTileCoord == maxY - 1){
                neighbourPositions.add(new Point(this.xTileCoord + xCoord, this.yTileCoord - 1));
                neighbourPositions.add(new Point(this.xTileCoord + xCoord, this.yTileCoord));
            } else {
                neighbourPositions.add(new Point(this.xTileCoord + xCoord, this.yTileCoord - 1));
                neighbourPositions.add(new Point(this.xTileCoord + xCoord, this.yTileCoord));
                neighbourPositions.add(new Point(this.xTileCoord + xCoord, this.yTileCoord + 1));
            }
        }
    }

    /**
     * Creates list of all tiles in neighborhood.
     * Tiles that contains obstacle are not added into List holding neighbour tiles.
     *
     * @param mapGrid that contains all MapTiles.
     */
    public void initializeNeighbourTilesList(MapTile[][] mapGrid){
        for (Point point : neighbourPositions){
            MapTile tempTile = mapGrid[point.x][point.y];
            if ( ! tempTile.obstaclePresent ) {
                this.neighbourTiles.add(tempTile);
            }
        }
    }

    /**
     * Set parent map tile.
     * @param mapTile
     */
    public void setParentMapTile(MapTile mapTile){
        this.parentTile = mapTile;
    }

    /**
     * Sets distance from target MapTile. Used later in A* pathfinding algorithm.
     * @param distanceInTiles int as a distance in tiles
     */
    public void setDistanceFromTarget(int distanceInTiles){
        this.distanceFromTarget = distanceInTiles * tileConstant;
    }

    /**
     * Get movement cost from this tile to neighbor tile based on a position.
     *
     * @param destinationMapTile neighbour tile to which we want to get movement cost.
     * @return integer as a movement cost - 2 values, straight or diagonal cost.
     */
    public int getMovementCostToTile(MapTile destinationMapTile){
        if (this.xTileCoord == destinationMapTile.xTileCoord || this.yTileCoord == destinationMapTile.yTileCoord){
            return straightMovementCost;
        } else {
            return diagonalMovementCost;
        }
    }

    /**
     * Get list of neighbour tiles.
     * @return List of tiles in neighborhood.
     */
    public List<MapTile> getNeighbourTiles() {
        return this.neighbourTiles;
    }

    /**
     * Get middle position of a tile.
     * @return current tile grid coordinates converted to global coordinates.
     *      Also adding half of tile width/height since we need middle coordinates.
     */
    public Point getTileMiddlePosition(){
        return new Point(((xTileCoord * 2 + 1) * MapManager.getMapTileHalfWidth()), (yTileCoord * 2 + 1) * (MapManager.getMapTileHalfHeight()));
    }
}
