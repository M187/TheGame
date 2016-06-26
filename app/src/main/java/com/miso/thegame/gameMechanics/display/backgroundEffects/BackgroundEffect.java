package com.miso.thegame.gameMechanics.display.backgroundEffects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by Miso on 25.6.2016.
 *
 * Class to represent effects.
 * Effects should be added to an effect manager //TODO...
 * Effects should be draw after background, but before any other objects.
 */
public abstract class BackgroundEffect {

    protected Paint myPaint = new Paint();
    protected Point position;
    protected BackgroundEffectTimeout myTimeout;

    public abstract void draw(Canvas canvas);

    public abstract boolean update();
}
