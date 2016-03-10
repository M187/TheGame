package com.miso.thegame;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by Miso on 10.1.2016.
 */
public class MenuActivity extends Activity {

    public static boolean isGameOn = false;
    private Intent gameIntent = null;
    private MediaPlayer mMediaPlayer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu_layout);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.intro);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(false);
        //mMediaPlayer.start();

    }

    public void newGameClick(View view) {
        this.gameIntent = new Intent(this, Game.class);
        this.isGameOn = true;
        startActivity(this.gameIntent);
    }

    public void resumeGameClick(View view){
        try {
            startActivity(this.gameIntent);
        } catch (NullPointerException e){
            System.out.println("Too bad - no game launched yet.");
        }
    }

    public void onDestroy(){
        mMediaPlayer.stop();
        super.onDestroy();
    }

    public void playerOptionClick(View view){
        Intent intent = new Intent(this, PlayerOptions.class);
        startActivity(intent);
    }

    public void quitClick(View viev){
        this.finish();
        System.exit(0);
    }
}
