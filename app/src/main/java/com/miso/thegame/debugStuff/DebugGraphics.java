package com.miso.thegame.debugStuff;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by michal.hornak on 26.06.2016.
 */
public class DebugGraphics extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void doIt(View view){
        setContentView(new DebugSurfaceView(this));
    }
}
