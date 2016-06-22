package com.miso.thegame.gameMechanics.movingObjects.spells;

import com.miso.thegame.gameMechanics.collisionHandlers.SATCollisionCalculator;
import com.miso.thegame.gameMechanics.movingObjects.MovableObject;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameViews.GameView2;

/**
 * Created by Miso on 28.11.2015.
 *
 * Aggregate of spells that directly interact with other gameObjects.
 * They can collide with other gameObjects.
 * They can not modify player/environment variables.
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
        projectileIterator += 1;
        if (projectileIterator >= 1000){
            projectileIterator = 0;
        }
    }

    public abstract void update();

    public abstract boolean playerHit(Player player);

    /**
     * Function that should be called when offensiveSpell is removed from offensive spell list.
     * It should make sure that proper animation is add as a "explosion".
     */
    public abstract void explode();
}
