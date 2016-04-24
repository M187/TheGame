package com.miso.thegame.gameMechanics.UserInterface;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.miso.thegame.R;
import com.miso.thegame.gameViews.GameVieew;

/**
 * Created by michal.hornak on 30.12.2015.
 */
public class EndgameEvents {

    private Bitmap endgamePopup;
    private int xDrawCoord;
    private int yDrawCoord;

    public EndgameEvents(Resources res){
        this.endgamePopup = BitmapFactory.decodeResource(res, R.drawable.gameover);
        this.xDrawCoord = (GameVieew.WIDTH / 2) - (endgamePopup.getWidth() / 2);
        this.yDrawCoord = (GameVieew.HEIGHT / 2) - (endgamePopup.getHeight() / 2);
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(this.endgamePopup, xDrawCoord, yDrawCoord, null);
    }
}
