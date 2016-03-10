package com.miso.thegame.gameMechanics.movingObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.Animation;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.map.pathfinding.Pathfinder;

import java.util.ArrayList;

/**
 * Created by Miso on 29.4.2015.
 */
public final class Player_Saucer extends Player {

    private Animation animation = new Animation();

    //TODO: static variables for coordinates. ? Init to a static variable in GamePanel?
    public Player_Saucer(Resources res,Point startingPosition, MapManager mapManager) {
        animation.initializeSprites(BitmapFactory.decodeResource(res, R.drawable.player_saucer), 70, 70, 8, 25);
        setX(startingPosition.x);
        setY(startingPosition.y);
        this.collisionObjectType = CollisionObjectType.Player;
        setSpeed(ConstantHolder.maximumPlayerSpeed);
        score = 0;
        dx = (getX());
        dy = (getY());

        this.mapManager = mapManager;
        setImage(animation.getImage());
        this.setCurrentGridCoordinates(super.getGridCoordinates());
    }

    @Override
    public ArrayList<Point> getObjectCollisionVertices() {
        ArrayList<Point> objectRotatedVertices = new ArrayList<>();

        for (Point p : this.getNonRotatedVertices()) {
            objectRotatedVertices.add(rotateVertexAroundCurrentPosition(p));
        }
        return objectRotatedVertices;
    }

    protected ArrayList<Point> getNonRotatedVertices() {
        ArrayList<Point> vertices = new ArrayList();
        vertices.add(new Point(this.x,this.y - 50));
        vertices.add(new Point(this.x-25,this.y + 37));
        vertices.add(new Point(this.x,this.y+50));
        vertices.add(new Point(this.x+25,this.y+37));

        return vertices;
    }

    public void drawObject(Canvas canvas){
        //Draw object
        animation.update();
        canvas.drawBitmap(this.animation.getImage(), this.getDisplayXCoord(), this.getDisplayYCoord(), null);
    }
}
