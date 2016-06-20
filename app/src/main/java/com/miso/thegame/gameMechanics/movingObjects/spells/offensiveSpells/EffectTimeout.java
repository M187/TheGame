package com.miso.thegame.gameMechanics.movingObjects.spells.offensiveSpells;

/**
 * Created by michal.hornak on 20.06.2016.
 */
public class EffectTimeout {

    public long startTime;
    public int effectDuration;

    /**
     * Creates object to hold timeout data.
     * @param effectDuration
     */
    public EffectTimeout(int effectDuration){
        this.startTime = System.currentTimeMillis();
        this.effectDuration = effectDuration;
    }

    public boolean isActive(){
        if (System.currentTimeMillis() - this.startTime > effectDuration){
            return true;
        } else {
            return false;
        }
    }
}
