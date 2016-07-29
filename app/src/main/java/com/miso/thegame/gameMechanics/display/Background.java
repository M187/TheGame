package com.miso.thegame.gameMechanics.display;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.movingObjects.Anchor;

/**
 * Created by Miso on 26.4.2015.
 */
public class Background {

    private Bitmap image;
    private int x;
    private int y;
//    private int xHalfRange;
//    private int yHalfRange;

    /**
     * Should be used in debug instance, where anchor is not needed.
     * @param image
     */
    public Background(Bitmap image, Context context){

        // Initialize display metrics.
        final WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final Display d = w.getDefaultDisplay();
        final DisplayMetrics m = new DisplayMetrics();
        d.getMetrics(m);

        this.image = Bitmap.createScaledBitmap(image, m.widthPixels, m.heightPixels, false);
    }

    public Background(Bitmap image, Anchor anchor){
        this.image = Bitmap.createScaledBitmap(image, GameView2.WIDTH, GameView2.HEIGHT, false);
        this.x = anchor.getX();
        this.y = anchor.getY();
//        this.xHalfRange = x + (GamePanel.WIDTH/2);
//        this.yHalfRange = y + (GamePanel.HEIGHT/2);
    }

    public void draw(Canvas canvas, Anchor anchor)
    {
        //Check in what sector is anchor -> decide on which side draw additional BG
        //Via flags onTopSide / onLeftSide, decide which one of 4 options to draw background use.

        //Check if background is not of display. If so, increment/decrement coordinate by display resolution.
        backgroundOffDisplay(anchor);

        int xDefaultDrawCoord = x - anchor.getX();
        int yDefaultDrawCoord = y - anchor.getY();
        //Check if anchor = bgCoords. If so, draw only one BG
        if (x == anchor.getX() && y == anchor.getY()){
            canvas.drawBitmap(image, xDefaultDrawCoord, yDefaultDrawCoord, null);
        } else {
            //Draw backgrounds due to anchor position
            drawCustom(canvas, xDefaultDrawCoord, yDefaultDrawCoord);
            if (anchor.getX() > x) {
                if (anchor.getY() > y) {
                    drawCustom(canvas, xDefaultDrawCoord + GameView2.WIDTH, yDefaultDrawCoord);
                    drawCustom(canvas, xDefaultDrawCoord, yDefaultDrawCoord + GameView2.HEIGHT);
                    drawCustom(canvas, xDefaultDrawCoord + GameView2.WIDTH, yDefaultDrawCoord + GameView2.HEIGHT);
                } else {
                    drawCustom(canvas, xDefaultDrawCoord + GameView2.WIDTH, yDefaultDrawCoord);
                    drawCustom(canvas, xDefaultDrawCoord, yDefaultDrawCoord - GameView2.HEIGHT);
                    drawCustom(canvas, xDefaultDrawCoord + GameView2.WIDTH, yDefaultDrawCoord - GameView2.HEIGHT);
                }
            } else {
                if (anchor.getY() > y) {
                    drawCustom(canvas, xDefaultDrawCoord - GameView2.WIDTH, yDefaultDrawCoord);
                    drawCustom(canvas, xDefaultDrawCoord, yDefaultDrawCoord + GameView2.HEIGHT);
                    drawCustom(canvas, xDefaultDrawCoord - GameView2.WIDTH, yDefaultDrawCoord + GameView2.HEIGHT);
                } else {
                    drawCustom(canvas, xDefaultDrawCoord - GameView2.WIDTH, yDefaultDrawCoord);
                    drawCustom(canvas, xDefaultDrawCoord, yDefaultDrawCoord - GameView2.HEIGHT);
                    drawCustom(canvas, xDefaultDrawCoord - GameView2.WIDTH, yDefaultDrawCoord - GameView2.HEIGHT);
                }
            }
        }
    }

    public void backgroundOffDisplay(Anchor anchor){
        if (x > anchor.getX() + GameView2.WIDTH){
            x = x - GameView2.WIDTH;
        }
        if (y > anchor.getY() + GameView2.HEIGHT){
            y = y - GameView2.HEIGHT;
        }
        if (x < anchor.getX() - GameView2.WIDTH){
            x = x + GameView2.WIDTH;
        }
        if (y < anchor.getY() - GameView2.HEIGHT){
            y = y + GameView2.HEIGHT;
        }
    }

    public void drawCustom(Canvas canvas, int x, int y){
        canvas.drawBitmap(image, x, y, null);
    }

}
