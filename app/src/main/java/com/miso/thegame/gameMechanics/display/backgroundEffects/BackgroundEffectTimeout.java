package com.miso.thegame.gameMechanics.display.backgroundEffects;

/**
 * Created by michal.hornak on 20.06.2016.
 */
public class BackgroundEffectTimeout {

    public long startTime;
    public int effectDuration;

    /**
     * Creates object to hold timeout data.
     * @param effectDuration in milliseconds
     */
    public BackgroundEffectTimeout(int effectDuration){
        this.startTime = System.currentTimeMillis();
        this.effectDuration = effectDuration;
    }

    /**
     * Has the effect timed out?
     * @return true if it has timed out.
     */
    public boolean isActive(){
        if (System.currentTimeMillis() - this.startTime > effectDuration){
            return false;
        } else {
            return true;
        }
    }
}
