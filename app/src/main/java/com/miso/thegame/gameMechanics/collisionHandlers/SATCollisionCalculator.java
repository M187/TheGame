package com.miso.thegame.gameMechanics.collisionHandlers;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Miso on 15.1.2016.
 */
public class SATCollisionCalculator {


    /**
     * Function to perform SeparateAxisTheorem on a List of Points.
     * <p/>
     * Iterate through each neighbour points and create axis from them
     * Do this for both objects, though do it in separate for loops.
     * foreach axis:
     * do projection of all object points onto it
     * find min and max for both objects
     * check if they intersects
     * <p/>
     * if not, break loop, return false
     * if yes, continue with rest of axis
     *
     * @param objectVertices      List of bordering points.
     * @param otherObjectVertices List of bordering points.
     * @return boolean - objects collide?
     */
    public boolean performSeparateAxisCollisionCheck(ArrayList<Point> objectVertices, ArrayList<Point> otherObjectVertices) {

        // Check for object axes
        for (int i = 0; i < objectVertices.size(); i++) {

            //Get normal vector to a chosen side
            Point axisVector = getAxis(i, objectVertices);

            ObjectProjectionOntoAxis objectProjection = projectObject(axisVector, objectVertices);
            ObjectProjectionOntoAxis otherObjectProjection = projectObject(axisVector, otherObjectVertices);

            //Can line be drawn between objects?
            if (projectionsDoNotOverlap(objectProjection, otherObjectProjection)) {
                return false;
            }
        }

        //Check for other object axes.
        for (int i = 0; i < otherObjectVertices.size(); i++) {

            //Get normal vector to a chosen side
            Point axisVector = getAxis(i, otherObjectVertices);

            ObjectProjectionOntoAxis objectProjection = projectObject(axisVector, objectVertices);
            ObjectProjectionOntoAxis otherObjectProjection = projectObject(axisVector, otherObjectVertices);

            //Can line be drawn between objects?
            if (projectionsDoNotOverlap(objectProjection, otherObjectProjection)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Perform collision check if a defined Point intersect with defined shape (defined via vertices)
     *
     * @param objectVertex point to be checked
     * @param otherObjectVertices object shape defined with neighbour vertices
     * @return intercet?
     */
    public boolean performSeparateAxisCollisionCheck(Point objectVertex, ArrayList<Point> otherObjectVertices) {

        //Check for other object axes.
        for (int i = 0; i < otherObjectVertices.size() - 1; i++) {

            //Get normal vector to a chosen side
            Point axisVector = getAxis(i, otherObjectVertices);

            int objectProjection = doDotProduct(axisVector, objectVertex);
            ObjectProjectionOntoAxis otherObjectProjection = projectObject(axisVector, otherObjectVertices);

            //Can line be drawn between objects?
            if (objectProjection <= otherObjectProjection.minimumProjectionValue ||  objectProjection >= otherObjectProjection.maximumProjectionValue) {
                return false;
            }
        }
        return true;
    }

    /**
     * Perform collision check if a defined Point and its radius (circle...) intersect with defined shape (defined via vertices)
     *
     * @param objectVertex point to be checked
     * @param circleRadius radius of a circle
     * @param otherObjectVertices object shape defined with neighbour vertices
     * @return intercet?
     */
    public boolean performSeparateAxisCollisionCheck(Point objectVertex, int circleRadius, ArrayList<Point> otherObjectVertices) {

        //Check for other object axes.
        for (int i = 0; i < otherObjectVertices.size() - 1; i++) {

            //Get normal vector to a chosen side
            Point axisVector = getAxis(i, otherObjectVertices);

            int objectProjection = doDotProduct(axisVector, objectVertex);
            ObjectProjectionOntoAxis circleProjection = new ObjectProjectionOntoAxis(objectProjection - circleRadius, objectProjection + circleRadius);
            ObjectProjectionOntoAxis otherObjectProjection = projectObject(axisVector, otherObjectVertices);

            //Can line be drawn between objects?
            //Can line be drawn between objects?
            if (projectionsDoNotOverlap(circleProjection, otherObjectProjection)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Gets axis from list of vertices based on a iterator
     * @param iterator for current point
     * @param objectVertices all of vertices
     * @return axis
     */
    private Point getAxis(int iterator, ArrayList<Point> objectVertices){
        if (iterator == objectVertices.size() - 1){
            return new Point(objectVertices.get(iterator).y - objectVertices.get(0).y, -(objectVertices.get(iterator).x - objectVertices.get(0).x));
        } else {
            return new Point(objectVertices.get(iterator).y - objectVertices.get(iterator + 1).y, -(objectVertices.get(iterator).x - objectVertices.get(iterator + 1).x));
        }
    }

    /**
     * Do dot product
     *
     * @param first  normal vector (axis)
     * @param second Point to be projected
     * @return dot product
     */
    private int doDotProduct(Point first, Point second) {

        return (first.x * second.x + first.y * second.y);
    }

    /**
     * Class to hold data about projection.
     * Similar to Point class, but has different names for variables.
     */
    private class ObjectProjectionOntoAxis {

        int minimumProjectionValue;
        int maximumProjectionValue;

        private ObjectProjectionOntoAxis(int min, int max){
            this.minimumProjectionValue = min;
            this.maximumProjectionValue = max;
        }
    }

    private boolean projectionsDoNotOverlap(ObjectProjectionOntoAxis objectProjection, ObjectProjectionOntoAxis otherObjectProjection){
        if (objectProjection.maximumProjectionValue < otherObjectProjection.minimumProjectionValue ||
                objectProjection.minimumProjectionValue > otherObjectProjection.maximumProjectionValue) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param normalVector to project onto
     * @param objectVertices - points to be projected
     * @return min and max value in a form of ObjectProjectionOntoAxis
     */
    private ObjectProjectionOntoAxis projectObject(Point normalVector, ArrayList<Point> objectVertices){
        int minValue = doDotProduct(normalVector, objectVertices.get(0));
        int maxValue = minValue;
        //Get min and max value for projection for other object
        for (int i = 1; i < objectVertices.size(); i++) {
            int dotProduct = doDotProduct(normalVector, objectVertices.get(i));
            if (minValue > dotProduct) {
                minValue = dotProduct;
            }
            if (maxValue < dotProduct) {
                maxValue = dotProduct;
            }
        }
        return new ObjectProjectionOntoAxis(minValue, maxValue);
    }

}
