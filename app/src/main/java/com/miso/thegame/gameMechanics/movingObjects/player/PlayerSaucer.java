package com.miso.thegame.gameMechanics.movingObjects.player;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.display.Animations.FlyingSaucer;
import com.miso.thegame.gameMechanics.display.Animations.StaticAnimation;
import com.miso.thegame.gameMechanics.map.MapManager;

import java.util.ArrayList;

/**
 * Created by Miso on 29.4.2015.
 */
public final class PlayerSaucer extends Player {

    private StaticAnimation animation = new StaticAnimation();

    //TODO: static variables for coordinates. ? Init to a static variable in GamePanel?
    public PlayerSaucer(Resources res, Point startingPosition, MapManager mapManager) {
        this.animation = new FlyingSaucer(startingPosition, res);
        //animation.initializeSprites(BitmapFactory.decodeResource(res, R.drawable.player_saucer), 70, 70, 8, 25);
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

    protected ArrayList<Point> getNonRotatedVertices() {
        ArrayList<Point> vertices = new ArrayList();
        vertices.add(new Point(this.x,this.y - 50));
        vertices.add(new Point(this.x-25,this.y + 37));
        vertices.add(new Point(this.x,this.y+50));
        vertices.add(new Point(this.x+25,this.y+37));

        return vertices;
    }

    public void drawObject(Canvas canvas, int x, int y){
        animation.update();
        canvas.drawBitmap(this.animation.getImage(), x, y, null);
    }
}
