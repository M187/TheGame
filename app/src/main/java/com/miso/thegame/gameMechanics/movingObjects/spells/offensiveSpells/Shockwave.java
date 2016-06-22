package com.miso.thegame.gameMechanics.movingObjects.spells.offensiveSpells;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.collisionHandlers.SATCollisionCalculator;
import com.miso.thegame.gameMechanics.movingObjects.enemies.Enemy;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameMechanics.movingObjects.spells.OffensiveSpell;

/**
 * Created by Miso on 17.11.2015.
 */
public class Shockwave extends OffensiveSpell {

    private double frameSinceCreation = 1;
    private Bitmap unscaledBitmap;
    private Player player;
    private int reachFactor;

    public Shockwave(Player player, int reachFactor, Resources res){
        super();
        this.collisionObjectType = CollisionObjectType.SpellPlayer;
        this.reachFactor = reachFactor;
        this.player = player;
        removeOnCollision = false;
        setX(player.getX());
        setY(player.getY());
        this.unscaledBitmap = BitmapFactory.decodeResource(res, R.drawable.spellshockwave);
        this.setImage(Bitmap.createScaledBitmap(unscaledBitmap, (int) ( (unscaledBitmap.getWidth()) * ((frameSinceCreation) / 20)), (int) (unscaledBitmap.getHeight() * ((frameSinceCreation) / 20)), true));
    }

    public boolean removeSpell(){
        if (frameSinceCreation > 20){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update(){
        this.setImage(Bitmap.createScaledBitmap(unscaledBitmap, (int) (unscaledBitmap.getWidth() * ((frameSinceCreation)/20) * reachFactor), (int) (unscaledBitmap.getHeight() * ((frameSinceCreation)/20) * reachFactor), true));
        this.frameSinceCreation += 1;
        setX(player.getX());
        setY(player.getY());
    }

    public boolean collideWithMovingObject(Enemy movingObject, SATCollisionCalculator satCollisionCalculator){
        if (movingObject.getDistanceFromPlayer() <= (((frameSinceCreation / 20) * 70 + (movingObject.getImage().getWidth()/2)) * reachFactor)){
            return true;
        } else {
            return false;
        }
    }

    public boolean playerHit(Player player){
        return false;
    }

    public void explode(){}
}
