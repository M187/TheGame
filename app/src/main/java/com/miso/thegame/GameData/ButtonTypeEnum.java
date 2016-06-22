package com.miso.thegame.GameData;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.ConstantHolder;

/**
 * Created by michal.hornak on 21.06.2016.
 */
public enum ButtonTypeEnum {

    Shockwave("Shockvawe", R.drawable.buttonshockwave2, ConstantHolder.shockwaveCooldown),
    TimeStop("Timestop",R.drawable.timestop, ConstantHolder.timestopCooldown),
    FreezingProjectiles("Freezing Projectiles",R.drawable.freeze, ConstantHolder.freezeCooldown);

    public final int resourceParameterString;
    public int buttonActionCooldown;
    private String buttonType;

    ButtonTypeEnum(String playerType, int resourceParameterString, int buttonActionCooldown) {
        this.buttonType = playerType;
        this.resourceParameterString = resourceParameterString;
        this.buttonActionCooldown = buttonActionCooldown;
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
