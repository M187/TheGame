package com.miso.thegame.gameMechanics.display;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.miso.thegame.GamePanel;
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

    public Background(Bitmap image, Anchor anchor){
        this.image = Bitmap.createScaledBitmap(image, GamePanel.WIDTH, GamePanel.HEIGHT, false);
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
                    drawCustom(canvas, xDefaultDrawCoord + GamePanel.WIDTH, yDefaultDrawCoord);
                    drawCustom(canvas, xDefaultDrawCoord, yDefaultDrawCoord + GamePanel.HEIGHT);
                    drawCustom(canvas, xDefaultDrawCoord + GamePanel.WIDTH, yDefaultDrawCoord + GamePanel.HEIGHT);
                } else {
                    drawCustom(canvas, xDefaultDrawCoord + GamePanel.WIDTH, yDefaultDrawCoord);
                    drawCustom(canvas, xDefaultDrawCoord, yDefaultDrawCoord - GamePanel.HEIGHT);
                    drawCustom(canvas, xDefaultDrawCoord + GamePanel.WIDTH, yDefaultDrawCoord - GamePanel.HEIGHT);
                }
            } else {
                if (anchor.getY() > y) {
                    drawCustom(canvas, xDefaultDrawCoord - GamePanel.WIDTH, yDefaultDrawCoord);
                    drawCustom(canvas, xDefaultDrawCoord, yDefaultDrawCoord + GamePanel.HEIGHT);
                    drawCustom(canvas, xDefaultDrawCoord - GamePanel.WIDTH, yDefaultDrawCoord + GamePanel.HEIGHT);
                } else {
                    drawCustom(canvas, xDefaultDrawCoord - GamePanel.WIDTH, yDefaultDrawCoord);
                    drawCustom(canvas, xDefaultDrawCoord, yDefaultDrawCoord - GamePanel.HEIGHT);
                    drawCustom(canvas, xDefaultDrawCoord - GamePanel.WIDTH, yDefaultDrawCoord - GamePanel.HEIGHT);
                }
            }
        }
    }

    public void backgroundOffDisplay(Anchor anchor){
        if (x > anchor.getX() + GamePanel.WIDTH){
            x = x - GamePanel.WIDTH;
        }
        if (y > anchor.getY() + GamePanel.HEIGHT){
            y = y - GamePanel.HEIGHT;
        }
        if (x < anchor.getX() - GamePanel.WIDTH){
            x = x + GamePanel.WIDTH;
        }
        if (y < anchor.getY() - GamePanel.HEIGHT){
            y = y + GamePanel.HEIGHT;
        }
    }

    public void drawCustom(Canvas canvas, int x, int y){
        canvas.drawBitmap(image, x, y, null);
    }

}
