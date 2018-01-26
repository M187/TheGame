package com.miso.thegame.GameData;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.ConstantHolder;

/**
 * Created by michal.hornak on 21.06.2016.
 */
public enum ButtonTypeEnum {

    Shockwave("Shockwave", R.drawable.buttonshockwave2, ConstantHolder.shockwaveCooldown),
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
        switch (playerType.toLowerCase()){
            case "shockwave":
                return ButtonTypeEnum.Shockwave;
            case "timestop":
                return ButtonTypeEnum.TimeStop;
            case "freezing projectiles":
                return ButtonTypeEnum.FreezingProjectiles;
        }
        return null;
    }

    public String getButtonTypeString() {
        return this.buttonType;
    }

}
