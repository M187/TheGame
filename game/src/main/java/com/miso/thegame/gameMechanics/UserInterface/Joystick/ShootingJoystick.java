package com.miso.thegame.gameMechanics.UserInterface.Joystick;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.UserInterface.Buttons.ButtonPlaceholder;
import com.miso.thegame.gameMechanics.objects.movingObjects.player.Player;

/**
 * Created by michal.hornak on 23.11.2015.
 */
public class ShootingJoystick extends Joystick {

    public Player player;
    public boolean primaryShootingActive = false;
    public Point primaryShootingVector;

    public ShootingJoystick(Resources resources, ButtonPlaceholder buttonPlaceholder) {
        this.buttonPlaceholder = buttonPlaceholder;
        buttonPlaceholder.setButtonOverridenByJoystick(true);
        this.middleX = buttonPlaceholder.getXDrawCoord() + (buttonPlaceholder.getImage().getWidth() / 2);
        this.middleY = buttonPlaceholder.getYDrawCoord() + (buttonPlaceholder.getImage().getHeight() / 2);
        this.eventX = this.getMiddleX();
        this.eventY = this.getMiddleY();
        this.layoutImage = BitmapFactory.decodeResource(resources, R.drawable.gamepadopaque);
        this.imageJoystickHead = BitmapFactory.decodeResource(resources, R.drawable.joystickhead);
        this.inputThreshold = layoutImage.getHeight() / 2;
        this.radius = inputThreshold + 50;

        paint.setColor(Color.RED);
        paint.setTextSize(2);
        paint.setMaskFilter(new BlurMaskFilter(2, BlurMaskFilter.Blur.NORMAL));
    }

    @Override
    public void draw(Canvas canvas, final Paint paint) {
        if (_dragging) {
            drawTargetingLine(canvas);
            super.draw(canvas, paint);
        }
    }

    public void drawTargetingLine(Canvas canvas) {
        canvas.drawLine((float) player.getMiddleXDisplayCoord(), (float) player.getMiddleYDisplayCoord(), (float) player.getMiddleXDisplayCoord() - ((middleX - eventX) * 4), (float) player.getMiddleYDisplayCoord() - ((middleY - eventY) * 4), paint);
    }

    protected void uninitJoystick(){
        this.primaryShootingActive = false;
        this._dragging = false;
    }

    public void resetJoystickPosition() {
        this.primaryShootingActive = false;
        eventX = getMiddleX();
        eventY = getMiddleY();
        _dragging = false;
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
        int deltaX = this.middleX - this.eventX;
        int deltaY = this.middleY - this.eventY;

        this.primaryShootingActive = true;
        this.primaryShootingVector = new Point(deltaX, deltaY);
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
        int deltaX = this.middleX - this.eventX;
        int deltaY = this.middleY - this.eventY;

        this.primaryShootingActive = true;
        this.primaryShootingVector = new Point(deltaX, deltaY);
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
