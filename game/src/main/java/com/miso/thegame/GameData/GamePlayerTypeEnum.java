package com.miso.thegame.GameData;

/**
 * Created by michal.hornak on 10.06.2016.
 */
public enum GamePlayerTypeEnum {

    PlayerTriangle("Triangle"),
    PlayerSpaceship("Spaceship"),
    PlayerSaucer("Saucer");

    private String playerType;

    GamePlayerTypeEnum(String playerType) {
        this.playerType = playerType;
    }

    public static GamePlayerTypeEnum getPlayerTypeFromTypeString(String playerType){
        switch (playerType){
            case "Spaceship":
                return GamePlayerTypeEnum.PlayerSpaceship;
            case "Saucer":
                return GamePlayerTypeEnum.PlayerSaucer;
            case "Triangle":
                return GamePlayerTypeEnum.PlayerTriangle;
        }
        return GamePlayerTypeEnum.PlayerSpaceship;
    }

    public String getTypeString() {
        return this.playerType;
    }

}
