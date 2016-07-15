package com.miso.thegame.gameMechanics.UserInterface;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.UserInterface.Buttons.ButtonPlaceholder;
import com.miso.thegame.gameMechanics.UserInterface.Buttons.FirstButtonPlaceholder;
import com.miso.thegame.gameMechanics.UserInterface.Buttons.SecondButtonPlaceholder;
import com.miso.thegame.gameMechanics.UserInterface.Buttons._PrimaryShootingButtonPlaceholder;
import com.miso.thegame.gameMechanics.UserInterface.Joystick.MovementJoystick;
import com.miso.thegame.gameMechanics.UserInterface.Joystick.ShootingJoystick;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;

import java.util.ArrayList;

/**
 * Created by Miso on 11.10.2015.
 */
public class Toolbar {

    private Bitmap image;
    private ArrayList<ButtonPlaceholder> buttonPlaceholders = new ArrayList<>();
    private MovementJoystick movementJoystick;
    private ShootingJoystick shootingJoystick;
    private PlayerStatusBar playerStatusBar;
    private Paint paint = new Paint();

    public Toolbar(Resources res, Player player, ButtonsTypeData buttonsTypeData){

        this.image = BitmapFactory.decodeResource(res, R.drawable.buttonfireball);

        ButtonPlaceholder primaryShootingButtonPlaceholder = new _PrimaryShootingButtonPlaceholder(res);

        ButtonPlaceholder firstButtonPlaceholder = new FirstButtonPlaceholder(res, buttonsTypeData.firstButtonType);
        ButtonPlaceholder secondButtonPlaceholder = new SecondButtonPlaceholder(res, buttonsTypeData.secondButtonType);

        getButtonPlaceholders().add(primaryShootingButtonPlaceholder);
        getButtonPlaceholders().add(firstButtonPlaceholder);
        getButtonPlaceholders().add(secondButtonPlaceholder);

        this.movementJoystick = new MovementJoystick(res);
        this.shootingJoystick = new ShootingJoystick(res, primaryShootingButtonPlaceholder);
        this.playerStatusBar = new PlayerStatusBar(player);

        this.paint.setAlpha(150);
    }

    public void draw(Canvas canvas){
        for (ButtonPlaceholder buttonPlaceholder : buttonPlaceholders) {
            buttonPlaceholder.draw(canvas, this.paint);
        }
        playerStatusBar.draw(canvas);
        movementJoystick.draw(canvas, paint);
        shootingJoystick.draw(canvas, paint);
    }

    public ArrayList<ButtonPlaceholder> getButtonPlaceholders() {
        return buttonPlaceholders;
    }

    public MovementJoystick getMovementJoystick() {
        return movementJoystick;
    }

    public ShootingJoystick getShootingJoystick() {
        return shootingJoystick;
    }
}
