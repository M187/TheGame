package com.miso.thegame.gameMechanics.UserInterface.Joystick;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import com.miso.thegame.gameMechanics.UserInterface.Buttons.ButtonPlaceholder;

/**
 * Created by Miso on 22.11.2015.
 *
 * Joystick requires buttonPlaceholder to work.
 * It takes its coordinates and use it to initialize itsef.
 * Also many specific events are handled via buttonPlaceholder, not joystick directly.
 */
public abstract class Joystick {

    protected ButtonPlaceholder buttonPlaceholder;
    protected int middleX;
    protected int middleY;
    protected static final Paint paint = new Paint();
    protected Bitmap layoutImage;
    protected Bitmap imageJoystickHead;
    protected int radius;
    protected boolean _dragging;
    protected int eventX;
    protected int eventY;
    protected int distance;
    protected int inputThreshold = radius;
    protected int pointerId = -1;

    /**
     * Is current event inside joystick circle?
     *
     * @param x event coord
     * @param y event coord
     * @return is current event inside joystick circle?
     */
    public boolean eventInJoystickRegion(int x, int y) {
        try {
            this.distance = (int) Math.sqrt(Math.pow((this.getMiddleX() - x), 2) + Math.pow((this.getMiddleY() - y), 2));
        } catch (Exception e){
            e.printStackTrace();
        }
        return distance <= getRadius();
    }

    public boolean onTouch(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                _dragging = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                this.uninitJoystick();
                break;
        }

        if (is_dragging()) {
            // get the pos
            assignEventPosition(event);
            return true;
        } else {
            // Snap back to center when the joystick is released
            eventX = getMiddleX();
            eventY = getMiddleY();
            return false;
        }
    }

    public boolean onTouch(MotionEvent event, int pointerId, int action, int iterator) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                _dragging = true;
                this.pointerId = pointerId;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                uninitJoystick();
                break;
        }

        if (is_dragging()) {
            // get the pos
            assignEventPosition(event, iterator);
            return true;
        } else {
            // Snap back to center when the joystick is released
            eventX = getMiddleX();
            eventY = getMiddleY();
            return false;
        }
    }

    protected void uninitJoystick(){
        this._dragging = false;
    }

    public void resetJoystickPosition() {
        eventX = getMiddleX();
        eventY = getMiddleY();
        _dragging = false;
    }

    protected void assignEventPosition(MotionEvent event){
        eventX = (int) event.getX();
        eventY = (int) event.getY();
    }

    protected void assignEventPosition(MotionEvent event, int iterator){
        eventX = (int) MotionEventCompat.getX(event, iterator);
        eventY = (int) MotionEventCompat.getY(event, iterator);
    }

    //<editor-fold desc="Getters">
    public int getMiddleX() {
        return middleX;
    }

    public int getMiddleY() {
        return middleY;
    }

    public int getRadius() {
        return radius;
    }

    public int getInputThreshold(){
        return inputThreshold;
    }

    public boolean is_dragging() {
        return _dragging;
    }

    public int getEventX() {
        return eventX;
    }

    public int getEventY() {
        return eventY;
    }

    public int getPointerId() {
        return pointerId;
    }

    public void setPointerId(int pointerId) {
        this.pointerId = pointerId;
    }
    //</editor-fold>

    public void draw(Canvas canvas) {
        canvas.drawBitmap(layoutImage, getMiddleX() - (layoutImage.getWidth() / 2), getMiddleY() - (layoutImage.getHeight() / 2), null);
        canvas.drawBitmap(imageJoystickHead, getEventX() - (imageJoystickHead.getWidth() / 2), getEventY() - (imageJoystickHead.getHeight() / 2), null);
    }

}
