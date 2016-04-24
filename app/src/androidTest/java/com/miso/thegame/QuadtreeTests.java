package com.miso.thegame;

import android.test.ActivityUnitTestCase;

import com.miso.thegame.gameMechanics.collisionHandlers.Quadtree;
import com.miso.thegame.gameViews.GamePanelSingleplayer;

/**
 * Created by michal.hornak on 04.12.2015.
 */
public class QuadtreeTests extends ActivityUnitTestCase<GameActivity> {

    private GameActivity gameActivity;
    private GamePanelSingleplayer gP;
    private Quadtree quadtree;

    public QuadtreeTests() {
        super(GameActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gameActivity = getActivity();
        //gP = new GamePanel(gameActivity);
    }
}
