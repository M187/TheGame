package com.miso.thegame.gameMechanics.UserInterface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.objects.movingObjects.player.Player;

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
        this.xDrawCoord = GameView2.WIDTH / 2 - (int)GameView2.scaleSize(175);
        this.yDrawCoord = GameView2.HEIGHT - (int)GameView2.scaleSize(70);

        paint.setColor(Color.BLACK);
        int scaling = (int)GameView2.scaleSize(10);
        paint.setTextSize(scaling);
        //paint.setAlpha(200);
        paint.setStrokeWidth(20);

        paint2.setColor(Color.WHITE);
        paint2.setAlpha(80);
        paint2.setStrokeWidth(10);
    }

    public void draw(Canvas canvas){
        drawText(canvas);
    }

    public void drawText(Canvas canvas) {
        //init these value into memory to save performance.
        canvas.drawRect(
                this.xDrawCoord + (int)GameView2.scaleSize(40),
                this.yDrawCoord + (int)GameView2.scaleSize(10),
                this.xDrawCoord + (int)GameView2.scaleSize(300),
                this.yDrawCoord + (int)GameView2.scaleSize(60),
                paint2);
        canvas.drawText(
                "Health: " + player.maxHealth + " / " + player.currentHealth,
                this.xDrawCoord + (int)GameView2.scaleSize(45),
                this.yDrawCoord + (int)GameView2.scaleSize(25),
                paint);
        canvas.drawRect(
                this.xDrawCoord + (int)GameView2.scaleSize(120),
                this.yDrawCoord + (int)GameView2.scaleSize(15),
                this.xDrawCoord + (int)GameView2.scaleSize(120 + (170 * ((float) player.currentHealth / player.maxHealth))),
                this.yDrawCoord + (int)GameView2.scaleSize(25),
                paint);
        canvas.drawText(
                "Ammo left: " + player.primaryAmunition + " / " + player.primaryAmunitionMaxValue,
                this.xDrawCoord + (int)GameView2.scaleSize(45),
                this.yDrawCoord + (int)GameView2.scaleSize(45),
                paint);
    }
}
