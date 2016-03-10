package com.miso.thegame.gameMechanics.UserInterface.Buttons;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.R;

/**
 * Created by Miso on 6.11.2015.
 */
public class _PrimaryShootingButtonPlaceholder extends ButtonPlaceholder {

    public _PrimaryShootingButtonPlaceholder(Resources res){
        this.cooldown = ConstantHolder.firebalCooldown;
        this.lastUse = 0;
        this.image = BitmapFactory.decodeResource(res, R.drawable.buttonfireball);
        this.iteratorToDrawXAxis = 0;
        this.iteratorToDrawYAxis = 0;
        setXDrawCoord();
        setYDrawCoord();
    }
}
