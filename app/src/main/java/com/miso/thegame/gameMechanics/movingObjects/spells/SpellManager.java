package com.miso.thegame.gameMechanics.movingObjects.spells;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.GamePanel;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.movingObjects.Player;
import com.miso.thegame.gameMechanics.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.movingObjects.enemies.Enemy;
import com.miso.thegame.gameMechanics.movingObjects.spells.enemySpells.offensiveSpells.EnemyOffensiveSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.defensiveSpells.DeffensiveSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.offensiveSpells.PlayerOffensiveSpell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Miso on 11.10.2015.
 */
public class SpellManager {

    private List<PlayerOffensiveSpell> playerOffensiveSpellList = new ArrayList<>();
    private List<EnemyOffensiveSpell> enemyOffensiveSpellList = new ArrayList<>();
    protected Resources resources;
    private List<DeffensiveSpell> deffensiveSpellList = new ArrayList<>();
    private long lastUse = 0;
    private int cooldown = ConstantHolder.firebalCooldown;

    public EnemiesManager enemiesManager;

    public boolean primaryShootingActive = false;
    public Point primaryShootingVector;
    public SpellCreator spellCreator;

    public SpellManager(Resources resources, Player player) {
        this.resources = resources;
        this.spellCreator = new SpellCreator(player, resources, playerOffensiveSpellList, enemyOffensiveSpellList, deffensiveSpellList);
    }

    public void update() {

        PlayerOffensiveSpell playerOffensiveSpell;
        Iterator<PlayerOffensiveSpell> playerOffensiveSpellIterator = this.playerOffensiveSpellList.iterator();
        while (playerOffensiveSpellIterator.hasNext()) {
            playerOffensiveSpell = playerOffensiveSpellIterator.next();
            if (playerOffensiveSpell.removeSpell()) {
                playerOffensiveSpellIterator.remove();
            } else {
                playerOffensiveSpell.moveObject();
            }
        }

        EnemyOffensiveSpell enemyOffensiveSpell;
        Iterator<EnemyOffensiveSpell> enemyOffensiveSpellIterator = this.enemyOffensiveSpellList.iterator();
        while (enemyOffensiveSpellIterator.hasNext()) {
            enemyOffensiveSpell = enemyOffensiveSpellIterator.next();
            if (enemyOffensiveSpell.removeSpell()) {
                enemyOffensiveSpellIterator.remove();
            } else {
                enemyOffensiveSpell.moveObject();
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
            this.spellCreator.addPlayerFireball(primaryShootingVector.x, primaryShootingVector.y);
            lastUse = System.currentTimeMillis();
        }
    }

    public void draw(Canvas canvas) {

        for (Spell offensiveSpell : getPlayerOffensiveSpellList()) {
            GamePanel.drawManager.drawOnDisplay(offensiveSpell, canvas);
        }

        for (Spell offensiveSpell : getEnemyOffensiveSpellList()) {
            GamePanel.drawManager.drawOnDisplay(offensiveSpell, canvas);
        }

        for (Spell deffensiveSpell : deffensiveSpellList) {
            GamePanel.drawManager.drawOnDisplay(deffensiveSpell, canvas);
        }
    }

    public List<PlayerOffensiveSpell> getPlayerOffensiveSpellList() {
        return playerOffensiveSpellList;
    }

    public List<EnemyOffensiveSpell> getEnemyOffensiveSpellList() {
        return enemyOffensiveSpellList;
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
