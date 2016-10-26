package com.miso.thegame.gameMechanics.objects.movingObjects.actions.spawning;

import android.graphics.Point;

/**
 * Created by michal.hornak on 05.10.2016.
 *
 * Interface for Spawner class.
 */
public interface Spawning {

    Spawner getSpawner();

    double getDistanceFromPlayer();

    int getSpawningDistanceThreshold();

    Point getPosition();
}
