package com.miso.thegame.gameMechanics.movingObjects.enemies.groundEnemies;

import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;

import java.util.ArrayList;

/**
 * Created by Miso on 30.1.2016.
 */
public class EnemyNest extends EnemyGround {

    private int shootingCd = 0;
    private Resources res;

    private MyApperance myApperance = new MyApperance();

    public EnemyNest(Resources res, Point starttingPosition) {
        super(starttingPosition);
        this.res = res;
        this.hitPoints = 80;
        this.playerInRange = false;
    }

    @Override
    public int getHalfWidth(){
        return 50;
    }

    @Override
    public int getHalfHeight(){
        return 50;
    }

    public void update(Player player, EnemiesManager enemiesManager) {
        this.distanceFromPlayer = (Math.sqrt(Math.pow(player.getX() - this.getX(), 2) + Math.pow(player.getY() - this.getY(), 2)));
        this.shootingCd -= 1;
        if (this.distanceFromPlayer < 1500) {
            this.playerInRange = true;
            this.performAction(player, enemiesManager);
        }
        this.playerInRange = false;
    }

    public void performAction(Player player, EnemiesManager enemiesManager) {
        if (this.shootingCd < 0) {
            enemiesManager.enemiesToBeAddedInThiFrame.add(new Enemy_basic(this.res, new Point(this.x, this.y)));
            this.shootingCd = 150;
        }
    }

    /**
     * Current behavior - remove 1hp.
     *
     * @param player         ...
     * @param enemiesManager to remove enemy from.
     */
    public void handleCollisionWithPlayer(Player player, EnemiesManager enemiesManager) {
        player.removeHealth(1);
        this.hitPoints -= 2;
    }

    @Override
    public boolean hitBySpell() {
        this.hitPoints -= 5;

        if (this.hitPoints < 0) {
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Point> getObjectCollisionVertices() {
        this.objectVertices.clear();
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() - 73, getY() - 50)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() - 25, getY() - 75)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() + 40, getY() - 25)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() + 75, getY())));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() + 60, getY() + 50)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() + 5, getY() + 72)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() - 25, getY() + 32)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() - 25, getY() + 25)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() - 72, getY())));
        return this.objectVertices;
    }

    @Override
    public void drawObject(Canvas canvas, int x, int y){

        myApperance.draw(canvas, new Point(x,y));
    }

    private class MyApperance{

        int radius = 20;
        private Paint paint = new Paint();
        private boolean growing = true;

        MyApperance(){
            this.paint.setColor(Color.GREEN);
            this.paint.setAntiAlias(true);
            this.paint.setMaskFilter(new BlurMaskFilter(3, BlurMaskFilter.Blur.NORMAL));
        }

        void draw(Canvas canvas, Point position){

            radius = (growing == true) ? radius + 1 : radius - 1;
            if (radius > 40 || radius < 20) {
                growing = (growing) ? false : true;
            }

            canvas.drawCircle(position.x + 2 * getHalfWidth(), position.y + 2 * getHalfHeight(), radius, paint);
        }
    }
}
