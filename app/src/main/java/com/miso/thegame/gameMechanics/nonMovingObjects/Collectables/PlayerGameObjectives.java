package com.miso.thegame.gameMechanics.nonMovingObjects.Collectables;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.Animation;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.Player;

/**
 * Created by Miso on 15.11.2015.
 */
public class PlayerGameObjectives extends Collectable{

    private Animation animation = new Animation();

    public PlayerGameObjectives(Resources res, Point position){//, GameObjectivePosition gameObjectivePosition){
        this.x = position.x;
        this.y = position.y;
        //todo: make this generic
        animation.initializeSprites(BitmapFactory.decodeResource(res, R.drawable.gameobjective_spritee), 35, 40, 7, 100);
        this.setImage(animation.getImage());
        initializeGridCoords();
        initializeVertices();
    }

    public void onInteraction(Player player, MapManager mapManager){
        player.objectivesCollected += 1;
        mapManager.getCollectableList().remove(this);
    }

    //Todo - add animations to parent method?
    public void draw(Canvas canvas, int x, int y){
        animation.update();
        canvas.drawBitmap(this.animation.getImage(), x, y, null);
    }

}
