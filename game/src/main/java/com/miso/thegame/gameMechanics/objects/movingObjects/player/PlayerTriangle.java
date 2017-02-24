package com.miso.thegame.gameMechanics.objects.movingObjects.player;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.objects.collisionHandlers.CollisionObjectType;

import java.util.ArrayList;

/**
 * Created by Miso on 5.6.2016.
 */
public class PlayerTriangle extends Player {

    private Paint paint = new Paint();
    private Paint circlePaint = new Paint();

    // constructor used in multiplayer Player objects
    protected PlayerTriangle(Point startingPosition, int color){
        this(startingPosition, null);
        this.circlePaint.setColor(color);

    }

    public PlayerTriangle(Point startingPosition, MapManager mapManager) {
        setX(startingPosition.x);
        setY(startingPosition.y);
        this.collisionObjectType = CollisionObjectType.Player;
        setSpeed(ConstantHolder.getMaximumSpeed());
        score = 0;
        dx = (getX());
        dy = (getY());

        this.mapManager = mapManager;
        this.setCurrentGridCoordinates(super.getGridCoordinates());

        this.paint.setColor(android.graphics.Color.RED);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paint.setAntiAlias(true);
        this.paint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));

        this.circlePaint.setColor(android.graphics.Color.BLUE);
        this.circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setAlpha(100);
    }

    public void drawObject(Canvas canvas, int x, int y){

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(x + (int) GameView2.scaleSize(12), y);
        path.lineTo(x, y + (int)GameView2.scaleSize(25));
        path.lineTo(x + (int)GameView2.scaleSize(25), y + (int)GameView2.scaleSize(25));
        path.lineTo(x + (int)GameView2.scaleSize(12), y);
        path.close();

        canvas.drawPath(path, this.paint);
        canvas.drawCircle(x + (int)GameView2.scaleSize(12), y + (int)GameView2.scaleSize(10), (int)GameView2.scaleSize(2), this.circlePaint);
        canvas.drawCircle(x + (int)GameView2.scaleSize(12), y + (int)GameView2.scaleSize(20), (int)GameView2.scaleSize(3), this.circlePaint);
    }

    protected ArrayList<Point> getNonRotatedVertices() {
        ArrayList<Point> vertices = new ArrayList();
        vertices.add(new Point(this.x + (int)GameView2.scaleSize(12)  - getHalfWidth(), this.y - getHalfHeight()));
        vertices.add(new Point(this.x + (int)GameView2.scaleSize(25) - getHalfWidth(), this.y + (int)GameView2.scaleSize(25) - getHalfHeight()));
        vertices.add(new Point(this.x - getHalfWidth(), this.y + (int)GameView2.scaleSize(25) - getHalfHeight()));

        return vertices;
    }

    @Override
    public int getHalfWidth(){
        return (int)GameView2.scaleSize(12);
    }
    @Override
    public int getHalfHeight(){
        return (int)GameView2.scaleSize(12);
    }

}
