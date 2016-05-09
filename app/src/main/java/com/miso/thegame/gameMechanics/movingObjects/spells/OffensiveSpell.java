package com.miso.thegame.gameMechanics.movingObjects.spells;

import com.miso.thegame.gameMechanics.collisionHandlers.SATCollisionCalculator;
import com.miso.thegame.gameMechanics.movingObjects.MovableObject;

/**
 * Created by Miso on 28.11.2015.
 */
public abstract class OffensiveSpell extends Spell {

    protected boolean removeOnCollision;
    protected boolean setMovement;
    protected String identificator = "";

    public OffensiveSpell(){
        setMovement = true;
    }

    public boolean collideWithMovingObject(MovableObject movableObject, SATCollisionCalculator satCollisionCalculator){
        return (satCollisionCalculator.performSeparateAxisCollisionCheck(this.getObjectCollisionVertices(), movableObject.getObjectCollisionVertices()));
    }

    public boolean isRemovedOnCollision() {
        return removeOnCollision;
    }

    public String getIdentificator() {
        return identificator;
    }
}
