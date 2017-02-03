package com.miso.thegame.debugStuff.graphics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.miso.thegame.R;

/**
 * Created by michal.hornak on 26.06.2016.
 */
public class DebugGraphics extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void doIt(View view){
        setContentView(new DebugSurfaceView(this));
    }

    @Override
    public void onResume(){
        super.onResume();
        setContentView(R.layout.debug_graphics);
    }
}
