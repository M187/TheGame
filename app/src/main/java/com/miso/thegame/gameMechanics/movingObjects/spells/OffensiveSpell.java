package com.miso.thegame.gameMechanics.movingObjects.spells;

import com.miso.thegame.gameMechanics.collisionHandlers.SATCollisionCalculator;
import com.miso.thegame.gameMechanics.movingObjects.MovableObject;
import com.miso.thegame.gameViews.GameView2;

/**
 * Created by Miso on 28.11.2015.
 */
public abstract class OffensiveSpell extends Spell {

    public static int projectileIterator = 0;

    protected boolean removeOnCollision;
    protected boolean setMovement;
    protected String identificator = "";

    public OffensiveSpell(){
        setMovement = true;
        assignIdentificator();
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

    /**
     * Static variable projectileIterator is shared amongst all offensive spells.
     * Should be incremented each time a spell is created.
     * Reset after thousand iterations - There should be no more than thousand spells at a time for a player I hope.
     */
    private void assignIdentificator(){
        this.identificator = GameView2.myNickname + Integer.toString(projectileIterator);
        projectileIterator =+ 1;
        if (projectileIterator >= 1000){
            projectileIterator = 0;
        }
    }
}
