package com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.offensiveSpells;

import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.movingObjects.spells.OffensiveSpell;

/**
 * Created by Miso on 28.11.2015.
 */
public abstract class PlayerOffensiveSpell extends OffensiveSpell {

    public PlayerOffensiveSpell(){
        super();
        this.collisionObjectType = CollisionObjectType.SpellPlayer;
    }
}
