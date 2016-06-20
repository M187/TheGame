package com.miso.thegame.gameMechanics.movingObjects.spells;

import android.content.res.Resources;
import android.graphics.Point;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerShootFreezingProjectile;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerShootProjectile;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.movingObjects.Anchor;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.movingObjects.spells.defensiveSpells.Blink;
import com.miso.thegame.gameMechanics.movingObjects.spells.defensiveSpells.DeffensiveSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.defensiveSpells.Timestop;
import com.miso.thegame.gameMechanics.movingObjects.spells.offensiveSpells.FreezeSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.offensiveSpells.FreezingProjectile;
import com.miso.thegame.gameMechanics.movingObjects.spells.offensiveSpells.Projectile;
import com.miso.thegame.gameMechanics.movingObjects.spells.offensiveSpells.Shockwave;
import com.miso.thegame.gameViews.GameView2;

import java.util.List;

/**
 * Created by michal.hornak on 18.04.2016.
 * <p/>
 * Class specifically created for purpose to hold functionality to add new spells
 */
public class SpellCreator {

    private Resources resources;
    private Player player;
    private List<OffensiveSpell> offensiveSpells;
    private List<DeffensiveSpell> deffensiveSpells;

    SpellCreator(Player player, Resources resources, List<OffensiveSpell> offensiveSpells, List<DeffensiveSpell> deffensiveSpells) {
        this.resources = resources;
        this.player = player;
        this.offensiveSpells = offensiveSpells;
        this.deffensiveSpells = deffensiveSpells;
    }

    /**
     * Add timestop spell to defensive list.
     */
    public void addTimestopSpell() {
        this.deffensiveSpells.add(new Timestop(this.player));
    }

    public void addPlayerProjectile(int deltaX, int deltaY) {
        if (player.primaryAmunition > 0) {

            if (SpellManager.freezeProjectilesActive) {
                Projectile newProjectile = new Projectile(this.player.getX(), this.player.getY(), this.player.getX() - deltaX, this.player.getY() - deltaY, CollisionObjectType.SpellPlayer, this.resources);
                this.offensiveSpells.add(newProjectile);
                player.primaryAmunition -= 1;

                //Inform other players about shot
                if (GameView2.isMultiplayerGame) {
                    propageteProjectileInformationToOtherPlayers(
                            new PlayerShootProjectile(
                                    newProjectile.getIdentificator(),
                                    new Point(this.player.getX(), this.player.getY()),
                                    new Point(deltaX, deltaY)));
                }
            } else {
                FreezingProjectile newProjectile = new FreezingProjectile(this.player.getX(), this.player.getY(), this.player.getX() - deltaX, this.player.getY() - deltaY, CollisionObjectType.SpellPlayer, this.resources);
                this.offensiveSpells.add(newProjectile);
                player.primaryAmunition -= 1;

                //Inform other players about shot
                if (GameView2.isMultiplayerGame) {
                    propageteProjectileInformationToOtherPlayers(
                            new PlayerShootFreezingProjectile(
                                    newProjectile.getIdentificator(),
                                    new Point(this.player.getX(), this.player.getY()),
                                    new Point(deltaX, deltaY)));
                }
            }
        }
    }

    private void propageteProjectileInformationToOtherPlayers(TransmissionMessage transmissionMessage) {
        GameView2.sender.sendMessage(transmissionMessage);
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

    public void fireProjectileTowardsPlayer(int x, int y) {
        offensiveSpells.add(new Projectile(x, y, player.getX(), player.getY(), CollisionObjectType.SpellEnemy, resources));
    }

    /**
     * Use by multiplayer game state updater.
     * Creates completely custom projectile.
     *
     * @param x                   starting coord
     * @param y                   starting coord
     * @param xVector             ...
     * @param yVector             ...
     * @param identificator       used to reference this projectile
     * @param collisionObjectType used to calculate collisions.
     */
    public void fireProjectile(int x, int y, int xVector, int yVector, String identificator, CollisionObjectType collisionObjectType) {
        offensiveSpells.add(new Projectile(x, y, x - xVector, y - yVector, collisionObjectType, identificator, resources));
    }

    public void fireFreezingProjectileTowardsPlayer(int x, int y) {
        offensiveSpells.add(new Projectile(x, y, player.getX(), player.getY(), CollisionObjectType.SpellEnemy, resources));
    }

    /**
     * Use by multiplayer game state updater.
     * Creates completely custom projectile.
     *
     * @param x                   starting coord
     * @param y                   starting coord
     * @param xVector             ...
     * @param yVector             ...
     * @param identificator       used to reference this projectile
     * @param collisionObjectType used to calculate collisions.
     */
    public void fireFreezingProjectile(int x, int y, int xVector, int yVector, String identificator, CollisionObjectType collisionObjectType) {
        offensiveSpells.add(new FreezingProjectile(x, y, x - xVector, y - yVector, collisionObjectType, identificator, resources));
    }

    public void performFreezeSpell() {
        this.deffensiveSpells.add(new FreezeSpell());
    }
}
