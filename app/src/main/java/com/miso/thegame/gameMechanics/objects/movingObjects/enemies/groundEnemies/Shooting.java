package com.miso.thegame.gameMechanics.objects.movingObjects.enemies.groundEnemies;

import android.graphics.Point;

/**
 * Created by michal.hornak on 03.10.2016.
 */
public interface Shooting {

    Shooter getShooter();

    double getDistanceFromPlayer();

    int getShootingDistanceThreshold();

    Point getPosition();
}
