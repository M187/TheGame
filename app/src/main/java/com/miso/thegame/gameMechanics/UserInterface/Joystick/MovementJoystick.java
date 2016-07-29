package com.miso.thegame.gameMechanics.UserInterface.Joystick;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.gameViews.GameView2;

/**
 * Created by michal.hornak on 23.11.2015.
 */
public class MovementJoystick extends Joystick {

    public MovementJoystick(Resources resources) {
        this.middleX = GameView2.WIDTH - 170;
        this.middleY = GameView2.HEIGHT - 170;
        this.eventX = this.getMiddleX();
        this.eventY = this.getMiddleY();
        this.layoutImage = BitmapFactory.decodeResource(resources, R.drawable.gamepadopaque);
        this.imageJoystickHead = BitmapFactory.decodeResource(resources, R.drawable.joystickhead);
        this.inputThreshold = layoutImage.getHeight() / 2;
        this.radius = inputThreshold + 300;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
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

    protected void assignEventPosition(MotionEvent event) {
        int inputEventX = (int) event.getX();
        int inputEventY = (int) event.getY();
        if ((int) Math.sqrt(Math.pow((this.getMiddleX() - inputEventX), 2) + Math.pow((this.getMiddleY() - inputEventY), 2)) <= this.inputThreshold) {
            eventX = inputEventX;
            eventY = inputEventY;
        } else {
            if ( checkForZeroDelta(inputEventX, inputEventY) ){return;}
            assignThresholdEventPosition(inputEventX, inputEventY);
        }
    }

    protected void assignEventPosition(MotionEvent event, int iterator) {
        int inputEventX = (int) MotionEventCompat.getX(event, iterator);
        int inputEventY = (int) MotionEventCompat.getY(event, iterator);
        if ((int) Math.sqrt(Math.pow((this.getMiddleX() - inputEventX), 2) + Math.pow((this.getMiddleY() - inputEventY), 2)) <= this.inputThreshold) {
            eventX = inputEventX;
            eventY = inputEventY;
        } else {
            if ( checkForZeroDelta(inputEventX, inputEventY) ){return;}
            assignThresholdEventPosition(inputEventX, inputEventY);
        }
    }

    private void assignThresholdEventPosition(int x, int y){
        double alpha = Math.atan( (double) Math.abs(x - middleX) / (double) Math.abs(y - middleY));
        if (x - middleX > 0) {
            eventX = middleX + (int) (Math.sin(alpha) * inputThreshold);
        } else {
            eventX = middleX - (int) (Math.sin(alpha) * inputThreshold);
        }
        if (y - middleY > 0) {
            eventY = middleY + (int) (Math.cos(alpha) * inputThreshold);
        } else {
            eventY = middleY - (int) (Math.cos(alpha) * inputThreshold);
        }
    }

    private boolean checkForZeroDelta(int x, int y){
        if (x - middleX == 0){eventY = middleY + inputThreshold; eventX = middleX; return true;}
        if (y - middleY == 0){eventY = middleY; eventX = middleX + inputThreshold; return true;}
        return false;
    }
}
