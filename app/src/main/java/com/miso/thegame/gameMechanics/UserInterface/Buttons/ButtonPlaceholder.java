package com.miso.thegame.gameMechanics.UserInterface.Buttons;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.miso.thegame.GamePanel;
import com.miso.thegame.gameMechanics.movingObjects.Player;
import com.miso.thegame.gameMechanics.movingObjects.spells.SpellManager;

/**
 * Created by Miso on 6.11.2015.
 */
public abstract class ButtonPlaceholder {

    protected int cooldown;
    protected long lastUse;
    protected Bitmap image;
    public int xDrawCoord = 50;
    public int yDrawCoord = GamePanel.HEIGHT;
    public int iteratorToDrawXAxis;
    public int iteratorToDrawYAxis;
    protected static final Paint paint = new Paint();
    private boolean buttonOverridenByJoystick = false;
    protected ButtonAction buttonAction = ButtonAction.Shockwave;

    /**
     * Function to initialize button. Should be called from constructor.
      * @param res - resources.
     * @param buttonAction default action used for button.
     */
    protected void initializeButtonPlaceholder(Resources res, ButtonAction buttonAction){
        this.cooldown = buttonAction.buttonActionCooldown;
        this.buttonAction = buttonAction;
        this.image = BitmapFactory.decodeResource(res, buttonAction.resourceParameterString);
        setXDrawCoord();
        setYDrawCoord();
    }

    /**
     * Big switch to decide what to do with input action.
     * @param spellManager
     * @param player
     */
    public void onClickEvent(SpellManager spellManager,Player player){

        switch (this.buttonAction){
            case Shockwave:
                this.setLastUse();
                spellManager.addShockwave(player, 2);
                return;
            case Timestop:
                this.setLastUse();
                spellManager.addTimestopSpell(player);
                return;
            case Freeze:
                this.setLastUse();
                spellManager.performFreezeSpell();
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
    public void draw(Canvas canvas){
        canvas.drawBitmap(getImage(), this.getXDrawCoord(), this.getYDrawCoord(), null);
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
