package com.miso.thegame.gameMechanics;

import android.graphics.Rect;

import com.miso.thegame.GameObject;
import com.miso.thegame.gameMechanics.map.MapManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.hornak on 04.12.2015.
 *
 * Use this class to split GameObjects into groups that are near on a map.
 * Then perform check for collision on a relevant group only.
 *
 * This should speed up iterating through arrays.
 */
public class Quadtree {

    private int MAX_OBJECTS = 10;
    private int MAX_LEVELS = 50;

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
        int subWidth = (int) (MapManager.getWorldWidth() / (2 * level));
        int subHeight = (int) (MapManager.getWorldHeight() / (2 * level));
        int x = bounds.left;
        int y = bounds.top;

        nodes[0] = new Quadtree(level + 1, new Rect(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level + 1, new Rect(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level + 1, new Rect(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level + 1, new Rect(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    /**
     * Determine which node the object belongs to. -1 means
     * object cannot completely fit within a child node and is part
     * of the parent node
     */
    private int getIndex(GameObject gO) {
        int index = -1;
        double verticalMidpoint = bounds.left + (MapManager.getWorldWidth() / 2);
        double horizontalMidpoint = bounds.top + (MapManager.getWorldHeight() / 2);

        // Object can completely fit within the top quadrants
        boolean topQuadrant = (gO.x < horizontalMidpoint && gO.x + gO.getImage().getWidth() < horizontalMidpoint);
        // Object can completely fit within the bottom quadrants
        boolean bottomQuadrant = (gO.x > horizontalMidpoint);

        // Object can completely fit within the left quadrants
        if (gO.y < verticalMidpoint && gO.y + gO.getImage().getHeight() < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
        }
        // Object can completely fit within the right quadrants
        else if (gO.x > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }

        return index;
    }

    /**
     * Insert the object into the quadtree. If the node
     * exceeds the capacity, it will split and add all
     * objects to their corresponding nodes.
     */
    public void insert(GameObject pRect) {
        if (nodes[0] != null) {
            int index = getIndex(pRect);

            if (index != -1) {
                nodes[index].insert(pRect);

                return;
            }
        }

        objects.add(pRect);

        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < objects.size()) {
                int index = getIndex(objects.get(i));
                if (index != -1) {
                    nodes[index].insert(objects.remove(i));
                } else {
                    i++;
                }
            }
        }
    }

    /**
     * Return all objects that could collide with the given object
     */
    public List retrieve(List returnObjects, GameObject pRect) {
        int index = getIndex(pRect);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnObjects, pRect);
        }

        returnObjects.addAll(objects);

        return returnObjects;
    }
}
