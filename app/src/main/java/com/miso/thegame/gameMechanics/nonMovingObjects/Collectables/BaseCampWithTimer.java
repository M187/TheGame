package com.miso.thegame.gameMechanics.nonMovingObjects.Collectables;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;

/**
 * Created by Miso on 21.12.2015.
 */
public class BaseCampWithTimer extends Collectible {

    private int timeoutFrames = 0;
    private Paint paint = new Paint();

    public BaseCampWithTimer(Resources res, Point position){
        super(position, BitmapFactory.decodeResource(res, R.drawable.basecamp));
        paint.setColor(Color.RED);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(45);
    }

    public void onInteraction(Player player, MapManager mapManager){
        if (!(player.currentHealth == player.maxHealth && player.primaryAmunition == player.primaryAmunitionMaxValue)) {
            if (this.timeoutFrames <= 0) {
                player.replenishPrimaryAmmo();
                player.addHealthToMax();
                this.timeoutFrames = 180;
            }
        }
    }

    public void draw(Canvas canvas, int x, int y){
        canvas.drawBitmap(this.getImage(), x, y, null);
        if (this.timeoutFrames > 0) {
            this.timeoutFrames -= 1;
            this.drawCooldown(canvas, new Point(x, y));
        }
    }

    public void drawCooldown(Canvas canvas, Point position){
        String number = Long.toString((this.timeoutFrames / 30));
        canvas.drawText(number, position.x + this.getHalfWidth(),
                position.y + this.getImage().getHeight() + ((paint.descent() + paint.ascent())), paint);
    }
}
