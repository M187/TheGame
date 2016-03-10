package com.miso.thegame;

import android.app.Application;
import android.graphics.Point;
import android.test.ApplicationTestCase;

import com.miso.thegame.gameMechanics.collisionHandlers.SATCollisionCalculator;

import java.util.ArrayList;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    SATCollisionCalculator collisionCalculator = new SATCollisionCalculator();

    public ApplicationTest() {
        super(Application.class);

        ArrayList<Point> a = new ArrayList<Point>();
        a.add(new Point(1,1));
        a.add(new Point(2, 1));
        a.add(new Point(1, 2));

        ArrayList<Point> b = new ArrayList<Point>();
        b.add(new Point(1, 1));
        b.add(new Point(2, 1));
        b.add(new Point(1, 2));

        assert collisionCalculator.performSeparateAxisCollisionCheck(a, b);
    }

}