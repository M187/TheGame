package com.miso.thegame.Networking.transmitionData;

/**
 * Created by michal.hornak on 20.04.2016.
 */
public enum Actions {

    Move("Move"),
    ShootProjectile("ShootProjectile"),
    None("Move");

    private String actionString;

    Actions(String actionString){
        this.actionString = actionString;
    }

    public String toString(){
        return this.actionString;
    }
}
