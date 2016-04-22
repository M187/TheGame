package com.miso.thegame.gameMechanics.UserInterface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameViews.GameViewAbstract;

/**
 * Created by Miso on 21.12.2015.
 */
public class PlayerStatusBar {

    private int xDrawCoord, yDrawCoord;
    public Player player;
    protected static final Paint paint = new Paint();

    public PlayerStatusBar(Player player){
        this.player = player;
        //todo - this needs to be generic ↓↓
        this.xDrawCoord = GameViewAbstract.WIDTH / 2 - 350;
        this.yDrawCoord = GameViewAbstract.HEIGHT - 140;
    }

    public void draw(Canvas canvas){
        drawText(canvas);
    }

    public void drawText(Canvas canvas){
        paint.setColor(Color.YELLOW);
        int scaling = 20;
        paint.setTextSize(scaling);
        canvas.drawText("Health: " + player.maxHealth + " / " + player.currentHealth, this.xDrawCoord + 70, this.yDrawCoord + 60, paint);

        paint.setStrokeWidth(10);
        canvas.drawRect(this.xDrawCoord + 270, this.yDrawCoord + 40, this.xDrawCoord + 270 + (350 * ((float) player.currentHealth / player.maxHealth)), this.yDrawCoord + 70, paint);

        canvas.drawText("Ammo left: " + player.primaryAmunition + " / " + player.primaryAmunitionMaxValue, this.xDrawCoord + 70, this.yDrawCoord + 100, paint);
    }
}
