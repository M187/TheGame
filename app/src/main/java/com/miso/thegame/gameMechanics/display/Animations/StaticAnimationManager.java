package com.miso.thegame.gameMechanics.display.Animations;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.gameViews.GameView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Miso on 9.2.2016.
 */
public class StaticAnimationManager {

    public static ArrayList<StaticAnimation> staticAnimationsList = new ArrayList<>();

    public void update(){
        Iterator<StaticAnimation> animationIterator = staticAnimationsList.iterator();
        while (animationIterator.hasNext()){
            StaticAnimation animation = animationIterator.next();
            boolean remove = animation.update();
            if (remove){
                animationIterator.remove();
            }
        }
    }

    public static void addExplosion(Point position, Resources res){
        staticAnimationsList.add(new Explosion(position.x, position.y, res));
    }

    public void draw(Canvas canvas){
        for(StaticAnimation animation : staticAnimationsList){
            GameView.drawManager.drawOnDisplay(animation, canvas);
        }
    }
}
