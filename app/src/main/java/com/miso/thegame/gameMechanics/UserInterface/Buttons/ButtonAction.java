package com.miso.thegame.gameMechanics.UserInterface.Buttons;

import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.R;

/**
 * Created by Miso on 23.12.2015.
 */
public enum ButtonAction {

    Shockwave (R.drawable.buttonshockwave2, ConstantHolder.shockwaveCooldown),
    Timestop (R.drawable.timestop, ConstantHolder.timestopCooldown),
    Freeze (R.drawable.freeze, ConstantHolder.freezeCooldown);

    public final int resourceParameterString;
    public int buttonActionCooldown;

    ButtonAction(int resourceParameterString, int buttonActionCooldown){
        this.resourceParameterString = resourceParameterString;
        this.buttonActionCooldown = buttonActionCooldown;
    }
}
