package com.miso.menu;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.miso.menu.options.OptionsActivityLoaderCallbackImpl;
import com.miso.thegame.gameMechanics.map.levels.NewLevelActivity;


public class MenuActivity extends OptionsActivityLoaderCallbackImpl {

    public static String KILL_COUNT = "0";

    private Intent gameIntent = null;
    private MediaPlayer mMediaPlayer;

    private AdView mAdView;
    private final int PLAYER_STATS_LIST_ID = 1110;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.activity_menu);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.intro);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(false);
        //mMediaPlayer.start();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ((TheGameApplication)getApplication()).startTracking();

        getLoaderManager().initLoader(PLAYER_STATS_LIST_ID, null, this);
    }

    public void onResume(){
        this.setContentView(R.layout.activity_menu);
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    public void newGameClickGround(View view) {
        setContentView(R.layout.loading_game);
        this.gameIntent = new Intent(this, NewLevelActivity.class);
        this.gameIntent.putExtra("Level", "Ground");
        startActivity(this.gameIntent);
    }

    public void newMultiplayerGame(View view){
        this.gameIntent = new Intent(this, MultiplayerLobby.class);
        this.gameIntent.putExtra("Level", "Default");
        startActivity(this.gameIntent);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    public void onDestroy(){
        if (mAdView != null) {
            mAdView.destroy();
        }
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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        KILL_COUNT = data.getString(0);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.game_widget_layout);
        remoteViews.setTextViewText(R.id.widget_player_kills, "Your kill-count: " + data.getString(0));
        remoteViews.setTextViewText(R.id.widget_player_level_points, "Your level-points: " + data.getString(2));

        appWidgetManager.updateAppWidget(appWidgetManager.getAppWidgetIds(new ComponentName(this, TheGameWidgetProvider.class)), remoteViews);
    }
}
