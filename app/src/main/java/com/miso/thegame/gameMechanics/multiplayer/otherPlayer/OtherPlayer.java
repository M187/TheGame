package com.miso.thegame.gameMechanics.multiplayer.otherPlayer;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.movingObjects.player.Player;

/**
 * Created by michal.hornak on 05.05.2016.
 */
public class OtherPlayer extends Player {

    private boolean isReadyForNextFrame = false;

    public OtherPlayer(Resources resources){
        super(resources, new Point(0,0), null);
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
