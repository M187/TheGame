package com.miso.thegame.gameMechanics.movingObjects.player;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.map.MapManager;

/**
 * Created by Miso on 5.6.2016.
 */
public class PlayerTank extends Player {

    private Bitmap turretImage = null;

    //TODO: static variables for coordinates. ? Init to a static variable in GamePanel?
    public PlayerTank(Resources res,Point startingPosition, MapManager mapManager) {
        setX(startingPosition.x);
        setY(startingPosition.y);
        this.collisionObjectType = CollisionObjectType.Player;
        setSpeed(ConstantHolder.maximumPlayerSpeed);
        score = 0;
        dx = (getX());
        dy = (getY());

        setImage(BitmapFactory.decodeResource(res, R.drawable.tankbody2));
        this.turretImage = (BitmapFactory.decodeResource(res, R.drawable.tankturret2));
        this.mapManager = mapManager;
        this.setCurrentGridCoordinates(super.getGridCoordinates());
    }

    public void drawObject(Canvas canvas, int x, int y){
        canvas.drawBitmap(this.getImage(), x, y, null);
        canvas.drawBitmap(this.turretImage, x + 2, y - 10, null);
    }
}
