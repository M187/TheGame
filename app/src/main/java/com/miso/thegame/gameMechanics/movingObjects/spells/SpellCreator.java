package com.miso.thegame.gameMechanics.movingObjects.spells;

import android.content.res.Resources;

import com.miso.thegame.gameMechanics.Anchor;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.movingObjects.spells.defensiveSpells.Blink;
import com.miso.thegame.gameMechanics.movingObjects.spells.defensiveSpells.DeffensiveSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.defensiveSpells.Timestop;
import com.miso.thegame.gameMechanics.movingObjects.spells.offensiveSpells.Projectile;
import com.miso.thegame.gameMechanics.movingObjects.spells.offensiveSpells.Shockwave;

import java.util.List;

/**
 * Created by michal.hornak on 18.04.2016.
 *
 * Class specifically created for purpose to hold functionality to add new spells
 */
public class SpellCreator {

    private Resources resources;
    private Player player;
    private List<OffensiveSpell> offensiveSpells;
    private List<DeffensiveSpell> deffensiveSpells;

    SpellCreator(Player player, Resources resources, List<OffensiveSpell> offensiveSpells, List<DeffensiveSpell> deffensiveSpells){
        this.resources = resources;
        this.player = player;
        this.offensiveSpells = offensiveSpells;
        this.deffensiveSpells = deffensiveSpells;
    }

    /**
     * Add timestop spell to defensive list.
     */
    public void addTimestopSpell(){
        this.deffensiveSpells.add(new Timestop(this.player));
    }

    public void addPlayerProjectile(int deltaX, int deltaY) {
        if (player.primaryAmunition > 0) {
            this.offensiveSpells.add(new Projectile(this.player.getX(), this.player.getY(), this.player.getX() - deltaX, this.player.getY() - deltaY, CollisionObjectType.SpellPlayer, this.resources));
            player.primaryAmunition -= 1;
        }
    }

    public void addPlayerShockwave(int reachFactor) {
        this.offensiveSpells.add(new Shockwave(this.player, reachFactor, this.resources));
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

    public void addFireball(int x, int y, int dx, int dy, CollisionObjectType collisionObjectType) {
        this.offensiveSpells.add(new Projectile(x, y, dx, dy, collisionObjectType, resources));
        player.primaryAmunition -= 1;
    }

    public void fireEnemyFireballTowardsPlayer(int x, int y) {
        offensiveSpells.add(new Projectile(x, y, player.getX(), player.getY(), CollisionObjectType.Enemy, resources));
    }
}
