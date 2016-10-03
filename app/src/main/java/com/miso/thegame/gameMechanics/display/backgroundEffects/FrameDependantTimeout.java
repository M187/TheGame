package com.miso.thegame.gameMechanics.display.backgroundEffects;

/**
 * Created by michal.hornak on 20.06.2016.
 */
public class FrameDependantTimeout {

    public int effectDuration;
    private int currentFrameCount = 0;

    /**
     * Creates object to hold timeout data.
     * @param effectDuration in milliseconds
     */
    public FrameDependantTimeout(int effectDuration){
        this.effectDuration = effectDuration;
    }

    /**
     * Returns true if sufficient amount of frames have passed.
     * @return true if timeout has passed.
     */
    public boolean update(){
        this.currentFrameCount++;
        if (currentFrameCount < effectDuration){
            return false;
        } else {
            return true;
        }
    }

    public int getCurrentFrameCount() {
        return currentFrameCount;
    }
}
