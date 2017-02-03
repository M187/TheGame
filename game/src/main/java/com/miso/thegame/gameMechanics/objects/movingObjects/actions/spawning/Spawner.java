package com.miso.thegame.gameMechanics.objects.movingObjects.actions.spawning;

import android.content.res.Resources;

import com.miso.thegame.gameMechanics.display.backgroundEffects.FrameDependantTimeout;
import com.miso.thegame.gameMechanics.objects.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.objects.movingObjects.enemies.groundEnemies.Enemy_basic;

/**
 * Created by michal.hornak on 05.10.2016.
 * <p/>
 * Supporting class to handle spawning of enemies.
 */
public class Spawner {

    public int cooldown;
    public FrameDependantTimeout timeout;
    private Spawning mountedOn;

    /**
     * @param cooldown  number of frames to shoot again.
     * @param mountedOn parent object - effectively object that will be spawning enemies.
     */
    public Spawner(int cooldown, Spawning mountedOn) {
        this.cooldown = cooldown;
        this.mountedOn = mountedOn;
        timeout = new FrameDependantTimeout(cooldown);
    }

    /**
     * Try to take shot.
     * Shot will be taken if timeout is not active and if player is in range.
     *
     * @param enemiesManager that will register enemy.
     * @param res resources for enemy
     */
    public void spawn(EnemiesManager enemiesManager, Resources res) {
        if (this.timeout.update() & inRange()) {
            this.timeout = new FrameDependantTimeout(cooldown);
            enemiesManager.enemiesToBeAddedInThiFrame.add(
                    new Enemy_basic(
                            res,
                            this.mountedOn.getPosition()));
        }
    }

    /**
     * @return is player in range?
     */
    private boolean inRange() {
        return (this.mountedOn.getDistanceFromPlayer() < this.mountedOn.getSpawningDistanceThreshold());
    }
}
