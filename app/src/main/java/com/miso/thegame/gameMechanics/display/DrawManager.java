package com.miso.thegame.gameMechanics.display;

import android.graphics.Canvas;

import com.miso.thegame.gameMechanics.Anchor;
import com.miso.thegame.GameObject;
import com.miso.thegame.GamePanel;
import com.miso.thegame.gameMechanics.display.StaticAnimations.StaticAnimation;
import com.miso.thegame.gameMechanics.movingObjects.MovableObject;
import com.miso.thegame.gameMechanics.nonMovingObjects.StaticObject;

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
            gameObject.setDisplayXCoord(gameObject.getX() - anchor.getX());
            gameObject.setDisplayYCoord(gameObject.getY() - anchor.getY());
            this.draw(gameObject, canvas);
        }
    }

    /**
     * Function to draw static object.
     * There is one BIG DIFFERENCE netween moving and static object
     *      Static object coords are its top-left, while it is middle coords for moving object
     * @param staticObject
     * @param canvas
     */
    public void drawOnDisplay(StaticObject staticObject, Canvas canvas){
        if (isVisible(staticObject)){
            staticObject.setDisplayXCoord(staticObject.getX() - anchor.getX());
            staticObject.setDisplayYCoord(staticObject.getY() - anchor.getY());
            staticObject.draw(canvas);
        }
    }

    public void drawOnDisplay(StaticAnimation staticAnimation, Canvas canvas){
        if (isVisible(staticAnimation)) {
            staticAnimation.setDisplayXCoord(staticAnimation.getX() - anchor.getX());
            staticAnimation.setDisplayYCoord(staticAnimation.getY() - anchor.getY());
            canvas.drawBitmap(staticAnimation.getImage(), staticAnimation.getDisplayXCoord(), staticAnimation.getDisplayYCoord(), null);
        }
    }
    
    /**
     * Calculates if object is in visible area regarding to anchor/object position.
     * @param gameObject object for which we want to find out if it is visiblle
     * @return visible or not visible.
     */
    public boolean isVisible(GameObject gameObject){
        try {
            if (gameObject.getX() + (gameObject.getImage().getWidth() / 2) - this.anchor.getX() < 0 ||
                    gameObject.getX() - (gameObject.getImage().getWidth() / 2) - this.anchor.getX() - GamePanel.WIDTH > 0 ||
                    gameObject.getY() + (gameObject.getImage().getHeight() / 2) - this.anchor.getY() < 0 ||
                    gameObject.getY() - (gameObject.getImage().getHeight() / 2) - this.anchor.getY() - GamePanel.HEIGHT > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NullPointerException e){
            System.out.print("Can't make decision if object visible.");
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
                staticObject.getX() - this.anchor.getX() - GamePanel.WIDTH > 0 ||
                staticObject.getY() + (staticObject.getImage().getHeight()) - this.anchor.getY() < 0 ||
                staticObject.getY() - this.anchor.getY() - GamePanel.HEIGHT > 0){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Draw an object on a display. Also rotate it due to heading.
     * There is one BIG DIFFERENCE netween moving and static object
     *      Static object coords are its top-left, while it is middle coords for moving object
     * @param gO player/enemy/spell
     * @param canvas - self explanatory.
     */
    public void draw(MovableObject gO, Canvas canvas){
        float degreesToRotate = (float)gO.getHeading();
        canvas.rotate(degreesToRotate, gO.getMiddleDisplayXCoord(), gO.getMiddleDisplayYCoord());
        gO.drawObject(canvas);
        canvas.rotate( - degreesToRotate, gO.getMiddleDisplayXCoord(), gO.getMiddleDisplayYCoord());

//  Uncomment to draw hit vertices
//        Paint p = new Paint();
//        p.setColor(Color.RED);
//        p.setStrokeWidth(5);
//
//        if (gO instanceof Enemy_carrier){
//            ArrayList<Point> toDraw = gO.getObjectCollisionVertices();
//
//            for (int i = 0;i < toDraw.size() - 1;i++){
//                canvas.drawLine(toDraw.get(i).x - anchor.getX(),toDraw.get(i).y - anchor.getY(),toDraw.get(i+1).x - anchor.getX(),toDraw.get(i+1).y - anchor.getY(), p);
//            }
//            canvas.drawLine(toDraw.get(0).x - anchor.getX(),toDraw.get(0).y - anchor.getY(),toDraw.get(toDraw.size() - 1).x - anchor.getX(),toDraw.get(toDraw.size() - 1).y - anchor.getY(), p);
//        }
//
//        if (gO instanceof Fireball){
//            ArrayList<Point> toDraw = gO.getObjectCollisionVertices();
//
//            for (int i = 0;i < toDraw.size() - 1;i++){
//                canvas.drawLine(toDraw.get(i).x - anchor.getX(),toDraw.get(i).y - anchor.getY(),toDraw.get(i+1).x - anchor.getX(),toDraw.get(i+1).y - anchor.getY(), p);
//            }
//            canvas.drawLine(toDraw.get(0).x - anchor.getX(),toDraw.get(0).y - anchor.getY(),toDraw.get(toDraw.size() - 1).x - anchor.getX(),toDraw.get(toDraw.size() - 1).y - anchor.getY(), p);
//        }
    }
}
