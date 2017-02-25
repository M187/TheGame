package com.miso.persistence.player;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by michal.hornak on 2/15/2017.
 */

public class PlayerStatsProvider extends ContentProvider {

    // helper constants for UriMatcher
    private static final int PLAYER_WHOLE_TABLE = 0;
    private static final int PLAYER_SKILLPOINTS = 1;
    private static final int PLAYER_UNLOCKED_SKILLS = 2;
    private static final int PLAYER_KILLS = 3;
    private static final UriMatcher URI_MATCHER;

    // prepare the uri matcher
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        URI_MATCHER.addURI(PlayerStatsContract.CONTENT_AUTHORITY,
                "PlayerStatistics/skillpoints",
                PLAYER_SKILLPOINTS);
        URI_MATCHER.addURI(PlayerStatsContract.CONTENT_AUTHORITY,
                "PlayerStatistics/unlocked_skills",
                PLAYER_UNLOCKED_SKILLS);
        URI_MATCHER.addURI(PlayerStatsContract.CONTENT_AUTHORITY,
                "PlayerStatistics/kills",
                PLAYER_KILLS);
        URI_MATCHER.addURI(PlayerStatsContract.CONTENT_AUTHORITY,
                "PlayerStatistics",
                PLAYER_WHOLE_TABLE);
    }

    private PlayerStatsDbHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new PlayerStatsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case PLAYER_SKILLPOINTS:
                builder.setTables(PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME);
                projection = new String[]{PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_POINTS};
                break;
            case PLAYER_KILLS:
                builder.setTables(PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME);
                projection = new String[]{PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_KILLS};
                break;
            case PLAYER_WHOLE_TABLE:
                builder.setTables(PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME);
                break;
            case PLAYER_UNLOCKED_SKILLS:
            default:
                return null;
        }

        Cursor cursor = builder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case PLAYER_SKILLPOINTS:
            case PLAYER_UNLOCKED_SKILLS:
            case PLAYER_KILLS:
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        int count;

        switch (URI_MATCHER.match(uri)){
            case PLAYER_KILLS:
                count = db.update(PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME,
                        values,
                        null,
                        null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
