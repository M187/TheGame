package com.miso.persistence.player;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.miso.persistence.player.PlayerStatsContract;

/**
 * Created by michal.hornak on 2/16/2017.
 */

public abstract class StatsActivityLoaderCallbackImpl extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String[] settingsProjection = {
            PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_KILLS,
            PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_COMPLETED,
            PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_POINTS
    };


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(
                this,
                PlayerStatsContract.PlayerStatisticssEntry.CONTENT_URI.buildUpon().appendPath("statistics").build(),
                settingsProjection,
                null,
                null,
                null);
        return loader;
    }

    @Override
    public abstract void onLoadFinished(Loader<Cursor> loader, Cursor data);

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
