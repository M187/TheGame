package com.miso.thegame.GameData;

/**
 * Created by michal.hornak on 21.06.2016.
 */
public enum ButtonTypeEnum {

    Shockwave("Shockvawe"),
    TimeStop("Timestop"),
    FreezingProjectiles("Freezing Projectiles");

    private String buttonType;

    ButtonTypeEnum(String playerType) {
        this.buttonType = playerType;
    }

    public static ButtonTypeEnum getButtonTypeFromButtonTypeString(String playerType){
        switch (playerType){
            case "Shockvawe":
                return ButtonTypeEnum.Shockwave;
            case "Timestop":
                return ButtonTypeEnum.TimeStop;
            case "Freezing Projectiles":
                return ButtonTypeEnum.FreezingProjectiles;
        }
        return ButtonTypeEnum.Shockwave;
    }

    public String getButtonTypeString() {
        return this.buttonType;
    }

}
