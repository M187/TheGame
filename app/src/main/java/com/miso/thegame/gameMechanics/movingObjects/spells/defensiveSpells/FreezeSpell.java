package com.miso.thegame.gameMechanics.movingObjects.spells.defensiveSpells;

import com.miso.thegame.gameMechanics.movingObjects.spells.EffectTimeout;
import com.miso.thegame.gameMechanics.movingObjects.spells.SpellManager;

/**
 * Created by michal.hornak on 20.06.2016.
 *
 * Class created to hold data about freeze spell.
 * This should handle update spell,
 * thus it should enable/disable freezing projectiles in update() method.
 */
public class FreezeSpell extends DeffensiveSpell {

    public EffectTimeout timeout;

    public void update(){
        SpellManager.freezeProjectilesActive = true;
    }

    public boolean removeSpell(){
        return timeout.isActive();
    }

}
