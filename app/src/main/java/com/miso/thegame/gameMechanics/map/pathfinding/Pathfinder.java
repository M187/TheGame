package com.miso.thegame.gameMechanics.map.pathfinding;


import android.graphics.Point;

import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.objects.GameObject;
import com.miso.thegame.gameMechanics.objects.movingObjects.MovableObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by michal.hornak on 11.12.2015.
 */
public class Pathfinder {

    public static MapGrid mapGrid;
    protected static Point lastPositionForMapGridDistanceCostUpdate = new Point(-1, -1);

    public Pathfinder(MapGrid mapGrid) {
        this.mapGrid = mapGrid;
    }

    public static class FindPath {

        /**
         * Function to return List of Maptiles as a path towards target.
         *
         * @param mO          object for which we want to calculate path.
         * @param destination object's destination.
         * @return List of tiles object must go through to reach target.
         */
        public static List<MapTile> findPath(MovableObject mO, GameObject destination) {
            List<MapTile> openList = new LinkedList<>();
            List<MapTile> closedList = new LinkedList<>();
            List<MapTile> neighbours = new ArrayList<>();
            Point objectTileCoord = mO.getGridCoordinates();
            Point destinationTileCoord = destination.getGridCoordinates();
            MapTile currentTile;
            try {
                currentTile = mapGrid.mapGrid[objectTileCoord.x][objectTileCoord.y];
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.print("Error while getting current tile for pathfinding. Trying to adapt coords.");
                if (mO.getX() >= MapManager.getWorldWidth()) {
                    objectTileCoord.x -= 1;
                }
                if (mO.getY() >= MapManager.getWorldHeight()) {
                    objectTileCoord.y -= 1;
                }
                if (mO.getY() >= MapManager.getWorldHeight() && mO.getX() >= MapManager.getWorldWidth()) {
                    objectTileCoord.x -= 1;
                    objectTileCoord.y -= 1;
                }
                currentTile = mapGrid.mapGrid[objectTileCoord.x][objectTileCoord.y];
            }

            currentTile.setParentMapTile(null);
            currentTile.totalMovementCost = 0;
            openList.add(currentTile);

            boolean notDone = true;

            while (notDone) {
                neighbours.clear();
                currentTile = getLowestCostTileFromOpenList(openList, currentTile);
                closedList.add(currentTile);
                openList.remove(currentTile);

                //ReachedGoal?
                if ((currentTile.xTileCoord == destinationTileCoord.x) && (currentTile.yTileCoord == destinationTileCoord.y)) {
                    try {
                        return getResultListOfMapTiles(currentTile);
                    } catch (Exception e) {
                        System.out.println("closed list size: " + closedList.size());
                        throw e;
                    }
                }

                neighbours.addAll(currentTile.getNeighbourTiles());
                neighbours.removeAll(closedList);

                for (MapTile mapTile : neighbours) {
                    if (openList.contains(mapTile)) {
                        // compare total movement costs.
                        if (mapTile.totalMovementCost > currentTile.getMovementCostToTile(mapTile) + currentTile.totalMovementCost) {
                            mapTile.setParentMapTile(currentTile);
                            mapTile.totalMovementCost = currentTile.getMovementCostToTile(mapTile) + currentTile.totalMovementCost;
                        }
                    } else {
                        mapTile.setParentMapTile(currentTile);
                        mapTile.totalMovementCost = currentTile.totalMovementCost + currentTile.getMovementCostToTile(mapTile);
                        openList.add(mapTile);
                    }
                }
            }
            return null;
        }

        private static List<MapTile> getResultListOfMapTiles(MapTile tile) {
            List<MapTile> returnList = new ArrayList<>();
            returnList.add(tile);
            if (tile.parentTile != null) {
                getResultListOfMapTiles(tile.parentTile, returnList);
            }
            return returnList;
        }

        private static void getResultListOfMapTiles(MapTile tile, List<MapTile> returnList) {
            returnList.add(tile);
            if (tile.parentTile != null) {
                getResultListOfMapTiles(tile.parentTile, returnList);
            }
        }

        private static MapTile getLowestCostTileFromOpenList(List<MapTile> openList, MapTile currentMapTile) {

            int iteratingTileCost;
            MapTile iteratingTile;

            if (openList.size() == 0){
                System.out.println("We are fucked general!");
            }

            int lowestCost = openList.get(0).distanceFromTarget + openList.get(0).getMovementCostToTile(currentMapTile);
            MapTile lowestCostTile = openList.get(0);

            for (int i = 1; i < openList.size(); i++) {
                iteratingTile = openList.get(i);
                iteratingTileCost = iteratingTile.distanceFromTarget + iteratingTile.getMovementCostToTile(currentMapTile);
                if (iteratingTileCost < lowestCost) {
                    lowestCost = iteratingTileCost;
                    lowestCostTile = iteratingTile;
                }
            }

            return lowestCostTile;
        }

        /**
         * Enrich mapGrid object with distance cost, based on specified gameObject.
         * If object is in same tile as a last one used, it does not perform cost calculations.
         *
         * @param gO to perform costAssignment to mapGrid
         */
        public static void updateMapGridDistanceCost(GameObject gO) {

            Point gridCoords = gO.getGridCoordinates();
            int xGridCoord = gridCoords.x;
            int yGridCoord = gridCoords.y;

            if (lastPositionForMapGridDistanceCostUpdate.equals(gridCoords)) {
                return;
            } else {
                for (int i = 0; i < mapGrid.numberOfMapTilesWidth; i++) {
                    for (int j = 0; j < mapGrid.numberOfMapTilesWidth; j++) {

                        int tileDeltaX = Math.abs(i - xGridCoord);
                        int tileDeltaY = Math.abs(j - yGridCoord);

                        if (tileDeltaX >= tileDeltaY) {
                            mapGrid.mapGrid[i][j].setDistanceFromTarget(tileDeltaX);
                        } else {
                            mapGrid.mapGrid[i][j].setDistanceFromTarget(tileDeltaY);
                        }
                    }
                }
                lastPositionForMapGridDistanceCostUpdate.x = xGridCoord;
                lastPositionForMapGridDistanceCostUpdate.y = yGridCoord;
            }

        }
    }
}
