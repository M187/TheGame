package com.miso.thegame.gameMechanics.objects.movingObjects.enemies.groundEnemies;


import com.miso.thegame.gameMechanics.display.backgroundEffects.FrameDependantTimeout;
import com.miso.thegame.gameMechanics.objects.movingObjects.spells.SpellCreator;

/**
 * Created by michal.hornak on 03.10.2016.
 *
 * Supporting class to handle shooting events.
 * Should be reusable all over code.
 */
public class Shooter {

    public int cooldown;
    public FrameDependantTimeout timeout;
    private Shooting mountedOn;

    /**
     * @param cooldown number of frames to shoot again.
     * @param mountedOn parent object - effectively object that will be shooting.
     */
    public Shooter(int cooldown, Shooting mountedOn){
        this.cooldown = cooldown;
        this.mountedOn = mountedOn;
        timeout = new FrameDependantTimeout(cooldown);
    }

    public void takeShot(SpellCreator spellCreator){
        if (this.timeout.update() & inRange()){
            this.timeout = new FrameDependantTimeout(cooldown);
            spellCreator.fireProjectileTowardsPlayer(
                    this.mountedOn.getPosition().x,
                    this.mountedOn.getPosition().y);
        }
    }

    private boolean inRange(){
        return (this.mountedOn.getDistanceFromPlayer() < this.mountedOn.getShootingDistanceThreshold());
    }
}
