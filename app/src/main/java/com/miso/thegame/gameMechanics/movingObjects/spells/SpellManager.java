package com.miso.thegame.gameMechanics.movingObjects.spells;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.display.Animations.StaticAnimationManager;
import com.miso.thegame.gameMechanics.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.movingObjects.enemies.Enemy;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.movingObjects.spells.defensiveSpells.DeffensiveSpell;
import com.miso.thegame.gameViews.GameView2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Miso on 11.10.2015.
 */
public class SpellManager {

    public EnemiesManager enemiesManager;
    public boolean primaryShootingActive = false;
    public Point primaryShootingVector;
    public SpellCreator spellCreator;
    protected Resources resources;
    private List<OffensiveSpell> offensiveSpellList = new ArrayList<OffensiveSpell>() {

        @Override
        public boolean remove(Object o) {
            if (super.remove(o)) {
                OffensiveSpell temp = (OffensiveSpell) o;
                StaticAnimationManager.addExplosion(new Point(temp.getX(), temp.getY()), 1);
                return true;
            } else {
                return false;
            }
        }
    };
    private List<DeffensiveSpell> deffensiveSpellList = new ArrayList<>();
    private long lastUse = 0;
    private int cooldown = ConstantHolder.firebalCooldown;

    public SpellManager(Resources resources, Player player) {
        this.resources = resources;
        this.spellCreator = new SpellCreator(player, resources, offensiveSpellList, deffensiveSpellList);
    }

    public void update() {

        this.updateSpells(this.offensiveSpellList);

        this.updateSpells(this.deffensiveSpellList);

        if (primaryShootingActive && (System.currentTimeMillis() - lastUse) > cooldown) {
            this.spellCreator.addPlayerProjectile(primaryShootingVector.x, primaryShootingVector.y);
            lastUse = System.currentTimeMillis();
        }
    }

    private void updateSpells(List<? extends Spell> spellList) {
        Spell spell;
        Iterator<? extends Spell> spellIterator = spellList.iterator();
        while (spellIterator.hasNext()) {
            spell = spellIterator.next();
            if (spell.removeSpell()) {
                spellIterator.remove();
            } else {
                spell.moveObject();
            }
        }
    }

    public void draw(Canvas canvas) {

        for (Spell offensiveSpell : getOffensiveSpellList()) {
            GameView2.drawManager.drawOnDisplay(offensiveSpell, canvas);
        }

        for (Spell deffensiveSpell : deffensiveSpellList) {
            GameView2.drawManager.drawOnDisplay(deffensiveSpell, canvas);
        }
    }

    public List<OffensiveSpell> getOffensiveSpellList() {
        return this.offensiveSpellList;
    }

    /**
     * Perform freeze spell. Take all existing enemies and set speed to 4.
     * (default speed of enemy is 5 at time of creation of this function)
     */
    public void performFreezeSpell() {
        for (Enemy enemy : enemiesManager.getEnemyList()) {
            enemy.setSpeed(4);
        }
    }
}
