package com.miso.thegame.gameMechanics.interfaces;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Miso on 19.2.2016.
 */
public interface Collidable {

    ArrayList<Point> getObjectCollisionVertices();

    public Point rotateVertexAroundCurrentPosition(double degreesToRotate, Point positionOfCentreForRotation ,Point vertexToBeRotated);
}
