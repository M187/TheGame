package com.miso.thegame.gameMechanics.display;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.GameObject;
import com.miso.thegame.gameMechanics.display.Animations.StaticAnimation;
import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.movingObjects.Anchor;
import com.miso.thegame.gameMechanics.movingObjects.MovableObject;
import com.miso.thegame.gameMechanics.movingObjects.enemies.groundEnemies.EnemyNest;
import com.miso.thegame.gameMechanics.movingObjects.spells.offensiveSpells.Projectile;
import com.miso.thegame.gameMechanics.nonMovingObjects.StaticObject;

import java.util.ArrayList;

/**
 * Created by michal.hornak on 03.11.2015.
 */
public class DrawManager {

    private Anchor anchor;

    public DrawManager(Anchor anchor){
        this.anchor = anchor;
    }

    /**
     * Function to convert global coord to coordinates that should be used.
     * Currently using top left corner as a [0,0]. Incrementing moving down/right.
     *
     */
    public void drawOnDisplay(MovableObject gameObject, Canvas canvas){
        if (isVisible(gameObject)){
            this.drawWithRotation(gameObject, canvas);
        }
    }

    /**
     * Function to drawWithRotation static object.
     * There is one BIG DIFFERENCE netween moving and static object
     *      Static object coords are its top-left, while it is middle coords for moving object
     * @param staticObject
     * @param canvas
     */
    public void drawOnDisplay(StaticObject staticObject, Canvas canvas){
        if (isVisible(staticObject)){
            staticObject.draw(canvas, staticObject.getX() - anchor.getX(), staticObject.getY() - anchor.getY());
        }
    }

    public void drawOnDisplay(StaticAnimation staticAnimation, Canvas canvas){
        if (isVisible(staticAnimation)) {
            //canvas.drawBitmap(staticAnimation.getImage(), staticAnimation.getX() - anchor.getX(), staticAnimation.getY() - anchor.getY(), null);

            staticAnimation.draw(canvas, new Point(staticAnimation.getX() - anchor.getX(), staticAnimation.getY() - anchor.getY()));
        }
    }
    
    /**
     * Calculates if object is in visible area regarding to anchor/object position.
     * @param gameObject object for which we want to find out if it is visiblle
     * @return visible or not visible.
     */
    public boolean isVisible(GameObject gameObject){
        try {
            if (gameObject.getX() + gameObject.getHalfWidth() - this.anchor.getX() < 0 ||
                    gameObject.getX() - gameObject.getHalfWidth() - this.anchor.getX() - GameView2.WIDTH > 0 ||
                    gameObject.getY() + gameObject.getHalfHeight() - this.anchor.getY() < 0 ||
                    gameObject.getY() - gameObject.getHalfHeight() - this.anchor.getY() - GameView2.HEIGHT > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NullPointerException e){
            System.out.print("Can't make decision if an object is visible.");
            return false;
        }
    }

    /**
     * Calculates if object is in visible area regarding to anchor/object position.
     * Static object's difference is that its coordinates are not in the middle, but top left corner.
     * @param staticObject object for which we want to find out if it is visiblle
     * @return visible or not visible.
     */
    public boolean isVisible(StaticObject staticObject){
        if (staticObject.getX() + (staticObject.getImage().getWidth()) - this.anchor.getX() < 0 ||
                staticObject.getX() - this.anchor.getX() - GameView2.WIDTH > 0 ||
                staticObject.getY() + (staticObject.getImage().getHeight()) - this.anchor.getY() < 0 ||
                staticObject.getY() - this.anchor.getY() - GameView2.HEIGHT > 0){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Draw an object on a display. Also rotate it due to heading.
     * There is one BIG DIFFERENCE netween moving and static object
     *      Static object coords are its top-left, while it is middle coords for moving object
     * @param gameObject player/enemy/spell
     * @param canvas - self explanatory.
     */
    public void drawWithRotation(MovableObject gameObject, Canvas canvas){
        float degreesToRotate = (float)gameObject.getHeading();
        canvas.rotate(degreesToRotate, gameObject.getX() - anchor.getX(), gameObject.getY() - anchor.getY());

        gameObject.drawObject(canvas, gameObject.getX() - anchor.getX() - gameObject.getHalfWidth(), gameObject.getY() - anchor.getY() - gameObject.getHalfHeight());

        canvas.rotate( - degreesToRotate, gameObject.getX() - anchor.getX(), gameObject.getY() - anchor.getY());

//  Uncomment to drawWithRotation hit vertices
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setStrokeWidth(5);

        if (gameObject instanceof EnemyNest){
            ArrayList<Point> toDraw = gameObject.getObjectCollisionVertices();

            for (int i = 0;i < toDraw.size() - 1;i++){
                canvas.drawLine(toDraw.get(i).x - anchor.getX(),toDraw.get(i).y - anchor.getY(),toDraw.get(i+1).x - anchor.getX(),toDraw.get(i+1).y - anchor.getY(), p);
            }
            canvas.drawLine(toDraw.get(0).x - anchor.getX(),toDraw.get(0).y - anchor.getY(),toDraw.get(toDraw.size() - 1).x - anchor.getX(),toDraw.get(toDraw.size() - 1).y - anchor.getY(), p);
        }

        if (gameObject instanceof Projectile){
            ArrayList<Point> toDraw = gameObject.getObjectCollisionVertices();

            for (int i = 0;i < toDraw.size() - 1;i++){
                canvas.drawLine(toDraw.get(i).x - anchor.getX(),toDraw.get(i).y - anchor.getY(),toDraw.get(i+1).x - anchor.getX(),toDraw.get(i+1).y - anchor.getY(), p);
            }
            canvas.drawLine(toDraw.get(0).x - anchor.getX(),toDraw.get(0).y - anchor.getY(),toDraw.get(toDraw.size() - 1).x - anchor.getX(),toDraw.get(toDraw.size() - 1).y - anchor.getY(), p);
        }
    }
}
