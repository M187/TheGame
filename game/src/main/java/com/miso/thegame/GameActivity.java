package com.miso.thegame;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.miso.persistence.player.PlayerStatsContract;
import com.miso.persistence.player.StatsActivityLoaderCallbackImpl;

/**
 * Created by michal.hornak on 2/7/2017.
 *
 * "Game activities are then split into multiplayer/singleplayer Activity, extending this class"
 */

public abstract class GameActivity extends StatsActivityLoaderCallbackImpl implements LoaderManager.LoaderCallbacks<Cursor>{

    public static DisplayMetrics metrics = new DisplayMetrics();
    public int PLAYER_STATS_LIST_ID = 1112;

    private volatile boolean dataLoaded = false;
    protected String db_kill_count = "0";
    protected String db_level_points = "0";

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        getLoaderManager().initLoader(PLAYER_STATS_LIST_ID, null, this);
    }

    private static final String[] settingsProjection = {
            PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_KILLS,
            PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_COMPLETED,
            PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_POINTS
    };


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        this.dataLoaded = true;
        data.moveToFirst();
        db_kill_count =  data.getString(0);
        db_level_points = data.getString(2);
    }


    /**
     * Updates database after game session
     */
    public void updatePlayerStatsKillCount(int killCount){
        if (!dataLoaded) return;
        ContentValues values = new ContentValues();
        killCount = killCount + Integer.parseInt(db_kill_count);
        db_kill_count = String.valueOf(killCount);
        values.put(PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_KILLS, killCount);
        getContentResolver().update(Uri.withAppendedPath(PlayerStatsContract.BASE_CONTENT_URI, "PlayerStatistics/kills"), values, null, null);
    }

    /**
     * Add level points after map finishes.
     */
    public void updatePlayerStatsLevelPoints(int levelPoints){
        if (!dataLoaded) return;
        ContentValues values = new ContentValues();
        levelPoints = levelPoints + Integer.parseInt(db_level_points);
        db_level_points = String.valueOf(levelPoints);
        values.put(PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_POINTS, levelPoints);
        getContentResolver().update(Uri.withAppendedPath(PlayerStatsContract.BASE_CONTENT_URI, "PlayerStatistics/level"), values, null, null);
    }
}
