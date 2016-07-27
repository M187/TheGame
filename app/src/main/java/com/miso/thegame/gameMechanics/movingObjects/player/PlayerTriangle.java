package com.miso.thegame.gameMechanics.movingObjects.player;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.map.MapManager;

import java.util.ArrayList;

/**
 * Created by Miso on 5.6.2016.
 */
public class PlayerTriangle extends Player {

    private Paint paint = new Paint();
    private Paint circlePaint = new Paint();

    public PlayerTriangle(Point startingPosition, MapManager mapManager) {
        setX(startingPosition.x);
        setY(startingPosition.y);
        this.collisionObjectType = CollisionObjectType.Player;
        setSpeed(ConstantHolder.maximumPlayerSpeed);
        score = 0;
        dx = (getX());
        dy = (getY());

        this.mapManager = mapManager;
        this.setCurrentGridCoordinates(super.getGridCoordinates());

        this.paint.setColor(android.graphics.Color.RED);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paint.setAntiAlias(true);
        this.paint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));

        this.circlePaint.setColor(android.graphics.Color.BLACK);
        this.circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setAlpha(100);
    }

    public void drawObject(Canvas canvas, int x, int y){

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(x + 25, y);
        path.lineTo(x, y + 50);
        path.lineTo(x + 50, y + 50);
        path.lineTo(x + 25, y);
        path.close();

        canvas.drawPath(path, this.paint);
        canvas.drawCircle(x + 25, y + 20, 5, this.circlePaint);
        canvas.drawCircle(x+25, y + 40, 5, this.circlePaint);
    }

    protected ArrayList<Point> getNonRotatedVertices() {
        ArrayList<Point> vertices = new ArrayList();
        vertices.add(new Point(this.x + 25  - getHalfWidth(), this.y - getHalfHeight()));
        vertices.add(new Point(this.x + 50 - getHalfWidth(), this.y + 50 - getHalfHeight()));
        vertices.add(new Point(this.x - getHalfWidth(), this.y + 50 - getHalfHeight()));

        return vertices;
    }

    @Override
    public int getHalfWidth(){
        return 25;
    }
    @Override
    public int getHalfHeight(){
        return 25;
    }

}
