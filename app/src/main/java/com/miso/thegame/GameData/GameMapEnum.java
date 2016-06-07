package com.miso.thegame.GameData;

/**
 * Created by Miso on 11.2.2016.
 */
public enum GameMapEnum {

    Level1("backgroundgrass"),
    Level2("backgroundsand"),
    SpaceLevel1("backgroundspace"),
    SpaceLevel2("backgroundspace"),
    BlankMap("backgroundgrass"),
    MultiplayerMap1("backgroundspace");

    private String background;

    GameMapEnum(String background){
        this.background = background;
    }

    public String getBackgroundImageName(){
        return this.background;
    }
}
