package com.miso.thegame.gameMechanics.UserInterface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameViews.GameView2;

/**
 * Created by Miso on 21.12.2015.
 */
public class PlayerStatusBar {

    protected static final Paint paint = new Paint();
    protected static final Paint paint2 = new Paint();
    public Player player;
    private int xDrawCoord, yDrawCoord;

    public PlayerStatusBar(Player player){
        this.player = player;
        //todo - this needs to be generic ↓↓
        this.xDrawCoord = GameView2.WIDTH / 2 - 350;
        this.yDrawCoord = GameView2.HEIGHT - 140;

        paint.setColor(Color.YELLOW);
        int scaling = 20;
        paint.setTextSize(scaling);
        paint.setAlpha(150);
        paint.setStrokeWidth(10);

        paint2.setColor(Color.WHITE);
        paint2.setAlpha(80);
        paint2.setStrokeWidth(10);
    }

    public void draw(Canvas canvas){
        drawText(canvas);
    }

    public void drawText(Canvas canvas) {
        canvas.drawRect(this.xDrawCoord + 60, this.yDrawCoord + 30, this.xDrawCoord + 610, this.yDrawCoord + 110, paint2);

        canvas.drawText("Health: " + player.maxHealth + " / " + player.currentHealth, this.xDrawCoord + 70, this.yDrawCoord + 60, paint);
        canvas.drawRect(this.xDrawCoord + 270, this.yDrawCoord + 40, this.xDrawCoord + 250 + (350 * ((float) player.currentHealth / player.maxHealth)), this.yDrawCoord + 70, paint);
        canvas.drawText("Ammo left: " + player.primaryAmunition + " / " + player.primaryAmunitionMaxValue, this.xDrawCoord + 70, this.yDrawCoord + 100, paint);
    }
}
