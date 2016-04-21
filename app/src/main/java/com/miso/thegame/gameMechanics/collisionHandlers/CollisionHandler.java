package com.miso.thegame.gameMechanics.collisionHandlers;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;

import com.miso.thegame.gameMechanics.GameObject;
import com.miso.thegame.gameMechanics.display.Animations.StaticAnimationManager;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.MovableObject;
import com.miso.thegame.gameMechanics.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.movingObjects.enemies.Enemy;
import com.miso.thegame.gameMechanics.movingObjects.enemies.groundEnemies.Enemy_basic;
import com.miso.thegame.gameMechanics.movingObjects.enemies.spaceEnemies.Enemy_alienShip;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.movingObjects.spells.OffensiveSpell;
import com.miso.thegame.gameMechanics.movingObjects.spells.Spell;
import com.miso.thegame.gameMechanics.movingObjects.spells.SpellManager;
import com.miso.thegame.gameMechanics.nonMovingObjects.Collectables.Collectible;
import com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles.Obstacle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Miso on 7.12.2015.
 */
public class CollisionHandler {

    private Player player;
    private EnemiesManager enemiesManager;
    private SpellManager spellManager;
    private MapManager mapManager;
    private Quadtree quadtree;
    private SATCollisionCalculator satCollisionCalculator = new SATCollisionCalculator();
    private Resources resources;

    private ArrayList<MovableObject> movingObjects = new ArrayList<>();
    // nice guide - http://www.ibm.com/developerworks/library/j-jtp0730/
    private SynchronousQueue<GameObject> movingObjectQueue = new SynchronousQueue<>();

    public CollisionHandler(Player player, EnemiesManager enemiesManager, SpellManager spellManager, MapManager mapManager, Resources res) {
        this.player = player;
        this.enemiesManager = enemiesManager;
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
                case Enemy:
                    this.handleColision((Enemy) movingObject, returnObjects);
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
                case Enemy:
                    possibleCollisionOfPlayerWithEnemy((Enemy) gameObject);
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
            spellManager.getOffensiveSpellList().remove(enemyOffensiveSpell);
        }
    }

    public void possibleCollisionOfPlayerWithEnemy(Enemy enemy) {
        if (satCollisionCalculator.performSeparateAxisCollisionCheck(player.getObjectCollisionVertices(), enemy.getObjectCollisionVertices())) {
            enemy.handleCollisionWithPlayer(player, enemiesManager);
        }
    }

    public void possibleCollisionOfPlayerWithCollectable(Player player, Collectible collectible) {
        if (collectible.intersectsWithMe(player, this.satCollisionCalculator)) {
            collectible.onInteraction(player, mapManager);
        }
    }

    //</editor-fold>

    //<editor-fold desc="enemy collision">

    /**
     * Handles collision of enemy with other objects.
     * 3 types of colidable objects:
     * Enemy, Spel, Obstacle
     *
     * @param returnObjects List of objects to check against.
     */
    public void handleColision(Enemy enemy, Set<GameObject> returnObjects) {
        returnObjects.remove(enemy);
        for (GameObject gameObject : returnObjects) {

            switch (gameObject.getCollisionObjectType()) {
                case SpellPlayer:
                    possibleCollisionOfEnemyWithSpell(enemy, (OffensiveSpell) gameObject);
                    break;
                case Enemy:
                    if (gameObject instanceof Enemy_alienShip | enemy instanceof Enemy_alienShip | enemy instanceof Enemy_basic | gameObject instanceof Enemy_basic) {
                        return;
                    }
                    possibleCollisionOfEnemyWithEnemy(enemy, (Enemy) gameObject);
                    break;
            }
        }
    }

    /**
     * Checks for enemy if it was hit by a spell.
     * If so, remove enemy from a enemyList, also checks if remove spell from spell list.
     */
    public void possibleCollisionOfEnemyWithSpell(Enemy enemy, OffensiveSpell offensiveSpell) {
        try {
            if (offensiveSpell.collideWithMovingObject(enemy, satCollisionCalculator)) {
                if (offensiveSpell.isRemovedOnCollision()) {
                    spellManager.getOffensiveSpellList().remove(offensiveSpell);
                    StaticAnimationManager.addExplosion(new Point(offensiveSpell.getX(), offensiveSpell.getY()), this.resources);
                }
                if (enemy.hitBySpell()) {
                    enemiesManager.getEnemyList().remove(enemy);
                }
            }
        } catch (NullPointerException e) {
            //System.out.println("Projectile probably already removed??");
        }
    }

    public void possibleCollisionOfEnemyWithEnemy(Enemy enemy, Enemy enemy2) {
        try {
            if (satCollisionCalculator.performSeparateAxisCollisionCheck(enemy.getObjectCollisionVertices(), enemy2.getObjectCollisionVertices())) {
                moveDueToIntersect(enemy, enemy2);
            }
        } catch (Exception e) {
            System.out.print("aaa");
        }
    }

    /**
     * Function to calculate where to move intersecting enemy, based on player position and intersection.
     * Get delta for X or Y. Then based on a current player position move enemy further from player in X or Y way based on which delta is bigger.
     * (Move axis is one that has bigger delta, always move further from player)
     *
     * @param enemy       - enemy (one that is closer to the player) to extract destination coordinates to move other enemy to.
     * @param enemyToMove - enemy which we want to change coordinates.
     */
    public void moveDueToIntersect(Enemy enemy, Enemy enemyToMove) {
        int deltaX = Math.abs(enemy.getX() - enemyToMove.getX());
        int deltaY = Math.abs(enemy.getY() - enemyToMove.getY());

        if (deltaX > deltaY) {
            if (enemyToMove.getX() <= enemy.getX()) {
                enemyToMove.setX(enemyToMove.getX() - deltaX);
            } else {
                enemyToMove.setX(enemyToMove.getX() + deltaX);
            }
        } else {
            if (enemyToMove.getY() <= enemy.getY()) {
                enemyToMove.setY(enemyToMove.getY() - deltaY);
            } else {
                enemyToMove.setY(enemyToMove.getY() + deltaY);
            }
            if (satCollisionCalculator.performSeparateAxisCollisionCheck(enemy.getObjectCollisionVertices(), enemyToMove.getObjectCollisionVertices())) {
                if (enemyToMove.getX() <= enemy.getX()) {
                    enemyToMove.setX(enemyToMove.getX() - deltaX);
                } else {
                    enemyToMove.setX(enemyToMove.getX() + deltaX);
                }
            }
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
        for (Enemy enemy : enemiesManager.getEnemyList()) {
            quadtree.insert(enemy);
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
        movingObjects.addAll(enemiesManager.getEnemyList());
    }

    public void initializeMovingObjects() {
        this.movingObjects.clear();
        this.movingObjects.add(this.player);
        this.movingObjects.addAll(this.spellManager.getOffensiveSpellList());
        this.movingObjects.addAll(this.enemiesManager.getEnemyList());
    }
}
