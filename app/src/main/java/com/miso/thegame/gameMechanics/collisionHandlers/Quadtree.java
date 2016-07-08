package com.miso.thegame.gameMechanics.collisionHandlers;

import android.graphics.Rect;

import com.miso.thegame.gameMechanics.GameObject;
import com.miso.thegame.gameMechanics.map.MapManager;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by michal.hornak on 04.12.2015.
 * <p/>
 * Use this class to split GameObjects into groups that are near on a map.
 * Then perform check for collision on a relevant group only.
 * <p/>
 * This should speed up iterating through arrays.
 */
public class Quadtree {

    private final int MAX_OBJECTS = 10;
    private final int MAX_LEVELS = 5;

    private int level;
    private ArrayList<GameObject> objects;
    private Rect bounds;
    private Quadtree[] nodes;

    /**
     * Constructor
     */
    public Quadtree(int pLevel, Rect pBounds) {
        level = pLevel;
        objects = new ArrayList();
        bounds = pBounds;
        nodes = new Quadtree[4];
    }

    /**
     * Clears the quadtree
     */
    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    /**
     * Splits the node into 4 subnodes
     */
    private void split() {
        int subWidth = (int) (MapManager.getWorldWidth() / (Math.pow(2, level)));
        int subHeight = (int) (MapManager.getWorldHeight() / (Math.pow(2, level)));
        int x = bounds.left;
        int y = bounds.top;

        nodes[0] = new Quadtree(level + 1, new Rect(x, y, x + subWidth, y + subHeight));
        nodes[1] = new Quadtree(level + 1, new Rect(x, y + subHeight, x + subWidth, y + (subHeight * 2)));
        nodes[2] = new Quadtree(level + 1, new Rect(x + subWidth, y, x + subWidth * 2, y + subHeight));
        nodes[3] = new Quadtree(level + 1, new Rect(x + subWidth, y + subHeight, x + subWidth * 2, y + subHeight * 2));
    }

    /**
     * Determine to which child node an object belongs to.
     *
     * 2 ways of calculating this.
     * 1. nonMovableObject has its x,y coordinates as a top left.
     * 2. movableObject has its x,y coordinates as a middle position.
     */
    private PositionIndexer getIndex(GameObject gameObject) {
        switch (gameObject.getCollisionObjectType()) {
            case Obstacle:
            case Collectible:
                return getNonMovableObjectIndex(gameObject);
            default:
                return getMovableObjectIndex(gameObject);
        }
    }

    //<editor-fold desc="Indexing functionality for assignment to child nodes/trees.">

    private PositionIndexer getNonMovableObjectIndex(GameObject gameObject) {

        double midpointXaxis = bounds.left + (MapManager.getWorldWidth() / (Math.pow(2, level)));
        double midpointYaxis = bounds.top + (MapManager.getWorldHeight() / (Math.pow(2, level)));

        PositionIndexer position = new PositionIndexer();
        position.addToLeftSector = ((gameObject.x <= midpointXaxis) ? true : false);
        position.addToRightSector = ((gameObject.x + gameObject.getImage().getWidth() >= midpointXaxis) ? true : false);
        position.addToTopSector = ((gameObject.y <= midpointYaxis) ? true : false);
        position.addToBottomSector = ((gameObject.y + gameObject.getImage().getHeight() >= midpointYaxis) ? true : false);

        return position;
    }

    private PositionIndexer getMovableObjectIndex(GameObject gameObject) {

        double midpointXaxis = bounds.left + (MapManager.getWorldWidth() / (Math.pow(2, level)));
        double midpointYaxis = bounds.top + (MapManager.getWorldHeight() / (Math.pow(2, level)));

        PositionIndexer position = new PositionIndexer();
        position.addToLeftSector = ((gameObject.x - gameObject.getHalfWidth() / 2 <= midpointXaxis) ? true : false);
        position.addToRightSector = ((gameObject.x + gameObject.getHalfWidth() / 2 >= midpointXaxis) ? true : false);
        position.addToTopSector = ((gameObject.y - gameObject.getHalfHeight() / 2 <= midpointYaxis) ? true : false);
        position.addToBottomSector = ((gameObject.y + gameObject.getHalfHeight() / 2 >= midpointYaxis) ? true : false);

        return position;
    }

    /**
     * Add gameObject to relevant child node.
     *
     * @param gameObject to be added.
     * @param positionIndexer data-holder with informations to which nodes object should be added.
     */
    private void addToRelevantChildNodes(GameObject gameObject, PositionIndexer positionIndexer) {
        try {
            if (positionIndexer.addToTopSector) {
                if (positionIndexer.addToLeftSector) {
                    this.nodes[0].insert(gameObject);
                }
                if (positionIndexer.addToRightSector) {
                    this.nodes[2].insert(gameObject);
                }
            }
            if (positionIndexer.addToBottomSector) {
                if (positionIndexer.addToLeftSector) {
                    this.nodes[1].insert(gameObject);
                }
                if (positionIndexer.addToRightSector) {
                    this.nodes[3].insert(gameObject);
                }
            }
        } catch (Exception e) {
            System.out.println("No child nodes! Adding to this node.");
            this.objects.add(gameObject);
        }
    }
    //</editor-fold>

    /**
     * Insert the object into the quadtree. If the node
     * exceeds the capacity, it will split and add all
     * objects to their corresponding nodes.
     */
    public void insert(GameObject gameObject) {
        if (nodes[0] != null) {
            PositionIndexer positionIndexer = getIndex(gameObject);
            addToRelevantChildNodes(gameObject, positionIndexer);
        } else {
            this.objects.add(gameObject);

            if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
                this.split();
                while (0 < objects.size()) {
                    PositionIndexer positionIndexer = getIndex(objects.get(0));
                    addToRelevantChildNodes(objects.get(0), positionIndexer);
                    objects.remove(0);
                }
            }
        }
     }

    /**
     * Return all objects that could collide with the given object
     */
    public Set retrieve(Set returnObjects, GameObject gameObject) {
        PositionIndexer positionIndexer = getIndex(gameObject);
        if (this.nodes[0] != null) {
            if (positionIndexer.addToTopSector) {
                if (positionIndexer.addToLeftSector){
                    this.nodes[0].retrieve(returnObjects, gameObject);
                }
                if (positionIndexer.addToRightSector){
                    this.nodes[2].retrieve(returnObjects, gameObject);
                }
            }
            if (positionIndexer.addToBottomSector) {
                if (positionIndexer.addToLeftSector){
                    this.nodes[1].retrieve(returnObjects, gameObject);
                }
                if (positionIndexer.addToRightSector){
                    this.nodes[3].retrieve(returnObjects, gameObject);
                }
            }
        }

        returnObjects.addAll(objects);

        return returnObjects;
    }

    private class PositionIndexer {

        boolean addToTopSector = false;
        boolean addToBottomSector = false;
        boolean addToLeftSector = false;
        boolean addToRightSector = false;
    }
}
