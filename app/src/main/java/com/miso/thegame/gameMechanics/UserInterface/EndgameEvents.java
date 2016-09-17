package com.miso.thegame.gameMechanics.UserInterface;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.gameViews.GameView2;

/**
 * Created by michal.hornak on 30.12.2015.
 */
public class EndgameEvents {

    private Bitmap endgamePopup;
    private Bitmap endgamePopupVictory;
    private int xDrawCoord;
    private int yDrawCoord;

    public EndgameEvents(Resources res){
        this.endgamePopup = BitmapFactory.decodeResource(res, R.drawable.gameover);
        this.endgamePopupVictory = BitmapFactory.decodeResource(res, R.drawable.gameovervictory);
        this.xDrawCoord = (GameView2.WIDTH / 2) - (endgamePopup.getWidth() / 2);
        this.yDrawCoord = (GameView2.HEIGHT / 2) - (endgamePopup.getHeight() / 2);
    }

    public void draw(Canvas canvas, boolean victory){
        if (victory){
            canvas.drawBitmap(this.endgamePopupVictory, xDrawCoord, yDrawCoord, null);
        }else {
            canvas.drawBitmap(this.endgamePopup, xDrawCoord, yDrawCoord, null);
        }
    }

    public void drawLevelCleared(Canvas canvas){
        canvas.drawBitmap(this.endgamePopupVictory, xDrawCoord, yDrawCoord, null);
    }
}
