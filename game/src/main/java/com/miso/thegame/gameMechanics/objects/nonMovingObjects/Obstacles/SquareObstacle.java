package com.miso.thegame.gameMechanics.objects.nonMovingObjects.Obstacles;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.miso.thegame.R;

/**
 * Created by Miso on 27.6.2016.
 */
public class SquareObstacle extends Obstacle {

    private Paint myPaint = new Paint();
    private int sideSize = 100;

    public SquareObstacle (Resources res, Point coordPoint){
        super(coordPoint, BitmapFactory.decodeResource(res, R.drawable.obstacletree1xx100x100));
        initializeRange();
        relativeTilePositions = new Point[]{
                new Point(0,0)
        };

        this.myPaint.setColor(Color.RED);
        this.myPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER));

        this.initializeVertices();
    }

    public void initializeRange(){
        int xSize = this.sideSize / 2;
        int ySize = this.sideSize / 2;
        this.range = (int)(Math.sqrt( xSize * xSize + ySize * ySize ));
    }

    @Override
    public void draw(Canvas canvas, int xPosition, int yPosition){

        this.myPaint.setColor(Color.RED);
        canvas.drawRect(
                xPosition,
                yPosition,
                xPosition + this.sideSize,
                yPosition + this.sideSize,
                myPaint);

        this.myPaint.setColor(Color.BLACK);
        canvas.drawRect(
                xPosition + 10,
                yPosition + 10,
                xPosition + this.sideSize - 10,
                yPosition + this.sideSize - 10,
                myPaint);
    }

    @Override
    protected void initializeVertices(){
        this.objectVertices.clear();
        this.objectVertices.add(new Point(getX(), getY()));
        this.objectVertices.add(new Point(getX(), getY() + this.sideSize));
        this.objectVertices.add(new Point(getX() + this.sideSize, getY() + this.sideSize));
        this.objectVertices.add(new Point(getX() + this.sideSize, getY()));
    }
}
