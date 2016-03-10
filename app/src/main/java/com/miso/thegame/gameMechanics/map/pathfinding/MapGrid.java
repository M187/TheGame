package com.miso.thegame.gameMechanics.map.pathfinding;

import android.graphics.Point;

import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.Obstacle;

/**
 * Created by michal.hornak on 10.12.2015.
 */
public class MapGrid {

    public int numberOfMapTilesWidth;
    public int numberOfMapTilesHeight;
    public MapTile[][] mapGrid;

    public MapGrid() {
        // X axis
        this.numberOfMapTilesWidth = MapManager.getWorldWidth() / MapManager.getMapTileWidth();
        // Y axis
        this.numberOfMapTilesHeight = MapManager.getWorldHeight() / MapManager.getMapTileHeight();

        initMyGrid();
        createMapTileNeighbourLists();
    }

    /**
     * Create whole grid of MapTiles.
     */
    public void initMyGrid() {
        this.mapGrid = new MapTile[this.numberOfMapTilesWidth][this.numberOfMapTilesHeight];

        for (int i = 0; i < this.numberOfMapTilesWidth; i++) {
            for (int j = 0; j < this.numberOfMapTilesHeight; j++) {
                this.mapGrid[i][j] = new MapTile(i, j, this.numberOfMapTilesWidth, this.numberOfMapTilesHeight);
            }
        }
    }

    /**
     * Cretaes map tile neighbour list for each tile in a grid.
     */
    public void createMapTileNeighbourLists() {
        for (int i = 0; i < this.numberOfMapTilesWidth; i++) {
            for (int j = 0; j < this.numberOfMapTilesWidth; j++) {
                try {
                    this.mapGrid[i][j].initializeNeighbourTilesList(this.mapGrid);
                } catch (Exception e) {
                    System.out.print("Error while initializing neighbour list for tile with coords: " + i + " / " + j);
                    throw e;
                }
            }
        }
    }

    /**
     * Add obstacle to mapGrid.
     * Also removes this MapTile from near neighboring array for neighbour position.
     *
     * @param relativeMapTilePosition
     */
    public void addObstacleToMapGrid(Point relativeMapTilePosition, Obstacle obstacle) {
        MapTile currentMapTile = this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x][obstacle.getGridCoordinates().y + relativeMapTilePosition.y];
        currentMapTile.obstaclePresent = true;
        System.out.println("current coords: " + currentMapTile.xTileCoord + ", " + currentMapTile.yTileCoord);
        for (MapTile mapTile : currentMapTile.getNeighbourTiles()) {
            mapTile.getNeighbourTiles().remove(currentMapTile);

        }

        // around obstacle, diagonal movement is prohibited! This should deal with it, removing relevant neighbour diagonal tiles for each horizontal, vertical neighbour.
        if (currentMapTile.xTileCoord != 0 && currentMapTile.xTileCoord != (MapManager.getWorldWidth() / 100) - 1 && currentMapTile.yTileCoord != 0 && currentMapTile.yTileCoord != ( MapManager.getWorldHeight() / 100 ) - 1) {
            // x + 1
            this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x + 1][obstacle.getGridCoordinates().y + relativeMapTilePosition.y].getNeighbourTiles().remove(this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x][obstacle.getGridCoordinates().y + relativeMapTilePosition.y + 1]);
            this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x + 1][obstacle.getGridCoordinates().y + relativeMapTilePosition.y].getNeighbourTiles().remove(this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x][obstacle.getGridCoordinates().y + relativeMapTilePosition.y - 1]);
            // x - 1
            this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x - 1][obstacle.getGridCoordinates().y + relativeMapTilePosition.y].getNeighbourTiles().remove(this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x][obstacle.getGridCoordinates().y + relativeMapTilePosition.y + 1]);
            this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x - 1][obstacle.getGridCoordinates().y + relativeMapTilePosition.y].getNeighbourTiles().remove(this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x][obstacle.getGridCoordinates().y + relativeMapTilePosition.y - 1]);
            // y + 1
            this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x][obstacle.getGridCoordinates().y + relativeMapTilePosition.y + 1].getNeighbourTiles().remove(this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x + 1][obstacle.getGridCoordinates().y + relativeMapTilePosition.y]);
            this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x][obstacle.getGridCoordinates().y + relativeMapTilePosition.y + 1].getNeighbourTiles().remove(this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x - 1][obstacle.getGridCoordinates().y + relativeMapTilePosition.y]);
            // y - 1
            this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x][obstacle.getGridCoordinates().y + relativeMapTilePosition.y - 1].getNeighbourTiles().remove(this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x + 1][obstacle.getGridCoordinates().y + relativeMapTilePosition.y]);
            this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x][obstacle.getGridCoordinates().y + relativeMapTilePosition.y - 1].getNeighbourTiles().remove(this.mapGrid[obstacle.getGridCoordinates().x + relativeMapTilePosition.x - 1][obstacle.getGridCoordinates().y + relativeMapTilePosition.y]);
        }
        //TODO add handler branch for baundry events.
    }
}

