package com.miso.thegame.gameMechanics.interfaces;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * Created by Miso on 19.2.2016.
 */
public interface Drawable {

    public Bitmap getImage();

    public void setImage(Bitmap image);

    public Point getDisplayCoord();

    public Point getMiddleDisplayCoord();

    public void setDisplayCoord(int displayMiddleYCoord, int displayMiddleXCoord);
}
