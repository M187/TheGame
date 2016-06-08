package com.miso.thegame.gameMechanics.display.Animations;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.gameViews.GameView2;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Miso on 9.2.2016.
 */
public class StaticAnimationManager {

    public static Resources resources;

    public static ArrayList<StaticAnimation> staticAnimationsList = new ArrayList<>();

    public static void addExplosion(Point position, int explosionType){
        //TODO: add here switch to define what animation should be created
        switch (explosionType) {
            case 1:
                staticAnimationsList.add(new Explosion(position.x, position.y, resources));
                break;
            case 2:
                staticAnimationsList.add(new Explosion2(position.x, position.y, resources));
                break;
        }
    }

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

    public void draw(Canvas canvas){
        for(StaticAnimation animation : staticAnimationsList){
            GameView2.drawManager.drawOnDisplay(animation, canvas);
        }
    }
}
