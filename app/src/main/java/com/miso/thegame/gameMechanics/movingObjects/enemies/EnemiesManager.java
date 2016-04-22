package com.miso.thegame.gameMechanics.movingObjects.enemies;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.map.pathfinding.Pathfinder;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.movingObjects.spells.SpellManager;
import com.miso.thegame.gameViews.GameViewAbstract;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Miso on 11.10.2015.
 */
public class EnemiesManager {

    //TODO try to do it as a set / linkedList
    private List<Enemy> enemyList = new ArrayList<>();
    public List<Enemy> enemiesToBeAddedInThiFrame = new ArrayList<>();
    Player player;
    Resources res;
    public SpellManager spellManager;

    public class CustomComparator implements Comparator<Enemy> {
        public int compare(Enemy e1, Enemy e2) {
            return (e1).compareTo(e2);
        }
    }

    public EnemiesManager(Player player, SpellManager spellManager, ArrayList<SingleEnemyInitialData> enemyInitialDatas, Resources res) {
        this.player = player;
        this.spellManager = spellManager;
        this.res = res;
        Pathfinder.FindPath.updateMapGridDistanceCost(player);

        EnemyFactory.getInstance().initializeEnemies(enemyList, enemyInitialDatas, res);
    }

    public void update() {
        if (player.changingTile) {
            Pathfinder.FindPath.updateMapGridDistanceCost(player);
        }

        if (!ConstantHolder.timestopActive) {
            for (Enemy enemy : this.getEnemyList()) {
                enemy.update(this.player, this);
            }
            for (Enemy enemy : this.enemiesToBeAddedInThiFrame){
                this.enemyList.add(enemy);
            }
            this.enemiesToBeAddedInThiFrame.clear();
            //Collections.sort(enemyList, new CustomComparator());
        }
    }

    public void draw(Canvas canvas) {
        for (Enemy enemy : getEnemyList()) {
            GameViewAbstract.drawManager.drawOnDisplay(enemy, canvas);
        }
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }
}
