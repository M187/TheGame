package com.miso.thegame.gameMechanics.nonMovingObjects.Obstacles;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.map.MapManager;

/**
 * Created by Miso on 16.11.2015.
 */
public class DefaultObstacle extends Obstacle {

    public DefaultObstacle(Resources res){
        super(new Point(0,0));
        this.x = randomGenerator.nextInt(MapManager.getWorldWidth());
        this.y = randomGenerator.nextInt(MapManager.getWorldHeight());
        this.setImage(BitmapFactory.decodeResource(res, R.drawable.spell2));
    }

    public DefaultObstacle(Resources res, int scaleFactor){
        super(new Point(0,0));
        this.x = randomGenerator.nextInt(MapManager.getWorldWidth());
        this.y = randomGenerator.nextInt(MapManager.getWorldHeight());
        Bitmap tempBitmap = BitmapFactory.decodeResource(res, R.drawable.spell2);
        this.setImage(Bitmap.createScaledBitmap(tempBitmap, tempBitmap.getWidth() * scaleFactor, tempBitmap.getHeight() * scaleFactor, true));
    }
}
