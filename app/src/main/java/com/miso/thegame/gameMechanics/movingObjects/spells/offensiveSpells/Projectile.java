package com.miso.thegame.gameMechanics.movingObjects.spells.offensiveSpells;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.spells.OffensiveSpell;

import java.util.ArrayList;

/**
 * Created by Miso on 12.10.2015.
 */
public class Projectile extends OffensiveSpell {

    private Point hitVertex;
    private Resources res;

    /**
     * Creates fireball object.
     *
     * @param x                   starting coord of an fireball, usually players one.
     * @param y                   starting coord of an fireball, usually players one.
     * @param Dx                  movement vector on relevant axis.
     * @param Dy                  movement vector on relevant axis.
     * @param collisionObjectType collision type of an projectile for collision checks.
     * @param res                 resource to obtain picture from.
     */
    public Projectile(int x, int y, int Dx, int Dy, CollisionObjectType collisionObjectType, Resources res) {
        super();
        //todo: add check for only possible collision types.
        this.collisionObjectType = collisionObjectType;
        removeOnCollision = true;
        this.res = res;
        setX(x);
        setY(y);
        setDx(Dx);
        setDy(Dy);
        setSpeed(30);
        setImage(BitmapFactory.decodeResource(res, R.drawable.smallfireball2));
    }

    /**
     * Is it behind bounds of ply field?
     * todo: add check for traveling distance.
     *
     * @return remove spell?
     */
    public boolean removeSpell() {
        if (this.getX() - this.getImage().getWidth() > MapManager.getWorldWidth() ||
                this.getX() + this.getImage().getWidth() < 0 ||
                this.getY() - this.getImage().getHeight() > MapManager.getWorldHeight() ||
                this.getY() + this.getImage().getHeight() < 0
                ) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void moveObject() {

        if (setMovement) {
            this.deltaX = getDx() - getX();
            this.deltaY = getDy() - getY();

            double speedFactor = this.getSpeed() / Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

            //get x,y coordinates using delta ratio
            frameDeltaX = (int) (this.deltaX * speedFactor);
            frameDeltaY = (int) (this.deltaY * speedFactor);

            calculateHeading();
            setMovement = false;
        }
        setX(getX() + frameDeltaX);
        setY(getY() + frameDeltaY);
    }


    public ArrayList<Point> getObjectCollisionVertices() {
        this.objectVertices.clear();
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(this.x, this.y - 10)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(this.x, this.y)));
        return this.objectVertices;
    }

}
