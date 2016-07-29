package com.miso.thegame.gameMechanics.UserInterface.Buttons;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.miso.thegame.GameData.ButtonTypeEnum;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.movingObjects.spells.SpellManager;

/**
 * Created by Miso on 6.11.2015.
 */
public abstract class ButtonPlaceholder {

    protected static final Paint paint = new Paint();
    public int xDrawCoord = 50;
    public int yDrawCoord = GameView2.HEIGHT;
    public int iteratorToDrawXAxis;
    public int iteratorToDrawYAxis;
    protected int cooldown;
    protected long lastUse;
    protected Bitmap image;
    protected ButtonTypeEnum buttonType = ButtonTypeEnum.Shockwave;
    private boolean buttonOverridenByJoystick = false;

    /**
     * Function to initialize button. Should be called from constructor.
      * @param res - resources.
     * @param buttonType default action used for button.
     */
    protected void initializeButtonPlaceholder(Resources res, ButtonTypeEnum buttonType){
        this.cooldown = buttonType.buttonActionCooldown;
        this.buttonType = buttonType;
        this.image = BitmapFactory.decodeResource(res, buttonType.resourceParameterString);
        setXDrawCoord();
        setYDrawCoord();
    }

    /**
     * Big switch to decide what to do with input action.
     * @param spellManager
     */
    public void onClickEvent(SpellManager spellManager){

        switch (this.buttonType){
            case Shockwave:
                this.setLastUse();
                spellManager.spellCreator.addPlayerShockwave(ConstantHolder.shockwaveReachFactor);
                return;
            case TimeStop:
                this.setLastUse();
                spellManager.spellCreator.addTimestopSpell();
                return;
            case FreezingProjectiles:
                this.setLastUse();
                spellManager.spellCreator.performFreezeSpell();
                return;
        }
    }

    /**
     * Check if player clicked on a buttonPlaceholder
     * @param xClickCoord passed from input object
     * @param yClickCoord passed from input object
     * @return clicked or not clicked on the layoutImage
     */
    public boolean clickedOnButton(int xClickCoord, int yClickCoord){
        if (buttonOverridenByJoystick){return false;}
        if (! isOnCooldown()) {
            if (this.getXDrawCoord() < xClickCoord &&
                    this.getXDrawCoord() + this.image.getWidth() > xClickCoord &&
                    this.getYDrawCoord() < yClickCoord &&
                    this.getYDrawCoord() + this.image.getWidth() > yClickCoord) {
                return true;
            }
        }
        return false;
    }

    public int getXDrawCoord(){
        return this.xDrawCoord;
    }
    public int getYDrawCoord(){
        return this.yDrawCoord;
    }
    protected void setXDrawCoord(){
        this.xDrawCoord = this.xDrawCoord + ((this.image.getWidth() +50) * iteratorToDrawXAxis);
    }
    protected void setYDrawCoord(){
        this.yDrawCoord = this.yDrawCoord - ((this.image.getHeight() + 75) * ( iteratorToDrawYAxis +1 ));
    }
    public Bitmap getImage(){
        return this.image;
    }
    public void draw(Canvas canvas, final Paint paint){
        canvas.drawBitmap(getImage(), this.getXDrawCoord(), this.getYDrawCoord(), paint);
        if (this.isOnCooldown()){
            this.drawCooldown(canvas);
        }
    }
    public void drawCooldown(Canvas canvas){
        paint.setColor(Color.MAGENTA);
        paint.setTextAlign(Paint.Align.CENTER);
        int scaling = 60;
        paint.setTextSize(scaling);
        String number = Long.toString(this.cooldownTimeLeftInSeconds() + 1);
        canvas.drawText(number, getXDrawCoord() + image.getWidth() / 2,
                getYDrawCoord() + image.getHeight() + ((paint.descent() + paint.ascent()) / 2), paint);
    }

    //<editor-fold desc="Cooldown handlers">

    public boolean isOnCooldown() {
        return ((System.currentTimeMillis() - lastUse) < cooldown);
    }

    public long cooldownTimeLeftInSeconds() {
        return ((lastUse + cooldown - System.currentTimeMillis()) / 1000);
    }

    public void setLastUse() {
        lastUse = System.currentTimeMillis();
    }
    //</editor-fold>

    public void setButtonOverridenByJoystick(boolean buttonOverridenByJoystick) {
        this.buttonOverridenByJoystick = buttonOverridenByJoystick;
    }
}
