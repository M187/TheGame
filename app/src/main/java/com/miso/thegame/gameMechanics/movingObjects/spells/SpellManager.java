package com.miso.thegame.gameMechanics.movingObjects.spells;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.GamePanel;
import com.miso.thegame.gameMechanics.Anchor;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.movingObjects.Player;
import com.miso.thegame.gameMechanics.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.movingObjects.enemies.Enemy;
import com.miso.thegame.gameMechanics.movingObjects.spells.enemySpells.offensiveSpells.EnemyFireball;
import com.miso.thegame.gameMechanics.movingObjects.spells.enemySpells.offensiveSpells.EnemyOffensiveSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.defensiveSpells.Blink;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.defensiveSpells.DeffensiveSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.defensiveSpells.Timestop;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.offensiveSpells.Fireball;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.offensiveSpells.PlayerOffensiveSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.offensiveSpells.Shockwave;

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
    private Player player;
    private long lastUse = 0;
    private int cooldown = ConstantHolder.firebalCooldown;

    public EnemiesManager enemiesManager;

    public boolean primaryShootingActive = false;
    public Point primaryShootingVector;

    public SpellManager(Resources resources, Player player) {
        this.player = player;
        this.resources = resources;
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
            addFireball(player, primaryShootingVector.x, primaryShootingVector.y);
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
     * Specific method to add Blink spell.
     *
     * @param player player...
     * @param deltaX move X coord
     * @param deltaY moveY coord
     */
    public void addBlinkSpell(Player player, Anchor anchor, int deltaX, int deltaY) {
        deffensiveSpellList.add(new Blink(player, anchor, deltaX, deltaY, resources));
    }

    public void addFireball(int x, int y, int dx, int dy) {
        getPlayerOffensiveSpellList().add(new Fireball(x, y, dx, dy, resources));
        player.primaryAmunition -= 1;
    }

    /**
     * Add timestop spell to defensive list.
     * @param player - set his timestopFlag to true.
     */
    public void addTimestopSpell(Player player){
        deffensiveSpellList.add(new Timestop(player));
    }

    public void addFireball(Player player, int deltaX, int deltaY) {
        if (player.primaryAmunition > 0) {
            getPlayerOffensiveSpellList().add(new Fireball(player.getX(), player.getY(), player.getX() - deltaX, player.getY() - deltaY, resources));
            player.primaryAmunition -= 1;
        }
    }

    public void addShockwave(Player player, int reachFactor) {
        getPlayerOffensiveSpellList().add(new Shockwave(player, reachFactor, resources));
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

    public void addEnemyProjectile(int x, int y, int dx, int dy) {
        getEnemyOffensiveSpellList().add(new EnemyFireball(x, y, dx, dy, resources));
    }
}
