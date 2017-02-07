package com.miso.thegame;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * Created by michal.hornak on 2/7/2017.
 *
 * "Game activities are then split into multiplayer/singleplayer Activity, extending this class"
 */

public abstract class GameActivity extends Activity {

    public static DisplayMetrics metrics = new DisplayMetrics();

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }
}
