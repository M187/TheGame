package com.miso.thegame.gameMechanics.movingObjects.spells;

import android.content.res.Resources;

import com.miso.thegame.gameMechanics.Anchor;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.movingObjects.spells.enemySpells.offensiveSpells.EnemyFireball;
import com.miso.thegame.gameMechanics.movingObjects.spells.enemySpells.offensiveSpells.EnemyOffensiveSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.defensiveSpells.Blink;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.defensiveSpells.DeffensiveSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.defensiveSpells.Timestop;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.offensiveSpells.Fireball;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.offensiveSpells.PlayerOffensiveSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.offensiveSpells.Shockwave;

import java.util.List;

/**
 * Created by michal.hornak on 18.04.2016.
 *
 * Class specifically created for purpose to hold functionality to add new spells
 */
public class SpellCreator {

    private Resources resources;
    private Player player;
    private List<PlayerOffensiveSpell> playerOffensiveSpells;
    private List<EnemyOffensiveSpell> enemyOffensiveSpells;
    private List<DeffensiveSpell> deffensiveSpells;

    SpellCreator(Player player, Resources resources, List<PlayerOffensiveSpell> playerOffensiveSpells, List<EnemyOffensiveSpell> enemyOffensiveSpells, List<DeffensiveSpell> deffensiveSpells){
        this.resources = resources;
        this.player = player;
        this.playerOffensiveSpells = playerOffensiveSpells;
        this.enemyOffensiveSpells = enemyOffensiveSpells;
        this.deffensiveSpells = deffensiveSpells;
    }

    /**
     * Add timestop spell to defensive list.
     * @param player - set his timestopFlag to true.
     */
    public void addTimestopSpell(){
        this.deffensiveSpells.add(new Timestop(this.player));
    }

    public void addPlayerFireball(int deltaX, int deltaY) {
        if (player.primaryAmunition > 0) {
            this.playerOffensiveSpells.add(new Fireball(this.player.getX(), this.player.getY(), this.player.getX() - deltaX, this.player.getY() - deltaY, this.resources));
            player.primaryAmunition -= 1;
        }
    }

    public void addPlayerShockwave(int reachFactor) {
        this.playerOffensiveSpells.add(new Shockwave(this.player, reachFactor, this.resources));
    }

    /**
     * Specific method to add Blink spell.
     *
     * @param player player...
     * @param deltaX move X coord
     * @param deltaY moveY coord
     */
    public void addBlinkSpell(Player player, Anchor anchor, int deltaX, int deltaY) {
        this.deffensiveSpells.add(new Blink(player, anchor, deltaX, deltaY, resources));
    }

    public void addFireball(int x, int y, int dx, int dy) {
        this.playerOffensiveSpells.add(new Fireball(x, y, dx, dy, resources));
        player.primaryAmunition -= 1;
    }

    public void fireEnemyFireballTowardsPlayer(int x, int y) {
        enemyOffensiveSpells.add(new EnemyFireball(x, y, player.getX(), player.getY(), resources));
    }
}
