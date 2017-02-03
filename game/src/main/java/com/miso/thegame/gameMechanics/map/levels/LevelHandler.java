package com.miso.thegame.gameMechanics.map.levels;

/**
 * Created by Miso on 14.9.2016.
 */
public class LevelHandler {

    private int levelNumber;

    public LevelHandler(){
        this.levelNumber = 1;
    }

    public LevelHandler(int startingLevel){
        this.levelNumber = startingLevel;
    }

    public void increaseLevel(){
        this.levelNumber += 1;
    }

    public int getLevelNumber() {
        return levelNumber;
    }
}
