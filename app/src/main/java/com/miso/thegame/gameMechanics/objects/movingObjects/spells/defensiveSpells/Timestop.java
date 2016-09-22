package com.miso.thegame.gameMechanics.objects.movingObjects.spells.defensiveSpells;

import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.objects.movingObjects.spells.EffectTimeout;

/**
 * Created by Miso on 23.12.2015.
 */
public class Timestop extends DeffensiveSpell{

    public EffectTimeout timeout = new EffectTimeout(ConstantHolder.timestopDuration);

    public void update(){
        ConstantHolder.timestopActive = true;
    }

    public boolean removeSpell(){
        return !timeout.isActive();
    }

    public void draw(){
        return;
    }
}
