package com.miso.thegame.gameMechanics.multiplayer.otherPlayer;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.movingObjects.player.Player;

import java.util.ArrayList;

/**
 * Created by michal.hornak on 05.05.2016.
 */
public class OtherPlayer extends Player {

    public boolean isDestroyed = false;
    private boolean isReadyForNextFrame = false;

    public OtherPlayer(Resources resources){
        super(resources, new Point(0,0), null);
    }

    public void setHeading(int heading){
        this.heading = heading;
    }

    public void update(){

    }

    public boolean isReadyForNextFrame() {
        return isReadyForNextFrame;
    }

    public void setIsReadyForNextFrame(boolean isReadyForNextFrame) {
        this.isReadyForNextFrame = isReadyForNextFrame;
    }

    public void drawObject(Canvas canvas, int x, int y) {
        //Draw object
        canvas.drawBitmap(this.getImage(), x, y, null);
    }

    protected ArrayList<Point> getNonRotatedVertices() {
        ArrayList<Point> vertices = new ArrayList();
//        vertices.add(new Point(this.x, this.y - 50));
//        vertices.add(new Point(this.x - 25, this.y + 37));
//        vertices.add(new Point(this.x, this.y + 50));
//        vertices.add(new Point(this.x + 25, this.y + 37));

        return vertices;
    }

}
