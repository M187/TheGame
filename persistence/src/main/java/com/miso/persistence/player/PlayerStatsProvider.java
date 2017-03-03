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
    private static final int PLAYER_KILLS = 3;
    private static final int PLAYER_LEVEL_POINTS = 4;

    private static final int PLAYER_ABILITIES = 5;
    private static final int BUY_ABILITY = 6;
    private static final int UNLOCKED_ABILITIES = 7;
    private static final UriMatcher URI_MATCHER;

    // prepare the uri matcher
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        URI_MATCHER.addURI(PlayerStatsContract.CONTENT_AUTHORITY,
                "PlayerStatistics/kills",
                PLAYER_KILLS);
        URI_MATCHER.addURI(PlayerStatsContract.CONTENT_AUTHORITY,
                "PlayerStatistics/statistics",
                PLAYER_WHOLE_TABLE);
        URI_MATCHER.addURI(PlayerStatsContract.CONTENT_AUTHORITY,
                "PlayerStatistics/buy_ability",
                BUY_ABILITY);
        URI_MATCHER.addURI(PlayerStatsContract.CONTENT_AUTHORITY,
                "PlayerStatistics/level",
                PLAYER_LEVEL_POINTS);
        URI_MATCHER.addURI(PlayerStatsContract.CONTENT_AUTHORITY,
                "PlayerStatistics/abilities",
                PLAYER_ABILITIES);
        URI_MATCHER.addURI(PlayerStatsContract.CONTENT_AUTHORITY,
                "PlayerStatistics/abilities/unlocked",
                UNLOCKED_ABILITIES);
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
            case PLAYER_KILLS:
                builder.setTables(PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME);
                projection = new String[]{PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_KILLS};
                break;
            case PLAYER_WHOLE_TABLE:
                builder.setTables(PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME);
                break;
            case PLAYER_LEVEL_POINTS:
                builder.setTables(PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME);
                projection = new String[]{PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_POINTS};
                break;
            case PLAYER_ABILITIES:
                builder.setTables(PlayerStatsContract.PlayerAbilitiesEntry.TABLE_NAME);
                break;
            case UNLOCKED_ABILITIES:
                builder.setTables(PlayerStatsContract.PlayerAbilitiesEntry.TABLE_NAME);
                selection = PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_UNLOCKED + "=?";
                selectionArgs = new String[]{"true"};
                break;
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

        switch (URI_MATCHER.match(uri)) {
            case PLAYER_KILLS:
                count = db.update(PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME,
                        values,
                        null,
                        null);
                break;
            case PLAYER_LEVEL_POINTS:
                count = db.update(PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME,
                        values,
                        null,
                        null);
                break;
            case BUY_ABILITY:
                ContentValues cv1 = new ContentValues();
                ContentValues cv2 = new ContentValues();

                cv1.put(PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_KILLS, values.getAsString(PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_KILLS));
                cv2.put(PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_UNLOCKED, true);

                count = db.update(PlayerStatsContract.PlayerStatisticssEntry.TABLE_NAME,
                        cv1,
                        null,
                        null);
                count += db.update(PlayerStatsContract.PlayerAbilitiesEntry.TABLE_NAME,
                        cv2,
                        PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_NAME + "=?",
                        new String[]{values.getAsString(PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_NAME)});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
