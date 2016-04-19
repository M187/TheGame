package com.miso.thegame.gameMechanics.nonMovingObjects.Collectables;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.display.Animations.CollectibleRotatingStar;
import com.miso.thegame.gameMechanics.display.Animations.StaticAnimation;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;

/**
 * Created by Miso on 15.11.2015.
 */
public class StarCollectible extends Collectible {

    private StaticAnimation animation = null;

    public StarCollectible(Resources res, Point position){
        super(position, new CollectibleRotatingStar(position, res).getImage());
        this.animation = new CollectibleRotatingStar(position, res);
    }

    public void onInteraction(Player player, MapManager mapManager){
        player.objectivesCollected += 1;
        mapManager.getCollectibleList().remove(this);
    }

    //Todo - add animations to parent method?
    public void draw(Canvas canvas, int x, int y){
        animation.update();
        canvas.drawBitmap(this.animation.getImage(), x, y, null);
    }

}
