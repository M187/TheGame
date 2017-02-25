package com.miso.thegame;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.miso.persistence.player.PlayerStatsContract;

/**
 * Created by michal.hornak on 2/7/2017.
 *
 * "Game activities are then split into multiplayer/singleplayer Activity, extending this class"
 */

public abstract class GameActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static DisplayMetrics metrics = new DisplayMetrics();
    public int PLAYER_STATS_LIST_ID = 1112;

    protected String db_kill_count = "0";

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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(
                this,
                PlayerStatsContract.PlayerStatisticssEntry.CONTENT_URI,
                settingsProjection,
                null,
                null,
                null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        data.moveToFirst();
        db_kill_count =  data.getString(0);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
