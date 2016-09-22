package com.miso.thegame.gameMechanics.objects.movingObjects.enemies;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.map.pathfinding.Pathfinder;
import com.miso.thegame.gameMechanics.objects.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.objects.movingObjects.spells.SpellManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Miso on 11.10.2015.
 */
public class EnemiesManager {

    public List<Enemy> enemiesToBeAddedInThiFrame = new ArrayList<>();
    public SpellManager spellManager;
    Player player;
    Resources res;
    //TODO try to do it as a set / linkedList
    private List<Enemy> enemyList = new ArrayList<>();

    public EnemiesManager(Player player, SpellManager spellManager, List<SingleEnemyInitialData> enemyInitialDatas, Resources res) {
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
            GameView2.drawManager.drawOnDisplay(enemy, canvas);
        }
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    public class CustomComparator implements Comparator<Enemy> {
        public int compare(Enemy e1, Enemy e2) {
            return (e1).compareTo(e2);
        }
    }
}
