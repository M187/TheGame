package com.miso.thegame.gameMechanics.movingObjects.spells;

import com.miso.thegame.gameMechanics.movingObjects.MovableObject;

/**
 * Created by Miso on 11.10.2015.
 */
public abstract class Spell extends MovableObject {

    public int cooldown = 30;
    public int cooldownPassed = 0;

    public boolean isOnCooldown(){
        if (cooldown > cooldownPassed){
            return true;
        } else {
            return false;
        }
    }

    protected void incrementCooldownPassed(){
        if (cooldownPassed < cooldown + 1){
            cooldownPassed++;
        }
    }

    /**
     * Should we remove spell due to timeout, or spell coords out of play field?
     * @return
     */
    public abstract boolean removeSpell();

    /**
     * Decide whether deltaCoordinate should be added or subtracted
     * @return
     */
    protected int deltaCoordDecider(int coordinate, int destinationCoordinate, int frameDelta){
        if (coordinate < destinationCoordinate){
            return frameDelta;
        } else {
            return (0 - frameDelta);
        }
    }
}
