package com.miso.thegame;

import android.test.ActivityUnitTestCase;

import com.miso.thegame.gameMechanics.Quadtree;

/**
 * Created by michal.hornak on 04.12.2015.
 */
public class QuadtreeTests extends ActivityUnitTestCase<Game> {

    private Game game;
    private GamePanel gP;
    private Quadtree quadtree;

    public QuadtreeTests() {
        super(Game.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        game = getActivity();
        //gP = new GamePanel(game);
    }
}
