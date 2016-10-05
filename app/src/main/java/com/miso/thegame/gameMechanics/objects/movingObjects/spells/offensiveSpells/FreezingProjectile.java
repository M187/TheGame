package com.miso.thegame.gameMechanics.objects.movingObjects.spells.offensiveSpells;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.display.Animations.StaticAnimationManager;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.objects.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.objects.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.objects.movingObjects.spells.OffensiveSpell;
import com.miso.thegame.gameMechanics.timeouts.EffectTimeout;

import java.util.ArrayList;

/**
 * Created by Miso on 12.10.2015.
 */
public class FreezingProjectile extends OffensiveSpell {

    /**
     * Creates fireball object.
     *
     * @param fromPosition                  starting coord of an fireball, usually players one.
     * @param destinationX                  movement vector on relevant axis.
     * @param destinationY                  movement vector on relevant axis.
     * @param collisionObjectType collision type of an projectile for collision checks.
     * @param res                 resource to obtain picture from.
     */
    public FreezingProjectile(Point fromPosition, int destinationX, int destinationY, CollisionObjectType collisionObjectType, Resources res) {
        super();
        this.collisionObjectType = collisionObjectType;
        removeOnCollision = true;
        this.setPosition(fromPosition);
        setDx(destinationX);
        setDy(destinationY);
        setSpeed(30);
        setImage(BitmapFactory.decodeResource(res, R.drawable.smallfireball));
    }

    public FreezingProjectile(Point fromPosition, int destinationX, int destinationY, CollisionObjectType collisionObjectType, String identificator, Resources res) {
        this(fromPosition, destinationX, destinationY, collisionObjectType, res);
        this.identificator = identificator;
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
    public void update() {

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
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(this.x, this.y - 15)));
        this.objectVertices.add(rotateVertexAroundCurrentPosition(new Point(this.x, this.y)));
        return this.objectVertices;
    }

    public boolean playerHit(Player player){
        player.removeHealth(1);
        player.setMovementDisabled(new EffectTimeout(3000));
        return true;
    }

    public void explode(){
        StaticAnimationManager.addExplosion(this.getPosition(), 1);
    }
}