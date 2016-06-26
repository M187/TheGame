package com.miso.thegame.gameMechanics.display.backgroundEffects;

/**
 * Created by michal.hornak on 20.06.2016.
 */
public class BackgroundEffectTimeout {

    public int effectDuration;
    private int currentFrameCount = 0;

    /**
     * Creates object to hold timeout data.
     * @param effectDuration in milliseconds
     */
    public BackgroundEffectTimeout(int effectDuration){
        this.effectDuration = effectDuration;
    }

    public boolean update(){
        this.currentFrameCount++;
        if (currentFrameCount > effectDuration){
            return false;
        } else {
            return true;
        }
    }

    public int getCurrentFrameCount() {
        return currentFrameCount;
    }
}
