package com.miso.thegame.Networking.transmitionData;

/**
 * Created by michal.hornak on 20.04.2016.
 */
public class PlayerData {

    private int deltaX;
    private int deltaY;
    private Actions action;

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public Actions getAction() {
        return action;
    }
}
