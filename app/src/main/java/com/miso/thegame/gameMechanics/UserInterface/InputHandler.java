package com.miso.thegame.gameMechanics.UserInterface;

import android.app.Activity;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import com.miso.thegame.gameMechanics.UserInterface.Buttons.ButtonPlaceholder;
import com.miso.thegame.gameMechanics.UserInterface.Joystick.MovementJoystick;
import com.miso.thegame.gameMechanics.UserInterface.Joystick.ShootingJoystick;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;
import com.miso.thegame.gameViews.GameView2;

/**
 * Created by Miso on 3.11.2015.
 */
public class InputHandler {

    GameView2 gP;
    MovementJoystick movementJoystick;
    ShootingJoystick shootingJoystick;
    Player player;
    int playerSpeed;

    public InputHandler(GameView2 gamePanel) {
        this.gP = gamePanel;
        this.player = gP.player;
        this.playerSpeed = gP.player.getSpeed();
        this.movementJoystick = gamePanel.toolbar.getMovementJoystick();
        this.shootingJoystick = gamePanel.toolbar.getShootingJoystick();
        this.shootingJoystick.player = gP.player;
    }

    public boolean processEvent(MotionEvent event) {
        int countOfEvents = event.getPointerCount();
        //System.out.println("Count of events " + countOfEvents);

        if (countOfEvents == 1) {
            return processSingleTouch(event);
        } else if (countOfEvents == 2) {
            return processMultiTouch(event);
        } else if (countOfEvents > 2) {
            //processTripleTouch();
            return false;
        }
        return false;
    }

    private boolean processSingleTouch(MotionEvent event) {
        int eventY;
        int eventX;
        eventX = (int) MotionEventCompat.getX(event, 0);
        eventY = (int) MotionEventCompat.getY(event, 0);

        //interacts with movementJoystick?
        if (movementJoystick.eventInJoystickRegion(eventX, eventY)) {
            movementJoystick.onTouch(event);
            return true;
        } else {
            movementJoystick.resetJoystickPosition();
        }
        //interacts with shootingJoystick?
        if (shootingJoystick.eventInJoystickRegion(eventX, eventY)) {
            shootingJoystick.onTouch(event);
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN){ return true;}
        } else {
            shootingJoystick.resetJoystickPosition();
        }
        //interacts with buttonPlaceholder?
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            for (ButtonPlaceholder buttonPlaceholder : gP.toolbar.getButtonPlaceholders()) {
                if (buttonPlaceholder.clickedOnButton(eventX, eventY)) {
                    buttonPlaceholder.onClickEvent(gP.spellManager);
                }
            }
        }
        return true;
    }

    private boolean processMultiTouch(MotionEvent event) {
        int iterator = 0, pointerId, eventX, eventY, currentPointerAction;
        int actionIndex = event.getActionIndex();
        boolean interactsWithPlayerJoy = false, interactsWithFireballJoy = false, interactsWithBlinkJoy = false;

        while (iterator < 2) {

            if (actionIndex == iterator){
                currentPointerAction = event.getActionMasked();
            } else {
                currentPointerAction = -1;
            }

            //System.out.println("action index is :" + actionIndex);
            try {
                pointerId = MotionEventCompat.getPointerId(event, iterator);
                eventX = (int) MotionEventCompat.getX(event, iterator);
                eventY = (int) MotionEventCompat.getY(event, iterator);

                //interacts with movementJoystick?
                if (movementJoystick.eventInJoystickRegion(eventX, eventY)) {
                    movementJoystick.onTouch(event, pointerId, currentPointerAction, iterator);
                    interactsWithPlayerJoy = true;
                }
                //interacts with shootingJoystick?
                else if (shootingJoystick.eventInJoystickRegion(eventX, eventY)) {
                    shootingJoystick.onTouch(event, pointerId, currentPointerAction, iterator);
                    interactsWithFireballJoy = true;
                }
                //interacts with buttonPlaceholder?
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN || event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
                    for (ButtonPlaceholder buttonPlaceholder : gP.toolbar.getButtonPlaceholders()) {
                        if (buttonPlaceholder.clickedOnButton(eventX, eventY)) {
                            buttonPlaceholder.onClickEvent(gP.spellManager);
                        }
                    }
                }

//                else if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP || event.getActionMasked() == MotionEvent.ACTION_UP){
//                    if (pointerId == movementJoystick.getPointerId()){
//                        movementJoystick.resetJoystickPosition();
//                    } else if (pointerId == shootingJoystick.getPointerId()){
//                        shootingJoystick.resetJoystickPosition();
//                    }
//                }


            } catch (Exception exception) {
                System.out.println("Unhandled exception during input phase.");
                exception.printStackTrace();
            }
            iterator++;
        }

        if (! interactsWithPlayerJoy) {
            movementJoystick.resetJoystickPosition();}
        if (! interactsWithFireballJoy) {
            shootingJoystick.resetJoystickPosition();}
        return true;
    }

    public void processFrameInput(){
        this.updatePlayerPosition();
        this.updatePrimaryShootingData();
    }

    /**
     * Updates player position based on a current playerJoystickHead position.
     * Rescale movement based on a player speed and distance from center.
     */
    private void updatePlayerPosition() {
        int tempDeltaX = (int) (((this.movementJoystick.getEventX() - this.movementJoystick.getMiddleX()) / (double) this.movementJoystick.getInputThreshold()) * playerSpeed);
        int tempDeltaY = (int) (((this.movementJoystick.getEventY() - this.movementJoystick.getMiddleY()) / (double) this.movementJoystick.getInputThreshold()) * playerSpeed);

        player.setDxViaJoystick(player.getX() + tempDeltaX);
        player.setDyViaJoystick(player.getY() + tempDeltaY);
        player.setFrameDeltaX(tempDeltaX);
        player.setFrameDeltaY(tempDeltaY);
    }

    private void updatePrimaryShootingData(){
        this.gP.spellManager.primaryShootingActive = this.shootingJoystick.primaryShootingActive;
        this.gP.spellManager.primaryShootingVector = this.shootingJoystick.primaryShootingVector;
    }

    /**
     * On click user should return to menu activity.
     * @param event
     */
    public boolean processEndgameEvent(MotionEvent event){
        ((Activity)gP.context).finish();
        return true;
    }
}
