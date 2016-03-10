package com.miso.thegame.gameMechanics.movingObjects.spells.enemySpells.offensiveSpells;

import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.movingObjects.spells.OffensiveSpell;

/**
 * Created by Miso on 28.11.2015.
 */
public abstract class EnemyOffensiveSpell extends OffensiveSpell {

    public EnemyOffensiveSpell(){
        super();
        this.collisionObjectType = CollisionObjectType.SpellEnemy;
    }

}
