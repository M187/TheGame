package com.miso.thegame.gameMechanics.movingObjects.spells.playerSpells.defensiveSpells;

import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.movingObjects.Player;

/**
 * Created by Miso on 23.12.2015.
 */
public class Timestop extends DeffensiveSpell{

    private long startTime = System.currentTimeMillis();
    private int durationTime = ConstantHolder.timestopDuration;
    private Player player;

    public Timestop(Player player){
        this.player = player;
    }

    public void update(){
        ConstantHolder.timestopActive = true;
    }

    public boolean removeSpell(){
        long currentTime = System.currentTimeMillis();
        if (currentTime - startTime > durationTime){
            ConstantHolder.timestopActive = false;
            return true;
        }else{
            return false;
        }
    }

    public void draw(){
        return;
    }
}
