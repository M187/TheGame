package com.miso.thegame.gameMechanics.multiplayer.otherPlayer;

import android.graphics.Point;

import com.miso.thegame.gameMechanics.objects.movingObjects.player.PlayerTriangle;

/**
 * Created by michal.hornak on 05.05.2016.
 */
public class OtherPlayer extends PlayerTriangle {

    public boolean isDestroyed = false;
    private boolean isReadyForNextFrame = false;

    public OtherPlayer(int color){
        super(new Point(0,0), color);
    }

    public void setHeading(int heading){
        this.heading = heading;
    }

    public void update(){

    }

    public boolean isReadyForNextFrame() {
        return isReadyForNextFrame;
    }

    public void setIsReadyForNextFrame(boolean isReadyForNextFrame) {
        this.isReadyForNextFrame = isReadyForNextFrame;
    }
}
