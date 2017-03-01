package com.miso.persistence.player;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

/**
 * Created by michal.hornak on 3/1/2017.
 */

public abstract class AbilityActivityLoaderCallbackImpl extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] settingsProjection = {
            PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_NAME,
            PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_DESCRIPTION,
            PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_PRICE,
            PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_UNLOCKED
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(
                this,
                PlayerStatsContract.PlayerAbilitiesEntry.CONTENT_URI,
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
