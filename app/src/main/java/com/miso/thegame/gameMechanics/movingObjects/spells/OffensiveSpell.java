package com.miso.thegame.gameMechanics.movingObjects.spells;

import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.collisionHandlers.SATCollisionCalculator;
import com.miso.thegame.gameMechanics.movingObjects.MovableObject;
import com.miso.thegame.gameMechanics.movingObjects.enemies.Enemy;
import com.miso.thegame.gameMechanics.movingObjects.spells.Spell;

/**
 * Created by Miso on 28.11.2015.
 */
public abstract class OffensiveSpell extends Spell {

    protected boolean removeOnCollision;
    protected boolean setMovement;

    public OffensiveSpell(){
        setMovement = true;
    }

    public boolean collideWithMovingObject(MovableObject movableObject, SATCollisionCalculator satCollisionCalculator){
        return (satCollisionCalculator.performSeparateAxisCollisionCheck(this.getObjectCollisionVertices(), movableObject.getObjectCollisionVertices()));
    }

    public boolean isRemovedOnCollision() {
        return removeOnCollision;
    }
}
