package com.miso.thegame.gameMechanics.collisionHandlers;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;

import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerHitMessage;
import com.miso.thegame.gameMechanics.GameObject;
import com.miso.thegame.gameMechanics.display.Animations.StaticAnimationManager;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.MovableObject;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.movingObjects.spells.OffensiveSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.Spell;
import com.miso.thegame.gameMechanics.movingObjects.spells.SpellManager;
import com.miso.thegame.gameMechanics.multiplayer.otherPlayer.OtherPlayerManager;
import com.miso.thegame.gameMechanics.nonMovingObjects.Collectables.Collectible;
import com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.Obstacle;
import com.miso.thegame.gameViews.GamePanelMultiplayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Miso on 7.12.2015.
 */
public class CollisionHandlerMultiplayer {

    private Player player;
    private OtherPlayerManager otherPlayerManager;
    private SpellManager spellManager;
    private MapManager mapManager;
    private Quadtree quadtree;
    private SATCollisionCalculator satCollisionCalculator = new SATCollisionCalculator();
    private Resources resources;

    private ArrayList<MovableObject> movingObjects = new ArrayList<>();
    // nice guide - http://www.ibm.com/developerworks/library/j-jtp0730/
    private SynchronousQueue<GameObject> movingObjectQueue = new SynchronousQueue<>();

    public CollisionHandlerMultiplayer (Player player, OtherPlayerManager otherPlayerManager, SpellManager spellManager, MapManager mapManager, Resources res) {
        this.player = player;
        this.otherPlayerManager = otherPlayerManager;
        this.spellManager = spellManager;
        this.mapManager = mapManager;
        //TODO: initialize for obstacles only once. Then create copy and use it with current Objects present on map. This assumes that obstacles CAN NOT be removed during gameplay on map.
        //TODO: implement DEEP cloning of binary tree :(
        this.quadtree = new Quadtree(1, new Rect(0, 0, MapManager.getWorldWidth(), MapManager.getWorldHeight()));
        this.resources = res;
        initializeMovingObjects();
    }

    /**
     * Main function to do collision checks.
     * <p/>
     * - Types of objects to detect collision: Player, Enemy, Spell, Obstacle, Collectible
     * Hierarchy of collision checks:
     * 1. Player with all
     * 2. Enemy with all but player
     * 3. Spell with Obstacles
     */
    public void performCollisionCheck() {

        long temp = System.nanoTime();

        this.refreshMovingObjects();
        this.initializeQuadtree();

        System.out.println(" -- Refreshing quad -tree duration: " + (System.nanoTime() - temp));

        Set<GameObject> returnObjects = new HashSet<>();
        for (MovableObject movingObject : movingObjects) {
            returnObjects.clear();
            quadtree.retrieve(returnObjects, movingObject);

            switch (movingObject.getCollisionObjectType()) {
                case Player:
                    this.handleCollisions((Player) movingObject, returnObjects);
                    break;
                case SpellPlayer:
                case SpellEnemy:
                    this.handleColision((OffensiveSpell) movingObject, returnObjects);
                    break;
            }
        }
    }

    //<editor-fold desc="player colision">

    /**
     * Handles collision of player with other objects.
     * 4 types of colidable objects:
     * Spel, Enemy, Collectible, Obstacle
     *
     * @param returnObjects List of objects to check against.
     */
    public void handleCollisions(Player player, Set<GameObject> returnObjects) {
        for (GameObject gameObject : returnObjects) {

            switch (gameObject.getCollisionObjectType()) {
                case SpellEnemy:
                    possibleCollisionOfPlayerWithEnemyOffensiveSpell((OffensiveSpell) gameObject);
                    break;
                case Collectible:
                    possibleCollisionOfPlayerWithCollectable(player, (Collectible) gameObject);
                    break;
            }
        }
    }

    public void possibleCollisionOfPlayerWithEnemyOffensiveSpell(OffensiveSpell enemyOffensiveSpell) {
        if (satCollisionCalculator.performSeparateAxisCollisionCheck(player.getObjectCollisionVertices(), enemyOffensiveSpell.getObjectCollisionVertices())) {
            player.removeHealth(1);
            GamePanelMultiplayer.sender.sendMessage(new PlayerHitMessage(GamePanelMultiplayer.myNickname));
            spellManager.getOffensiveSpellList().remove(enemyOffensiveSpell);
        }
    }

    public void possibleCollisionOfPlayerWithCollectable(Player player, Collectible collectible) {
        if (collectible.intersectsWithMe(player, this.satCollisionCalculator)) {
            collectible.onInteraction(player, mapManager);
        }
    }

    //</editor-fold>

    public void handleColision(OffensiveSpell offensiveSpell, Set<GameObject> returnObjects) {
        for (GameObject gameObject : returnObjects) {
            switch (gameObject.getCollisionObjectType()) {
                case Obstacle:
                    possibleCollisionOfSpellWithObstacle(offensiveSpell, (Obstacle) gameObject);
                    break;
            }
        }
    }

    public void possibleCollisionOfSpellWithObstacle(OffensiveSpell offensiveSpell, Obstacle obstacle) {
        try {
            if (offensiveSpell.isRemovedOnCollision() && satCollisionCalculator.performSeparateAxisCollisionCheck(obstacle.getObjectCollisionVertices(), offensiveSpell.getObjectCollisionVertices())) {
                spellManager.getOffensiveSpellList().remove(offensiveSpell);
                StaticAnimationManager.addExplosion(new Point(offensiveSpell.getX(), offensiveSpell.getY()), this.resources);
            }
        } catch (Exception e) {
            System.out.print("aaa");
        }
    }

    //<editor-fold desc="quadtree init">
    public void initializeQuadtree() {
        quadtree.clear();

        quadtree.insert(this.player);
        for (Spell spell : spellManager.getOffensiveSpellList()) {
            quadtree.insert(spell);
        }
        for (Obstacle obstacle : mapManager.getObstaclesList()) {
            quadtree.insert(obstacle);
        }
        for (Collectible collectible : mapManager.getCollectibleList()) {
            quadtree.insert(collectible);
        }
    }
    //</editor-fold>

    public void refreshMovingObjects() {
        movingObjects.clear();
        movingObjects.add(player);
        movingObjects.addAll(spellManager.getOffensiveSpellList());
    }

    public void initializeMovingObjects() {
        this.movingObjects.clear();
        this.movingObjects.add(this.player);
        this.movingObjects.addAll(this.spellManager.getOffensiveSpellList());
    }
}
