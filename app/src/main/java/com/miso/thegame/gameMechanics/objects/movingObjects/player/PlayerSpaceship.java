package com.miso.thegame.gameMechanics.objects.movingObjects.player;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.objects.collisionHandlers.CollisionObjectType;

import java.util.ArrayList;

/**
 * Created by michal.hornak on 10.06.2016.
 */
public class PlayerSpaceship extends Player {

    protected PlayerEngineMotors playerEngineMotors;

    public PlayerSpaceship(Resources res, Point startingPosition, MapManager mapManager) {
        setX(startingPosition.x);
        setY(startingPosition.y);
        this.collisionObjectType = CollisionObjectType.Player;
        setSpeed(ConstantHolder.maximumPlayerSpeed);
        score = 0;
        dx = (getX());
        dy = (getY());

        this.mapManager = mapManager;
        setImage(BitmapFactory.decodeResource(res, R.drawable.player));
        this.playerEngineMotors = new PlayerEngineMotors(res);
        this.setCurrentGridCoordinates(super.getGridCoordinates());
    }

    public void drawObject(Canvas canvas, int x, int y) {

        //Draw engine fire
        if (this.isMoving) {
            this.playerEngineMotors.drawObject(this, canvas, x, y);
        }
        //Draw object
        canvas.drawBitmap(this.getImage(), x, y, null);
    }

    protected ArrayList<Point> getNonRotatedVertices() {
        ArrayList<Point> vertices = new ArrayList();
        vertices.add(new Point(this.x, this.y - (int)GameView2.scaleSize(25)));
        vertices.add(new Point(this.x - (int)GameView2.scaleSize(12), this.y + (int)GameView2.scaleSize(19)));
        vertices.add(new Point(this.x, this.y + (int)GameView2.scaleSize(25)));
        vertices.add(new Point(this.x + (int)GameView2.scaleSize(12), this.y + (int)GameView2.scaleSize(19)));

        return vertices;
    }

    /**
     * Created by Miso on 25.1.2016.
     */
    public static class PlayerEngineMotors {

        public Bitmap image;

        public PlayerEngineMotors(Resources res) {
            this.image = BitmapFactory.decodeResource(res, R.drawable.playerenginefire);
        }

        public void drawObject(Player player, Canvas canvas, int x, int y) {
            if (player.turningClockwise) {
                canvas.drawBitmap(this.image, x + GameView2.scaleSize(4), y + GameView2.scaleSize(42), null);
                player.turningClockwise = false;
            } else if (player.isTurningCounterclockwise) {
                canvas.drawBitmap(this.image, x + GameView2.scaleSize(16), y + GameView2.scaleSize(42), null);
                player.isTurningCounterclockwise = false;
            } else {
                canvas.drawBitmap(this.image, x + GameView2.scaleSize(4), y + GameView2.scaleSize(42), null);
                canvas.drawBitmap(this.image, x + GameView2.scaleSize(16), y + GameView2.scaleSize(42), null);
            }
        }
    }

}
