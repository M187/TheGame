package com.miso.thegame.gameMechanics.objects.movingObjects.enemies.groundEnemies;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.objects.movingObjects.enemies.EnemiesManager;
import com.miso.thegame.gameMechanics.objects.movingObjects.player.Player;

import java.util.ArrayList;

/**
 * Created by Miso on 30.1.2016.
 *
 * Deprecated. Use other one.
 */
public class Enemy_nest extends EnemyGround {

    private int shootingCd = 0;
    private Resources res;

    public Enemy_nest(Resources res, Point starttingPosition) {
        super(starttingPosition);
        this.res = res;
        this.hitPoints = 80;
        setImage(BitmapFactory.decodeResource(res, R.drawable.asteroid));
    }

    public void update(Player player, EnemiesManager enemiesManager) {
        this.distanceFromPlayer = (Math.sqrt(Math.pow(player.getX() - this.getX(), 2) + Math.pow(player.getY() - this.getY(), 2)));
        this.shootingCd -= 1;
        if (this.distanceFromPlayer < 1500) {
            this.performAction(player, enemiesManager);
        }
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

}
