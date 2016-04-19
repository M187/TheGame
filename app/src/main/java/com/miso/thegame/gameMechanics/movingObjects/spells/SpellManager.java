package com.miso.thegame.gameMechanics.movingObjects.spells;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.GamePanel;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.movingObjects.enemies.Enemy;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.movingObjects.spells.defensiveSpells.DeffensiveSpell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Miso on 11.10.2015.
 */
public class SpellManager {

    //TODO: unify these 2 arrays
    protected Resources resources;
    private List<OffensiveSpell> offensiveSpellList = new ArrayList<>();
    private List<DeffensiveSpell> deffensiveSpellList = new ArrayList<>();
    private long lastUse = 0;
    private int cooldown = ConstantHolder.firebalCooldown;

    public EnemiesManager enemiesManager;

    public boolean primaryShootingActive = false;
    public Point primaryShootingVector;
    public SpellCreator spellCreator;

    public SpellManager(Resources resources, Player player) {
        this.resources = resources;
        this.spellCreator = new SpellCreator(player, resources, offensiveSpellList, deffensiveSpellList);
    }

    public void update() {

        OffensiveSpell offensiveSpell;
        Iterator<OffensiveSpell> offensiveSpellIterator = this.offensiveSpellList.iterator();
        while (offensiveSpellIterator.hasNext()) {
            offensiveSpell = offensiveSpellIterator.next();
            if (offensiveSpell.removeSpell()) {
                offensiveSpellIterator.remove();
            } else {
                offensiveSpell.moveObject();
            }
        }

        DeffensiveSpell deffensiveSpell;
        Iterator<DeffensiveSpell> deffensiveSpellIterator = this.deffensiveSpellList.iterator();
        while (deffensiveSpellIterator.hasNext()) {
            deffensiveSpell = deffensiveSpellIterator.next();
            if (deffensiveSpell.removeSpell()) {
                deffensiveSpellIterator.remove();
            } else {
                deffensiveSpell.update();
            }
        }

        if (primaryShootingActive && (System.currentTimeMillis() - lastUse) > cooldown) {
            this.spellCreator.addPlayerProjectile(primaryShootingVector.x, primaryShootingVector.y);
            lastUse = System.currentTimeMillis();
        }
    }

    public void draw(Canvas canvas) {

        for (Spell offensiveSpell : getOffensiveSpellList()){
            GamePanel.drawManager.drawOnDisplay(offensiveSpell, canvas);
        }

        for (Spell deffensiveSpell : deffensiveSpellList) {
            GamePanel.drawManager.drawOnDisplay(deffensiveSpell, canvas);
        }
    }

    public List<OffensiveSpell> getOffensiveSpellList(){
        return this.offensiveSpellList;
    }

    /**
     * Perform freeze spell. Take all existing enemies and set speed to 4.
     * (default speed of enemy is 5 at time of creation of this function)
     */
    public void performFreezeSpell(){
        for (Enemy enemy : enemiesManager.getEnemyList()){
            enemy.setSpeed(4);
        }
    }
}
