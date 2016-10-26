package com.miso.thegame.gameMechanics.objects.movingObjects.actions.shooting;

import android.graphics.Point;

/**
 * Created by michal.hornak on 03.10.2016.
 *
 * Interface for Shooter class
 */
public interface Shooting {

    Shooter getShooter();

    double getDistanceFromPlayer();

    int getShootingDistanceThreshold();

    Point getPosition();
}
