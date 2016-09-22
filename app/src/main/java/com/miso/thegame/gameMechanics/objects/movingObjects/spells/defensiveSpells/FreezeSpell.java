package com.miso.thegame.gameMechanics.objects.movingObjects.spells.defensiveSpells;

import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.objects.movingObjects.spells.EffectTimeout;

/**
 * Created by michal.hornak on 20.06.2016.
 *
 * Class created to hold data about freeze spell.
 * This should handle update spell,
 * thus it should enable/disable freezing projectiles in update() method.
 */
public class FreezeSpell extends DeffensiveSpell {

    public EffectTimeout timeout = new EffectTimeout(ConstantHolder.freezeDuration);

    public void update(){
        ConstantHolder.freezeActive = true;
    }

    public boolean removeSpell(){
        return !timeout.isActive();
    }

    public void draw(){
        return;
    }
}
