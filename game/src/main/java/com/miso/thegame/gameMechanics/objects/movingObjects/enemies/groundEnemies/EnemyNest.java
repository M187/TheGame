package com.miso.thegame.gameMechanics.objects.movingObjects.enemies.groundEnemies;

import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.objects.movingObjects.actions.shooting.Shooter;
import com.miso.thegame.gameMechanics.objects.movingObjects.actions.shooting.Shooting;
import com.miso.thegame.gameMechanics.objects.movingObjects.actions.spawning.Spawner;
import com.miso.thegame.gameMechanics.objects.movingObjects.actions.spawning.Spawning;
import com.miso.thegame.gameMechanics.objects.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.objects.movingObjects.player.Player;

import java.util.ArrayList;

/**
 * Created by Miso on 30.1.2016.
 */
public class EnemyNest extends EnemyGround implements Shooting, Spawning {

    private Resources res;
    private Shooter shooter;
    private Spawner spawner;
    protected int maxRadius;

    private MyApperance myApperance = new MyApperance();

    public EnemyNest(Resources res, Point starttingPosition) {
        super(starttingPosition);
        this.res = res;
        this.hitPoints = 80;
        this.shooter = new Shooter(60, this);
        this.spawner = new Spawner(150, this);

        this.maxRadius = (int)GameView2.scaleSize(26);
        this.halfWidth = this.maxRadius / 2;
        this.halfHeight = this.maxRadius / 2;
    }

    @Override
    public int getHalfWidth(){
        return this.halfWidth;
    }

    @Override
    public int getHalfHeight(){
        return this.halfHeight;
    }

    public void update(Player player, EnemiesManager enemiesManager) {
        this.distanceFromPlayer = (Math.sqrt(Math.pow(player.getX() - this.getX(), 2) + Math.pow(player.getY() - this.getY(), 2)));
        getSpawner().spawn(enemiesManager, this.res);
        getShooter().takeShot(enemiesManager.spellManager.spellCreator);
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
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() -25, getY() -25)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() + 25, getY() - 25)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() + 25, getY() + 25)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(getX() - 25, getY() + 25)));
        return this.objectVertices;
    }

    @Override
    public void drawObject(Canvas canvas, int x, int y){
        myApperance.draw(canvas, new Point(x,y));
    }

    @Override
    public Shooter getShooter() {
        return this.shooter;
    }

    @Override
    public int getShootingDistanceThreshold() {
        return 500;
    }

    @Override
    public Spawner getSpawner() {
        return this.spawner;
    }

    @Override
    public int getSpawningDistanceThreshold() {
        return 1500;
    }

    private class MyApperance{

        int radius = (int)GameView2.scaleSize(26);
        private Paint paint = new Paint();
        private boolean growing = true;

        MyApperance(){
            this.paint.setColor(Color.GREEN);
            this.paint.setAntiAlias(true);
            this.paint.setMaskFilter(new BlurMaskFilter(3, BlurMaskFilter.Blur.NORMAL));
        }

        void draw(Canvas canvas, Point position){

            radius = (growing == true) ? radius + 1 : radius - 1;
            if (radius > maxRadius || radius < maxRadius/2) {
                growing = (growing) ? false : true;
            }

            canvas.drawCircle(position.x + radius/2, position.y + radius/2,  radius, paint);
        }
    }
}
