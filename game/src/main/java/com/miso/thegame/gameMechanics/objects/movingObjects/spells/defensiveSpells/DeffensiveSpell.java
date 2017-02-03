package com.miso.thegame.gameMechanics.objects.movingObjects.spells.defensiveSpells;

import com.miso.thegame.gameMechanics.objects.movingObjects.spells.Spell;

/**
 * Created by Miso on 28.11.2015.
 *
 * Aggregate of the spells that don't interact with other game objects directly.
 * There is no possibility of a collision - they only affects game state via modifying player/environment settings.
 */
public abstract class DeffensiveSpell extends Spell{

    public abstract void update();
}
