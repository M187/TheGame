package com.miso.thegame.gameMechanics.objects.movingObjects.spells;

import com.miso.thegame.gameMechanics.objects.movingObjects.MovableObject;

/**
 * Created by Miso on 11.10.2015.
 */
public abstract class Spell extends MovableObject {

    /**
     * Should we remove spell due to timeout, or spell coords out of play field?
     * @return
     */
    public abstract boolean removeSpell();

    public abstract void update();
}
